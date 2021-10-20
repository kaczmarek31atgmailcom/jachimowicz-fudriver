'use strict';

angular.module('frontendApp')
.controller('ChangeAssignedPaletteTypeCtrl',['$scope','EastMushroomsWarehouseService',function($scope,EastMushroomsWarehouseService){

  function init(){
    var alreadyAssignedPaletteTypeId;
    $scope.paletteTypes = [];
    $scope.params.warehousePalettes.forEach(function(warehousePalette){

      if(warehousePalette.paletteId === parseInt($scope.params.paletteId)){
        alreadyAssignedPaletteTypeId = warehousePalette.paletteTypeId;
      }
    });
    $scope.params.paletteTypes.forEach(function(paletteType){
      if(paletteType.paletteId !== alreadyAssignedPaletteTypeId){
        $scope.paletteTypes.push(paletteType);
      }
    })
  }
init();


  $scope.changePaletteType = function(){
    var command = {};
    command.paletteId = $scope.params.paletteId;
    command.paletteTypeId = $scope.paletteTypeId;
    EastMushroomsWarehouseService.changeWarehousePaletteType(command);
  }
}]);
