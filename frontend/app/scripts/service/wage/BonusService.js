"use strict";

angular.module("frontendApp")
  .factory("BonusService", ["$rootScope", "$http", function ($rootScope, $http) {
    var service = {};

    service.addFixedBonus = function (command) {
      $http({
        method: 'POST',
        url: '/fudriver/rest/salary/fixed-bonus',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response, data) {
        $rootScope.$broadcast('BonusCreated', data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };

    service.addPercentageBonus = function (command) {
      $http({
        method: 'POST',
        url: '/fudriver/rest/salary/percentage-bonus',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response, data) {
        $rootScope.$broadcast('BonusCreated', data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };

    service.getActiveBonuses = function (successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/salary/bonus/active',
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response, data) {
        successFn(response.data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };


    service.deleteBonus = function (id) {
      $http({
        method: 'DELETE',
        url: '/fudriver/rest/salary/bonus',
        data: id,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response, data) {
        $rootScope.$broadcast('BonusDeleted');
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };


    return service;
  }]);
