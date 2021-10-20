'use strict';

angular.module('frontendApp')
  .controller('RemoveReservationCtrl', ['$scope', '$location', '$filter', 'growl', 'RemoveReservationService', function ($scope, $location, $filter, growl, RemoveReservationService) {

    $scope.boxes = [];


    $scope.focusInputText = function () {
      barForm.barcode.focus();
    };

    $scope.parseBarcode = function () {
      $scope.notADigit = false;
      if ((!$scope.barcode.match(/^\d+$/)) && $scope.barcode.length > 0) {
        $scope.previousBarcode = $scope.barcode;
        growl.error("create-order-remove-reservation.message.notADigit", {ttl: 2000});
        $scope.barcode = '';
        errorSound();
      }

      if (($scope.barcode.length == 13) && ($scope.barcode.substr(0, 3) != '999')) {
        var supplierId = $scope.barcode.substr(0, 3);
        var uniqId = $scope.barcode.substr(3, 5);
        var pickerId = $scope.barcode.substr(8, 5);
        getDetails(supplierId, uniqId, pickerId);
        $scope.barcode = '';
      }
    };

    var getDetails = function (supplierId, uniqId, pickerId) {
      RemoveReservationService.getBox(supplierId, uniqId, pickerId, function (box) {
        if (!box.id) {
          errorSound();
          growl.error("create-order-remove-reservation.message.notExists", {ttl: 2000});
          return;
        }
        if ((box.id) && (!box.isReserved)) {
          errorSound();
          growl.error("create-order-remove-reservation.message.notReserved", {ttl: 2000});
          return;
        }
        if(isBarcodeAlreadyScanned(supplierId,uniqId,pickerId)){
          errorSound();
          growl.warning("create-order-remove-reservation.message.alreadyScanned",{ttl:2000})
          return;
        }

        if (box.isReserved) {
          $scope.boxes.push(box);
        }
      })
    };

    var isBarcodeAlreadyScanned = function(supplierId,uniqId,pickerId){
      for(var i = 0;i<$scope.boxes.length;i++){
        if((parseInt($scope.boxes[i].supplierId) === parseInt(supplierId)) && (parseInt($scope.boxes[i].uniqId) === parseInt(uniqId)) && (parseInt($scope.boxes[i].pickerId) === parseInt(pickerId))){
          return true;
          break;
        }
      }
    return false;
    };


    var errorSound = function () {
      var audio = new Audio('/fudriver/sounds/blackhole.wav');
      audio.play();
    };

    $scope.getBarcode = function (supplierId, uniqId, pickerId) {
      return addTrailingZeroes(3, supplierId) + addTrailingZeroes(5, uniqId) + addTrailingZeroes(5, pickerId);
    };

    $scope.getPaletteNo = function (paletteId) {
      return '999' + addTrailingZeroes(10, paletteId);
    };

    var addTrailingZeroes = function (requiredLength, item) {
      var result = item.toString();
      while (result.length < requiredLength) {
        result = '0' + result;
      }
      return result;
    };

    $scope.cancel = function () {
      $scope.boxes = [];
    };

    $scope.removeReservation = function () {
      var command = {};
      command.items = [];
      $scope.boxes.forEach(function (box) {
        var item = {};
        item.uniqId = box.uniqId;
        item.pickerId = box.pickerId;
        command.items.push(item);
      });
      RemoveReservationService.unReserve(command);
    };

    $scope.$on('error', function () {
      errorSound();
      growl.error("error", {ttl: 3000});
    });


    $scope.$on('UnreservationSucceed', function () {
      growl.success("create-order-remove-reservation.message.success", {ttl: 2000})
      $scope.boxes = [];
    });

  }]);
