'use strict';

angular.module('frontendApp')
  .controller('EastMushroomsWzCtrl', ['$scope', 'growl', '$routeParams', 'SupplierIdService', 'EastMushroomsWarehouseService',
    function ($scope, growl, $routeParams, SupplierIdService, EastMushroomsWarehouseService) {
      var wzId = $routeParams.wzId;
      $scope.showBackToReleaseBreadcrumb = EastMushroomsWarehouseService.showBackToReleasesBreadcrumb;
      $scope.shipment = EastMushroomsWarehouseService.shipment;
      EastMushroomsWarehouseService.shipment= {'id':''};
      function init() {
        SupplierIdService.then(function (service) {
          var producerId = service.supplierId;

          EastMushroomsWarehouseService.getWz(wzId, function (wz) {
            $scope.wz = wz;
            $scope.showRemoteTypes = shouldShowRemoteTypes(wz);
            $scope.types = $scope.getTypes(wz);
            $scope.palettes = $scope.getPalettes(wz, producerId);
            $scope.totalAmount = $scope.getTotalAmount(wz);
            $scope.totalWeight = $scope.getTotalWeight(wz);
          })
        });
      }

      init();

      $scope.getTypes = function (wz) {
        var types = [];
        var tmp = [];
        wz.types.forEach(function (item) {
          if ((tmp[item.localTypeId] === undefined) || (tmp[item.localTypeId] === null)) {
            var type = {};
            type.localTypeId = item.localTypeId;
            type.localTypeName = item.localTypeName;
            type.localTypeWeight = item.localTypeWeight;
            type.remoteTypeId = item.remoteTypeId;
            type.remoteTypeWeight = item.remoteTypeWeight;
            type.remoteTypeName = item.remoteTypeName;
            type.amount = item.amount;
            type.totalWeight = Math.round(item.amount * item.localTypeWeight * 100) / 100;
            tmp[item.localTypeId] = type;
          } else {
            tmp[item.localTypeId].amount += item.amount;
            tmp[item.localTypeId].totalWeight = Math.round(tmp[item.localTypeId].amount * tmp[item.localTypeId].localTypeWeight * 10) / 10;
          }
        });
        tmp.forEach(function (item) {
          types.push(item);
        });
        return types.sort(compareTypes);
      };

      $scope.addLeadingZeroes = function (length, item) {
        if (item !== undefined) {
          while (item.toString().length < length) {
            item = '0' + item;
          }
          return item.toString();
        }
      };

      $scope.getPalettes = function (wz, producerId) {
        var tmp = [];
        wz.types.forEach(function (item) {
          var type = {};
          if ((tmp[item.warehousePaletteId] === undefined) || (tmp[item.warehousePaletteId] === null)) {
            var palette = {};
            palette.id = $scope.addLeadingZeroes(3, producerId) + $scope.addLeadingZeroes(10, item.warehousePaletteId);
            palette.amount = item.amount;
            palette.types = [];
            palette.totalWeight = Math.round(item.localTypeWeight * item.amount * 10) / 10;
            type.localTypeId = item.localTypeId;
            type.localTypeWeight = item.localTypeWeight;
            type.localTypeName = item.localTypeName;
            type.remoteTypeId = item.remoteTypeId;
            type.remoteTypeName = item.remoteTypeName;
            type.remoteTypeWeight = item.remoteTypeWeight;
            type.amount = item.amount;
            type.weight = Math.round(type.localTypeWeight * type.amount * 10) / 10;
            palette.types.push(type);
            tmp[item.warehousePaletteId] = palette;
          } else {
            tmp[item.warehousePaletteId].amount += item.amount;
            tmp[item.warehousePaletteId].totalWeight += (Math.round(item.localTypeWeight * item.amount * 100) / 100);
            type.localTypeId = item.localTypeId;
            type.localTypeWeight = item.localTypeWeight;
            type.localTypeName = item.localTypeName;
            type.remoteTypeId = item.remoteTypeId;
            type.remoteTypeName = item.remoteTypeName;
            type.remoteTypeWeight = item.remoteTypeWeight;
            type.amount = item.amount;
            type.weight = Math.round(type.localTypeWeight * type.amount * 10) / 10;
            tmp[item.warehousePaletteId].types.push(type);

          }
        });
        var palettes = [];
        tmp.forEach(function (item) {
          palettes.push(item);
        });
        return palettes.sort(comparePalettes);
      };

      $scope.getTotalAmount = function (wz) {
        var amount = 0;
        wz.types.forEach(function (type) {
          amount += type.amount;
        });
        return amount;
      };

      $scope.getTotalWeight = function (wz) {
        var totalWeight = 0;
        wz.types.forEach(function (type) {
          totalWeight += (type.localTypeWeight * type.amount);
        });
        return Math.round(totalWeight * 100)/100;
      };


      function shouldShowRemoteTypes(wz) {
        for (var i = 0; i < wz.types.length; i++) {
          if (wz.types[i].remoteTypeId > 0) {
            return true;
          }
        }
      }

      function compareTypes(a, b) {
        if (a.localTypeName > b.localTypeName) {
          return 1;
        }
        if (a.localTypeName < b.localTypeName) {
          return -1;
        }
        return 0;
      }

      function comparePalettes(a, b) {
        if (a.id > b.id) {
          return 1;
        }
        if (a.id < b.id) {
          return -1;
        }
        return 0;
      }
    }]);
