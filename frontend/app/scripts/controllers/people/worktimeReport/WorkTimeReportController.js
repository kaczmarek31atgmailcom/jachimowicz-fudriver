'use strict';

angular.module('frontendApp')
  .controller('WorkTimeReportCtrl', ['$scope', 'growl', 'timeSheetService', function ($scope, growl, timeSheetService) {

    $scope.loading = 0;
    $scope.endDay = moment();
    $scope.startDay = moment().subtract(7, 'days');

    $scope.init = function () {
      $scope.days = [];
      $scope.people = [];
      $scope.loading = 1;
      timeSheetService.getWorkTime(moment($scope.startDay).format('YYYY-MM-DD'), moment($scope.endDay).format('YYYY-MM-DD'), function (workMinutes) {
        $scope.workMinutes = workMinutes;
        $scope.days = extractDays(workMinutes);
        $scope.people = extractPeople(workMinutes);
        $scope.loading = 0;
      })
    };
    $scope.init();

    var extractDays = function (workMinutes) {
      var tmp = [];
      var days = [];
      workMinutes.forEach(function (item) {
        tmp.push(item.day)
      });
      days = Array.from(new Set(tmp));
      days.sort();
      return days;
    };

    var extractPeople = function (workMinutes) {
      var tmp = [];
      var people = [];
      workMinutes.forEach(function (item) {
        var person = {};
        person.id = item.personId;
        person.nr = item.nr;
        person.name = item.name;
        person.surname = item.surname;
        tmp[person.id] = person;
      });
      tmp.sort(comparePeople);
      people = Array.from(new Set(tmp));

      for(var i = 0; i< people.length;i++){
        if((people[i] === undefined) || (people[i] === null)){
          people.splice(i,1);
        }
      }
      return people;
    };

    var comparePeople = function (a, b) {
      if (a.personId < b.personId) {
        return -1;
      }
      if (a.personId > b.personId) {
        return 1;
      }
      return 0;
    };

    $scope.findMinutes = function (personId, day) {
      if ((personId !== undefined) && personId !== null) {
        var result = 0;
        for (var i = 0; i < $scope.workMinutes.length; i++) {
          if (($scope.workMinutes[i].personId === personId) && ($scope.workMinutes[i].day === day)) {
            result = parseInt($scope.workMinutes[i].workMinutes);
          }
        }
        return result;
      }
    };

    $scope.findDailyTotal = function(day){
      var result = 0;
      $scope.workMinutes.forEach(function(item){
        if(item.day === day){
          result += item.workMinutes;
        }
      });
    return result;
    };

    $scope.findPersonTotal = function(personId){
      var result = 0;
      $scope.workMinutes.forEach(function(item){
        if(item.personId === personId){
          result += item.workMinutes;
        }
      });
      return result;
    };

    $scope.exportAction = function () {
      switch ($scope.export_action) {
        case 'pdf':
          $scope.$broadcast('export-pdf', {});
          break;
        case 'excel':
          $scope.$broadcast('export-excel', {});
          break;
        case 'doc':
          $scope.$broadcast('export-doc', {});
          break;
        default:
          console.log('no event caught');
      }
    };

    $scope.export = function () {
      $scope.export_action = 'excel';
      $scope.exportAction();
    };


  }]);
