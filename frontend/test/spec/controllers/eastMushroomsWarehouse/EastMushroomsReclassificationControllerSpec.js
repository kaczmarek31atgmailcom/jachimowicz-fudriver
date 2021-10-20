'use strict';

describe('EastMushroomsReclassificationControllerSpec', function () {
  beforeEach(module('frontendApp'));

  var $controller;
  var rootScope;

  beforeEach(inject(function ($rootScope, _$controller_) {
    $controller = _$controller_;
    rootScope = $rootScope;
  }));


  it('should check supplierId', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsReclassificationCtrl', {$scope: $scope});
    $scope.supplierId = '017';
    expect($scope.checkValidityOfSupplierId(11)).toBeTruthy();
    expect($scope.checkValidityOfSupplierId(110)).toBeFalsy();
    expect($scope.checkValidityOfSupplierId('017')).toBeTruthy();

  });

  it('should check if there are only digits', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsReclassificationCtrl', {$scope: $scope});
    $scope.supplierId = '017';
    expect($scope.checkIfDigitsOnly(11)).toBeTruthy();
    expect($scope.checkIfDigitsOnly(110)).toBeTruthy();
    expect($scope.checkIfDigitsOnly('017')).toBeTruthy();
    expect($scope.checkIfDigitsOnly('12d34')).toBeFalsy();
    expect($scope.checkIfDigitsOnly('e')).toBeFalsy();
    expect($scope.checkIfDigitsOnly('1.2')).toBeFalsy();
  });

  it('should generate barcode', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsReclassificationCtrl', {$scope: $scope});
    $scope.supplierId = '017';
    var warehouseUnit = {"pickerId":1234,"uniqId":88};
    expect($scope.getBarcode(warehouseUnit)).toEqual('0170008801234');
  });

  it('should find already scanned box', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsReclassificationCtrl', {$scope: $scope});
    var boxes = [{"pickerId":1234,"uniqId":88},{"pickerId":1235,"uniqId":88},{"pickerId":1234,"uniqId":89}];
    var warehouseUnit = {"pickerId":1234,"uniqId":88};
    expect($scope.checkIfWarehouseUnitAlreadyScanned(warehouseUnit,boxes)).toBeTruthy();
    var warehouseUnit2 = {"pickerId":1234,"uniqId":90};
    expect($scope.checkIfWarehouseUnitAlreadyScanned(warehouseUnit2,boxes)).toBeFalsy();
  });


});
