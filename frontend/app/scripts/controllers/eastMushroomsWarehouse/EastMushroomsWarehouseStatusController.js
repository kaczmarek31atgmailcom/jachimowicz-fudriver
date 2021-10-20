'use strict';

angular.module('frontendApp')
.controller('EastMushroomsWarehouseStatusCtrl',['$scope','growl','EastMushroomsWarehouseService',function($scope,growl,EastMushroomsWarehouseService){

  function onlyUnique(value, index, self) {
    return self.indexOf(value) === index;
  }


  function init(){
    EastMushroomsWarehouseService.getWarehouseStatus(function(stock){
      $scope.stock = $scope.countTypeTotalWeight(stock);
      $scope.totalAmount = $scope.getTotalAmount($scope.stock);
      $scope.totalWeight = $scope.getTotalWeight($scope.stock);
    });
    EastMushroomsWarehouseService.getWarehousePalettesReadyToRelease(function(palettes){
      $scope.palettes = palettes;
      $scope.palettesTotalAmount = $scope.getPalettesAmount(palettes);
    })
  }
  init();


  $scope.countTypeTotalWeight = function(stock){
    stock.forEach(function(item){
      item.totalWeight = Math.round(item.typeWeight * item.amount * 100)/100;
    });
    return stock.sort(compareStockTypes);
  };

  $scope.getTotalAmount = function(stock){
    var totalAmount = 0;
    stock.forEach(function(item){
      totalAmount += item.amount;
    });
    return totalAmount;
  };

  $scope.getTotalWeight = function(stock){
    var totalWeight = 0;
    stock.forEach(function(item){
      totalWeight += (item.typeWeight * item.amount);
    });
    return Math.round(totalWeight * 100)/100;
  };

  $scope.getPalettesAmount = function(palettes){
    var palettesNumberArray = [];
    palettes.forEach(function(palette){
      if((palette.amount > 0 ) && (palette.warehousePaletteStatus === 'ON_STOCK')){
        palettesNumberArray[palette.paletteId] = palette.paletteId;
      }
    });
    return palettesNumberArray.filter(onlyUnique).length;
  };

  function compareStockTypes(a,b){
    if(a.typeName > b.typeName){
      return 1;
    }
    if(a.typeName < b.typeName){
      return -1;
    }
    return 0;
  }
}]);
