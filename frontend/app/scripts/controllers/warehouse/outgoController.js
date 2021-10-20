'use strict';

angular.module('frontendApp').controller('outgoCtrl', ['$scope', '$location', '$interval','outgoService', function ($scope, $location, $interval, outgoService) {


  $scope.init = function () {
    outgoService.getOrders(function (orders) {
      $scope.orders = orders;
    });
  };


  var intervalPromise = $interval(function () {
    outgoService.getOrders(function (orders) {
  //    updateOrders(orders);
      $scope.orders = orders;
    });
  }, 10000);
  $scope.$on('$destroy', function () {
    $interval.cancel(intervalPromise);
  });

$scope.openDeliveryLetter = function(orderId){
  $location.url("/outgo/delivery-letter/" + orderId);
};


  var updateOrders = function (newOrders) {
    var newOrders = newOrders;
    var foundOrder = false;
    for (var i = $scope.orders.length - 1; i >= 0; i--) {
      newOrders.forEach(function (newOrder) {
        if ($scope.orders[i].id == newOrder.id) {
          foundOrder = true;
          order.plate = newOrder.plate;
          order.customerId = newOrder.customerId;
          order.customerName = newOrder.customerName;
          order.requiredDate = newOrder.requiredDate;
        }
      });
      if (!foundOrder) {
        $scope.orders.splice(i, 1);
      }
      foundOrder = false;
    }

    foundOrder = false;
    newOrders.forEach(function (newOrder) {
      $scope.orders.forEach(function (order) {
        if (newOrder.id == order.id) {
          foundOrder = true;
        }
      });
      if (!foundOrder) {
        $scope.orders.push(newOrder);
      }
      foundOrder = false;
    });
  };




  $scope.findPaletteNo = function (paletteNo) {
    return '999' + addTrailingZeroes(paletteNo, 10);
  };

  var addTrailingZeroes = function (item, requiredLenght) {
    var result = item.toString();
    while (result.length < requiredLenght) {
      result = '0' + result;
    }
    return result;
  };

  $scope.openReleaseBoard = function(orderId){
    $location.url("/outgo-release/" + orderId);
  };

  $scope.unPrepareOrder = function(orderId){
    var command = {};
    command.orderId = orderId;
    outgoService.unPrepareOrder(command)
  };

  $scope.$on('OrderUnReleased',function(response,data){
    var removedOrderId = data.data.entityId;
    for(var i =0; i< $scope.orders.length; i++){
      if($scope.orders[i].id == removedOrderId){
        $scope.orders.splice(i,1);
      }
    }
  })

}]);
