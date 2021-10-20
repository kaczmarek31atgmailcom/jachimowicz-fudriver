'use strict';

angular.module('frontendApp')
  .controller('wageDetailsCtrl', ['$scope', '$location', 'wageService', function ($scope, $location, wageService) {

    $scope.today = new Date();
    $scope.regularErrorMessage = false;
    $scope.sundayErrorMessage = false;
    $scope.bonusErrorMessage = false;
    $scope.regularValue = '';
    $scope.sundayValue = '';
    $scope.bonusValue = '';


    $scope.init = function () {
      if (wageService.getTypeId() == undefined) {
        $location.url("/wage");
      } else {
        wageService.getMinEditDate(wageService.getTypeId(), function (date) {
          $scope.minDate = moment(date.minDate).format("YYYY-MM-DD");
          $scope.minNonPayedDate = moment(date.minNonPayedDate).format("YYYY-MM-DD");
        });
        wageService.getWageHistory(wageService.getTypeId(), function (wages) {
          $scope.wages = wages;
        });
        $scope.typeName = wageService.getTypeName();
      }
    };

    $scope.valueChanged = function () {
      if (($scope.regularValue.length > 0 ) && (($scope.regularValue == 'true') || (isNaN($scope.regularValue)))) {
        $scope.regularErrorMessage = true;
      } else {
        $scope.regularErrorMessage = false;
      }

      if (($scope.sundayValue.length > 0 ) && (($scope.sundayValue == 'true') || (isNaN($scope.sundayValue)))) {
        $scope.sundayErrorMessage = true;
      } else {
        $scope.sundayErrorMessage = false;
      }

      if (($scope.bonusValue.length > 0) && ($scope.bonusValue == 'true') || (isNaN($scope.bonusValue))) {
        $scope.bonusErrorMessage = true;
      } else {
        $scope.bonusErrorMessage = false;
      }
    };

    $scope.submitButtonDisabled = function () {
      var result = true;
      if (!$scope.regularErrorMessage && !$scope.sundayErrorMessage && !$scope.bonusErrorMessage && ($scope.regularValue > 0) && ($scope.sundayValue > 0) && ($scope.bonusValue > 0) && ($scope.startDate != undefined)) {
        result = false;
      }
      return result;
    };

    $scope.submit = function(){
      var command = {};
      command.typeId = wageService.getTypeId();
      command.startDate = $scope.startDate;
      command.regularValue = parseInt($scope.regularValue * 100);
      command.sundayValue = parseInt($scope.sundayValue * 100);
      command.bonusValue = parseInt($scope.bonusValue * 100);
      wageService.addWage(command);
    };


    $scope.$on('WageCreated',function(){
      wageService.getMinEditDate(wageService.getTypeId(), function (date) {
        $scope.minDate = moment(date.minDate).format("YYYY-MM-DD");
      });
      wageService.getWageHistory(wageService.getTypeId(), function (wages) {
        if(wages.length > $scope.wages.length){
          $scope.wages.unshift(wages[0]);
        }
      });
      $scope.startDate = '';
    });

    $scope.isWageEditable = function(startDate){
      return moment(startDate).isSameOrAfter($scope.minNonPayedDate);
    };

    $scope.deleteWage = function(startDate){
      var command = {};
      command.typeId = wageService.getTypeId();
      command.startDate = startDate;
      wageService.deleteWage(command);
    };

    $scope.$on('WageDeleted',function(){
      wageService.getMinEditDate(wageService.getTypeId(), function (date) {
        $scope.minDate = moment(date.minDate).format("YYYY-MM-DD");
        $scope.minNonPayedDate = moment(date.minNonPayedDate).format("YYYY-MM-DD");
      });
      wageService.getWageHistory(wageService.getTypeId(), function (wages) {
        var found = false;
        for(var i = 0; i< $scope.wages.length;i++){
            wages.forEach(function(newWage){
            if(newWage.startDate == $scope.wages[i].startDate){
              found = true;
            }
          });
        if(!found){
          $scope.wages.splice(i,1);
        }
          found = false;
        }
      });

    })
  }]);
