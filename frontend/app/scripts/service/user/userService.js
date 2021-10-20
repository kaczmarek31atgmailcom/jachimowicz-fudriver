'use strict';

angular.module('frontendApp').
  factory('userService', ['$rootScope','$http', function($rootScope,$http){
  var service = {};

  service.getActiveUsers = function(successFn){
      $http({
        method: 'GET',
        url: '/fudriver/rest/user?active=true'
      }).then(function successCallback(response,data){
        successFn(response.data);
        $rootScope.$broadcast('ActiveUsersLoaded');
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };

  service.getAllLogins = function(successFn){
    $http({
      method: 'GET',
      url: '/fudriver/rest/user/login'
    }).then(function successCallback(response,data){
      successFn(response.data);
      $rootScope.$broadcast('LoginsLoaded');
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.createUser = function(addUserCommand){
    $http({
      method: 'POST',
      url: '/fudriver/rest/user',
      data: addUserCommand,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response,data){
      $rootScope.$broadcast('UserCreated',data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.updateUser = function(editUserCommand){
    $http({
      method: 'PUT',
      url: '/fudriver/rest/user',
      data: editUserCommand,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response,data){
      $rootScope.$broadcast('UserUpdated',data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };

  service.deleteUser = function(deleteUserCommand){
    $http({
      method: 'DELETE',
      url: '/fudriver/rest/user',
      data: deleteUserCommand,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response,data){
      $rootScope.$broadcast('UserDeleted',data);
    }, function errorCallback(response){
      $rootScope.$broadcast('error', response);
    });
  };


  return service;
}]);
