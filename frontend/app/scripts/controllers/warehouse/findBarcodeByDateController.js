'use strict';

angular.module('frontendApp').controller('findBarcodeByDateCtrl', ['$scope', '$filter', 'findBarcodeService', function ($scope, $filter, findBarcodeService) {


  $scope.supplierId = "";
  $scope.cycleId;
  $scope.message;


  $scope.init = function () {
    findBarcodeService.getSupplierId(function (supplierId) {
      var id = supplierId.toString();
      while (id.length < 3) {
        id = '0' + id;
      }
      $scope.supplierId = id;
    });
  };

  $scope.focusInputText = function () {
    barForm.barcode.focus();
  };

  $scope.findCycles = function () {
    var day = $filter('date')($scope.date, 'yyyyMMdd');
    findBarcodeService.findCycles(day, function (cycles) {
      $scope.cycles = cycles;
      $scope.cycleId = $scope.cycles[0].cycleId;
    });
  };


  $scope.parseBarcode = function () {
    $scope.message = '';

    if ($scope.date == undefined) {
      playSound('error');
      $scope.message = 'noHarvestDate';
      $scope.barcode = '';
      return;
    }
    if ($scope.cycleId == undefined) {
      playSound('error');
      $scope.message = 'noCycle';
      $scope.barcode = '';
      return
    }


    if ((!$scope.barcode.match(/^\d+$/)) && $scope.barcode.length > 0) {
      $scope.notADigit = true;
      $scope.barcode = '';
      playSound('error');
      $scope.message = 'notADigit';
      return;
    }
    if (($scope.barcode.length == 3) && (($scope.barcode != $scope.supplierId))) {
      $scope.errorMessage = 'invalidSupplierId';
      $scope.barcode = '';
      playSound('error');
      $scope.message = 'invalidSupplier';
      return;
    }


    if ($scope.barcode.length == 13) {
      var uniqId = $scope.barcode.substr(3, 5);
      var pickerId = $scope.barcode.substr(8, 5);
      findBarcodeService.findBox(pickerId, uniqId, function (box) {
        $scope.box = box;
        if ($scope.box.cycleId == null) {
          $scope.message = 'notOnStock';
          playSound('error');
        }
        else if ($scope.compareDate($scope.box.harvestDate, $scope.date) && ($scope.cycleId == $scope.box.cycleId)) {
          playSound('found');
          $scope.message='equal';
        } else {
          playSound('beep');
          $scope.message = 'different';
        }
      });
      $scope.barcode = '';
    }
  };


  $scope.compareDate = function (dateA, dateB) {
    var dayA = moment(dateA);
    var dayB = moment(dateB);
    return dayA.isSame(dayB);
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
