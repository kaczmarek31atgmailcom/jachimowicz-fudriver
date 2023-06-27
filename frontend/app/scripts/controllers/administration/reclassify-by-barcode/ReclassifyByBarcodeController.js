'use strict';

angular.module('frontendApp')
.controller('ReclassifyByBarcodeCtrl',['$scope','growl','barcodeService','scannerService','TypeService',
  function($scope,growl,barcodeService,scannerService,TypeService){


  $scope.details = [];
  $scope.barcode = "";
  $scope.showSpinner = false;
  $scope.farmId;
  $scope.orderByField = 'code';
$scope.targetTypeId = {};

  $scope.setOrderByField = function (value) {
    $scope.orderByField = value;
  };


  function compareTypes(a,b){
    if(a.exportType > b.exportType){
      return 1;
    }
    if(a.exportType < b.exportType){
      return -1;
    }
    if(a.name > b.name){
      return 1;
    }
    if(a.name < b.name){
      return -1;
    }
  }

  function addLeadingZeroes(targetLength,inputString){
    var inputString = inputString.toString();
    for(var i = inputString.length; i< targetLength;i++){
      inputString = '0' + inputString;
    }
    return inputString.toString();
  }


  function init(){
    scannerService.getSupplierId(function(farmId){
      $scope.farmId = addLeadingZeroes(3,farmId);
      TypeService.getActiveTypes(function(types){
        $scope.types = types;
      })
    })
  }
  init();

  $scope.focusInputText = function () {
    document.getElementById("barcode").focus();
  };


  function getBarcode(codeInfo){
    return addLeadingZeroes(3,$scope.farmId) + addLeadingZeroes(5,codeInfo.uniqId) + addLeadingZeroes(5,codeInfo.pickerId);;
  }

  $scope.parseBarcode = function (barcode) {
    $scope.notADigit = false;
    $scope.errorMessage = '';
    if ((!barcode.match(/^\d+$/)) && barcode.length > 0) {
      $scope.notADigit = true;
      $scope.barcode = '';
    }
    if (barcode.length === 13) {
      $scope.showSpinner = true;
      var farmId = barcode.substr(0, 3);
      var uniqId = barcode.substr(3, 5);
      var pickerId = barcode.substr(8, 5);
      if(!($scope.farmId === farmId)){
        growl.error('checkCode.invalid.farm.error', {ttl: 5000});
        $scope.barcode = '';
        $scope.showSpinner = false;
        return;
      }
      barcodeService.getCheckCode(pickerId,uniqId,function(codeInfo){
        if((codeInfo.hasOwnProperty('uniqId')) && (codeInfo.uniqId > 0) &&(codeInfo.hasOwnProperty('pickerId')) && (codeInfo.pickerId > 0)) {
          codeInfo.code = getBarcode(codeInfo);
          $scope.details.unshift(codeInfo);
          $scope.showSpinner = false;
        } else {
          $scope.showSpinner = false;
          growl.error('checkCode.no-barcode.error', {ttl: 5000});
        }
      })
      $scope.barcode = '';
    }
  }

  $scope.reclassify = function(){
    var commands = [];
    $scope.details.forEach(function(detail){
      if(detail.typeId !== $scope.targetTypeId){
        var command = {};
        command.uniqId = detail.uniqId;
        command.pickerId = detail.pickerId;
        command.targetTypeId = $scope.targetTypeId;
        commands.push(command);
      }
    })
    barcodeService.reclassifyByBarcode(commands);
  };

$scope.removeBox = function(box){
    var index = $scope.details.indexOf(box)
    $scope.details.splice(index,1);
}

$scope.$on('ReclassificationSuccessful',function(){
  growl.success('reclassify-by-barcode.message.success',{ttl:3000})
  $scope.details = [];
})

}]);
