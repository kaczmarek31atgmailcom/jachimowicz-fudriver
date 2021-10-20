'use strict';

angular.module('frontendApp')
  .controller('ProductionOrderCtrl', ['$scope', 'growl', 'ProductionOrderService',
    function ($scope, growl, ProductionOrderService) {

      var startDate = moment().subtract(1, 'days').format("YYYY-MM-DD");
      var endDate = moment().add(3, 'days').format("YYYY-MM-DD");


      $scope.getDaysBetween = function (startDate, endDate) {
        var result = [];
        for (var day = startDate; moment(day).isSameOrBefore(endDate); day = moment(day).add(1, 'days')) {
          result.push(moment(day).format("YYYY-MM-DD"));
        }
        return result;
      }


      $scope.days = $scope.getDaysBetween(startDate, endDate);

      function init() {
        ProductionOrderService.getOrders(startDate, endDate, function (orders) {
          $scope.orders = orders;
          $scope.types = $scope.getTypes(orders);

        })
      }

      init();


      $scope.findOrder = function (day, warehouseTypeId, orders) {
        for (var i = 0; i < orders.length; i++) {
          if ((orders[i].warehouseTypeId === warehouseTypeId) && (moment(orders[i].dueDate).isSame(day))){
            //return orders[i].deliveredAmount + '/' + orders[i].dueAmount;
            return orders[i].dueAmount;
          }
        }
        return '0';
      }

      $scope.getTypes = function(orders){
        var tmp = [];
        orders.forEach(function(order){
          var type = {};
          type.warehouseTypeId = order.warehouseTypeId;
          type.warehouseTypeName = order.warehouseTypeName;
          type.warehouseTypeWeight = order.warehouseTypeWeight;
          type.localTypeId = order.localTypeId;
          type.localTypeName = order.localTypeName;
          type.localTypeWeight = order.localTypeWeight;
          tmp[type.warehouseTypeId] = type;
        })
        var result = [];
        tmp.forEach(function(item){
          result.push(item);
        })
        return result.sort(compareTypes);
      }


      function compareTypes(a,b){
        if(a.warehouseTypeName > b.warehouseTypeName){
          return 1;
        }
        if(a.warehouseTypeName < b.warehouseTypeName){
          return -1;
        }
        return 0;
      }

      $scope.$on('error',function(){
        growl.error('error',{ttl:5000})
      });


    }])
