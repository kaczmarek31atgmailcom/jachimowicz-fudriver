'use strict';

angular.module('frontendApp')
.factory('ReportsService',['$rootScope','$http',function ($rootScope, $http) {
  var service = {};
  service.startDate = null;
  service.endDate = null;
  service.harvest = null;
  service.picker = null;
  service.showPcs = null;

  service.getTypesByPickers = function (startDate,endDate,successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/reports/typesByPickers/' + startDate + '/' + endDate,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.getTypesByPicker = function (pickerId,startDate,endDate,successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/reports/typesByPicker/' + pickerId + '/' + startDate + '/' + endDate,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.getPicker = function (pickerId,successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/reports/typesByPicker/picker/' + pickerId ,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
    });
  };


  return service;
}]);
