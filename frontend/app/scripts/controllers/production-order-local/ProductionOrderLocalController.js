'use strict';
angular.module('frontendApp')
  .controller('ProductionOrderLocalCtrl', ['$rootScope','$scope', '$modal','growl', 'ProductionOrderLocalService',
    function ($rootScope,$scope, $modal, growl, ProductionOrderService) {


    $scope.startDate = moment();
    $scope.endDate = moment().add(7, 'days');
    $scope.loading = 0;
    $scope.days = [];

    $scope.init = function () {
      $scope.days = getDaysBetween($scope.startDate,$scope.endDate);
      $scope.loadin = 1;
      ProductionOrderService.getOrders(moment($scope.startDate).format("YYYY-MM-DD"), moment($scope.endDate).format("YYYY-MM-DD"),
        function (orders) {
          $scope.orders = orders;
          $scope.types = $scope.getUniqueTypes(orders);
          $scope.loading = 0;
        })
    }
    $scope.init();


    $scope.getUniqueTypes = function(orders){
      var tmp = [];
      orders.forEach(function(order){
        var type = {};
        type.id = order.typeId;
        type.name = order.typeName;
        type.weight = order.typeWeight;
        tmp[type.id] = type;
      })
      var result = [];
      tmp.forEach(function(item){
        result.push(item);
      })
      return result.sort(compareTypes);
    }

    $scope.openEditOrderModal = function(day,typeId){
      var scope = $rootScope.$new();
      scope.params = {day: day, typeId:typeId};
      $modal({
        scope: scope,
        animation: $scope.animationsEnabled,
        templateUrl: 'views/production-order-local/edit-order-modal.html',
        show: true
      })
    }



    function getDaysBetween(startDate,endDate){
      var days = [];
      var day = startDate;
      while(moment(day).isSameOrBefore(endDate)){
        days.push(moment(day).format("YYYY-MM-DD"));
        day = moment(day).add(1,'day');
      }
      return days;
    }

    $scope.findOrderAmount = function(day,typeId){
      var result = 0;

      for(var i=0; i<= $scope.orders.length;i++){
        if( ($scope.orders[i] !== undefined) && ($scope.orders[i].dueDate !== null) &&($scope.orders[i].dueDate !== undefined) && ($scope.orders[i].typeId === typeId) && ($scope.orders[i].dueDate === day)){
          result =  $scope.orders[i].amount;
          break;
        }
      }
      return result;
    }

    function compareTypes(a,b){
      if(a.name > b.name ){
        return 1;
      }
      if(a.name < b.name){
        return -1;
      }
      return 0;
    }

      $scope.$on('error', function (event, data) {
        growl.error('error',{ttl:5000});
      });

    $scope.$on('OrderUpdated',function(){
      growl.success('production.order.local.message.success',{ttl:4000});
      $scope.init();
    })

  }])
