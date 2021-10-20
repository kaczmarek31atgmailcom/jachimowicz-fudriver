'use strict';

angular.module('frontendApp')
.factory('findBarcodeService',['$rootScope','$http',function($rootScope,$http){
  var service = {};

  service.findCycles = function (day, successFn) {
    $http({
      url: '/fudriver/rest/brc/report/findBox/cycles/' + day,
      method: 'GET',
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
      $rootScope.$broadcast('CyclesLoaded');
    }), function errorCallback(response) {
      $rootScope.$broadcast('error', response);
    }
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


  service.findBox = function (pickerId,uniqId,successFn) {
    $http({
      url: '/fudriver/rest/brc/report/findBox/' + pickerId + '/' + uniqId,
      method: 'GET'
    })
      .success(function (data) {
        successFn(data);
        $rootScope.$broadcast('BoxLoaded');
      })
      .error(function (result) {
        $rootScope.$broadcast('error', result.message);
      })
  };

  service.findBoxZarobki = function (pickerId,uniqId,successFn) {
    $http({
      url: '/fudriver/rest/brc/report/findBoxZarobki/' + pickerId + '/' + uniqId,
      method: 'GET'
    })
      .success(function (data) {
        successFn(data);
        $rootScope.$broadcast('BoxLoaded');
      })
      .error(function (result) {
        $rootScope.$broadcast('error', result.message);
      })
  };




  return service;
}]);
