'use strict';

angular.module("frontendApp")
.controller("CompanyDataCtrl",["$scope","growl","$location","CompanyDataService",function($scope,growl,$location,CompanyDataService){

  var init = function(){
    CompanyDataService.getCompaniesData(function(companies){
      $scope.companies = companies
    })
  };
  init();

  $scope.openEditPage = function(company){
    $location.url("company/" + company.id);
  };

  $scope.openCreatePage = function(){
    $location.url("company-create");
  };



}]);
