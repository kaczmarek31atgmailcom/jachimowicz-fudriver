'use strict';

angular.module('frontendApp')
.factory('DeliveryLetterService',['$rootScope','$http',function($rootScope,$http){
  var service = {};

  service.getLetter = function(orderId,successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/deliveryLetter/order/' + orderId
    }).then(function successCallback(response,data){
      successFn(response.data);
      $rootScope.$broadcast('DeliveryLetterLoaded');
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.updatePositions = function (command) {
    $http({
      method: 'PUT',
      url: '/fudriver/rest/deliveryLetter/palettes-position',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).success(function (data) {
      $rootScope.$broadcast('PositionsUpdated',data);
    }).error(function (result) {
      $rootScope.$broadcast('error', result);
    });
  };



  return service;
}]);
