'use strict';

angular.module('frontendApp').factory('peopleService', ['$rootScope', '$http',
  function ($rootScope, $http) {
    var peopleService = {};

    peopleService.getPeople = function (active, successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/person/headers?active=' + active
      }).then(function successCallback(response,data){
        successFn(response.data);
        $rootScope.$broadcast('peopleHeadersLoaded');
      }, function errorCallback(response){
        $rootScope.$broadcast('no-such-person', response);
      });
    };



    peopleService.getGroups = function (active, successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/person/group?active=' + active
      }).then(function successCallback(response,data){
        successFn(response.data);
        $rootScope.$broadcast('peopleGroupsLoaded');
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };


    peopleService.getPersonByRfid = function (rfid, successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/person/rfid/' + rfid
      }).then(function successCallback(response,data){
        successFn(response.data);
        $rootScope.$broadcast('PersonByRfidFound');
      }, function errorCallback(response){
        $rootScope.$broadcast('error', response);
      });
    };



    peopleService.getYearBars = function(personId,successFn){
      $http.get('/fudriver/rest/person/yearBars/' + personId)
        .success(function(data){
        successFn(data);
      }).error(function(result){
        $rootScope('error', result);
      })
    };

    peopleService.getEmployee = function (employeeId, successFn) {
      $http.get('/fudriver/rest/person/' + employeeId)
        .success(function (data) {
          successFn(data);
          $rootScope.$broadcast('personInformationDownloaded');
        }).error(function (result) {
        $rootScope.$broadcast('error', result.error);
      })
    };

    peopleService.getReservedNumbers = function (successFn) {
      $http.get('/fudriver/rest/person/reservedNumbers')
        .success(function (data) {
          successFn(data);
          $rootScope.$broadcast('ReservedNumbersLoaded');
        }).error(function (result) {
        $rootScope.$broadcast('error', result.error);
      })
    };


    peopleService.getWorkPeriods = function(employeeId, successFn){
      $http.get('/fudriver/rest/person/workPeriod/' + employeeId)
        .success(function(date) {
          successFn(date);
        })
          .error(function(result){
          $rootScope.$broadcast('error', result.error);
        })
    };

    peopleService.updateEmployee = function (employee) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/person',
        data: employee,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('employeeUpdated',data)
      }).error(function (result) {
        $rootScope.$broadcast('error', result);
      });
    };

    peopleService.createEmployee = function (employee) {
      $http({
        method: 'POST',
        url: '/fudriver/rest/person',
        data: employee,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('employeeCreated',data);
      }).error(function (result) {
        $rootScope.$broadcast('error', result);
      });
    };


    peopleService.activate = function (employee) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/person/activate',
        data: employee,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('employeeActivated');
      }).error(function (result) {
        $rootScope.$broadcast('error', result);
      });
    };

    peopleService.inactivate = function (employee) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/person/inactivate',
        data: employee,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('employeeInactivated');
      }).error(function (result) {
        $rootScope.$broadcast('error', result);
      });
    };

    peopleService.updateTimeSheet = function (timeSheet) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/person/timeSheet',
        data: timeSheet,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('timeSheetUpdated');
      }).error(function (result) {
        $rootScope.$broadcast('error', result);
      });
    };

    peopleService.registerBadge = function (command) {
      $http({
        method: 'POST',
        url: '/fudriver/rest/person/badge',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response){
        $rootScope.$broadcast('badgeRegistered');
      }, function errorCallback(response){
        $rootScope.$broadcast('badgeRegistrationError',response.data);
      });
    };

    peopleService.changePassword = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/person/password',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response){
        $rootScope.$broadcast('passwordChanged');
      }, function errorCallback(response){
        $rootScope.$broadcast('error',response.data);
      });
    };


    peopleService.deleteBadge = function (command) {
      $http({
        method: 'DELETE',
        url: '/fudriver/rest/person/badge',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response){
        $rootScope.$broadcast('badgeDeleted');
      }, function errorCallback(response){
        $rootScope.$broadcast('badgeRegistrationError',response.data);
      });
    };

    peopleService.getForeignerAlert = function (successFn) {
      $http({
        method: 'GET',
        url: '/fudriver/rest/person/foreignerAlert',
      }).then(function successCallback(response){
        successFn(response.data);
      }, function errorCallback(response){
        $rootScope.$broadcast('no-such-person', response);
      });
    };


    peopleService.updateForeignerAlert = function (command) {
      $http({
        method: 'PUT',
        url: '/fudriver/rest/person/foreignerAlert',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).then(function successCallback(response){
        $rootScope.$broadcast('foreignerAlertUpdated');
      }, function errorCallback(response){
        $rootScope.$broadcast('error',response.data);
      });
    };


    return peopleService;
  }]);
