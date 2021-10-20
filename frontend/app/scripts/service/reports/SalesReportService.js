'use strict';

angular.module('frontendApp').
factory('SalesReportService',['$rootScope','$http',function($rootScope,$http){
  var service = {};

  service.getSalesReport = function (startDate,endDate,successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/warehouse/nonbrc/sales-report/' + startDate + '/' + endDate,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
    });
  };


  return service;
}]);
