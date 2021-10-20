'use strict';

angular.module('frontendApp').
  controller('outgoSummaryCtrl',['$scope','$location','outgoService',function($scope,$location,outgoService){

  $scope.customers = [];
  $scope.palettes = [];
  $scope.outgoSummary = [];
  $scope.totalAmount = 0;
  $scope.totalWeight = 0;
  $scope.errorMessage = false;

  $scope.init = function(){
    outgoService.getCustomers(function(data){
      $scope.customers = data;
    });
    outgoService.getPalettes(function(data){
      $scope.palettes = data;
    });
    outgoService.getOutgoSummary(function(data){
      $scope.outgoSummary = data;
      getTotals();
    });
  };

  var getTotals = function(){
    $scope.outgoSummary.forEach(function(item){
      $scope.totalWeight += item.weight;
      $scope.totalAmount += item.amount;
    });
  };

  $scope.submit = function(){
    var command={};
    command.customerId = $scope.customerId;
    command.palettes = findPalettes();
    outgoService.createWz(command);
    $scope.outgoSummary = null;
    $scope.customerId = null;
  };

  $scope.$on('WzCreated', function(event,data){
    $location.url("/wzDetail/" + data.data.entityId);
  });

  $scope.$on('WzCreationError',function(){
    $scope.errorMessage = true;
  });


  var findPalettes = function(){
    var result =[];
    $scope.palettes.forEach(function(item){
      if(item.amount != undefined && item.amount > 0){
        result.push(item);
      }
    });
  return result;
  }
}]);
