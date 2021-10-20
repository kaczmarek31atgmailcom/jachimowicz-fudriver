'use strict';

angular.module('frontendApp')
.factory('ProductionOrderService',['$rootScope','$http', function($rootScope,$http){
  var service ={};

  service.getOrders = function(startDate,endDate,successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/productionOrder/' + startDate + '/' + endDate
    }).then(function successCallback(response){
      successFn(response.data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };


  return service;
}])
