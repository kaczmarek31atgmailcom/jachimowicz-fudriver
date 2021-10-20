"use strict";

angular.module("frontendApp")
  .controller("PeopleWagesCtrl", ["$scope", "growl", 'wageService', function ($scope, growl, wageService) {

    $scope.payrollTypes = [];
    $scope.payrollTypes.push("ACCORD");
    $scope.payrollTypes.push("HOURLY");

    var init = function () {
      wageService.getHeaders(function (accordHeaders) {
        $scope.accordHeaders = accordHeaders;
        wageService.getActivePeopleWages(function (people) {
          $scope.people = divideWagesByHundred(people);
        })
      });
    };
    init();

    function divideWagesByHundred(people){
      people.forEach(function(person){
        person.hourlyRegularWage = (person.hourlyRegularWage / 100).toString().replace(".",",");
        person.hourlySundayWage = (person.hourlySundayWage / 100).toString().replace(".",",");
        person.hourlyBonusWage = (person.hourlyBonusWage / 100).toString().replace(".",",");
      });
      return people;
    }

    $scope.updatePayrollType = function(personId,payrollType){
      var command = {};
      command.personId = personId;
      command.payrollType = payrollType;
      wageService.updateEmployeePayrollType(command);
    };

    $scope.updateAccordHeader = function(person){
      var command = {};
      command.personId = person.id;
      command.headerId = person.wageHeaderId;
      wageService.updateEmployeeAccordHeader(command);
    };

    $scope.updateRegularWage = function(personId,value){
      console.log("Updating regular wage: " + personId + " " + value);
      value = Math.round((value.toString().replace(",",".")) * 100);
      if(!(validateNumber(value))){
        growl.error("error.digitOnly",{ttl:5000});
        return false;
      }
      var command = {};
      command.personId = personId;
      command.value = value;
      wageService.updateEmployeeHourlyRegularWage(command,function(response){
        if(response.status !== 200){
          growl.error("error",{ttl:5000});
          return false;
        }
        growl.success("payroll.people.wages.message.wage-updated", {ttl:3000});
        return true;
      });
    };

    $scope.updateSundayWage = function(personId,value){
      value = Math.round((value.toString().replace(",",".")) * 100);
      if(!(validateNumber(value))){
        growl.error("error.digitOnly",{ttl:5000});
        return false;
      }
      var command = {};
      command.personId = personId;
      command.value = value;
      wageService.updateEmployeeHourlySundayWage(command,function(response){
        if(response.status !== 200){
          growl.error("error",{ttl:5000});
          return false;
        }
        growl.success("payroll.people.wages.message.wage-updated", {ttl:3000});
        return true;
      });
    };

    $scope.updateBonusWage = function(personId,value){
      value = Math.round((value.toString().replace(",",".")) * 100);
      if(!(validateNumber(value))){
        growl.error("error.digitOnly",{ttl:5000});
        return false;
      }
      var command = {};
      command.personId = personId;
      command.value = value;
      wageService.updateEmployeeHourlyBonusWage(command,function(response){
        if(response.status !== 200){
          growl.error("error",{ttl:5000});
          return false;
        }
        growl.success("payroll.people.wages.message.wage-updated", {ttl:3000});
        return true;
      });
    };

    var validateNumber = function(number){
      var regexp = /^\d+$/;
      return regexp.test(number.toString());
    };

    $scope.$on("error",function(){
      growl.error("error",{ttl:5000});
      init();
    });

    $scope.$on("EmployeeHourlyWageUpdated", function(){
      growl.success("payroll.people.wages.message.wage-updated", {ttl:3000})
    });

    $scope.$on("EmployeePayrollTypeUpdated", function(){
      growl.success("payroll.people.wages.message.payrollType-updated", {ttl:3000})
    })



  }]);
