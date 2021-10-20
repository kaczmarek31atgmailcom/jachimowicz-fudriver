'use strict';

angular.module("frontendApp")
.factory("TrolleyManService",["$rootScope","$http",function($rootScope,$http){

  var service = {};

  service.getAllTrolleyMan = function (successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/trolley-man'
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallback(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.createTrolleyMan = function (command) {
    $http({
      method: 'POST',
      url: '/fudriver/rest/trolley-man',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).success(function (response,data) {
      $rootScope.$broadcast('TrolleyManCreated', response,data);
    }).error(function (result) {
      $rootScope.$broadcast('error', result);
    });
  };

  service.updateTrolleyMan = function (command) {
    $http({
      method: 'PUT',
      url: '/fudriver/rest/trolley-man',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).success(function (response,data) {
      $rootScope.$broadcast('TrolleyManUpdated', response,data);
    }).error(function (result) {
      $rootScope.$broadcast('error', result);
    });
  };

service.getTrolleyManReport = function (startDate,endDate,successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/trolley-man/report/startDate/' + startDate + '/endDate/' + endDate
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallback(response) {
      $rootScope.$broadcast('error', response);
    });
  };


  return service;
}]);
