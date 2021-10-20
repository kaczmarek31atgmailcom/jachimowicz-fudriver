'use strict';

angular.module('frontendApp')
  .controller('reclassifyByBarcodeCtrl', ['$scope', 'reclassifyByBarcodeService', function ($scope, reclassifyByBarcodeService) {

    $scope.codes = [];
    $scope.paletteId;
    $scope.orderId;
    $scope.reasonId;
    $scope.warehouseOnly = true;
    $scope.qualityStatus = 'NOT_CHECKED';


    $scope.init = function () {
      reclassifyByBarcodeService.getTypes(function (types) {
        $scope.types = types;
        $scope.typeId = $scope.types[0].id;
        $scope.focusInputText();
      });
      reclassifyByBarcodeService.getSupplierId(function (supplierId) {
        $scope.supplierId = supplierId;
      });

      reclassifyByBarcodeService.getReasons(function (reasons) {
        $scope.reasons = toList(reasons);
        $scope.reasonId = $scope.reasons[0].id;
      })

    };


    var toList = function (map) {
      var output = [];
      for (var key in map) {
        var reason = {"id": key, "description": map[key]};
        output.push(reason);
      }
      return output;
    };


    $scope.focusInputText = function () {
      barForm.barcode.focus();
    };


    $scope.parseBarcode = function () {
      $scope.message = '';

      if ((!$scope.barcode.match(/^\d+$/)) && $scope.barcode.length > 0) {
        $scope.notADigit = true;
        $scope.barcode = '';
        playSound('error');
        $scope.message = 'notADigit';
        return;
      }
      if (($scope.barcode.length === 3) && (($scope.barcode != $scope.supplierId))) {
        $scope.errorMessage = 'invalidSupplierId';
        $scope.barcode = '';
        playSound('error');
        $scope.message = 'invalidSupplier';
        return;
      }

      if ($scope.barcode.length === 13) {
        var uniqId = $scope.barcode.substr(3, 5);
        var pickerId = $scope.barcode.substr(8, 5);
        reclassifyByBarcodeService.findBox(uniqId, pickerId, function (item) {
          var box = item;

          if (box.id === null) {
            $scope.message = 'notOnStock';
            playSound('error');
            $scope.barcode = '';
            return;
          }
          if (box.reserved) {
            $scope.message = 'reserved';
            $scope.paletteId = '999' + addLeadingZeroes(10, box.paletteId);
            $scope.orderId = box.orderId;
            playSound('error');
            $scope.barcode = '';
          } else {
            $scope.addToCodes(box);
          }
        });
        $scope.barcode = '';
      }
    };

    $scope.removeFromCodes = function (boxId) {
      for (var i = 0; i < $scope.codes.length; i++) {
        if ($scope.codes[i].id === boxId) {
          $scope.codes.splice(i, 1);
        }
      }
    };

    $scope.addToCodes = function (box) {
      for (var i = 0; i < $scope.codes.length; i++) {
        if ($scope.codes[i].id === box.id) {
          $scope.message = 'alreadyAdded';
          return;
        }
      }
      $scope.codes.push(box);
    };


    $scope.getBarcode = function (uniqId, pickerId) {
      return addLeadingZeroes(3, $scope.supplierId) + addLeadingZeroes(5, uniqId) + addLeadingZeroes(5, pickerId);
    };

    var addLeadingZeroes = function (length, item) {
      var result = item.toString();
      for (var i = result.length; i < length; i++) {
        result = '0' + result;
      }
      return result;
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

    $scope.cancel = function () {
      $scope.codes = [];
    };

    $scope.reclassify = function () {
      var command = {};
      command.items = [];
      $scope.codes.forEach(function (code) {
        var item = {};
        item.uniqId = code.uniqId;
        item.pickerId = code.pickerId;
        item.typeId = $scope.typeId;
        item.supplierId = $scope.supplierId;
        item.reasonId = $scope.reasonId;
        item.warehouseOnly = $scope.warehouseOnly;
        item.qualityStatus = $scope.qualityStatus;
        command.items.push(item);
      });
      reclassifyByBarcodeService.reclassify(command);
    };


    $scope.$on('ReclassificationSucceed', function () {
      $scope.codes = [];
      $scope.message = 'success';
    });

    $scope.$on('error', function () {
      $scope.message = 'unknownError';
      playSound('error');
    });

    $scope.setWarehouseOnly = function (value) {
      if(value){
        $scope.warehouseOnly = true;
      }  else {
        $scope.warehouseOnly = false;
      }
    };

    $scope.setQualityStatus = function(status){
      if(status === 'ACCEPTED' || status === 'NOT_CHECKED' || status === 'REJECTED'){
        $scope.qualityStatus = status;
      }
    }

  }]);
