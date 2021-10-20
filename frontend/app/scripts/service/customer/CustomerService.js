'use strict';

angular.module('frontendApp')
  .factory('CustomerService', ['$rootScope', '$http', function ($rootScope, $http) {
    var service = {};

    service.getActiveCustomers = function(successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/customer/active'
      }).then(function successCallback(response,data){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };

    service.deleteCustomer = function(customerId){
      $http({
        method: 'DELETE',
        url: '/fudriver/rest/customer/' + customerId
      }).then(function successCallback(response,data){
        $rootScope.$broadcast('CustomerDeleted',response,data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };


    service.createCustomer = function (command) {
      $http({
        method: 'POST',
        url: '/fudriver/rest/customer',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('CustomerCreated',data);
      }).error(function (result) {
        $rootScope.$broadcast('error', result);
      });
    };


    service.updateCustomer = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/customer',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('CustomerUpdated',data);
      }).error(function (result) {
        $rootScope.$broadcast('error', result);
      });
    };


    return service;
  }]);
