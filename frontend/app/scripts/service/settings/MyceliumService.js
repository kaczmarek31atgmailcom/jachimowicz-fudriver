"use strict";

angular.module("frontendApp")
  .factory("MyceliumService", ["$rootScope", "$http", function ($rootScope, $http) {
    var service = {};

    service.createMycelium = function (command) {
      $http({
        method: 'POST',
        url: '/fudriver/rest/settings/mycelium',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data) {
        $rootScope.$broadcast('MyceliumCreated', data);
      }, function errorCallback(data) {
        $rootScope.$broadcast('error', data);
      });
    };

    service.updateMyceliumName = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/settings/mycelium/name',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data) {
        $rootScope.$broadcast('MyceliumNameUpdated', data);
      }, function errorCallback(data) {
        $rootScope.$broadcast('error', data);
      });
    };


    service.getActiveMyceliums = function (successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/settings/mycelium/active',
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data) {
        successFn(data.data)
      }, function errorCallback(data) {
        $rootScope.$broadcast('error', data);
      });
    };

    service.deleteMycelium = function (id) {
      $http({
        method: 'DELETE',
        url: '/fudriver/rest/settings/mycelium/' + id,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data) {
        $rootScope.$broadcast('MyceliumDeleted', data);
      }, function errorCallback(data) {
        $rootScope.$broadcast('error', data);
      });
    };

    return service;
  }]);
