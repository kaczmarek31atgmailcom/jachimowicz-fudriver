'use sctrict';

angular.module('frontendApp')
  .controller('outgoReleaseCtrl', ['$scope', '$route', '$location','growl','outgoService', function ($scope, $route, $location, growl,outgoService) {
    $scope.orderId = $route.current.params.orderId;
    $scope.message = '';
    $scope.errorMessage = '';
    $scope.order = {};
    $scope.order.palettes = [];
    $scope.hideSubmit = false;


    $scope.init = function () {
      outgoService.getOrder($scope.orderId, function (order) {
        $scope.order = order;
      })
    };

    $scope.focusInputText = function () {
      setTimeout(function () {
        barForm.barcode.focus();
      }, 1);
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


      if (($scope.barcode.length == 3) && ($scope.barcode != '999')) {
        $scope.errorMessage = 'Invalid prefix';
        $scope.previousBarcode = $scope.barcode;
        $scope.barcode = '';
        errorSound();
      }

      if (($scope.barcode.length == 13) && ($scope.barcode.substr(0, 3) == '999')) {
        var paletteId = parseInt($scope.barcode.substr(3, 10));
        if (checkPalette(paletteId)) {
          var command = {};
          command.paletteId = paletteId;
          outgoService.releasePalette(command);
        }
        $scope.barcode = '';
      }
    };

    $scope.openDeliveryLetter = function(orderId){
      $location.url("/outgo-release/delivery-letter/" + orderId);
    }

    $scope.$on('PaletteReleased', function (response, data) {
      updateReleasedPaletteStatus(data.data.entityId);
    });

    var updateReleasedPaletteStatus = function (paletteId) {
      $scope.order.palettes.forEach(function (palette) {
        if (palette.id === paletteId) {
          palette.status = 1;
        }
      })
    };

    var checkPalette = function (paletteId) {
      var result = false;
      var paletteNotFound = true;
      var paletteId = parseInt(paletteId);
      $scope.order.palettes.forEach(function (palette) {
        if ((paletteId == palette.id) && (palette.status == 0)) {
          result = true;
          palette.completed = true;
          paletteNotFound = false;
          $scope.errorMessage = '';

        }
        if ((paletteId == palette.id) && (palette.status == 1)) {
          $scope.message = '';
          $scope.errorMessage = 'PaletteAlreadyReleased';
          paletteNotFound = false;
          errorSound();
        }
      });
      if (paletteNotFound) {
        $scope.message = '';
        $scope.previousBarcode = $scope.barcode;
        $scope.errorMessage = 'PaletteNotFound';
        errorSound();
      }
      return result;
    };

    $scope.isOrderCompleted = function () {
      var result = true;
      $scope.order.palettes.forEach(function (palette) {
        if (palette.status == 0) {
          result = false;
        }
      });
      return result;
    };

    $scope.findPaletteNo = function (paletteNo) {
      return '999' + addTrailingZeroes(paletteNo, 10);
    };


    var addTrailingZeroes = function (item, requiredLength) {
      var result = item.toString();
      while (result.length < requiredLength) {
        result = '0' + result;
      }
      return result;
    };

    var errorSound = function () {
      var audio = new Audio('/fudriver/sounds/blackhole.wav');
      audio.play();
    };

    $scope.unreleasePalette = function (paletteId) {
      var command = {};
      command.paletteId = paletteId;
      outgoService.unreleasePalette(command);
    };

    $scope.$on('PaletteUnReleased', function (response, data) {
      updateUnReleasedPaletteStatus(data.data.entityId);
    });

    var updateUnReleasedPaletteStatus = function (paletteId) {
      $scope.order.palettes.forEach(function (palette) {
        if (palette.id === paletteId) {
          palette.status = 0;
        }
      })
    };

    $scope.generateWz = function () {
      $scope.hideSubmit = true;
      var command = {};
      command.orderId = $scope.orderId;
      outgoService.createWz(command);
    };

    $scope.$on('WzCreated',function(response,data){
      $location.url('/outgo-release/wz/' + data.data.entityId)
    });

    $scope.$on('error',function(){
      growl.error('Coś poszło nie tak');
    })
  }]);
