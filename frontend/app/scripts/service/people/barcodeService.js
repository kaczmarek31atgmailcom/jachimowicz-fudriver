'use strict';

angular.module('frontendApp').factory('barcodeService', ['$rootScope', '$http',
  function ($rootScope, $http) {
    var service = {};


    service.createBarcodes = function (command) {
      $http({
        method: 'POST',
        url: '/fudriver/rest/person/uniq',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('barcodesCreated',data);
      }).error(function (result) {
        $rootScope.$broadcast('error', result);
      });
    };

    service.deleteBarcodes = function (command) {
      $http({
        method: 'DELETE',
        url: '/fudriver/rest/person/uniq',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('barcodesDeleted',data);
      }).error(function (result) {
        $rootScope.$broadcast('error', result);
      });
    };

    service.resetBarcodes = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/person/barcode/reset',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('barcodesReseted',data);
      }).error(function (result) {
        $rootScope.$broadcast('error', result);
      });
    };


    service.getNotUsedBarcodes = function (personId, successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/person/barcode/notUsed/' + personId
      }).
        success(function(data){
        successFn(data);
        $rootScope.$broadcast('unusedBarcodesLoaded');
      }).
        error(function(result){
        $rootScope.$broadcast('error', result.message);
      })
    };

    service.getBarcodeHeaders = function (successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/person/barcode'
      }).
      success(function(data){
        successFn(data);
        $rootScope.$broadcast('BarcodeHeadersLoaded');
      }).
      error(function(result){
        $rootScope.$broadcast('error', result.message);
      })
    };


    return service;
  }]);
