'use strict';

angular.module('frontendApp')
  .controller('EastMushroomsChangePaletteCtrl', ['$scope', '$rootScope', '$modal', 'growl', '$location', 'EastMushroomsWarehouseService',
    function ($scope, $rootScope, $modal, growl, $location, EastMushroomsWarehouseService) {

      function onlyUnique(value, index, self) {
        return self.indexOf(value) === index;
      }

      function init() {
        EastMushroomsWarehouseService.getWarehousePalettesReadyToRelease(function (palettes) {
          $scope.sourcePalettes = $scope.createSourcePalettes(palettes);
        })
      }

      init();


      $scope.goToBoxByBox = function () {
        $location.url('warehouse-east/change-palette-by-box');
      };

      $scope.createSourcePalettes = function (palettes) {
        var sourcePalettes = [];
        palettes.forEach(function (palette) {
          if ((sourcePalettes[palette.paletteId] === undefined) || (sourcePalettes[palette.paletteId] === null)) {
            var sourcePalette = {};
            sourcePalette.paletteId = palette.paletteId;
            sourcePalette.types = [];
            sourcePalette.totalAmount = 0;
            sourcePalettes[palette.paletteId] = sourcePalette;
          } else {
            sourcePalette = sourcePalettes[palette.paletteId];
          }
          var type = {};
          type.typeId = palette.localTypeId;
          type.typeName = palette.localTypeName;
          type.typeWeight = palette.localTypeWeight;
          type.amount = palette.amount;
          sourcePalette.types.push(type);
          sourcePalette.totalAmount += type.amount;
        });
        return sourcePalettes.filter(onlyUnique);
      };

      $scope.openSelectPaletteModal = function (sourcePaletteId) {

        var scope = $rootScope.$new();
        scope.params = {sourcePaletteId: sourcePaletteId, palettes: $scope.sourcePalettes};
        $modal({
          scope: scope,
          animation: true,
          templateUrl: 'views/eastMushroomsWarehouse/select-palette-modal.html',
          show: true
        })
      };

      $scope.$on('WarehousePalettesJoined', function () {
        init();
        growl.success('eastMushrooms.change.palette.select.palette.join.success.message', {ttl: 3000});
      });

      $scope.$on('error', function () {
        growl.error('error', {ttl: 5000})
      })

    }]);


<!------------------------------------------------------------------------------------->
angular.module('frontendApp')
  .controller('EastMushroomsWarehouseSelectPaletteCtrl', ['$scope', 'EastMushroomsWarehouseService',
    function ($scope, EastMushroomsWarehouseService) {

      $scope.sourcePaletteId = $scope.params.sourcePaletteId;
      $scope.palettes = getPalettes($scope.sourcePaletteId, $scope.params.palettes);

      function getPalettes(sourcePaletteId, palettes) {
        var result = [];
        palettes.forEach(function (palette) {
          if (palette.paletteId !== sourcePaletteId) {
            result.push(palette.paletteId);
          }
        });
        return result.sort(comparePalettes);
      }

      $scope.joinPalettes = function () {
        var command = {};
        command.sourcePaletteId = $scope.sourcePaletteId;
        command.targetPaletteId = $scope.targetPaletteId;
        EastMushroomsWarehouseService.joinWarehousePalettes(command);
      };

      function comparePalettes(a, b) {
        if (a > b) {
          return 1;
        }
        if (a < b) {
          return -1;
        }
        return 0;
      }

    }
  ]);
