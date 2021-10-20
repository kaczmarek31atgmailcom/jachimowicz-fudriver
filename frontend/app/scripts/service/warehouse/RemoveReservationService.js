'use strict';

angular.module('frontendApp')
.factory('RemoveReservationService',['$rootScope','$http',function($rootScope,$http){
  var service = {};

  service.getBox = function(supplierId,uniqId,pickerId,successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/order/boxForUnreserve/' + supplierId + '/' + uniqId + '/' + pickerId
    }).then(function successCallback(response){
      successFn(response.data);
      $rootScope.$broadcast('BoxLoaded');
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };


  service.unReserve = function(command){
    $http({
      method: 'PUT',
      url: '/fudriver/rest/warehouse/brc/unreserveList',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data){
      $rootScope.$broadcast('UnreservationSucceed',data);
    }, function errorCallback(data){
      $rootScope.$broadcast('error', data);
    });
  };


  return service;
}]);
