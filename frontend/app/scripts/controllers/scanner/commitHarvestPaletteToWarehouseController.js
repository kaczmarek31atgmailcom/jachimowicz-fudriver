'use strict';

angular.module('frontendApp')
  .controller('CommitHarvestPaletteToWarehouseCtrl', ['$scope', 'DepotService', 'scannerService','EastMushroomsWarehouseService',
    function ($scope, DepotService, scannerService, EastMushroomsWarehouseService) {

      $scope.headers = [];
      $scope.checkedAmount = 0;
      $scope.checkedWeight = 0;
      $scope.dbClickPreventer = false;

      function init() {
        DepotService.getActiveDepots(function (depots) {
          var defaultPaletteTypeId;
          $scope.depots = depots.sort(compareDepots);
          if (depots.length > 0) {
            $scope.depotId = depots[0].id;
          }
          EastMushroomsWarehouseService.getActivePaletteTypes(function(paletteTypes){
            $scope.paletteTypes = paletteTypes.sort(comparePaletteTypes);
            if(($scope.paletteTypes !== undefined) && ($scope.paletteTypes !== null) && ($scope.paletteTypes.length > 0)){
              $scope.paletteTypeId = $scope.paletteTypes[0].paletteId;
            }

            scannerService.getWozekHeaders(function (headers) {
              headers = $scope.removeOnHoldPalettes(headers);
              $scope.headers = addCheckedFlag(headers);
            });
          })
        })
      }

      init();

      $scope.removeOnHoldPalettes = function(headers){
        var result = [];
        headers.forEach(function(header){
          if(header.spaceId === 0){
            result.push(header);
          }
        });
        return result;
      };

      function addCheckedFlag(headers) {
        headers.forEach(function (header) {
          header.checked = '';
        });
        return headers;
      }

      $scope.check = function (trolleyNr) {
        for (var i = 0; i < $scope.headers.length; i++) {
          if ($scope.headers[i].wozekNr === trolleyNr) {
            $scope.headers[i].checked = !$scope.headers[i].checked;
          }
        }
        $scope.checkedAmount = $scope.countCheckedAmount($scope.headers);
        $scope.checkedWeight = $scope.countCheckedWeight($scope.headers);
      };

      $scope.countCheckedAmount = function (headers) {
        var result = 0;
        headers.forEach(function(header){
          if(header.checked === true){
            result += header.totalPcs;
          }
        });
        return result;
      };

      $scope.countCheckedWeight = function (headers) {
        var result = 0;
        headers.forEach(function(header){
          if(header.checked === true){
            result += header.totalWeight;
          }
        });
        return result;
      };

      $scope.commitTrolley = function () {
        if(!$scope.dbClickPreventer) {
          $scope.dbClickPreventer = true;
          var command = {};
          var harvestPalettes = [];
          $scope.headers.forEach(function (header) {
            if (header.checked) {
              harvestPalettes.push(header.wozekNr);
            }
          });
          if (harvestPalettes.length > 0) {
            command.depotId = $scope.depotId;
            command.paletteTypeId = $scope.paletteTypeId;
            command.harvestPalettes = harvestPalettes;
            scannerService.commitEastMushroomsTrolleyStrightToWarehouse(command);
          }
        }
      };


      function compareDepots(a, b) {
        if (a.name > b.name) {
          return 1;
        }
        if (a.name < b.name) {
          return -1;
        }
        return 0;
      }


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
