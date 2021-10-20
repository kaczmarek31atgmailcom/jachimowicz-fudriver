"use strict";

angular.module("frontendApp")
  .factory("DepotService", ["$rootScope", "$http", function ($rootScope, $http) {
    var service = {};

    service.createDepot = function (command) {
      $http({
        method: 'POST',
        url: '/fudriver/rest/settings/depot',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data) {
        $rootScope.$broadcast('DepotCreated', data);
      }, function errorCallback(data) {
        $rootScope.$broadcast('error', data);
      });
    };

    service.updateDepotName = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/settings/depot',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data) {
        $rootScope.$broadcast('DepotNameUpdated', data);
      }, function errorCallback(data) {
        $rootScope.$broadcast('error', data);
      });
    };


    service.getActiveDepots = function (successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/settings/depot/active',
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data) {
        successFn(data.data)
      }, function errorCallback(data) {
        $rootScope.$broadcast('error', data);
      });
    };

    service.deleteDepot = function (id) {
      $http({
        method: 'DELETE',
        url: '/fudriver/rest/settings/depot/' + id,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data) {
        $rootScope.$broadcast('DepotDeleted', data);
      }, function errorCallback(data) {
        $rootScope.$broadcast('error', data);
      });
    };


    return service;
  }]);
