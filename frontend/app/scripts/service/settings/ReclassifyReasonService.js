'use strict';

angular.module('frontendApp')
.factory('reclassifyReasonService',['$rootScope','$http',function($rootScope,$http){
  var service = {};

  service.saveReason = function(command){
    $http({
      method: 'POST',
      url: '/fudriver/rest/settings/reclassifyReason',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data){
      $rootScope.$broadcast('ReasonCreated',data);
    }, function errorCallback(data){
      $rootScope.$broadcast('error', data);
    });
  };

  service.removeReason = function(command){
    $http({
      method: 'DELETE',
      url: '/fudriver/rest/settings/reclassifyReason',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response,data){
      $rootScope.$broadcast('ReasonRemoved',response,data);
    }, function errorCallback(data){
      $rootScope.$broadcast('error', data);
    });
  };


  service.getReasons = function (successFn) {
    $http({
      url: '/fudriver/rest/settings/reclassifyReason',
      method: 'GET'
    })
      .success(function (data) {
        successFn(data);
        $rootScope.$broadcast('reasonsLoaded');
      })
      .error(function (result) {
        $rootScope.$broadcast('error', result.message);
      })
  };



  return service;
}]);
