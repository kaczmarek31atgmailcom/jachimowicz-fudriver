"use strict";

angular.module("frontendApp")
.factory("ChamberService",["$rootScope","$http",function($rootScope,$http){
  var service = {};

  service.getActiveChambers = function (successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/settings/chamber/active',
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data) {
      successFn(data.data)
    }, function errorCallback(data) {
      $rootScope.$broadcast('error', data);
    });
  };

  service.deleteChamber = function (chamberId) {
    $http({
      method: 'DELETE',
      url: '/fudriver/rest/settings/chamber/ ' + chamberId,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data) {
      $rootScope.$broadcast("ChamberDeleted");
    }, function errorCallback(data) {
      $rootScope.$broadcast('error', data);
    });
  };

  service.updateDepot = function (command) {
    $http({
      method: 'PUT',
      url: '/fudriver/rest/settings/chamber/depot',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data) {
      $rootScope.$broadcast('ChamberDepotUpdated', data);
    }, function errorCallback(data) {
      $rootScope.$broadcast('error', data);
    });
  };

  service.updateCompany = function (command) {
    $http({
      method: 'PUT',
      url: '/fudriver/rest/settings/chamber/company',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data) {
      $rootScope.$broadcast('ChamberCompanyUpdated', data);
    }, function errorCallback(data) {
      $rootScope.$broadcast('error', data);
    });
  };


  service.updateArea = function (command) {
    $http({
      method: 'PUT',
      url: '/fudriver/rest/settings/chamber/area',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data) {
      $rootScope.$broadcast('ChamberAreaUpdated', data);
    }, function errorCallback(data) {
      $rootScope.$broadcast('error', data);
    });
  };

  service.updateName = function (command) {
    $http({
      method: 'PUT',
      url: '/fudriver/rest/settings/chamber/name',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data) {
      $rootScope.$broadcast('ChamberNameUpdated', data);
    }, function errorCallback(data) {
      $rootScope.$broadcast('error', data);
    });
  };

  service.createChamber = function (command) {
    $http({
      method: 'POST',
      url: '/fudriver/rest/settings/chamber',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data) {
      $rootScope.$broadcast('ChamberCreated', data);
    }, function errorCallback(data) {
      $rootScope.$broadcast('error', data);
    });
  };




  return service;
}]);
