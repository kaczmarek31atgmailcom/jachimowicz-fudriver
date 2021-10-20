"use strict";

angular.module('frontendApp')
.controller("BonusDeleteCtrl",["$scope","BonusService",function($scope,BonusService){

  $scope.deleteBonus = function(id){
    BonusService.deleteBonus(id);
  };

}]);
