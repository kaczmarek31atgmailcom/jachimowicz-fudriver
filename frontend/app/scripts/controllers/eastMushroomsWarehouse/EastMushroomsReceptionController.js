'use strict';

angular.module('frontendApp')
  .controller('EastMushroomsReceptionCtrl', ['$scope','$rootScope', 'growl', '$timeout','$modal', 'EventService', 'DepotService', 'EastMushroomsWarehouseService',
    function ($scope, $rootScope, growl, $timeout, $modal,EventService, DepotService, EastMushroomsWarehouseService) {

      function onlyUnique(value, index, self) {
        return self.indexOf(value) === index;
      }
      $scope.disabledAcceptKeys = [];

      $scope.models = {
        selected: null,
        lists: {"eastMushroomsWarehouseReceptionNotAssignedHarvestPalettes": []}
      };

      function init() {
        $scope.harvestPalettes = [];
        $scope.warehousePalettes = [];
        $scope.models.lists = {};
        DepotService.getActiveDepots(function (depots) {
          $scope.depots = depots;
          if (depots.length > 0) {
            $scope.depotId = depots[0].id
          }
        });
        EastMushroomsWarehouseService.getActivePaletteTypes(function(paletteTypes){
          $scope.paletteTypes = paletteTypes.sort(comparePaletteTypes);
          if(($scope.paletteTypes !== undefined) && ($scope.paletteTypes !== null) && ($scope.paletteTypes.length > 0)){
            $scope.paletteTypeId = $scope.paletteTypes[0].paletteId;
          }
        });


        EastMushroomsWarehouseService.getWaitingHarvestPalettes(function (harvestPalettes) {
          $scope.harvestPalettes = harvestPalettes;
          EastMushroomsWarehouseService.getCreatedWarehousePalettes(function (warehousePalettes) {
            $scope.warehousePalettes = warehousePalettes;
            $scope.models.lists.eastMushroomsWarehouseReceptionNotAssignedHarvestPalettes = harvestPalettes;
            warehousePalettes.forEach(function (palette) {
              var paletteId = palette.paletteId;
              $scope.models.lists[paletteId] = getAssignedHarvestPalettes(palette);
            })
          })
        })
      }

      init();

      function getAssignedHarvestPalettes(warehousePalette) {
        var result = [];
        warehousePalette.warehouseUnits.forEach(function (unit) {
          if (result[unit.harvestPaletteId] === undefined) {
            result[unit.harvestPaletteId] = {
              "paletteId": unit.harvestPaletteId,
              "typeName": unit.typeName,
              "typeWeight": unit.typeWeight,
              "amount": 1
            }
          } else {
            result[unit.harvestPaletteId].amount += 1;
          }
        });
        return result.filter(onlyUnique);
      }

      $scope.createWarehousePalette = function () {
        var command = {};
        command.depotId = $scope.depotId;
        command.paletteTypeId = $scope.paletteTypeId;
        EastMushroomsWarehouseService.createWarehousePalette(command);
      };

      $scope.countWarehousePaletteTotal = function(warehousePalette) {
       var result = 0;
        warehousePalette.forEach(function(item){
          result += item.amount;
        });
        return result;
      };

      $scope.showChangeAssignedPaletteTypeModal = function(paletteId){
          var scope = $rootScope.$new();
        scope.params = {paletteId: paletteId,paletteTypes: $scope.paletteTypes,warehousePalettes: $scope.warehousePalettes};
          $modal({
            scope: scope,
            animation: $scope.animationsEnabled,
            templateUrl: 'views/eastMushroomsWarehouse/modals/changeAssignedPaletteTypeModal.html',
            show: true
          })
        };



      $scope.event = EventService.getEvents(function (response) {
        var response = JSON.parse(response);
        var message = response.source;
        if (message === 'EAST_WAREHOUSE_PALETTE_CREATED') {
          EastMushroomsWarehouseService.getCreatedWarehousePalette(response.id, function (warehousePalette) {
            $scope.warehousePalettes.push(warehousePalette);
            $scope.models.lists[warehousePalette.paletteId] = [];
          })
        }
        if ((message === 'EAST_WAREHOUSE_PALETTE_ASSIGNED') ||
          (message === 'EAST_WAREHOUSE_HARVEST_PALETTE_REJECTED') ||
          (message === 'EAST_WAREHOUSE_HARVEST_PALETTE_CREATED') ||
          (message === 'EAST_WAREHOUSE_PALETTE_ACCEPTED_TO_WAREHOUSE') ||
          (message === 'EAST_WAREHOUSE_PALETTE_TYPE_CHANGED')
        ) {
          $timeout(init, 400);
        }
      });

      $scope.itemMovedFromHarvestPalette = function (list) {
        for (var property in $scope.models.lists) {
          if ($scope.models.lists.hasOwnProperty(property)) {
            $scope.models.lists[property].forEach(function (item) {
              if (item.paletteId === list.paletteId) {
                assignHarvestPaletteToWarehousePalette(property, item.paletteId)
              }
            });
          }
        }
      };
      $scope.itemMovedFromWarehousePalette = function (list) {
        for (var property in $scope.models.lists) {
          if ($scope.models.lists.hasOwnProperty(property)) {
            $scope.models.lists[property].forEach(function (item) {
              if ((item.paletteId === list.paletteId) && (property !== 'eastMushroomsWarehouseReceptionNotAssignedHarvestPalettes')) {
                moveHarvestPaletteToOtherWarehousePalette(item.paletteId, property);
              }
              if ((item.paletteId === list.paletteId) && (property === 'eastMushroomsWarehouseReceptionNotAssignedHarvestPalettes')) {
                unassignHarvestPaletteFromWarehousePalette(item.paletteId)
              }
            });
          }
        }
      };

      function assignHarvestPaletteToWarehousePalette(warehousePaletteId, harvestPaletteId) {
        var command = {};
        command.warehousePaletteId = warehousePaletteId;
        command.harvestPaletteId = harvestPaletteId;
        EastMushroomsWarehouseService.assignWarehousePalette(command);
      }

      function unassignHarvestPaletteFromWarehousePalette(paletteId) {
        var command = {};
        command.harvestPaletteId = paletteId;
        EastMushroomsWarehouseService.unassignWarehousePalette(command);
      }

      function moveHarvestPaletteToOtherWarehousePalette(harvestPaletteId, warehousePaletteId) {
        var command = {};
        command.harvestPaletteId = harvestPaletteId;
        command.warehousePaletteId = warehousePaletteId;
        EastMushroomsWarehouseService.moveHarvestPalette(command);
      }

      $scope.rejectHarvestPalette = function (harvestPaletteId) {
        var command = {};
        command.wozekNr = harvestPaletteId;
        EastMushroomsWarehouseService.rejectHarvestPalette(command);
      };

      $scope.showAssignedPaletteType = function(warehousePaletteId){
        var paletteTypeName;
          $scope.warehousePalettes.forEach(function (warehousePalette) {
              if (warehousePalette.paletteId === parseInt(warehousePaletteId)) {
                 paletteTypeName = findPaletteType(warehousePalette.paletteTypeId).name;
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

      $scope.acceptWarehousePalette = function (warehousePaletteId) {
        $scope.disabledAcceptKeys[warehousePaletteId] = 1;
        var command = {};
        command.warehousePaletteId = warehousePaletteId;
        EastMushroomsWarehouseService.acceptWarehousePaletteToWarehouse(command);
      };

      function comparePaletteTypes(a,b){
        if(a.sortOrder > b.sortOrder){
          return 1;
        }
        if(a.sortOrder < b.sortOrder){
          return -1;
        }
        if(a.name > b.name){
          return 1;
        }
        if(a.name < b.name ){
          return -1;
        }
        return 0;
      }

    }]);
