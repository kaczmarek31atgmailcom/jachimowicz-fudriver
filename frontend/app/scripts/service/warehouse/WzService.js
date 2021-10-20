'use strict';

angular.module('frontendApp').factory('wzService', ['$rootScope', '$http', function ($rootScope, $http) {
  var service = {};
  var header = {};

  service.setHeader = function (header) {
    header = header;
  };

  service.getHeader = function () {
    return header;
  };

  service.getHeader = function (id, successFn) {
    if (id != undefined) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/warehouse/wz/brc/header/' + id,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response) {
        successFn(response.data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    }
  };

  service.getHeaders = function (startDate, endDate, successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/warehouse/wz/brc/headers',
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      },
      params: {'startDate': startDate, 'endDate': endDate}
    }).then(function successCallback(response) {
      successFn(response.data);
      $rootScope.$broadcast('HeadersLoaded');
    }, function errorCallback(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.getDetails = function (id, successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/warehouse/wz/brc/details/' + id,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
      $rootScope.$broadcast('DetailsLoaded');
    }, function errorCallback(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.getPalettes = function(id, successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/warehouse/wz/brc/palette/' + id,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response){
      successFn(response.data);
    },function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };


  return service;
}]);
