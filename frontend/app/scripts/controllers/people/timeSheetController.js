'use strict';

angular.module('frontendApp')
  .controller('timeSheetCtrl', ['$scope', '$route', 'growl','$location', 'timeSheetService','peopleService',
    function ($scope, $route, growl, $location, timeSheetService,peopleService) {

    $scope.dateTo = moment().add(1, 'days').format("YYYY-MM-DD");
    $scope.dateFrom = moment().subtract(7, 'days').format("YYYY-MM-DD");
      var employeeId = $route.current.params.employeeId;


    $scope.init = function () {
      timeSheetService.getDayBars(moment($scope.dateFrom).format("YYYY-MM-DD"), moment($scope.dateTo).format("YYYY-MM-DD"), employeeId, function (bars) {
        $scope.dayBars = bars;
        countDailyWorkTime();
        countDailyPauseTime();
        countDailyBusyTime();
      });
      peopleService.getEmployee(employeeId,function(person){
        $scope.person = person;
      })
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


      $scope.getBars = function () {
      var endDate = moment($scope.dateTo).format("YYYY-MM-DD");
      var startDate = moment($scope.dateFrom).format("YYYY-MM-DD");
      timeSheetService.getDayBars(startDate,endDate, employeeId, function (bars) {
        $scope.dayBars = bars;
        countDailyWorkTime();
      });
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


    $scope.edit = function(day){
      var theDay = moment(day).format("YYYY-MM-DD");
      $location.url("/people/worktime/timeSheet/" + employeeId + "/" + theDay);
    };

    $scope.$on('error',function(){
      growl.error("error", {ttl:5000});
      });

  }]);
