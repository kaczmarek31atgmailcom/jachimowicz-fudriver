'use strict';

angular.module('frontendApp')
.controller('findByBarcodeCtrl',['$scope','growl','findBarcodeService',function($scope,growl,findBarcodeService){

  var init = function () {
    findBarcodeService.getSupplierId(function (supplierId) {
      var id = supplierId.toString();
      while (id.length < 3) {
        id = '0' + id;
      }
      $scope.supplierId = id;
      $scope.focusInputText();
    });
  };
  init();

  $scope.focusInputText = function () {
    setTimeout(function () {
      barForm.barcode.focus();
    }, 10);
  };


  $scope.parseBarcode = function () {
    $scope.message = '';


    if ((!$scope.barcode.match(/^\d+$/)) && $scope.barcode.length > 0) {
      $scope.notADigit = true;
      $scope.barcode = '';
      playSound('error');
      growl.error('warehouse.find.barcode.message.not-a-digit',{ttl:5000});
      return;
    }
    if (($scope.barcode.length == 3) && (($scope.barcode != $scope.supplierId))) {
      $scope.barcode = '';
      playSound('error');
      growl.error('warehouse.find.barcode.message.invalid-supplier',{ttl:5000});
      return;
    }

    if ($scope.barcode.length == 13) {
      var uniqId = $scope.barcode.substr(3, 5);
      var pickerId = $scope.barcode.substr(8, 5);
      findBarcodeService.findBoxZarobki(pickerId, uniqId, function (box) {
        $scope.box = box;
        if((box.typeId == null) && (box.uniqId === null)) {
          growl.error('warehouse.find.barcode.message.not-on-stock', {ttl: 5000})
        }
      });
      $scope.previousBarcode = $scope.barcode;
      $scope.barcode = '';
    }
  };

  var playSound = function (type) {
    switch (type) {
      case 'error':
        var audio = new Audio('/fudriver/sounds/blackhole.wav');
        audio.play();
        break;
      case 'beep':
        var audio = new Audio('/fudriver/sounds/beep.wav');
        audio.play();
        break;
      case 'found':
        var audio = new Audio('/fudriver/sounds/found.wav');
        audio.play();
        break;
    }
  };


}]);
