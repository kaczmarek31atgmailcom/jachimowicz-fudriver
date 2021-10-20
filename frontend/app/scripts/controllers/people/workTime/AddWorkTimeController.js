"use strict";

angular.module("frontendApp")
.controller("AddWorkTimeCtrl",["$scope","$route","growl","timeSheetService",function($scope,$route,growl,timeSheetService){

  $scope.employeeId = $route.current.params["employeeId"];
  $scope.day = $route.current.params["day"];

  $scope.init = function () {
    timeSheetService.getWorkPeriods($scope.employeeId, $scope.day, $scope.day, function (workPeriods) {
      $scope.workPeriods = workPeriods;
    })
  };
  $scope.init();
}]);
