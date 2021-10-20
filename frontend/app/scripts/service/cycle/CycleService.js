"use strict";

angular.module("frontendApp")
  .factory("CycleService", ["$rootScope", "$http", function ($rootScope, $http) {
    var service = {};

    service.getAllCycles = function (startDay, endDay, successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/cycle/' + startDay + '/' + endDay
      }).then(function successCallback(response) {
        successFn(response.data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };

    service.getSingleCycleHeader = function (cycleId, successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/cycle/header/' + cycleId
      }).then(function successCallback(response) {
        successFn(response.data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };


    service.getDates = function (successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/cycle/dates'
      }).then(function successCallback(response) {
        successFn(response.data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };

    service.getSingleCycleDatesById = function (id, successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/cycle/' + id + '/dates'
      }).then(function successCallback(response) {
        successFn(response.data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };

    service.getSingleCycleDetails = function (id, successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/cycle/' + id + '/details'
      }).then(function successCallback(response) {
        successFn(response.data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };


    service.getSingleCycleDatesByChamberId = function (id, successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/cycle/chamber/' + id + '/dates'
      }).then(function successCallback(response) {
        successFn(response.data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };

    service.createCycle = function (command) {
      $http({
        method: 'POST',
        url: '/fudriver/rest/cycle',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (response,data) {
        $rootScope.$broadcast('CycleCreated', response,data);
      }).error(function (result) {
        $rootScope.$broadcast('error', result);
      });
    };

    service.updateCycleStartDate = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/cycle/startDate',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('CycleUpdated', data);
      }).error(function (result,data) {
        $rootScope.$broadcast('error', result,data);
      });
    };

    service.updateCycleStartFirstPeriodDate = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/cycle/startFirstPeriod',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('CycleUpdated', data);
      }).error(function (result,data) {
        $rootScope.$broadcast('error', result,data);
      });
    };


    service.updateCycleStartSecondPeriodDate = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/cycle/startSecondPeriod',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('CycleUpdated', data);
      }).error(function (result,data) {
        $rootScope.$broadcast('error', result,data);
      });
    };

    service.updateCycleStartThirdPeriodDate = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/cycle/startThirdPeriod',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('CycleUpdated', data);
      }).error(function (result,data) {
        $rootScope.$broadcast('error', result,data);
      });
    };

    service.updateCycleArea = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/cycle/area',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('CycleUpdated', data);
      }).error(function (result,data) {
        $rootScope.$broadcast('error', result,data);
      });
    };

    service.updateCycleWeight = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/cycle/weight',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('CycleUpdated', data);
      }).error(function (result,data) {
        $rootScope.$broadcast('error', result,data);
      });
    };

    service.updateCycleHumidity = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/cycle/humidity',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('CycleUpdated', data);
      }).error(function (result,data) {
        $rootScope.$broadcast('error', result,data);
      });
    };

    service.updateCycleMycelium = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/cycle/mycelium',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('CycleUpdated', data);
      }).error(function (result,data) {
        $rootScope.$broadcast('error', result,data);
      });
    };

    service.updateCycleSubsoil = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/cycle/subsoil',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('CycleUpdated', data);
      }).error(function (result,data) {
        $rootScope.$broadcast('error', result,data);
      });
    };

    service.updateCycleTechnologist = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/cycle/technologist',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('CycleUpdated', data);
      }).error(function (result,data) {
        $rootScope.$broadcast('error', result,data);
      });
    };

    service.closeCycle = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/cycle/close',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('CycleClosed', data);
      }).error(function (result,data) {
        $rootScope.$broadcast('error', result,data);
      });
    };

    service.getCyclesByBrigades = function (startDate, endDate, successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/cycle/brigades/' + startDate + '/' + endDate
      }).then(function successCallback(response) {
        successFn(response.data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };

    service.getTechnologistsHeadersByCycles = function (startDate, endDate, successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/cycle/technologists/header/' + startDate + '/' + endDate
      }).then(function successCallback(response) {
        successFn(response.data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };

    return service;
  }]);
