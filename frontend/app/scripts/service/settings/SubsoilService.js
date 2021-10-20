"use strict";

angular.module("frontendApp")
  .factory("SubsoilService", ["$rootScope", "$http", function ($rootScope, $http) {
    var service = {};

    service.createSubsoil = function (command) {
      $http({
        method: 'POST',
        url: '/fudriver/rest/settings/subsoil',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data) {
        $rootScope.$broadcast('SubsoilCreated', data);
      }, function errorCallback(data) {
        $rootScope.$broadcast('error', data);
      });
    };

    service.updateSubsoilName = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/settings/subsoil',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data) {
        $rootScope.$broadcast('SubsoilNameUpdated', data);
      }, function errorCallback(data) {
        $rootScope.$broadcast('error', data);
      });
    };


    service.getActiveSubsoils = function (successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/settings/subsoil/active',
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data) {
        successFn(data.data)
      }, function errorCallback(data) {
        $rootScope.$broadcast('error', data);
      });
    };

    service.deleteSubsoil = function (id) {
      $http({
        method: 'DELETE',
        url: '/fudriver/rest/settings/subsoil/' + id,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data) {
        $rootScope.$broadcast('SubsoilDeleted', data);
      }, function errorCallback(data) {
        $rootScope.$broadcast('error', data);
      });
    };


    return service;
  }]);
