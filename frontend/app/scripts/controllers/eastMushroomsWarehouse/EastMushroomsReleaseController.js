'use strict';

angular.module('frontendApp')
  .controller('EastMushroomsReleaseCtrl', ['$scope', '$rootScope', '$timeout','$modal', 'growl', '$location', 'EastMushroomsWarehouseService', 'CustomerService', 'EventService',
    function ($scope, $rootScope, $timeout,$modal, growl, $location, EastMushroomsWarehouseService, CustomerService, EventService) {


      $scope.assignedWarehousePalettes = [];
      $scope.assignedUnitsTotalAmount = 0;
      $scope.warehousePalettes = [];
      $scope.paletteTypes = [];

      function onlyUnique(value, index, self) {
        return self.indexOf(value) === index;
      }

      $scope.focusInputText = function () {
        $timeout(function(){
          document.getElementById("barcode").focus();
        },1000);
      };


      function init() {
        EastMushroomsWarehouseService.getWarehousePalettesReadyToRelease(function (warehousePalettes) {
          $scope.warehousePalettes = warehousePalettes;
          $scope.notAssignedPalettes = $scope.createPalettes(warehousePalettes);
          CustomerService.getActiveCustomers(function (customers) {
            $scope.customers = customers;
            if (customers.length > 0) {
              $scope.customer = customers[0];
            }
          });


          EastMushroomsWarehouseService.getActivePaletteTypes(function (paletteTypes) {
            $scope.paletteTypes = paletteTypes;
            if (($scope.paletteTypes !== undefined) && ($scope.paletteTypes !== null) && ($scope.paletteTypes.length > 0)) {
              $scope.paletteTypeId = $scope.paletteTypes[0].paletteId;
            }
          });
        });
      }

      init();


      $scope.createPalettes = function (warehousePalettes) {
        var result = [];
        warehousePalettes.forEach(function (palette) {
          if (palette.paletteId > 0) {
            if (result[palette.paletteId] !== undefined) {
              result[palette.paletteId].totalAmount += palette.amount;
              result[palette.paletteId].types.push(
                {
                  "localTypeId": palette.localTypeId,
                  "localTypeName": palette.localTypeName,
                  "localTypeWeight": palette.localTypeWeight,
                  "remoteTypeId": palette.remoteTypeId,
                  "remoteTypeName": palette.remoteTypeName,
                  "remoteTypeWeight": palette.remoteTypeWeight,
                  "amount": palette.amount
                }
              )
            } else {
              result[palette.paletteId] = {
                "paletteId": palette.paletteId,
                "totalAmount": palette.amount,
                "types": [{
                  "localTypeId": palette.localTypeId,
                  "localTypeName": palette.localTypeName,
                  "localTypeWeight": palette.localTypeWeight,
                  "remoteTypeId": palette.remoteTypeId,
                  "remoteTypeName": palette.remoteTypeName,
                  "remoteTypeWeight": palette.remoteTypeWeight,
                  "amount": palette.amount
                }]
              }
            }
          }
        });
        return result.filter(onlyUnique);
      };

      $scope.createPalette = function (palette) {
        var result = {};
        result.totalAmount = palette.amount;
        result.paletteId = palette.paletteId;
        result.types = [{
          "localTypeId": palette.localTypeId,
          "localTypeName": palette.localTypeName,
          "localTypeWeight": palette.localTypeWeight,
          "remoteTypeId": palette.remoteTypeId,
          "remoteTypeName": palette.remoteTypeName,
          "remoteTypeWeight": palette.remoteTypeWeight,
          "amount": palette.amount
        }];
        return result;
      };

      $scope.move = function (paletteId) {
        var assignedPalette = $scope.findAssignedPalette(paletteId);
        var notAssignedPalette = $scope.findNotAssignedPalette(paletteId);
        /* Usuwanie palety z wysyłki po drugim skanowaniu
        if (assignedPalette) {
          $scope.notAssignedPalettes.push(assignedPalette);
          for (var i = 0; i < $scope.assignedWarehousePalettes.length; i++) {
            if ($scope.assignedWarehousePalettes[i].paletteId === paletteId) {
              $scope.assignedUnitsTotalAmount -= $scope.assignedWarehousePalettes[i].totalAmount;
              $scope.assignedWarehousePalettes.splice(i, 1);
            }
          }
        }
         */
        if (notAssignedPalette) {
          $scope.assignedWarehousePalettes.push(notAssignedPalette);
          for (var j = 0; j < $scope.notAssignedPalettes.length; j++) {
            if ($scope.notAssignedPalettes[j].paletteId === paletteId) {
              $scope.assignedUnitsTotalAmount += $scope.notAssignedPalettes[j].totalAmount;
              $scope.notAssignedPalettes.splice(j, 1);
            }
          }
        }
        $scope.focusInputText();
      };

      $scope.findAssignedPalette = function (paletteId) {
        for (var i = 0; i < $scope.assignedWarehousePalettes.length; i++) {
          if ($scope.assignedWarehousePalettes[i].paletteId === paletteId) {
            return $scope.assignedWarehousePalettes[i];
          }
        }
      };
      $scope.findNotAssignedPalette = function (paletteId) {
        for (var i = 0; i < $scope.notAssignedPalettes.length; i++) {
          if ($scope.notAssignedPalettes[i].paletteId === paletteId) {
            return $scope.notAssignedPalettes[i];
          }
        }
      };

      $scope.event = EventService.getEvents(function (response) {
        var response = JSON.parse(response);
        var message = response.source;
        if (message === 'EAST_WAREHOUSE_PALETTE_TYPE_CHANGED') {

          EastMushroomsWarehouseService.getWarehousePalettesReadyToRelease(function (warehousePalettes) {
            $scope.warehousePalettes = warehousePalettes;
            $scope.notAssignedPalettes = $scope.createPalettes(warehousePalettes);

            for (var i = 0; i < $scope.notAssignedPalettes.length; i++) {
              $scope.assignedWarehousePalettes.forEach(function (assignedPalette) {
                if (assignedPalette.paletteId === $scope.notAssignedPalettes[i].paletteId) {
                  $scope.notAssignedPalettes.splice(i, 1);
                }
              })
            }
          });
        }

        if (message === 'EAST_WAREHOUSE_PALETTE_ACCEPTED_TO_WAREHOUSE') {
          var newPaletteId = parseInt(response.id);

          $timeout(function(){
            EastMushroomsWarehouseService.getWarehousePaletteReadyToRelease(newPaletteId, function (newPalette) {
              $scope.notAssignedPalettes.push($scope.createPalette(newPalette));
            });
          },2000);


        }
      });

      $scope.isReleaseButtonDisabled = function () {
        if ($scope.assignedWarehousePalettes.length === 0) {
          return true;
        } else {
          if ($scope.customer.producerGroup !== 'None') {
            for (var i = 0; i < $scope.assignedWarehousePalettes.length; i++) {
              for (var j = 0; j < $scope.assignedWarehousePalettes[i].types.length; j++) {
                if ($scope.assignedWarehousePalettes[i].types[j].remoteTypeId === 0) {
                  return true;
                }
              }
            }
          }
        }
      };

      $scope.printPaletteLabel = function (paletteId) {
        EastMushroomsWarehouseService.printPaletteLabel(paletteId);
      };

      $scope.createShipment = function () {
        var command = {};
        command.customerId = $scope.customer.id;
        command.warehousePalettes = [];
        $scope.assignedWarehousePalettes.forEach(function (palette) {
          command.warehousePalettes.push(palette.paletteId);
        });
        if ((command.customerId > 0) && (command.warehousePalettes.length > 0)) {
          EastMushroomsWarehouseService.createShipment(command);
        }
      };

      $scope.showAssignedPaletteType = function (warehousePaletteId) {
        var paletteTypeName;
        $scope.warehousePalettes.forEach(function (warehousePalette) {
          if (warehousePalette.paletteId === parseInt(warehousePaletteId)) {
            var paletteType = findPaletteType(warehousePalette.paletteTypeId);
            if ((paletteType !== null) && (paletteType !== undefined)) {
              paletteTypeName = findPaletteType(warehousePalette.paletteTypeId).name;
            }
          }
        });
        return paletteTypeName;
      };

      function findPaletteType(id) {
        for (var i = 0; i < $scope.paletteTypes.length; i++) {
          if ($scope.paletteTypes[i].paletteId === id) {
            return $scope.paletteTypes[i];
          }
        }
      }

      $scope.parseBarcode = function(barcode){
        if(barcode.length === 13){
          $scope.barcode = '';
          $scope.move(parseInt(barcode.substring(3,13)));
        }
      }


      $scope.showChangeAssignedPaletteTypeModal = function (paletteId) {
        var scope = $rootScope.$new();
        scope.params = {paletteId: paletteId, paletteTypes: $scope.paletteTypes, warehousePalettes: $scope.warehousePalettes};
        $modal({
          scope: scope,
          animation: $scope.animationsEnabled,
          templateUrl: 'views/eastMushroomsWarehouse/modals/changeAssignedPaletteTypeModal.html',
          show: true
        })
      };


      $scope.$on('WarehouseShipmentCreated', function (response, data) {
        var shipmentId = data.data.entityId;
        if (shipmentId > 0) {
          EastMushroomsWarehouseService.showBackToReleasesBreadcrumb = true;
          $location.url("/warehouse-east/shipment/" + shipmentId);
        }

      });

      $scope.$on('error', function () {
        growl.error('error', {ttl: 5000})
      })

      $scope.$watch('customer',function(){
        $scope.focusInputText();
      })




    }]);
