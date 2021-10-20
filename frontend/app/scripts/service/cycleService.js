'use strict';

angular.module('frontendApp').factory('cycleService', ['$rootScope','$http',
  function($rootScope,$http) {
    var cycleService = {};

    cycleService.getHeaders = function(successFn){
      $http.get('/fudriver/rest/cycleHeaders')
        .success(function(data){
          successFn(data);
          $rootScope.$broadcast('cycleHeadersLoaded');
        })
        .error(function(result){
          $rootScope.$broadcast('error', result.error);
        })
    };

    cycleService.getHale = function(successFn){
      $http.get('/fudriver/rest/hale')
        .success(function(data){
          successFn(data);
          $rootScope.$broadcast('chumbersLoaded');
        })
        .error(function(result){
          $rootScope.$broadcast('error', result.error);
        })
    };

    cycleService.getDetails = function(cycleId, successFn){
      $http({
        url: '/fudriver/rest/hala/' + cycleId,
        method: 'GET'
      }).success(function(data){
        successFn(data);
        $rootScope.$broadcast("halaStatsLoaded");
      }).error(function(result){
        $rootScope.$broadcast('error', result.message);
      })
    };

    return cycleService;
  }]);
