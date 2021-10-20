"use strict";

angular.module("frontendApp")
  .controller("AddBonusModalCtrl", ["$scope", "BonusService", function ($scope, BonusService) {

    $scope.isFixedFormInvalid = true;
    $scope.isPercentageFormInvalid = true;
    $scope.name = undefined;
    $scope.value = undefined;

    $scope.validateFixedForm = function () {
      $scope.isFixedFormInvalid = false;
      if (($scope.name === undefined) || ($scope.value === undefined)) {
        $scope.isFixedFormInvalid = true;
        return;
      }
      if (!($scope.name.length > 0)) {
        $scope.isFixedFormInvalid = true;
        return;
      }
      var pattern = new RegExp('^[-]?[0-9]+[.]?[0-9]*$');
      if ($scope.value !== undefined) {
        $scope.value.replace(",", ".");
        console.log($scope.value);
        console.log("validations: " + pattern.test($scope.value));
        $scope.isFixedFormInvalid = !pattern.test($scope.value);
      } else {
        $scope.isFixedFormInvalid = true;
      }
    };

    $scope.validatePercentageForm = function () {
      $scope.isPercentageFormInvalid = false;
      if (($scope.name === undefined) || ($scope.value === undefined)) {
        $scope.isPercentageFormInvalid = true;
        return;
      }
      if (!($scope.name.length > 0)) {
        $scope.isPercentageFormInvalid = true;
        return;
      }
      var pattern = new RegExp('^[-]?[0-9]+$');
      if ($scope.value !== undefined) {
        $scope.value.replace(",", ".");
        $scope.isPercentageFormInvalid = !pattern.test($scope.value);
      } else {
        $scope.isPercentageFormInvalid = true;
      }
    };

    $scope.createFixedBonus = function () {
      if (!$scope.isFixedFormInvalid) {
        var command = {};
        command.name = $scope.name;
        command.param = parseInt($scope.value * 100);
        BonusService.addFixedBonus(command);
      }
    };

    $scope.createPercentageBonus = function () {
      if (!$scope.isPercentageFormInvalid) {
        var command = {};
        command.name = $scope.name;
        command.param = parseInt($scope.value);
        BonusService.addPercentageBonus(command);
      }
    };


  }]);
