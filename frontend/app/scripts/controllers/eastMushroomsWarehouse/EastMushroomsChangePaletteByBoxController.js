'use strict';

angular.module('frontendApp')
  .controller('EastMushroomsChangePaletteByBoxCtrl', ['$scope', 'growl', '$location', 'scannerService', 'DepotService', 'EastMushroomsWarehouseService',
    function ($scope, growl, $location, scannerService, DepotService, EastMushroomsWarehouseService) {

      $scope.boxes = [];

      function onlyUnique(value, index, self) {
        return self.indexOf(value) === index;
      }


      function init() {
        DepotService.getActiveDepots(function (depots) {
          $scope.depots = depots.sort(compareDepots);
          if (depots.length > 0) {
            $scope.depotId = depots[0].id;
          }
        });
        EastMushroomsWarehouseService.getWarehousePalettesReadyToRelease(function (palettes) {
          $scope.palettes = $scope.getPalettes(palettes);
          if ($scope.palettes.length > 0) {
            $scope.currentPalette = $scope.palettes[0];
          }
        });

        EastMushroomsWarehouseService.getActivePaletteTypes(function(paletteTypes){
          $scope.paletteTypes = paletteTypes.sort(comparePaletteTypes);
          if(($scope.paletteTypes !== undefined) && ($scope.paletteTypes !== null) && ($scope.paletteTypes.length > 0)){
            $scope.paletteTypeId = $scope.paletteTypes[0].paletteId;
          }
        });


        scannerService.getSupplierId(function (supplierId) {
          var id = supplierId.toString();
          while (id.length < 3) {
            id = '0' + id;
          }
          $scope.supplierId = id;
          $scope.focusInputText();
        });
      }

      init();

      $scope.getPalettes = function (palettes) {
        var result = [];
        palettes.forEach(function (palette) {
          result[palette.paletteId] = {"paletteId": palette.paletteId};
        });
        return result.filter(onlyUnique);
      };


      $scope.focusInputText = function () {
        document.getElementById("barcode").focus();
      };


      $scope.goToPalettes = function () {
        $location.url('warehouse-east/change-palette');
      };

      $scope.parseBarcode = function (barcode) {
        if ((barcode === null) || (barcode === undefined)) {
          return;
        }
        if (barcode.length === 13) {
          if (!$scope.checkValidityOfSupplierId(barcode)) {
            errorSound();
            growl.error("eastMushrooms.reclassification.invalid.supplier.id.message", {ttl: 5000});
          } else if ($scope.checkIfDigitsOnly(barcode)) {
            findWarehouseUnit(barcode.toString().substr(8, 5), barcode.toString().substr(3, 5));
          }
          $scope.barcode = ''
        }
      };

      function findWarehouseUnit(uniqId, pickerId) {
        EastMushroomsWarehouseService.getWarehouseUnit(uniqId, pickerId, function (warehouseUnit) {
          warehouseUnit.barcode = $scope.getBarcode(warehouseUnit);
          if (warehouseUnit === null) {
            growl.error("eastMushrooms.reclassification.box.not.found.message", {ttl: 5000});
            errorSound();
            return;
          }

          if ($scope.checkIfWarehouseUnitAlreadyScanned(warehouseUnit, $scope.boxes)) {
            growl.error("eastMushrooms.reclassification.box.already-scanned.message", {ttl: 5000});
            errorSound();
            return;
          }
          if (warehouseUnit.warehouseUnitStatus === 'RELEASED') {
            growl.error("eastMushrooms.reclassification.box.released.message", {ttl: 5000});
            errorSound();
            return;
          }
          if (warehouseUnit.warehouseUnitStatus === 'CREATED') {
            growl.error("eastMushrooms.reclassification.box.created.message", {ttl: 5000});
            errorSound();
            return;
          }
          if (warehouseUnit.warehouseUnitStatus === 'ON_STOCK') {
            $scope.boxes.push(warehouseUnit);
          }
        })
      }

      $scope.checkValidityOfSupplierId = function (barcode) {
        var barcode = barcode.toString();
        if (barcode.length < 3) {
          return true;
        } else {
          if (barcode.toString().substr(0, 3) === $scope.supplierId) {
            return true;
          } else {
            return;
          }
        }

      };

      $scope.checkIfDigitsOnly = function (barcode) {
        var barcode = barcode.toString();
        if (!barcode.match(/^\d+$/)) {
          return false;
        }
        return true;
      };

      $scope.getBarcode = function (warehouseUnit) {
        var barcode = $scope.supplierId.toString();
        var uniqId = addLeadingZeroes(5, warehouseUnit.uniqId);
        var pickerId = addLeadingZeroes(5, warehouseUnit.pickerId);
        barcode += uniqId;
        barcode += pickerId;
        return barcode.toString();
      };

      function addLeadingZeroes(targetLength, inputString) {
        var inputString = inputString.toString();
        for (var i = inputString.length; i < targetLength; i++) {
          inputString = '0' + inputString;
        }
        return inputString.toString();
      }

      $scope.checkIfWarehouseUnitAlreadyScanned = function (warehouseUnit, boxes) {
        for (var i = 0; i < boxes.length; i++) {
          if ((boxes[i].uniqId === warehouseUnit.uniqId) && (boxes[i].pickerId === warehouseUnit.pickerId)) {
            return true;
          }
        }
      };

      $scope.addPalette = function () {
        if ($scope.depots.length === 1) {
          var command = {};
          command.depotId = angular.copy($scope.depots[0].id)
          command.paletteTypeId = angular.copy($scope.paletteTypeId);
          createPaletteAction(command);
        }
        if ($scope.depots.length > 1) {
          openSelectDepotModal();
        }
      };

      function openSelectDepotModal() {
        var scope = $rootScope.$new();
        scope.params = {depots: $scope.depots};
        $modal({
          scope: scope,
          animation: true,
          templateUrl: 'views/eastMushroomsWarehouse/select-depot-modal.html',
          show: true
        })
      }

      function createPaletteAction(command) {
        EastMushroomsWarehouseService.createAcceptedWarehousePalette(command);
      }

      $scope.moveBoxes = function(){
        var command = {};
        command.targetPaletteId = $scope.currentPalette.paletteId;
        command.boxes = [];
        $scope.boxes.forEach(function(box){
          command.boxes.push(box.id);
        });
        if((command.targetPaletteId > 0) && command.boxes.length > 0){
          EastMushroomsWarehouseService.moveBoxesBetweenWarehousePalettes(command);
        }
      };


      function errorSound() {
        var audio = new Audio('/fudriver/sounds/blackhole.wav');
        audio.play();
      }

      function compareDepots(a, b) {
        if (a.name > b.name) {
          return 1;
        }
        if (a.name < b.name) {
          return -1;
        }
        return 0;
      }

      $scope.$on('error',function(){
        growl.error('error',{ttl:5000})
      });

      $scope.$on('WarehousePaletteCreated',function(response,data){
        var newPaletteId;
        if(data.hasOwnProperty('data')){
          if(data.data.hasOwnProperty('entityId')){
            newPaletteId = data.data.entityId;
          }
        }
        if((newPaletteId !== undefined) && (newPaletteId !== null)){
          var palette = {paletteId : newPaletteId};
          $scope.palettes.push(palette);
        }
        growl.success('eastMushrooms.change.palette.by.box.new.palette.created.message', {ttl:3000})
      });

      $scope.$on('BoxesMovedToOtherPalette',function(){
        $scope.boxes = [];
        growl.success('eastMushrooms.change.palette.by.box.success.message', {ttl:3000})
      })

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


<!------------------------------------------------------------------------------------->
angular.module('frontendApp')
  .controller('EastMushroomsSelectDepotCtrl', ['$scope', 'EastMushroomsWarehouseService', function ($scope, EastMushroomsWarehouseService) {

    $scope.depots = $scope.params.depots;
    $scope.createPaletteAction = function() {
      if(($scope.depotId !== undefined) && ($scope.depotId !== null)){
        EastMushroomsWarehouseService.createAcceptedWarehousePalette($scope.id);

      }
    }
  }
  ]);
