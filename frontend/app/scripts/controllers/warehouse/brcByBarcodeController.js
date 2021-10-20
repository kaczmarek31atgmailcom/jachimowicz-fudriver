'use strict';

angular.module('frontendApp')
  .controller('brcByBarcodeCtrl', ['$scope', '$location', 'brcSearchByCodeService', function ($scope, $location, brcSearchByCodeService) {

    $scope.previousBarcode;


    $scope.init = function () {
      $scope.warehouseHeaders = brcSearchByCodeService.getWarehouseHeaders();
      $scope.wzHeaders = brcSearchByCodeService.getWzHeaders();
      $scope.theBox = brcSearchByCodeService.getTheBox();
      $scope.previousBarcode = brcSearchByCodeService.getBarcode();
    };

    $scope.focusInputText = function () {
      setTimeout(function () {
        barForm.barcode.focus();
      }, 1);
    };


    $scope.parseBarcode = function () {
      if ((!$scope.barcode.match(/^\d+$/)) && $scope.barcode.length > 0) {
        $scope.notADigit = true;
        $scope.barcode = '';
        errorSound();
      }

      if ($scope.barcode.length === 13) {
        $scope.previousBarcode = $scope.barcode;
        var uniqId = $scope.barcode.substr(3, 5);
        var pickerId = $scope.barcode.substr(8, 5);
        brcSearchByCodeService.getBoxDetails(uniqId, pickerId, function (box) {
          $scope.theBox = box;
          brcSearchByCodeService.setTheBox($scope.theBox);
        });
        $scope.barcode = '';
      }
    };


    $scope.$on('BoxDetailsLoaded', function () {

      brcSearchByCodeService.getWarehouseHeadersForCycle($scope.theBox.harvestDate, $scope.theBox.cycleId, function (headers) {
        $scope.warehouseHeaders = headers;
        brcSearchByCodeService.setWarehouseHeaders($scope.warehouseHeaders);
      });
      brcSearchByCodeService.getWzHeadersForCycle($scope.theBox.harvestDate, $scope.theBox.cycleId, function (wzHeaders) {
        $scope.wzHeaders = wzHeaders;
        brcSearchByCodeService.setWzHeaders($scope.wzHeaders);
      });
    });


    $scope.toPaletteNo = function (paletteId) {
      if (paletteId === undefined) {
        paletteId = '0';
      }
      var result = paletteId.toString();
      for (var i = result.length; i < 10; i++) {
        result = '0' + result;
      }
      return '999' + result;
    };


    $scope.stockDetails = function (header) {
      brcSearchByCodeService.setTypeId(header.typeId);
      brcSearchByCodeService.setDepotId(header.depotId);
      brcSearchByCodeService.setBarcode($scope.previousBarcode);
      $location.url("/brcByBarcodeStockDetails");
    };

    $scope.wzDetails = function (header) {
      brcSearchByCodeService.setWzId(header.headerId);
      brcSearchByCodeService.setTypeId(header.typeId);
      brcSearchByCodeService.setBarcode($scope.previousBarcode);
      $location.url("/brcByBarcodeWzDetails");
    };


  }]);


///////////////////////////////////

angular.module('frontendApp')
  .controller('brcByBarcodeWarehouseDetailsCtrl', ['$scope', '$location', '$filter', 'brcReportService', 'brcSearchByCodeService', function ($scope, $location, $filter, brcReportService, brcSearchByCodeService) {

    var supplierId;

    $scope.init = function () {
      var date = $filter('date')(brcSearchByCodeService.getTheBox().harvestDate, 'yyyyMMdd');
      var cycleId = brcSearchByCodeService.getTheBox().cycleId;
      var typeId = brcSearchByCodeService.getTypeId();
      var depotId = brcSearchByCodeService.getDepotId();
      brcSearchByCodeService.getStockDetails(date, typeId, cycleId, depotId, function (details) {
        $scope.details = details;
      });

      brcReportService.getSupplierId(function (data) {
        supplierId = data;
      });
    };

    $scope.findBarcode = function (uniqId, pickerId) {
      return addHeadingZeroes(3, supplierId) + addHeadingZeroes(5, uniqId) + addHeadingZeroes(5, pickerId);
    };

    var addHeadingZeroes = function (requiredLength, input) {
      var inputString = input.toString();
      while (inputString.length < requiredLength) {
        inputString = '0' + inputString;
      }
      return inputString;
    }
  }]);


///////////////////////////////////

angular.module('frontendApp')
  .controller('brcByBarcodeWzDetailsCtrl', ['$scope', '$location', '$filter', 'brcReportService', 'brcSearchByCodeService', function ($scope, $location, $filter, brcReportService, brcSearchByCodeService) {

    var supplierId;

    $scope.init = function () {
      var date = $filter('date')(brcSearchByCodeService.getTheBox().harvestDate, 'yyyyMMdd');
      var cycleId = brcSearchByCodeService.getTheBox().cycleId;
      var wzId = brcSearchByCodeService.getWzId();
      var typeId = brcSearchByCodeService.getTypeId();

      brcSearchByCodeService.getWzDetails(date, typeId, cycleId, wzId, function (details) {
        $scope.details = details;
      });


      brcReportService.getSupplierId(function (data) {
        supplierId = data;
      });
    };

    $scope.findBarcode = function (uniqId, pickerId) {
      return addHeadingZeroes(3, supplierId) + addHeadingZeroes(5, uniqId) + addHeadingZeroes(5, pickerId);
    };

    var addHeadingZeroes = function (requiredLength, input) {
      var inputString = input.toString();
      while (inputString.length < requiredLength) {
        inputString = '0' + inputString;
      }
      return inputString;
    }

  }]);

