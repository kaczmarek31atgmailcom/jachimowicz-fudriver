'use strict';

angular.module('frontendApp')
.controller('EastMushroomsReclassificationCtrl',['$scope','growl','EastMushroomsWarehouseService','scannerService','TypeService',
function($scope,growl,EastMushroomsWarehouseService,scannerService,TypeService){

  $scope.boxes = [];

  $scope.focusInputText = function () {
//      setTimeout(function () {
    //barForm.barcode.focus();
    document.getElementById("barcode").focus();
    //    }, 1);
  };


  function init(){
    scannerService.getSupplierId(function (supplierId) {
      var id = supplierId.toString();
      while (id.length < 3) {
        id = '0' + id;
      }
      $scope.supplierId = id;
      $scope.focusInputText();
    });

    TypeService.getActiveTypes(function(types){
      $scope.types = types;
      if($scope.types.length > 0){
        $scope.currentType = $scope.types[0];
      }
    })
  }

  init();


  $scope.parseBarcode = function(barcode){
    if((barcode === null) || (barcode === undefined)){
      return;
    }
    if(barcode.length === 13){
      if(!$scope.checkValidityOfSupplierId(barcode)){
        errorSound();
        growl.error("eastMushrooms.reclassification.invalid.supplier.id.message", {ttl: 5000});
      } else if($scope.checkIfDigitsOnly(barcode)){
        findWarehouseUnit(barcode.toString().substr(8,5),barcode.toString().substr(3,5));
      }
      $scope.barcode = ''
    }
  };

  function findWarehouseUnit(uniqId,pickerId){
    EastMushroomsWarehouseService.getWarehouseUnit(uniqId,pickerId,function(warehouseUnit){
      warehouseUnit.barcode = $scope.getBarcode(warehouseUnit);
      if(warehouseUnit === null ){
        growl.error("eastMushrooms.reclassification.box.not.found.message", {ttl: 5000});
        errorSound();
        return;
      }

      if($scope.checkIfWarehouseUnitAlreadyScanned(warehouseUnit,$scope.boxes)){
        growl.error("eastMushrooms.reclassification.box.already-scanned.message", {ttl: 5000});
        errorSound();
        return;
      }
      if(warehouseUnit.warehouseUnitStatus === 'RELEASED') {
        growl.error("eastMushrooms.reclassification.box.released.message", {ttl: 5000});
        errorSound();
        return;
      }
      if(warehouseUnit.warehouseUnitStatus === 'CREATED') {
        growl.error("eastMushrooms.reclassification.box.created.message", {ttl: 5000});
        errorSound();
        return;
      }
      if(warehouseUnit.warehouseUnitStatus === 'ON_STOCK') {
        $scope.boxes.push(warehouseUnit);
      }
    })
  }

  $scope.checkIfWarehouseUnitAlreadyScanned = function(warehouseUnit,boxes){
    for(var i=0; i< boxes.length; i++){
      if((boxes[i].uniqId === warehouseUnit.uniqId) && (boxes[i].pickerId === warehouseUnit.pickerId)){
        return true;
      }
    }
  };

  $scope.getBarcode = function(warehouseUnit){
    var barcode = $scope.supplierId.toString();
    var uniqId = addLeadingZeroes(5,warehouseUnit.uniqId);
    var pickerId = addLeadingZeroes(5,warehouseUnit.pickerId);
    barcode += uniqId;
    barcode += pickerId;
    return barcode.toString();
  };

  function addLeadingZeroes(targetLength,inputString){
    var inputString = inputString.toString();
    for(var i = inputString.length; i< targetLength;i++){
      inputString = '0' + inputString;
    }
  return inputString.toString();
  }

  $scope.checkIfDigitsOnly = function(barcode){
    var barcode = barcode.toString();
    if (!barcode.match(/^\d+$/)){
      return false;
    }
    return true;
  };

  $scope.checkValidityOfSupplierId = function(barcode){
    var barcode = barcode.toString();
    if(barcode.length <3){
      return true;
    } else {
      if(barcode.toString().substr(0,3) === $scope.supplierId){
        return true;
      }
      else {
        return;
      }
    }

  };

  function errorSound(){
    var audio = new Audio('/fudriver/sounds/blackhole.wav');
    audio.play();
  }

  $scope.$on('WarehouseUnitReclassified',function(){
    $scope.boxes = [];
    growl.success("eastMushrooms.reclassification.box.success.message", {ttl:5000});
  });

  $scope.$on('error',function(){
    growl.error('error',{ttl:5000})
  });

  $scope.reclassify = function(){
    var command = {};
    command.targetTypeId = $scope.currentType.id;
    command.sourceTypeIds = [];
    $scope.boxes.forEach(function(box){
      command.sourceTypeIds.push(box.id);
    });
    if(command.sourceTypeIds.length > 0){
      EastMushroomsWarehouseService.reclassify(command);
    }
  }


}]);
