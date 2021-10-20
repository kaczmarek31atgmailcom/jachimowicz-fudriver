'use strict';

angular.module('frontendApp')
  .factory('wageService', ['$rootScope', '$http', function ($rootScope, $http) {
    var service = {};
    var typeId;
    var typeName;

    service.setTypeName = function (newTypeName) {
      typeName = newTypeName;
    };

    service.getTypeName = function () {
      return typeName;
    };

    service.setTypeId = function (newTypeId) {
      typeId = newTypeId;
    };

    service.getTypeId = function () {
      return typeId;
    };

    service.getHeaders = function (successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/wage/wageHeader',
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response, data) {
        successFn(response.data);
        $rootScope.$broadcast('WageHeadersLoaded', data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };

    service.getWages = function (headerId, successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/wage/' + headerId,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response, data) {
        successFn(response.data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };

    service.getActivePeopleWages = function (successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/wage/activePeople',
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response, data) {
        successFn(response.data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };


    service.updateWage = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/wage',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response, data) {
        $rootScope.$broadcast('WageUpdated', data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };

    service.updateEmployeePayrollType = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/wage/employee/payrollType',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response, data) {
        $rootScope.$broadcast('EmployeePayrollTypeUpdated', data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };

    service.updateEmployeeAccordHeader = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/wage/employee/accordHeader',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response, data) {
        $rootScope.$broadcast('EmployeePayrollTypeUpdated', data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };

    service.updateEmployeeHourlyRegularWage = function (command, callBack) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/wage/employee/hourlyRegularWage',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response) {
        callBack(response);
      }, function errorCallback(response) {
        callBack(response);
      });
    };

    service.updateEmployeeHourlySundayWage = function (command, callBack) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/wage/employee/hourlySundayWage',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response) {
        callBack(response);
      }, function errorCallback(response) {
        callBack(response);
      });
    };

    service.updateEmployeeHourlyBonusWage = function (command, callBack) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/wage/employee/hourlyBonusWage',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response) {
        callBack(response)

      }, function errorCallback(response) {
        callBack(response)
      });
    };

    service.addWageHeader = function (command) {
      $http({
        method: 'POST',
        url: '/fudriver/rest/wage/wageHeader',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response, data) {
        $rootScope.$broadcast('WageHeaderCreated', data);
      }, function errorCallback(response) {
        $rootScope.$broadcast('error', response);
      });
    };


    return service;
  }]);
