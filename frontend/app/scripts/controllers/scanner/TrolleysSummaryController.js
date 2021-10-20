"use strict";

angular.module("frontendApp")
.controller("TrolleysSummaryCtrl",["$scope","growl","$location","scannerService",function($scope,growl,$location,scannerService){

  var init = function(){
    scannerService.getTrolleysSummary(function(summary){
      $scope.summary = summary;
      $scope.totalAmount = getTotalAmount(summary);
      $scope.totalWeight = getTotalWeight(summary);
    })
  };
  init();

  var getTotalAmount = function(summary){
    var result = 0;
    summary.forEach(function(item){
      result += item.totalAmount;
    });
    return result;
  };

  var getTotalWeight = function(summary){
    var result = 0;
    summary.forEach(function(item){
      result += item.totalWeight;
    });
    return result;
  }

}]);
