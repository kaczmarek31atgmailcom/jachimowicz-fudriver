'use strict';

angular.module('frontendApp')
.factory('CurrencyService',['$rootScope','$http',function($rootScope,$http){
  var service = {};

  service.setActiveCurrencies = function(command){
    $http({
      method: 'POST',
      url: '/fudriver/rest/settings/currency/active',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data){
      $rootScope.$broadcast('ActiveCurrenciesSet',data);
    }, function errorCallback(data){
      $rootScope.$broadcast('error', data);
    });
  };

  service.getAllCurrencies = function (successFn) {
    $http({
      url: '/fudriver/rest/settings/currency',
      method: 'GET'
    })
      .success(function (data) {
        successFn(data);
      })
      .error(function (result) {
        $rootScope.$broadcast('error', result.message);
      })
  };

  service.getActiveCurrencies = function (successFn) {
    $http({
      url: '/fudriver/rest/settings/currency/active',
      method: 'GET'
    })
      .success(function (data) {
        successFn(data);
      })
      .error(function (result) {
        $rootScope.$broadcast('error', result.message);
      })
  };


  return service;
}]);
