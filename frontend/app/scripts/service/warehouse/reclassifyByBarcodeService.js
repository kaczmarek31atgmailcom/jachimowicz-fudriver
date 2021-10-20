'use strict';

angular.module('frontendApp')
.factory('reclassifyByBarcodeService',['$rootScope','$http',function($rootScope,$http){
  var service = {};

  service.getTypes = function(successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/warehouse/reclassifyByCode/types'
    }).then(function successCallback(response){
      successFn(response.data);
      $rootScope.$broadcast('TypesLoaded');
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };


  service.findBox = function(uniqId,pickerId,successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/warehouse/reclassifyByCode/box/' + uniqId + '/' + pickerId
    }).then(function successCallback(response){
      successFn(response.data);
      $rootScope.$broadcast('BoxLoaded');
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };



  service.getSupplierId = function (successFn) {
    $http({
      url: '/fudriver/rest/config/supplierId',
      method: 'GET'
    })
      .success(function (data) {
        successFn(data);
        $rootScope.$broadcast('SupplierIdLoaded');
      })
      .error(function (result) {
        $rootScope.$broadcast('error', result.message);
      })
  };

  service.getReasons = function (successFn) {
    $http({
      url: '/fudriver/rest/warehouse/reclassifyByCode/reclassifyReason',
      method: 'GET'
    })
      .success(function (data) {
        successFn(data);
        $rootScope.$broadcast('reasonsLoaded');
      })
      .error(function (result) {
        $rootScope.$broadcast('error', result.message);
      })
  };


  service.reclassify = function(command){
    $http({
      method: 'PUT',
      url: '/fudriver/rest/warehouse/brc/reclassifyByBarcode',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data){
      $rootScope.$broadcast('ReclassificationSucceed',data);
    }, function errorCallback(data){
      $rootScope.$broadcast('error', data);
    });
  };



  return service;
}]);
