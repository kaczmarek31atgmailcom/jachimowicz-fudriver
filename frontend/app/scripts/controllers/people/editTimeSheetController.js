/* global moment */
'use strict';

angular.module('frontendApp')
  .controller('editTimeSheetCtrl', ['$scope', '$route', '$http', '$location', '$window', 'EventService', '$translate',
    function ($scope, $route, $http, $location, $window, EventService, $translate) {
      var day = moment($route.current.params.theDay, 'YYYY-MM-DD', true);
      var destroyed = false;
      $scope.employeeId = $route.current.params.employeeId;
      $scope.workPeriods = [];
      $scope.hours = [0, 4, 8, 12, 16, 20, 24];
      $scope.selectedDay = day.toDate();
      $scope.dayLabel = day.format('DD.MM.YYYY');
      $scope.newPeriod = { start: '08:00', end: '16:00' };

      function errorMessage(response) {
        if (response.status === 409) { return 'people.timesheet.edit.message.overlap'; }
        var body = response.data && response.data.body;
        if (body === 'Start day after end day') { return 'people.timesheet.edit.message.invalid-range'; }
        if (body === 'Okres pracy musi rozpocząć się i zakończyć tego samego dnia') {
          return 'people.timesheet.edit.message.same-day-required';
        }
        return 'people.timesheet.edit.message.request-failed';
      }

      function timeOnDay(value) {
        return /^([01]\d|2[0-3]):[0-5]\d$/.test(value || '') ? moment(day.format('YYYY-MM-DD') + ' ' + value, 'YYYY-MM-DD HH:mm', true) : null;
      }

      function summarize() {
        var worked = 0;
        var pauses = 0;
        var lastEnd = null;
        $scope.timeline = [];
        $scope.workPeriods.forEach(function (period) {
          var start = moment(period.startDate);
          var end = period.endDate ? moment(period.endDate) : null;
          period.start = start.format('HH:mm');
          period.end = end ? end.format('HH:mm') : '';
          period.originalStart = period.start;
          period.originalEnd = period.end;
          period.duration = end ? Math.max(0, end.diff(start, 'minutes')) : null;
          if (end) { worked += Math.max(0, end.valueOf() - start.valueOf()); }
          if (lastEnd && start.isAfter(lastEnd)) { pauses += start.diff(lastEnd); }
          if (end && (!lastEnd || end.isAfter(lastEnd))) { lastEnd = end; }
          var displayedEnd = end || (day.isSame(moment(), 'day') ? moment() : day.clone().endOf('day'));
          var left = Math.max(0, start.diff(day.clone().startOf('day'), 'seconds') / 864);
          var width = Math.max(0.3, Math.min(100 - left, displayedEnd.diff(start, 'seconds') / 864));
          $scope.timeline.push({id: period.id, left: left, width: width, open: !end,
            start: period.start, end: period.end});
        });
        $scope.workMinutes = Math.floor(worked / 60000);
        $scope.pauseMinutes = Math.floor(pauses / 60000);
        $scope.openCount = $scope.workPeriods.filter(function (p) { return !p.endDate; }).length;
        if (!$scope.newPeriod.touched && lastEnd && !$scope.openCount) {
          var suggestedStart = lastEnd.clone().startOf('minute');
          if (suggestedStart.isBefore(lastEnd)) { suggestedStart.add(1, 'minute'); }
          var suggestedEnd = moment.min(suggestedStart.clone().add(1, 'hour'), day.clone().hour(23).minute(59).second(0));
          $scope.newPeriod = suggestedEnd.isAfter(suggestedStart) ? {
            start: suggestedStart.format('HH:mm'), end: suggestedEnd.format('HH:mm')
          } : {start: '', end: ''};
        }
      }

      function load() {
        $scope.externalChange = false;
        if (destroyed) { return; }
        $scope.loading = true;
        return $http.get('/fudriver/rest/person/workPeriods', {params: {
          personId: $scope.employeeId, startDay: day.format('YYYY-MM-DD'), endDay: day.format('YYYY-MM-DD')
        }}).then(function (response) {
          $scope.workPeriods = response.data.sort(function (a, b) { return moment(a.startDate).valueOf() - moment(b.startDate).valueOf(); });
          summarize();
          $scope.loadFailed = false;
        }, function (response) {
          $scope.error = errorMessage(response);
          $scope.loadFailed = true;
        }).finally(function () { $scope.loading = false; });
      }

      $scope.periodLabel = function (period) {
        return $translate.instant('people.timesheet.edit.period-range', {
          start: period.start, end: period.end || $translate.instant('people.timesheet.edit.in-progress')
        });
      };
      $scope.durationLabel = function (minutes) {
        return $translate.instant('people.timesheet.edit.duration', {
          hours: Math.floor((minutes || 0) / 60), minutes: (minutes || 0) % 60
        });
      };
      $scope.isDirty = function (period) {
        return period.start !== period.originalStart || period.end !== period.originalEnd;
      };
      $scope.hasOtherChanges = function (period) {
        return $scope.workPeriods.some(function (other) { return other.id !== period.id && $scope.isDirty(other); });
      };
      $scope.hasChanges = function () {
        return $scope.workPeriods.some($scope.isDirty);
      };
      $scope.validation = function (period, adding) {
        var start = timeOnDay(period.start);
        var end = period.end ? timeOnDay(period.end) : null;
        if (!start || (period.end && !end) || (adding && !end)) { return 'people.timesheet.edit.message.invalid-time'; }
        if (!adding && period.endDate && !end) { return 'people.timesheet.edit.message.end-required'; }
        if (end && !end.isAfter(start)) { return 'people.timesheet.edit.message.invalid-range'; }
        var collision = $scope.workPeriods.some(function (other) {
          return other.id !== period.id && (!other.endDate || start.isBefore(moment(other.endDate))) &&
            (!end || moment(other.startDate).isBefore(end));
        });
        return collision ? 'people.timesheet.edit.message.overlapping-times' : '';
      };

      function mutate(method, url, data, message) {
        $scope.busy = true;
        $scope.error = '';
        $scope.notice = '';
        return $http({method: method, url: url, data: data,
          headers: {'Content-Type': 'application/json; charset=UTF-8'}
        }).then(function () {
          $scope.notice = message;
          return load();
        }, function (response) { $scope.error = errorMessage(response); })
          .finally(function () { $scope.busy = false; });
      }
      $scope.save = function (period) {
        if ($scope.busy || $scope.hasOtherChanges(period) || $scope.validation(period, false)) { return; }
        // Preserve seconds in fields that the user did not change.
        mutate('PUT', '/fudriver/rest/person/workPeriod', {id: period.id,
          startDate: period.start === period.originalStart ? period.startDate : timeOnDay(period.start).valueOf(),
          endDate: period.end ? (period.end === period.originalEnd ? period.endDate : timeOnDay(period.end).valueOf()) : null
        }, 'people.timesheet.edit.message.saved');
      };
      $scope.cancel = function (period) { period.start = period.originalStart; period.end = period.originalEnd; };
      $scope.addPeriod = function () {
        if ($scope.busy || $scope.validation($scope.newPeriod, true)) { return; }
        if ($scope.hasChanges()) { $scope.error = 'people.timesheet.edit.message.pending-changes'; return; }
        mutate('POST', '/fudriver/rest/workTimeRecorder/closed', {personId: Number($scope.employeeId),
          startTime: timeOnDay($scope.newPeriod.start).valueOf(), endTime: timeOnDay($scope.newPeriod.end).valueOf()
        }, 'people.timesheet.edit.message.created');
      };
      $scope.remove = function (period) {
        if ($scope.busy || $scope.hasOtherChanges(period) || !$window.confirm($translate.instant('people.timesheet.edit.confirm-delete', {
          start: period.originalStart, end: period.originalEnd || $translate.instant('people.timesheet.edit.in-progress')
        }))) { return; }
        mutate('DELETE', '/fudriver/rest/person/workPeriod', {workPeriodId: period.id}, 'people.timesheet.edit.message.deleted');
      };
      $scope.close = function (period) {
        if ($scope.busy || $scope.hasChanges()) { return; }
        mutate('PUT', '/fudriver/rest/person/workPeriod/close', period.id, 'people.timesheet.edit.message.closed');
      };
      $scope.navigate = function (offset) {
        if ($scope.busy) { $scope.selectedDay = day.toDate(); return; }
        var next = offset === null ? moment($scope.selectedDay) : day.clone().add(offset, 'days');
        if (next.isValid()) { $location.path('/people/worktime/timeSheet/' + $scope.employeeId + '/' + next.format('YYYY-MM-DD')); }
      };
      $scope.refresh = function () {
        if (!$scope.hasChanges() || $window.confirm($translate.instant('people.timesheet.edit.confirm-refresh'))) { $scope.error = ''; $scope.notice = ''; load(); }
      };
      var unsubscribe = EventService.getEvents(function (raw) {
        var event;
        try { event = JSON.parse(raw); } catch (ignored) { return; }
        if (String(event.id) === String($scope.employeeId) && /EMPLOYEE_WORK_(STARTED|ENDED)/.test(event.source)) {
          $scope.$evalAsync(function () {
            if (destroyed) { return; }
            if ($scope.busy || $scope.hasChanges()) { $scope.externalChange = true; }
            else { load(); }
          });
        }
      });
      $scope.$on('$locationChangeStart', function (event) {
        if ($scope.busy || ($scope.hasChanges() && !$window.confirm($translate.instant('people.timesheet.edit.confirm-leave')))) {
          event.preventDefault();
          $scope.selectedDay = day.toDate();
        }
      });
      $scope.$on('$destroy', function () { destroyed = true; if (unsubscribe) { unsubscribe(); } });
      if (!day.isValid()) { $scope.error = 'people.timesheet.edit.message.invalid-day'; $scope.loadFailed = true; return; }
      $http.get('/fudriver/rest/person/' + $scope.employeeId).then(function (response) {
        $scope.person = response.data;
      }, function () { $scope.personFailed = true; });
      load();
    }]);
