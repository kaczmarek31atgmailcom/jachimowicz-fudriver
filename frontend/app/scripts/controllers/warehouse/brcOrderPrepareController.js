'use strict';

angular.module('frontendApp')
  .controller('brcOrderPrepareCtrl', ['$scope', '$interval', '$location', 'brcOrderPrepareService', function ($scope, $interval, $location,brcOrderPrepareService) {

    $scope.activePalette = '';
    $scope.previousBarcode = '';
    $scope.errorMessage = '';

    var errorSound = function () {
      var audio = new Audio('/fudriver/sounds/blackhole.wav');
      audio.play();
    };


    $scope.init = function () {
      brcOrderPrepareService.getOpenOrders(function (orders) {
        $scope.orders = orders;
      });

      $scope.supplierId = brcOrderPrepareService.getSupplierId();
      if ($scope.supplierId == undefined) {
        brcOrderPrepareService.retrieveSupplierId(function (supplierId) {
          $scope.supplierId = supplierId;
        });
      }
    };


    var intervalPromise = $interval(function () {
      brcOrderPrepareService.getOpenOrders(function (orders) {
        //updateOrders(orders);
        $scope.orders = orders;
      });
    }, 5000);
    $scope.$on('$destroy', function () {
      $interval.cancel(intervalPromise);
    });

    var updateOrders = function (newOrders) {
      var newOrders = newOrders;
      var foundOrder = false;
      for (var i = $scope.orders.length - 1; i >= 0; i--) {
        newOrders.forEach(function (newOrder) {
          if ($scope.orders[i].id == newOrder.id) {
            foundOrder = true;
          }
        });
        if (!foundOrder) {
          $scope.orders.splice(i, 1);
        }
        foundOrder = false;
      }

      foundOrder = false;
      newOrders.forEach(function (newOrder) {
        $scope.orders.forEach(function (order) {
          if (newOrder.id == order.id) {
            foundOrder = true;
            order.plate = newOrder.plate;
            order.customerId = newOrder.customerId;
            order.customerName = newOrder.customerName;
            order.requiredDate = newOrder.requiredDate;
          }
        });
        if (!foundOrder) {
          $scope.orders.push(newOrder);
        }
        foundOrder = false;
      });
    };


    $scope.focusInputText = function () {
      setTimeout(function () {
        barForm.barcode.focus();
      }, 500);
    };


    $scope.findPaletteNo = function (paletteNo) {
      return '999' + addTrailingZeroes(paletteNo, 10);
    };

    var addTrailingZeroes = function (item, requiredLenght) {
      var result = item.toString();
      while (result.length < requiredLenght) {
        result = '0' + result;
      }
      return result;
    };

    $scope.parseBarcode = function () {
      $scope.errorMessage = '';
      $scope.notADigit = false;
      $scope.errorMessage = '';
      if ((!$scope.barcode.match(/^\d+$/)) && $scope.barcode.length > 0) {
        $scope.errorMessage = 'Not a digit';
        $scope.previousBarcode = $scope.barcode;
        $scope.barcode = '';
        errorSound();
      }

      if ((($scope.barcode.length == 3) && ($scope.barcode != $scope.supplierId)) && ($scope.barcode != '999')) {
        $scope.errorMessage = 'Invalid prefix';
        $scope.previousBarcode = $scope.barcode;
        $scope.barcode = '';
        errorSound();
      }
      if (($scope.barcode.length == 13) && ($scope.barcode.substr(0, 3) == '999')) {

        var paletteNo = parseInt($scope.barcode.substr(3, 10));

        if (isPaletteReadyToScan(paletteNo, getExistingPaletteNumbers())) {
          $scope.activePalette = paletteNo;
          getCurrentPalette();
          $scope.previousBarcode = $scope.barcode;
          $scope.barcode = '';
        } else {
          $scope.errorMessage = "Choose palette";
          $scope.barcode = '';
          errorSound();
        }

      }

      if (($scope.barcode.length == 13) && ($scope.barcode.substr(0, 3) != '999')) {
        reserve();
        $scope.previousBarcode = $scope.barcode;
        $scope.barcode = '';
      }
    };

    var reserve = function () {
      var command = {};
      command.uniqId = parseInt($scope.barcode.substr(3, 5));
      command.pickerId = parseInt($scope.barcode.substr(8, 5));
      command.paletteId = $scope.activePalette;
      brcOrderPrepareService.reserve(command);
    };

    $scope.$on('BoxCommited', function (event, data) {
      var barcode = data.data.barcode;
      var batchId = data.data.entityId;
      var status = data.data.status;
      if (status == 'ERROR') {
        $scope.errorMessage = data.data.body;
        errorSound();
      } else {
        var detail = {};
        detail.uniqId = barcode.substr(0, 5);
        detail.pickerId = barcode.substr(5, 5);
        $scope.currentPalette.batches.forEach(function (batch) {
          if (batch.id == batchId) {
            batch.details.push(detail);
            addItemToBatchHeader(batchId);
          }
        });
      }
    });


    var getExistingPaletteNumbers = function () {
      var paletteNumbers = [];
      $scope.orders.forEach(function (order) {
        order.palettes.forEach(function (palette) {
          paletteNumbers.push(parseInt(palette.id));
        })
      });
      return paletteNumbers;
    };

    var isPaletteReadyToScan = function (paletteNo, activePalettes) {
      return (activePalettes.indexOf(paletteNo) > -1);
    };

    $scope.getReservedPercent = function (amount, reservedAmount) {
      var result = 0;
      if (amount > 0) {
        result = (reservedAmount * 100) / amount;
      }
      return result;
    };

    var getCurrentPalette = function () {
      $scope.currentPalette = {};
      $scope.orders.forEach(function (order) {
        order.palettes.forEach(function (palette) {
          if (palette.id == $scope.activePalette) {
            $scope.currentPalette = palette;
            palette.batches.forEach(function (batch) {
              brcOrderPrepareService.getBatchDetails(batch.id, function (details) {
                batch.details = details;
              })
            })
          }
        })
      });
    };


    var addItemToBatchHeader = function (batchId) {
      $scope.currentPalette.batches.forEach(function (batch) {
        if (batch.id == batchId) {
          batch.reservedAmount++;
        }
      })
    };

    var removeItemFromBatchHeader = function (batchId) {
      $scope.currentPalette.batches.forEach(function (batch) {
        if (batch.id == batchId) {
          batch.reservedAmount--;
        }
      })
    };


    $scope.getBarcode = function (detail) {
      var barcode = '';
      barcode = addLeadingZeroes(3, $scope.supplierId) + addLeadingZeroes(5, detail.uniqId) + addLeadingZeroes(5, detail.pickerId);
      return barcode;
    };

    var addLeadingZeroes = function (length, input) {
      var result = input.toString();
      while (result.length < length) {
        result = '0' + result;
      }
      return result;
    };

    $scope.removeReservation = function (uniqId, pickerId) {
      var command = {};
      command.pickerId = pickerId;
      command.uniqId = uniqId;
      brcOrderPrepareService.removeReservation(command);
    };

    $scope.$on('BoxUnCommited', function (event, data) {
      var barcode = data.data.barcode;
      var uniqId = barcode.substr(0, 5);
      var pickerId = barcode.substr(5, 5);
      $scope.currentPalette.batches.forEach(function (batch) {
        for (var i = batch.details.length - 1; i >= 0; i--) {
          if ((batch.details[i].pickerId == parseInt(pickerId) ) && (batch.details[i].uniqId == parseInt(uniqId))) {
            batch.details.splice(i, 1);
            removeItemFromBatchHeader(batch.id);
          }
        }
      });
    });



    $scope.$on('error', function (event, data) {
      errorSound();
      $scope.errorMessage = data.data.body;
    });


    $scope.goToMenu = function () {
      $scope.activePalette = '';
    };

    $scope.isOrderCompleted = function(order){
      var result = true;
      order.palettes.forEach(function(palette){
        palette.batches.forEach(function(batch){
          if(batch.amount != batch.reservedAmount){
            result = false;
          }
        });
      });
    return result;
    };

    $scope.prepareOrder = function(orderId){
      var command = {};
      command.orderId = orderId;
      brcOrderPrepareService.prepare(command);
    };

    $scope.$on('OrderPrepared', function(event,data){
      var orderId = data.data.entityId;
      for(var i = $scope.orders.length -1; i>=0;i--){
        if($scope.orders[i].id == orderId){
          $scope.orders.splice(i, 1);
        }
      }
    });

    $scope.openRemoveScreen = function(){
      $location.url("/order-prepare/remove-reservation")
    };
  }]);
