'use strict';

angular.module('frontendApp')
.controller('EditCompanyCtrl',["$scope","$location","growl","$routeParams","CompanyDataService",
  function($scope,$location,growl,$routeParams,companyDataService){

  var companyId = $routeParams.companyId;

  var init = function(){
    companyDataService.getCompany(companyId,function(company){
      $scope.company = company;
    })
  };
  init();

    $scope.updateCompanyData = function(){
      var command = angular.copy($scope.company);
      companyDataService.updateCompanyData(companyId,command);
    };

    $scope.$on("error",function(){
      growl.error("error",{ttl:5000});
    });

    $scope.$on("CompanyDataUpdated",function(){
      init();
      growl.success("settings.company.message.success", {ttl:3000});
    })

}]);
