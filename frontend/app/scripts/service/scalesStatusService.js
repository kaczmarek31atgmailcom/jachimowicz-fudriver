'use strict';

angular.module('frontendApp').factory('scalesStatusService', ['$rootScope','$http',
  function($rootScope,$http) {
    var scalesStatusService = {};

    scalesStatusService.getStatus = function(successFn) {
      $http.get( '/fudriver/rest/scalesStatus')
        .success(function (data) {
          successFn(data);
          $rootScope.$broadcast('scalesStatusLoaded');
        }).error(function(result) {
          $rootScope.$broadcast('error', result.error);
        });
    };

    return scalesStatusService;
  }]);
