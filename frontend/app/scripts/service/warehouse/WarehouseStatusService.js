'use strict';
angular.module('frontendApp')
.factory('warehouseStatusService',['$rootScope','$http',function($rootScope,$http){
  var service = {};

  service.getTypes = function(successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/warehouse/brc/type'
    }).then(function successCallback(response){
      successFn(response.data);
      $rootScope.$broadcast('TypesLoaded');
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.getDepots = function(successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/warehouse/brc/depot'
    }).then(function successCallback(response){
      successFn(response.data);
      $rootScope.$broadcast('DepotsLoaded');
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.getStock = function(successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/warehouse/brc/stock'
    }).then(function successCallback(response){
      successFn(response.data);
      $rootScope.$broadcast('StockLoaded');
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };


  service.getAgedStock = function(successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/warehouse/brc/stock/age'
    }).then(function successCallback(response){
      successFn(response.data);
      $rootScope.$broadcast('AgedStockLoaded');
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };


  service.getTotalTrolleys = function(successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/wozek/total'
    }).then(function successCallback(response){
      successFn(response.data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };





  return service;
}]);
