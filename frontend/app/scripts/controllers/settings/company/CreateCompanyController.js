'use strict';

angular.module('frontendApp')
  .controller('CreateCompanyCtrl',["$scope","$location","growl","CompanyDataService",
    function($scope,$location,growl,companyDataService){

      $scope.company= {};

      $scope.createCompany = function(company){
        var command = {};
        command.name = company.name;
        command.street = company.street;
        command.city = company.city;
        command.nip = company.nip;
        command.regon = company.regon;
        command.phoneNo = company.phoneNo;
        command.email = company.email;
        command.ggn = company.ggn;
        companyDataService.createCompany(command);
      };


      $scope.$on('CompanyCreated', function () {
        $location.url("/company");
      });

      $scope.$on('error',function(){
        growl.error('error',{ttl:5000})
      });

    }]);
