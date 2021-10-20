'use strict';

angular.module('frontendApp').factory('collectedByChamberService', ['$rootScope','$http',
  function($rootScope,$http) {
    var service = {};

    service.getHarvestByChamber = function(startDate,endDate,successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/collectedByChamber/' + startDate + '/' + endDate
      }).then(function successCallback(response,data){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };

    return service;
  }]);
