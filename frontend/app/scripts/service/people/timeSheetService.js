'use strict';

angular.module('frontendApp')
.factory('timeSheetService',['$rootScope','$http',function($rootScope,$http){
  var service = {};

  service.getDayBars = function (startDay, endDay,personId, successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/person/dayBars',
      params: {startDay: startDay, endDay:endDay, personId: personId}
    }).then(function successCallback(response,data){
      successFn(response.data);
      $rootScope.$broadcast('dayBarsLoaded');
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.getWorkPeriods = function (personId, startDay, endDay, successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/person/workPeriods',
      params: {startDay: startDay, endDay:endDay, personId: personId}
    }).then(function successCallback(response,data){
      successFn(response.data);
      $rootScope.$broadcast('workPeriodsLoaded');
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.saveWorkPeriod = function(command){
    $http({
      method: 'PUT',
      url: '/fudriver/rest/person/workPeriod',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response,data){
      $rootScope.$broadcast('WorkPeriodUpdated',data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.saveClosedWorkPeriod = function(command){
    $http({
      method: 'POST',
      url: '/fudriver/rest/workTimeRecorder/closed',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response,data){
      $rootScope.$broadcast('ClosedWorkPeriodCreated',data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.deletePeriod = function(command){
    $http({
      method: 'DELETE',
      url: '/fudriver/rest/person/workPeriod',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response,data){
      $rootScope.$broadcast('WorkPeriodDeleted',response,data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.closePeriod = function(periodId){
    $http({
      method: 'PUT',
      url: '/fudriver/rest/person/workPeriod/close',
      data: periodId,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response,data){
      $rootScope.$broadcast('WorkPeriodClosed',response,data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.getWorkTime = function (startDay, endDay, successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/person/workMinutes/' + startDay + '/' + endDay
    }).then(function successCallback(response,data){
      successFn(response.data);
      $rootScope.$broadcast('workTimeLoaded');
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };



  return service;
}]);
