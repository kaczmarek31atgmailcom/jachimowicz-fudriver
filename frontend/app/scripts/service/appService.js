'use strict';
angular.module('frontendApp').factory('appService', ['$rootScope','$http',
  function($rootScope, $http){
    var service = {};

    service.getMassHarvest = function(successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/settings/massHarvest'
      }).then(function successCallback(response,data){
        successFn(response.data);
        $rootScope.$broadcast('massHarvestConfigLoaded');
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };


    service.getActiveBrc = function(successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/settings/activeBrc'
      }).then(function successCallback(response,data){
        successFn(response.data);
        $rootScope.$broadcast('activeBrcConfigLoaded');
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };

    service.getActiveEastMushrooms = function(successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/settings/activeEastMushrooms'
      }).then(function successCallback(response){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };


    service.getWarehouseOrders = function(successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/settings/warehouseOrders'
      }).then(function successCallback(response){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };


    service.getVersion = function(successFn){
      $http({
        method: 'GET',
        url: '/fudriver/version/git.properties'
      }).then(function successCallback(response,data){
        successFn(response.data);
        $rootScope.$broadcast('versionLoaded');
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };


    return service;
  }]);
