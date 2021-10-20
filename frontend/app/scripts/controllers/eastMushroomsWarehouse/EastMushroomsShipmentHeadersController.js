'use strict';

angular.module('frontendApp')
  .controller('EastMushroomsShipmentHeadersCtrl', ['$scope', 'growl','$location', 'EastMushroomsWarehouseService',
    function ($scope, growl, $location, EastMushroomsWarehouseService) {

      $scope.startDate = EastMushroomsWarehouseService.startDate;
      $scope.endDate = EastMushroomsWarehouseService.endDate;

      $scope.init = function () {
        EastMushroomsWarehouseService.getShipmentHeaders(moment($scope.startDate).format("YYYY-MM-DD"),
          moment($scope.endDate).format("YYYY-MM-DD"), function (headers) {
            $scope.headers = getShipmentSummaryAmount(headers);
            $scope.headers.sort(compareHeaders);
          })
      };
      $scope.init();


      function getShipmentSummaryAmount(shipments) {
        if (shipments.length > 0) {
          shipments.forEach(function (shipment) {
            shipment.totalAmount = 0;
            if (shipment.wzHeaders.length > 0) {
              shipment.wzHeaders.forEach(function (wzHeader) {
                shipment.totalAmount += wzHeader.totalAmount;
              })
            }
          })
        }
        return shipments;
      }


      $scope.showShipment = function(shipmentId){
        EastMushroomsWarehouseService.startDate = $scope.startDate;
        EastMushroomsWarehouseService.endDate = $scope.endDate;
        $location.url("/warehouse-east/shipment/" + shipmentId);
      }

      function compareHeaders(a, b) {
        if (a.id < b.id) {
          return 1;
        }
        if (a.id > b.id) {
          return -1;
        }
        return 0;
      }

      $scope.$on('error',function(){
        growl.error('error',{ttl:5000})
      });


    }]);
