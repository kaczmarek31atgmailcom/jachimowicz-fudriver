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




    service.reclassifyByBarcode = function(command){
      $http({
        url: '/fudriver/rest/reclassify/byBarcode',
        method: 'PUT',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      })
        .success(function(response,data) {
          $rootScope.$broadcast('ReclassificationSuccessful', response, data);
        })
        .error(function(result){
          $rootScope.$broadcast('error', result);
        })
    };


    service.getCheckCode = function (pickerId,uniqId, successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/uniq/checkCode/pickerId/' + pickerId + '/uniqId/' + uniqId
      }).then(function successCallback(response, data) {
        successFn(response.data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };

    return service;
  }]);
