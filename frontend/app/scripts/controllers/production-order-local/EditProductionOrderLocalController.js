'use strict';

angular.module('frontendApp')
.controller('EditProductionOrderLocalCtrl',['$scope','ProductionOrderLocalService', function($scope,ProductionOrderLocalService){

  $scope.day = $scope.params.day;
  $scope.typeId = $scope.params.typeId;

  console.log($scope.params);
  function init(){
    ProductionOrderLocalService.getOrder($scope.day, $scope.typeId,function(order){
      $scope.order = order;
    })
  }
init();

  $scope.updateOrder = function(amount){
    var command = {}
    command.typeId = angular.copy($scope.typeId)
    command.day = angular.copy($scope.day)
    command.amount = amount;
    ProductionOrderLocalService.updateOrder(command);
  }

}])
