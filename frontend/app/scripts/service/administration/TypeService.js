"use strict";

angular.module("frontendApp")
.factory("TypeService",["$http","$rootScope",function($http,$rootScope){

var service = {};

  service.getActiveTypes = function (successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/type/active'
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallback(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.createType = function (command) {
    $http({
      method: 'POST',
      url: '/fudriver/rest/type',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).success(function (response,data) {
      $rootScope.$broadcast('TypeCreated', response,data);
    }).error(function (result) {
      $rootScope.$broadcast('error', result);
    });
  };

  service.deleteType = function (id) {
    $http({
      method: 'DELETE',
      url: '/fudriver/rest/type/' + id,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data) {
      $rootScope.$broadcast('TypeDeleted', data);
    }, function errorCallback(data) {
      $rootScope.$broadcast('error', data);
    });
  };

  service.updateTypeBoxAssignment = function (command) {
    $http({
      method: 'PUT',
      url: '/fudriver/rest/type/box',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).success(function (response,data) {
      $rootScope.$broadcast('NewBoxAssigned', response,data);
    }).error(function (result) {
      $rootScope.$broadcast('error', result);
      return false;
    });
  };

  service.updateTypeGroupAssignment = function (command) {
    $http({
      method: 'PUT',
      url: '/fudriver/rest/type/typeGroup',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).success(function (response,data) {
      $rootScope.$broadcast('NewTypeGroupAssigned', response,data);
    }).error(function (result) {
      $rootScope.$broadcast('error', result);
      return false;
    });
  };

  service.updateTypeSizeAssignment = function (command) {
    $http({
      method: 'PUT',
      url: '/fudriver/rest/type/typeSize',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).success(function (response,data) {
      $rootScope.$broadcast('NewTypeSizeAssigned', response,data);
    }).error(function (result) {
      $rootScope.$broadcast('error', result);
      return false;
    });
  };

  service.updateTypeName = function (command) {
    $http({
      method: 'PUT',
      url: '/fudriver/rest/type/name',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).success(function (response,data) {
      $rootScope.$broadcast('TypeNameUpdated', response,data);
    }).error(function (result) {
      $rootScope.$broadcast('error', result);
      return false;
    });
  };


  service.getActiveGroups = function (successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/typeGroup/active'
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallback(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.createTypeGroup = function (command) {
    $http({
      method: 'POST',
      url: '/fudriver/rest/typeGroup',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).success(function (response,data) {
      $rootScope.$broadcast('TypeGroupCreated', response,data);
    }).error(function (result) {
      $rootScope.$broadcast('error', result);
    });
  };

  service.updateTypeGroupName = function (command) {
    $http({
      method: 'PUT',
      url: '/fudriver/rest/typeGroup',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).success(function (response,data) {
      $rootScope.$broadcast('TypeGroupNameUpdated', response,data);
    }).error(function (result) {
      $rootScope.$broadcast('error', result);
    });
  };

  service.deleteTypeGroup = function (id) {
    $http({
      method: 'DELETE',
      url: '/fudriver/rest/typeGroup/' + id,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data) {
      $rootScope.$broadcast('TypeGroupDeleted', data);
    }, function errorCallback(data) {
      $rootScope.$broadcast('error', data);
    });
  };

  service.getActiveTypeSizes = function (successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/typeSize/active'
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallback(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.createTypeSize = function (command) {
    $http({
      method: 'POST',
      url: '/fudriver/rest/typeSize',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).success(function (response,data) {
      $rootScope.$broadcast('TypeSizeCreated', response,data);
    }).error(function (result) {
      $rootScope.$broadcast('error', result);
    });
  };

  service.updateTypeSizeName = function (command) {
    $http({
      method: 'PUT',
      url: '/fudriver/rest/typeSize',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).success(function (response,data) {
      $rootScope.$broadcast('TypeSizeNameUpdated', response,data);
    }).error(function (result) {
      $rootScope.$broadcast('error', result);
    });
  };

  service.deleteTypeSize = function (id) {
    $http({
      method: 'DELETE',
      url: '/fudriver/rest/typeSize/' + id,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data) {
      $rootScope.$broadcast('TypeSizeDeleted', data);
    }, function errorCallback(data) {
      $rootScope.$broadcast('error', data);
    });
  };

  service.getTypeSizesByCycle = function (startDate,endDate,successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/typeSize/cycles/' + startDate + '/' + endDate
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallback(response) {
      $rootScope.$broadcast('error', response);
    });
  };


  return service;
}]);
