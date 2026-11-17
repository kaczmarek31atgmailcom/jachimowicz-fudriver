/* global moment */
'use strict';

angular.module('frontendApp')
  .controller('timeSheetCtrl', ['$scope', '$route', '$http', '$location', '$translate', 'EventService',
    function ($scope, $route, $http, $location, $translate, EventService) {
      var requestId = 0;
      var destroyed = false;
      var appliedFrom;
      var appliedTo;
      $scope.employeeId = $route.current.params.employeeId;
      $scope.dateFrom = moment().subtract(6, 'days').startOf('day').toDate();
      $scope.dateTo = moment().startOf('day').toDate();
      $scope.days = [];
      $scope.filters = {showEmpty: true};
      $scope.hours = [0, 4, 8, 12, 16, 20, 24];

      $scope.durationLabel = function (minutes) {
        return $translate.instant('people.timesheet.edit.duration', {
          hours: Math.floor((minutes || 0) / 60), minutes: (minutes || 0) % 60
        });
      };
      $scope.rangeError = function () {
        var from = moment($scope.dateFrom);
        var to = moment($scope.dateTo);
        if (!$scope.dateFrom || !$scope.dateTo || !from.isValid() || !to.isValid()) {
          return 'people.timesheet.card.invalid-dates';
        }
        if (from.isAfter(to, 'day')) { return 'people.timesheet.card.reversed-range'; }
        if (to.diff(from, 'days') >= 366) { return 'people.timesheet.card.range-too-long'; }
        return '';
      };
      $scope.rangeChanged = function () {
        return appliedFrom && (moment($scope.dateFrom).format('YYYY-MM-DD') !== appliedFrom.format('YYYY-MM-DD') ||
          moment($scope.dateTo).format('YYYY-MM-DD') !== appliedTo.format('YYYY-MM-DD'));
      };

      function makeDay(date, records) {
        var from = date.valueOf();
        var until = date.clone().add(1, 'day').valueOf();
        var day = {key: date.format('YYYY-MM-DD'), dateLabel: date.format('DD.MM.YYYY'),
          weekdayKey: 'people.timesheet.card.weekday-' + date.day(),
          today: date.isSame(moment(), 'day'), weekend: date.day() === 0 || date.day() === 6,
          workMillis: 0, pauseMillis: 0, periodCount: 0, openCount: 0, timeline: []};
        var previousEnd = null;
        var firstStart = null;
        var lastEnd = null;
        records.forEach(function (record) {
          var rawStart = moment(record.startDate).valueOf();
          var open = record.endDate === null || record.endDate === undefined;
          if (open && !moment(rawStart).isSame(date, 'day')) { return; }
          var rawEnd = open ? (day.today ? moment().valueOf() : until - 60000) : moment(record.endDate).valueOf();
          if (!isFinite(rawStart) || !isFinite(rawEnd) || rawEnd < rawStart || rawStart >= until || rawEnd < from) { return; }
          if (rawEnd === from && rawStart < from) { return; }
          var start = Math.max(from, rawStart);
          var end = Math.min(until, rawEnd);
          day.periodCount++;
          if (open) { day.openCount++; }
          else { day.workMillis += Math.max(0, end - start); }
          if (previousEnd !== null && start > previousEnd) {
            day.pauseMillis += start - previousEnd;
            day.timeline.push({type: 'pause', start: previousEnd, end: start});
          }
          if (previousEnd !== null && start < previousEnd) { day.overlap = true; }
          previousEnd = previousEnd === null ? end : Math.max(previousEnd, end);
          firstStart = firstStart === null ? start : Math.min(firstStart, start);
          lastEnd = lastEnd === null ? end : Math.max(lastEnd, end);
          day.timeline.push({type: open ? 'open' : 'work', start: start, end: end});
        });
        day.timeline.forEach(function (bar) {
          bar.left = (bar.start - from) * 100 / (until - from);
          bar.width = Math.max(0, (bar.end - bar.start) * 100 / (until - from));
        });
        day.firstTime = firstStart === null ? '' : moment(firstStart).format('HH:mm');
        day.lastTime = lastEnd === null ? '' : moment(lastEnd).format('HH:mm');
        day.workMinutes = Math.floor(day.workMillis / 60000);
        day.pauseMinutes = Math.floor(day.pauseMillis / 60000);
        return day;
      }

      function summarize(records, from, to) {
        var ordered = records.slice().sort(function (a, b) { return moment(a.startDate).valueOf() - moment(b.startDate).valueOf(); });
        var days = [];
        var total = {workMinutes: 0, pauseMinutes: 0, recordedDays: 0, openCount: 0, periodCount: 0};
        for (var date = from.clone(); !date.isAfter(to, 'day'); date.add(1, 'day')) {
          var day = makeDay(date, ordered);
          days.push(day);
          total.workMinutes += day.workMinutes;
          total.pauseMinutes += day.pauseMinutes;
          total.periodCount += day.periodCount;
          total.openCount += day.openCount;
          if (day.periodCount) { total.recordedDays++; }
        }
        $scope.days = days.reverse();
        $scope.total = total;
        $scope.hasOverlaps = days.some(function (day) { return day.overlap; });
      }

      function load(from, to) {
        if (destroyed) { return; }
        var current = ++requestId;
        $scope.loading = true;
        $scope.error = '';
        $scope.externalChange = false;
        return $http.get('/fudriver/rest/person/workPeriods', {params: {
          personId: $scope.employeeId, startDay: from.format('YYYY-MM-DD'), endDay: to.format('YYYY-MM-DD')
        }}).then(function (response) {
          if (destroyed || current !== requestId) { return; }
          summarize(response.data, from, to);
          appliedFrom = from.clone(); appliedTo = to.clone();
          $scope.rangeLabel = from.format('DD.MM.YYYY') + ' – ' + to.format('DD.MM.YYYY');
          $scope.loaded = true;
        }, function () {
          if (!destroyed && current === requestId) { $scope.error = 'people.timesheet.card.load-failed'; }
        }).finally(function () { if (!destroyed && current === requestId) { $scope.loading = false; } });
      }
      $scope.applyRange = function () {
        if (!$scope.rangeError()) { return load(moment($scope.dateFrom).startOf('day'), moment($scope.dateTo).startOf('day')); }
      };
      $scope.preset = function (name) {
        $scope.dateFrom = (name === 'month' ? moment().startOf('month') : moment().subtract(6, 'days').startOf('day')).toDate();
        $scope.dateTo = (name === 'month' ? moment().endOf('month').startOf('day') : moment().startOf('day')).toDate();
        return $scope.applyRange();
      };
      $scope.shiftRange = function (direction) {
        if (!appliedFrom || $scope.loading) { return; }
        var length = appliedTo.diff(appliedFrom, 'days') + 1;
        $scope.dateFrom = appliedFrom.clone().add(direction * length, 'days').toDate();
        $scope.dateTo = appliedTo.clone().add(direction * length, 'days').toDate();
        return $scope.applyRange();
      };
      $scope.refresh = function () {
        if (appliedFrom) { return load(appliedFrom.clone(), appliedTo.clone()); }
        return $scope.applyRange();
      };
      $scope.edit = function (day) {
        $location.path('/people/worktime/timeSheet/' + $scope.employeeId + '/' + day.key);
      };
      $scope.barLabel = function (bar) {
        return $translate.instant('people.timesheet.card.bar-' + bar.type, {
          start: moment(bar.start).format('HH:mm'), end: moment(bar.end).format('HH:mm')
        });
      };
      $scope.visibleDay = function (day) { return $scope.filters.showEmpty || day.periodCount > 0; };
      var unsubscribe = EventService.getEvents(function (raw) {
        var event;
        try { event = JSON.parse(raw); } catch (ignored) { return; }
        if (String(event.id) === String($scope.employeeId) && /EMPLOYEE_WORK_(STARTED|ENDED)/.test(event.source)) {
          $scope.$evalAsync(function () {
            if (destroyed) { return; }
            if ($scope.loading) { $scope.externalChange = true; }
            else { $scope.refresh(); }
          });
        }
      });
      $scope.$on('$destroy', function () { destroyed = true; requestId++; if (unsubscribe) { unsubscribe(); } });
      $http.get('/fudriver/rest/person/' + $scope.employeeId).then(function (response) {
        if (!destroyed) { $scope.person = response.data; }
      }, function () { if (!destroyed) { $scope.personFailed = true; } });
      $scope.applyRange();
    }]);
