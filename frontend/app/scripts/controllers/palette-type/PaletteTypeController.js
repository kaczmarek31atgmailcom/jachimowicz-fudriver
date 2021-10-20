'use strict';

angular.module('frontendApp')
  .controller('PaletteTypeCtrl',['$scope','growl','EastMushroomsWarehouseService',function($scope,growl,EastMushroomsWarehouseService){


    function init(){
      EastMushroomsWarehouseService.getActivePaletteTypes(function(palettes){
        $scope.palettes = palettes.sort(comparePalettes);
      })
    }
    init();


    $scope.paletteMoved = function(){
      var index = 1;
      console.log($scope.palettes);
      $scope.palettes.forEach(function(palette){
        palette.sortOrder = index;
        index++;
      })
      var command = {}
      command.palettes = angular.copy($scope.palettes);
      EastMushroomsWarehouseService.updatePaletteTypeSortOrder(command);
    }


    function comparePalettes(a,b){
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

    $scope.$on('error',function(){
      growl.error("error",{ttl:5000});
    });

  }]);
