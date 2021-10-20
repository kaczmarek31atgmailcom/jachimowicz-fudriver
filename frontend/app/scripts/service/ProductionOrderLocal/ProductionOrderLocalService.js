'use strict';

angular.module('frontendApp')
  .factory('ProductionOrderLocalService',['$rootScope','$http', function($rootScope,$http){
    var service ={};

    service.getOrders = function(startDate,endDate,successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/productionOrderLocal/startDate/' + startDate + '/endDate/' + endDate
      }).then(function successCallback(response){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };


    service.getOrder = function(day,typeId,successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/productionOrderLocal/day/' + day + '/typeId/' + typeId
      }).then(function successCallback(response){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };

    service.updateOrder = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/productionOrderLocal',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('OrderUpdated',data);
      }).error(function (result) {
        $rootScope.$broadcast('error', result);
      });
    };


    return service;
  }])
