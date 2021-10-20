'use strict';

angular.module('frontendApp')
  .factory('TruckService',['$rootScope','$http',function($rootScope,$http){
    var service = {};

    service.saveTruck = function(command){
      $http({
        method: 'POST',
        url: '/fudriver/rest/truck',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(data){
        $rootScope.$broadcast('TruckCreated',data);
      }, function errorCallback(data){
        $rootScope.$broadcast('error', data);
      });
    };

    service.getTrucks = function(successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/truck'
      }).then(function successCallback(response,data){
        successFn(response.data);
        $rootScope.$broadcast('TrucksLoaded');
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };

    service.deleteTruck = function(id){
      $http({
        method: 'DELETE',
        url: '/fudriver/rest/truck/' + id
      }).then(function successCallback(response,data){
        $rootScope.$broadcast('TruckDeleted');
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };


    return service;
  }]);
