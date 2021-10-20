'use strict';

angular.module('frontendApp').factory('waitingBoardService', ['$rootScope','$http',function($rootScope, $http){
  var service = {};

  service.getWaitingHeaders = function(successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/waitingBoard/header'
    }).then(function successCallback(response){
      successFn(response.data);
      $rootScope.$broadcast('HeadersLoaded');
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.commit = function(command){
    $http({
      method: 'POST',
      url: '/fudriver/rest/waiting/wozek/commit',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data){
      $rootScope.$broadcast('WozekCommited',data);
    }, function errorCallback(data){
      $rootScope.$broadcast('WozekCommitError', data);
    });
  };

  service.reject = function(command){
    $http({
      method: 'POST',
      url: '/fudriver/rest/waiting/wozek/reject',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data){
      $rootScope.$broadcast('WozekRejected', data);
    }, function errorCallback(data){
      $rootScope.$broadcast('WozekRejectError', data);
    });
  };



  return service;
}]);
