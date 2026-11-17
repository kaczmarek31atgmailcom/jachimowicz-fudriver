'use strict';

angular.module('frontendApp')
  .controller('WorkTimeCtrl', ['$scope', '$location', '$http', '$filter', 'EventService',
    function ($scope, $location, $http, $filter, EventService) {
      var destroyed = false;
      var requestId = 0;
      var pendingStatus = {};
      $scope.people = [];
      $scope.filters = {query: $scope.searchText || '', group: '', status: '', includeInactive: false};
      $scope.sortField = 'employee';
      $scope.reverseSort = false;
      $scope.settings = {pageSize: 25, sortField: 'employee'};
      $scope.sortFields = [{key: 'employee', label: 'employee'}, {key: 'id', label: 'id'},
        {key: 'nr', label: 'number'}, {key: 'group', label: 'group'}, {key: 'status', label: 'status'}];
      $scope.page = 1;
      $scope.pageSizes = [25, 50, 100];

      function normalize(value) {
        var letters = {'ą': 'a', 'ć': 'c', 'ę': 'e', 'ł': 'l', 'ń': 'n', 'ó': 'o', 'ś': 's', 'ź': 'z', 'ż': 'z'};
        return String(value === null || value === undefined ? '' : value).toLowerCase()
          .replace(/[ąćęłńóśźż]/g, function (letter) { return letters[letter]; });
      }
      function groupKey(person) { return person.groupName ? 'group:' + person.groupName : 'ungrouped'; }
      function updateTotals() {
        var present = $scope.people.filter(function (person) { return person.isPresent; }).length;
        $scope.total = {all: $scope.people.length, present: present, away: $scope.people.length - present};
      }
      function paginate() {
        $scope.pageCount = Math.max(1, Math.ceil($scope.filteredPeople.length / $scope.settings.pageSize));
        $scope.page = Math.max(1, Math.min($scope.page, $scope.pageCount));
        var offset = ($scope.page - 1) * $scope.settings.pageSize;
        $scope.pagePeople = $scope.filteredPeople.slice(offset, offset + $scope.settings.pageSize);
        $scope.firstResult = $scope.filteredPeople.length ? offset + 1 : 0;
        $scope.lastResult = Math.min(offset + $scope.settings.pageSize, $scope.filteredPeople.length);
      }
      function filterPeople(resetPage) {
        var words = normalize($scope.filters.query).trim().split(/\s+/).filter(function (word) { return !!word; });
        var filtered = $scope.people.filter(function (person) {
          return (!$scope.filters.group || groupKey(person) === $scope.filters.group) &&
            (!$scope.filters.status || ($scope.filters.status === 'present' ? person.isPresent : !person.isPresent)) &&
            words.every(function (word) { return person.searchValue.indexOf(word) !== -1; });
        });
        var sorts = {employee: ['sortSurname', 'sortName', 'id'], id: ['id'], nr: ['nr', 'id'],
          group: ['sortGroup', 'sortSurname', 'id'], status: ['isPresent', 'sortSurname', 'id']};
        $scope.filteredPeople = $filter('orderBy')(filtered, sorts[$scope.sortField], $scope.reverseSort);
        if (resetPage) { $scope.page = 1; }
        paginate();
      }
      $scope.applyFilters = function () { filterPeople(true); };
      $scope.clearFilters = function () {
        $scope.filters.query = ''; $scope.filters.group = ''; $scope.filters.status = '';
        $scope.applyFilters();
      };
      $scope.hasFilters = function () { return !!($scope.filters.query || $scope.filters.group || $scope.filters.status); };
      $scope.sortBy = function (field) {
        $scope.reverseSort = $scope.sortField === field ? !$scope.reverseSort : false;
        $scope.sortField = field;
        $scope.settings.sortField = field;
        $scope.applyFilters();
      };
      $scope.sortDirection = function (field) {
        return $scope.sortField !== field ? 'none' : ($scope.reverseSort ? 'descending' : 'ascending');
      };
      $scope.goToPage = function (page) { $scope.page = page; paginate(); };
      $scope.changePageSize = function () { $scope.page = 1; paginate(); };
      $scope.editTimeSheet = function (person) { $location.path('/people/worktime/' + person.id); };

      $scope.load = function () {
        var current = ++requestId;
        pendingStatus = {};
        $scope.loading = true; $scope.error = '';
        return $http.get('/fudriver/rest/person/headers', {params: {active: !$scope.filters.includeInactive}})
          .then(function (response) {
            if (destroyed || current !== requestId) { return; }
            var groups = {};
            $scope.people = response.data.map(function (source) {
              var person = angular.extend({}, source);
              if (Object.prototype.hasOwnProperty.call(pendingStatus, String(person.id))) {
                person.isPresent = pendingStatus[String(person.id)];
              }
              person.searchValue = normalize([person.id, person.nr, person.name, person.surname, person.groupName].join(' '));
              person.sortSurname = normalize(person.surname); person.sortName = normalize(person.name);
              person.sortGroup = normalize(person.groupName);
              person.initials = ((person.name || '').charAt(0) + (person.surname || '').charAt(0)).toUpperCase();
              groups[groupKey(person)] = {key: groupKey(person), label: person.groupName || ''};
              return person;
            });
            $scope.groups = Object.keys(groups).map(function (key) { return groups[key]; })
              .sort(function (a, b) { return normalize(a.label).localeCompare(normalize(b.label)); });
            if ($scope.filters.group && !groups[$scope.filters.group]) { $scope.filters.group = ''; }
            updateTotals(); filterPeople(false);
            $scope.updatedAt = new Date(); $scope.loaded = true;
          }, function () {
            if (!destroyed && current === requestId) { $scope.error = 'people.timesheet.list.load-failed'; }
          }).finally(function () { if (!destroyed && current === requestId) { $scope.loading = false; } });
      };
      var unsubscribe = EventService.getEvents(function (raw) {
        var event;
        try { event = JSON.parse(raw); } catch (ignored) { return; }
        if (event.source !== 'EMPLOYEE_WORK_STARTED' && event.source !== 'EMPLOYEE_WORK_ENDED') { return; }
        $scope.$evalAsync(function () {
          if (destroyed) { return; }
          var present = event.source === 'EMPLOYEE_WORK_STARTED';
          pendingStatus[String(event.id)] = present;
          $scope.people.forEach(function (person) { if (String(person.id) === String(event.id)) { person.isPresent = present; } });
          updateTotals(); filterPeople(false); $scope.updatedAt = new Date();
        });
      });
      // Keep the application-wide search field working alongside this page's filters.
      $scope.$watch('searchText', function (value, previous) {
        if (value !== previous) { $scope.filters.query = value || ''; $scope.applyFilters(); }
      });
      $scope.$on('$destroy', function () { destroyed = true; requestId++; if (unsubscribe) { unsubscribe(); } });
      $scope.filteredPeople = [];
      $scope.load();
    }]);
