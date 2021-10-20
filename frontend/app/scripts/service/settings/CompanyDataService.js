"use strict";

angular.module("frontendApp")
.factory("CompanyDataService",["$rootScope","$http", function ($rootScope,$http){
 var service = {};

  service.updateCompanyData = function(companyId,command){
    $http({
      method: 'PUT',
      url: '/fudriver/rest/settings/company/' + companyId,
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data){
      $rootScope.$broadcast('CompanyDataUpdated',data);
    }, function errorCallback(data){
      $rootScope.$broadcast('error', data);
    });
  };

  service.createCompany = function(command){
    $http({
      method: 'POST',
      url: '/fudriver/rest/settings/company',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(data){
      $rootScope.$broadcast('CompanyCreated',data);
    }, function errorCallback(data){
      $rootScope.$broadcast('error', data);
    });
  };


  service.getCompany = function (companyId,successFn) {
    $http({
      url: '/fudriver/rest/settings/company/' + companyId,
      method: 'GET'
    })
      .success(function (data) {
        successFn(data);
      })
      .error(function (result) {
        $rootScope.$broadcast('error', result.message);
      })
  };

  service.getCompaniesData = function (successFn) {
    $http({
      url: '/fudriver/rest/settings/company',
      method: 'GET'
    })
      .success(function (data) {
        successFn(data);
      })
      .error(function (result) {
        $rootScope.$broadcast('error', result.message);
      })
  };


  return service;
}]);
