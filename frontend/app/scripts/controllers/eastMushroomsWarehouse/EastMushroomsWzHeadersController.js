'use strict';

angular.module('frontendApp')
  .controller('EastMushroomsWzHeadersCtrl', ['$scope', 'growl', '$location', 'EastMushroomsWarehouseService',
    function ($scope, growl, $location, EastMushroomsWarehouseService) {

      $scope.startDate = EastMushroomsWarehouseService.startDate;
      $scope.endDate = EastMushroomsWarehouseService.endDate;

      $scope.init = function () {
        EastMushroomsWarehouseService.getShipmentHeaders(moment($scope.startDate).format("YYYY-MM-DD"),
          moment($scope.endDate).format("YYYY-MM-DD"), function (headers) {
            $scope.headers = extractWzHeaders(headers);
            $scope.headers.sort(compareHeaders);
          })
      };
      $scope.init();

      function extractWzHeaders(headers){
        var result = [];
        if(headers.length > 0){
          headers.forEach(function(header){
            if(header.wzHeaders.length > 0){
              header.wzHeaders.forEach(function(wzHeader){
                result.push(wzHeader);
              })
            }
          })
        }
        return result;
      }

      $scope.showWz = function (wzId) {
        EastMushroomsWarehouseService.showBackToReleasesBreadcrumb = false;
        EastMushroomsWarehouseService.startDate = $scope.startDate;
        EastMushroomsWarehouseService.endDate = $scope.endDate;
        $location.url("/warehouse-east/wz/" + wzId);
      };

      function compareHeaders(a, b) {
        if (a.id < b.id) {
          return 1;
        }
        if (a.id > b.id) {
          return -1;
        }
        return 0;
      }
    }]);
