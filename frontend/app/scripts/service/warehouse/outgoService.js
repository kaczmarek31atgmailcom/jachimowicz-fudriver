'use strict';

angular.module('frontendApp').
  factory('outgoService',['$rootScope','$http',function($rootScope,$http){
  var service = {};




  service.getOrders = function(successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/orders/prepared',
    }).then(function successCallback(response,data){
      successFn(response.data);
      $rootScope.$broadcast('OrdersLoaded');
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };


  service.getOrder = function(orderId,successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/orders/prepared/' + orderId,
    }).then(function successCallback(response,data){
      successFn(response.data);
      $rootScope.$broadcast('OrderLoaded');
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };


  service.releasePalette = function(command){
    $http({
      method: 'PUT',
      url: '/fudriver/rest/order/palette/release',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data){
      $rootScope.$broadcast('PaletteReleased',data);
    }, function errorCallback(data){
      $rootScope.$broadcast('error', data);
    });
  };

  service.unreleasePalette = function(command){
    $http({
      method: 'PUT',
      url: '/fudriver/rest/order/palette/unrelease',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data){
      $rootScope.$broadcast('PaletteUnReleased',data);
    }, function errorCallback(data){
      $rootScope.$broadcast('error', data);
    });
  };

  service.unPrepareOrder = function(command){
    $http({
      method: 'PUT',
      url: '/fudriver/rest/order/unprepare',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data){
      $rootScope.$broadcast('OrderUnReleased',data);
    }, function errorCallback(data){
      $rootScope.$broadcast('error', data);
    });
  };

  service.createWz = function(command){
    $http({
      method: 'POST',
      url: '/fudriver/rest/warehouse/brc/wz',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data){
      $rootScope.$broadcast('WzCreated',data);
    }, function errorCallback(data){
      $rootScope.$broadcast('error', data);
    });
  };



  return service;
}]);
