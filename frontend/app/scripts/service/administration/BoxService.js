"use strict";

angular.module("frontendApp")
.factory("BoxService",["$rootScope","$http",function($rootScope,$http){

  var service = {};

  service.getActiveBoxes = function (successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/box/active'
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallback(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.createBox = function (command) {
    $http({
      method: 'POST',
      url: '/fudriver/rest/box',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).success(function (response,data) {
      $rootScope.$broadcast('BoxCreated', response,data);
    }).error(function (result) {
      $rootScope.$broadcast('error', result);
    });
  };

  service.updateBoxName = function (command) {
    $http({
      method: 'PUT',
      url: '/fudriver/rest/box',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).success(function (response,data) {
      $rootScope.$broadcast('BoxNameUpdated', response,data);
    }).error(function (result) {
      $rootScope.$broadcast('error', result);
    });
  };

  service.deleteBox = function (id) {
    $http({
      method: 'DELETE',
      url: '/fudriver/rest/box/' + id,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data) {
      $rootScope.$broadcast('BoxDeleted', data);
    }, function errorCallback(data) {
      $rootScope.$broadcast('error', data);
    });
  };

  return service;
}]);
