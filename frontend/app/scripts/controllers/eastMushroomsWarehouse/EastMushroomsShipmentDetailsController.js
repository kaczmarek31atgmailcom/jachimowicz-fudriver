'use strict';

angular.module('frontendApp')
  .controller('EastMushroomsShipmentDetailsCtrl', ['$scope', 'growl', '$routeParams', '$location', 'EastMushroomsWarehouseService', 'SupplierIdService',
    function ($scope, grow, $routeParams, $location, EastMushroomsWarehouseService, SupplierIdService) {

      var shipmentId = $routeParams.shipmentId;

      function init() {
        SupplierIdService.then(function (service) {
          $scope.producerId = service.supplierId;

          EastMushroomsWarehouseService.getShipmentHeader(shipmentId, function (header) {
            $scope.header = header;
          });

          EastMushroomsWarehouseService.getShipmentPalettes(shipmentId, function (palettes) {
            $scope.typeTotals = $scope.getTypesSummary(palettes);
            $scope.totalNumberOfBoxes = $scope.getTotalNumberOfBoxes(palettes);
            $scope.totalWeight = $scope.getTotalWeight(palettes);
            $scope.paletteTypeTotals = $scope.getPaletteTypeSummary(palettes);
            $scope.palettes = palettes;

          })
        });
      }

      init();

      $scope.getTypesSummary = function (palettes) {
        var tmp = [];
        palettes.forEach(function (palette) {
          palette.types.forEach(function (type) {
            if ((tmp[type.typeId] === undefined) || (tmp[type.typeId] === null)) {
              var typeTotal = {};
              typeTotal.typeId = type.typeId;
              typeTotal.typeName = type.typeName;
              typeTotal.typeWeight = type.typeWeight;
              typeTotal.amount = type.amount;
              typeTotal.weight = type.weight;
              tmp[type.typeId] = (typeTotal);
            } else {
              tmp[type.typeId].weight += type.weight;
              tmp[type.typeId].amount += type.amount;
            }
          })
        });
        var result = [];
        tmp.forEach(function (line) {
          result.push(line);
        });
        return result;
      };

      $scope.getTotalNumberOfBoxes = function (palettes) {
        var result = 0;
        palettes.forEach(function (palette) {
          palette.types.forEach(function (type) {
            result += type.amount;
          })
        });
        return result;
      };

      $scope.getPaletteTypeSummary = function (palettes) {
        var tmp = [];
        palettes.forEach(function (palette) {
          if ((tmp[palette.paletteTypeId] === undefined) || (tmp[palette.paletteTypeId] === null)) {
            var paletteTotal = {};
            paletteTotal.paletteTypeId = palette.paletteTypeId;
            paletteTotal.paletteTypeName = palette.paletteTypeName;
            paletteTotal.amount = 1;
            tmp[palette.paletteTypeId] = paletteTotal;
          } else {
            tmp[palette.paletteTypeId].amount++;
          }
        });
        var result = [];
        tmp.forEach(function (palette) {
          result.push(palette);
        });
        return result;
      };

      $scope.getTotalWeight = function(palettes){
        var result = 0;
        palettes.forEach(function(palette){
          palette.types.forEach(function (type){
            result += type.weight;
          })
        });
        return result;
      };


      $scope.showWz = function (wzId, header) {
        EastMushroomsWarehouseService.shipment = header;

        $location.url("warehouse-east/wz/" + wzId);
      }
    }]);
