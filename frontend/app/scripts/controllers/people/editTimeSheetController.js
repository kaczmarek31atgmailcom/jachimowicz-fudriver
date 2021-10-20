"use strict";

angular.module('frontendApp')
  .controller('editTimeSheetCtrl', ['$scope', '$route', 'growl', 'timeSheetService', 'EventService','peopleService',
    function ($scope, $route, growl, timeSheetService, EventService, peopleService) {

      $scope.startTime = {};
      $scope.employeeId = $route.current.params.employeeId;
      var theDay = $route.current.params.theDay;
      $scope.newStartTime = moment(theDay);
      $scope.newEndTime = moment(theDay);

      var init = function () {
        timeSheetService.getWorkPeriods($scope.employeeId, theDay, theDay, function (workPeriods) {
          $scope.workPeriods = workPeriods;
        });
        timeSheetService.getDayBars(theDay, theDay, $scope.employeeId, function (bars) {
          $scope.dayBars = bars;
          countDailyWorkTime();
          countDailyPauseTime();
          countDailyBusyTime();
          createEmptyDayBars($scope.dateFrom,$scope.dateTo,$scope.dayBars);
          $scope.barPeriods = $scope.dayBars[0].barPeriods;
        });
        peopleService.getEmployee($scope.employeeId,function(person){
          $scope.person = person;
        })
      };
      init();

      $scope.close = function (workPeriodId) {
        timeSheetService.closePeriod(workPeriodId);
      };

      $scope.addPeriod = function(){
        if(($scope.checkIfTimeInExistingPeriod($scope.newStartTime)) || ($scope.checkIfTimeInExistingPeriod($scope.newEndTime))){
          return false;
        }
        if(($scope.startTime === null) || ($scope.endTime === null)){
          return false;
        }
        var command = {};
        command.personId = $scope.employeeId;
        command.startTime = $scope.newStartTime;
        command.endTime = $scope.newEndTime;
        timeSheetService.saveClosedWorkPeriod(command);
      };

      $scope.checkIfTimeInExistingPeriod = function(date){
          for(var i = 0; i< $scope.workPeriods.length; i++) {
            if ((moment(date).isSameOrAfter(moment($scope.workPeriods[i].startDate))) && (moment(date).isSameOrBefore(moment($scope.workPeriods[i].endDate)))) {
              growl.error("people.timesheet.edit.message.invalid.period",{ttl:"5000"});
              return true;
            }
            if ((moment(date).isSameOrAfter(moment($scope.workPeriods[i].startDate))) && ($scope.workPeriods[i].endDate === null)) {
              growl.error("people.timesheet.edit.message.invalid.period",{ttl:"5000"});
              return true;
            }
          }
        return false;
      };

      $scope.save = function (workPeriod) {
        var command = {};
        command.id = workPeriod.id;
        command.startDate = workPeriod.startDate;
        command.endDate = workPeriod.endDate;
        timeSheetService.saveWorkPeriod(command);
      };

      $scope.event = EventService.getEvents(function (resp) {
        var response = JSON.parse(resp);
        var message = response.source;
        if ((message === 'EMPLOYEE_WORK_STARTED' || message === 'EMPLOYEE_WORK_ENDED') && (parseInt(response.id) === parseInt($scope.employeeId))) {
          timeSheetService.getWorkPeriods($scope.employeeId, startDay, endDay, function (workPeriods) {
            $scope.workPeriods = workPeriods;
          });
        init();
        }
      });

      $scope.$on('error', function (event, response) {
        if (response.data.body == 'Start day after end day') {
          growl.error('people.timesheet.edit.message.start-after-begining', {ttl: 5000});
        } else {
          growl.error('error', {ttl: 4000});
        }
      });

      $scope.$on('WorkPeriodUpdated', function () {
        init();
        growl.success('people.timesheet.edit.message.period-updated', {ttl: 4000})
      });

      $scope.$on('WorkPeriodClosed', function () {
        init();
        growl.success('people.timesheet.edit.message.period-closed', {ttl: 4000})
      });

      $scope.delete = function (workPeriodId) {
        var command = {};
        command.workPeriodId = workPeriodId;
        timeSheetService.deletePeriod(command);
      };


      $scope.$on('WorkPeriodDeleted', function (event, response) {
        removeWorkPeriod(response.data.entityId);
        growl.success('people.timesheet.edit.message.period-deleted', {ttl: 4000});
        init();
      });

      $scope.$on('ClosedWorkPeriodCreated',function(event,response){
        growl.success("people.timesheet.edit.message.period-created",{ttl:3000});
        init();
      });

      $scope.countWorkMinutes = function (endDate, startDate) {
        var result = 0;
        if (endDate != undefined) {
          result = parseInt((endDate - startDate) / 1000 / 60);
        }
        if (result >= 0) {
          return result;
        }
        return 0;
      };

      var removeWorkPeriod = function (workPeriodId) {
        for (var i = $scope.workPeriods.length - 1; i >= 0; i--) {
          if ($scope.workPeriods[i].id == workPeriodId) {
            $scope.workPeriods.splice(i, 1);
          }
        }
      };


      var createEmptyDayBars = function(startDay,endDay,dayBars){
        var theDay = moment(startDay);
        var endDay = moment(endDay);
        var emptyDays = [];
        while(theDay.isBefore(endDay)){
          theDay = theDay.add(1,'days');
        }
      };

      var countDailyWorkTime = function(){
        $scope.dayBars.forEach(function(dayBar){
          dayBar.workTime = 0;
          dayBar.barPeriods.forEach(function(barPeriod){
            if((barPeriod.barType === 'ACTIVE') || (barPeriod.barType === 'PAUSE')){
              var barWorkTime = barPeriod.endDate - barPeriod.startDate;
              dayBar.workTime += parseInt((barWorkTime / 1000 / 60));
            }
          })
        })
      };

      var countDailyBusyTime = function(){
        $scope.dayBars.forEach(function(dayBar){
          dayBar.busyTime = 0;
          dayBar.barPeriods.forEach(function(barPeriod){
            if(barPeriod.barType === 'ACTIVE'){
              var barWorkTime = barPeriod.endDate - barPeriod.startDate;
              dayBar.busyTime += parseInt((barWorkTime / 1000 / 60));
            }
          })
        })
      };

      var countDailyPauseTime = function(){
        $scope.dayBars.forEach(function(dayBar){
          dayBar.pauseTime = 0;
          dayBar.barPeriods.forEach(function(barPeriod){
            if(barPeriod.barType === 'PAUSE'){
              var barWorkTime = barPeriod.endDate - barPeriod.startDate;
              dayBar.pauseTime += parseInt((barWorkTime / 1000 / 60));
            }
          })
        })
      };

      $scope.getBarClass = function(barType){
        if(barType === 'ACTIVE'){
          return 'progress-bar progress-bar-info progress-bar-striped'
        }
        if(barType === 'PAUSE'){
          return 'progress-bar progress-bar-warning progress-bar-striped'
        }
        return 'progress-bar transparent'
      };



    }]);

