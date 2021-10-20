'use strict';

angular.module('frontendApp')
.factory('ReclassifyReportService',['$rootScope','$http',function($rootScope,$http){
  var service = {};

  service.getLocalReclassifications = function(startDate, endDate, successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/audit/localReclassification/' + startDate + '/' + endDate
    }).then(function successCallback(response,data){
      successFn(response.data);
      $rootScope.$broadcast('ReclassificationsLoaded');
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };



  return service;
}]);
