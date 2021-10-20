'use strict';

describe('reclassifyByBarcodeCtrl', function () {
  beforeEach(module('frontendApp'));

  var $controller;
  var rootScope;

  beforeEach(inject(function ($rootScope, _$controller_) {
    $controller = _$controller_;
    rootScope = $rootScope;
  }));

  describe('Add new item to the list', function () {
    it('should add new item', function () {
      var $scope = rootScope.$new();
      var controller = $controller('reclassifyByBarcodeCtrl', {$scope: $scope});
      $scope.codes = [];
      var box = {};
      box.id = 1;
      $scope.addToCodes(box);
      expect($scope.codes.length).toEqual(1);
    });


    it('should add new item', function () {
      var $scope = rootScope.$new();
      var controller = $controller('reclassifyByBarcodeCtrl', {$scope: $scope});
      $scope.codes = [];
      var box = {};
      box.id = 1;
      $scope.addToCodes(box);
      expect($scope.codes[0]).toEqual(box);
    });


    it('should not add the same item twice', function () {
      var $scope = rootScope.$new();
      var controller = $controller('reclassifyByBarcodeCtrl', {$scope: $scope});
      $scope.codes = [];
      var box = {};
      box.id = 1;
      $scope.addToCodes(box);
      $scope.addToCodes(box);
      expect($scope.codes.length).toEqual(1);
    });

    it('should add two different items', function () {
      var $scope = rootScope.$new();
      var controller = $controller('reclassifyByBarcodeCtrl', {$scope: $scope});
      $scope.codes = [];
      var box = {};
      box.id = 1;
      var box2 = {};
      box2.id = 2;
      $scope.addToCodes(box);
      $scope.addToCodes(box2);
      expect($scope.codes.length).toEqual(2);
      expect($scope.codes[0].id).toEqual(1);
      expect($scope.codes[1].id).toEqual(2);
    });
  });

  describe('Remove item from the list', function () {
    it('should remove an item', function () {
      var $scope = rootScope.$new();
      var controller = $controller('reclassifyByBarcodeCtrl', {$scope: $scope});
      $scope.codes = [];
      var box = {};
      box.id = 1;
      $scope.addToCodes(box);
      expect($scope.codes.length).toEqual(1);
      $scope.removeFromCodes(box.id);
      expect($scope.codes.length).toEqual(0);
    });

    it('should remove an item from 3 element list', function () {
      var $scope = rootScope.$new();
      var controller = $controller('reclassifyByBarcodeCtrl', {$scope: $scope});
      $scope.codes = [];
      var box = {};
      box.id = 1;
      var box2 = {};
      box2.id = 2;
      var box3 = {};
      box3.id = 3;
      $scope.addToCodes(box);
      $scope.addToCodes(box2);
      $scope.addToCodes(box3);
      expect($scope.codes.length).toEqual(3);
      $scope.removeFromCodes(box2.id);
      expect($scope.codes.length).toEqual(2);
      expect($scope.codes[0].id).toEqual(1);
      expect($scope.codes[1].id).toEqual(3);
      $scope.removeFromCodes(box2.id);
      expect($scope.codes.length).toEqual(2);
      expect($scope.codes[0].id).toEqual(1);
      expect($scope.codes[1].id).toEqual(3);
    });
  });

  describe('Generate barcode', function () {
    it('should generate valid barcode', function () {
      var $scope = rootScope.$new();
      var controller = $controller('reclassifyByBarcodeCtrl', {$scope: $scope});
      $scope.supplierId = 49;
      var uniqId = 101;
      var pickerId = 1001;
      var result = $scope.getBarcode(uniqId, pickerId);
      expect(result).toEqual('0490010101001');
    });

    it('should generate valid barcode for 5 number params', function () {
      var $scope = rootScope.$new();
      var controller = $controller('reclassifyByBarcodeCtrl', {$scope: $scope});
      $scope.supplierId = 321;
      var uniqId = 12345;
      var pickerId = 67891;
      var result = $scope.getBarcode(uniqId, pickerId);
      expect(result).toEqual('3211234567891');
    });

  });


});
