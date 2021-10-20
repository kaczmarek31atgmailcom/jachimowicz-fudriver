'use strict';

angular.module('frontendApp').
  factory('calendarService',['$rootScope','$http',function($rootScope,$http){
  var service = {};

  service.openMonth = function(command){
    $http({
      method: 'POST',
      url: '/fudriver/rest/calendar',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response,data){
      $rootScope.$broadcast('MonthOpened',data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };


  service.setDayType = function(command){
    $http({
      method: 'PUT',
      url: '/fudriver/rest/calendar',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response,data){
      $rootScope.$broadcast('DayTypeChanged',data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };


  service.getDaysInMonth = function(month,successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/calendar',
      params: {'month': month},
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response,data){
      successFn(response.data);
      $rootScope.$broadcast('DaysFromDbImported',data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };



  return service;
}]);
