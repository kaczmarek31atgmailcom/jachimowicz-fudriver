'use strict';

angular.module('frontendApp').controller('calendarCtrl', ['$scope','calendarService', function ($scope,calendarService) {

  var now = moment();


  $scope.openCalendar = function(){
    var command = {};
    var firstDayOfMonth = moment(now).format("YYYY-MM") + '-01';
    command.date = firstDayOfMonth;
    calendarService.openMonth(command);
  };


  $scope.countDaysInMonth = function () {
    $scope.currentMonthName = moment(now).format("YYYY-MM");
    var daysInMonth = moment(now).daysInMonth();
    var firstDayOfMonth = moment(now).format("YYYY-MM") + '-01';
    var currentDayOfMonth = firstDayOfMonth;
    var weekDayOfFirstDayInMonth = moment(firstDayOfMonth, 'YYYY-MM-DD').weekday();
    var beforeMonthStartsDaysCounter = 0;
    $scope.month = [];
    var week = [];
    for (var i = 0; i < weekDayOfFirstDayInMonth; i++) {
      var day = {};
      beforeMonthStartsDaysCounter++;
      week.push(day);
    }
// Dni aktywnego miesiąca
    for (var i = 0; i < daysInMonth; i++) {
      var day = {};
      day.date = currentDayOfMonth;
      day.shortDate = moment(currentDayOfMonth).format("DD");
      day.weekDay = moment(currentDayOfMonth).weekday();
      currentDayOfMonth = moment(currentDayOfMonth).add(1, 'days').format("YYYY-MM-DD");
      week.push(day);
      if (week.length == 7) {
        $scope.month.push(week);
        week = [];
      }
    }

    if ((week.length > 0) && (week.length < 7)) {
      while (week.length < 7) {
        var day = {};
        week.push(day);
      }
      $scope.month.push(week);
    }
  };


  $scope.getDaysFromDb = function(){
    var currentMonthName = moment(now).format("YYYY-MM");
    calendarService.getDaysInMonth(currentMonthName,function(data){
      $scope.persistedDays = data;
      })
  };


  $scope.getDateStatus = function(queryDate){
    var result = 'alert alert-default'
    $scope.persistedDays.forEach(function(day){
      if(day.date == queryDate){
        if(day.dayType == 1) {
          result = 'alert alert-danger'
        }
        if(day.dayType == 2) {
          result = 'alert alert-warning'
        }

      }
    });
    return result;
  };

  $scope.subtractMonth = function () {
    now = moment(now).subtract(1, 'months');
    $scope.openCalendar();
  };


  $scope.addMonth = function () {
    now = moment(now).add(1, 'months');
    $scope.openCalendar();
  };

  $scope.$on('MonthOpened',function(){
    $scope.getDaysFromDb();
  });

  $scope.$on('DaysFromDbImported', function(){
    $scope.countDaysInMonth();
  });

  $scope.$on('DayTypeChanged',function(){
    $scope.getDaysFromDb();
  });

  $scope.setSunday = function(date){
    var command = {};
    command.date = date;
    command.dayType = 1;
    calendarService.setDayType(command);
  };

  $scope.setBonusDay = function(date){
    var command = {};
    command.date = date;
    command.dayType = 2;
    calendarService.setDayType(command);
  };


}]);
