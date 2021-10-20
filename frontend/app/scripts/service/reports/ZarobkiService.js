'use strict';

angular.module('frontendApp')
.factory('ZarobkiService',['$rootScope','$http',function($rootScope,$http){
  var service = {};

  service.getScannerManReport = function (startDate,endDate, successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/zarobki/scannerManReport/' + startDate + '/' + endDate
    }).
    success(function(data){
      successFn(data);
    }).
    error(function(result){
      $rootScope.$broadcast('error', result.message);
    })
  };

  return service;
}]);
