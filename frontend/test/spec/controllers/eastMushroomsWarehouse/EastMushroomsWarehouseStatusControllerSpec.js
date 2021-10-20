'use strict';

describe('EastMushroomsWarehouseControllerSpec', function () {
  beforeEach(module('frontendApp'));

  var $controller;
  var rootScope;

  beforeEach(inject(function ($rootScope, _$controller_) {
    $controller = _$controller_;
    rootScope = $rootScope;
  }));


  it('should count total weight of each type', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsWarehouseStatusCtrl', {$scope: $scope});
    var stock = [
      {"typeId": 81, "typeName": "f", "typeWeight": 2.5, "amount": 6},
      {"typeId": 150, "typeName": "c", "typeWeight": 2.5, "amount": 1},
      {"typeId": 152, "typeName": "b", "typeWeight": 2.5, "amount": 2},
      {"typeId": 156, "typeName": "e", "typeWeight": 0.5, "amount": 1},
      {"typeId": 179, "typeName": "d", "typeWeight": 1.5, "amount": 5},
      {"typeId": 184, "typeName": "a", "typeWeight": 2.4, "amount": 4}
    ];
    var tested = $scope.countTypeTotalWeight(stock);
    expect(tested[0].totalWeight).toEqual(9.6);
    expect(tested[1].totalWeight).toEqual(5);
    expect(tested[2].totalWeight).toEqual(2.5);
    expect(tested[3].totalWeight).toEqual(7.5);
    expect(tested[4].totalWeight).toEqual(0.5);
    expect(tested[5].totalWeight).toEqual(15);
  });

  it('should count total amount of boxes', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsWarehouseStatusCtrl', {$scope: $scope});
    var stock = [
      {"typeId": 81, "typeName": "f", "typeWeight": 2.5, "amount": 6},
      {"typeId": 150, "typeName": "c", "typeWeight": 2.5, "amount": 1},
      {"typeId": 152, "typeName": "b", "typeWeight": 2.5, "amount": 2},
      {"typeId": 156, "typeName": "e", "typeWeight": 0.5, "amount": 1},
      {"typeId": 179, "typeName": "d", "typeWeight": 1.5, "amount": 5},
      {"typeId": 184, "typeName": "a", "typeWeight": 2.4, "amount": 4}
    ];
    var tested = $scope.getTotalAmount(stock);
    expect(tested).toEqual(19);
  });

  it('should count total weight', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsWarehouseStatusCtrl', {$scope: $scope});
    var stock = [
      {"typeId": 81, "typeName": "f", "typeWeight": 2.5, "amount": 6},
      {"typeId": 150, "typeName": "c", "typeWeight": 2.5, "amount": 1},
      {"typeId": 152, "typeName": "b", "typeWeight": 2.5, "amount": 2},
      {"typeId": 156, "typeName": "e", "typeWeight": 0.5, "amount": 1},
      {"typeId": 179, "typeName": "d", "typeWeight": 1.5, "amount": 5},
      {"typeId": 184, "typeName": "a", "typeWeight": 2.4, "amount": 4}
    ];
    var tested = $scope.getTotalWeight(stock);
    expect(tested).toEqual(40.1);
  });

  it('should count palettes amount', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsWarehouseStatusCtrl', {$scope: $scope});
    var palettes = [{
      amount: 21,
      localTypeId: 224,
      localTypeName: "7/9 FLAT 1,81 KG.",
      localTypeWeight: 1.81,
      paletteId: 1,
      remoteTypeId: 0,
      remoteTypeName: null,
      remoteTypeWeight: 0,
      warehousePaletteStatus: "ON_STOCK"
    },
      {
        amount: 20,
        localTypeId: 391,
        localTypeName: "8/10 FLAT TRAN 0,250",
        localTypeWeight: 0.75,
        paletteId: 1,
        remoteTypeId: 0,
        remoteTypeName: null,
        remoteTypeWeight: 0,
        warehousePaletteStatus: "ON_STOCK"
      },
      {
        amount: 139,
        localTypeId: 393,
        localTypeName: "4/5HISZ CzT0120,250",
        localTypeWeight: 2,
        paletteId: 1,
        remoteTypeId: 0,
        remoteTypeName: null,
        remoteTypeWeight: 0,
        warehousePaletteStatus: "ON_STOCK"
      },
      {
        amount: 180,
        localTypeId: 393,
        localTypeName: "4/5HISZ CzT0120,250",
        localTypeWeight: 2,
        paletteId: 2,
        remoteTypeId: 0,
        remoteTypeName: null,
        remoteTypeWeight: 0,
        warehousePaletteStatus: "ON_STOCK"
      },
      {
        amount: 11,
        localTypeId: 90,
        localTypeName: "1/2 MINI 3 KG ",
        localTypeWeight: 3,
        paletteId: 3,
        remoteTypeId: 0,
        remoteTypeName: null,
        remoteTypeWeight: 0,
        warehousePaletteStatus: "ON_STOCK"
      },
      {
        amount: 6,
        localTypeId: 109,
        localTypeName: "4/6 GRECJA 0,500 KG ",
        localTypeWeight: 2,
        paletteId: 3,
        remoteTypeId: 0,
        remoteTypeName: null,
        remoteTypeWeight: 0,
        warehousePaletteStatus: "ON_STOCK"
      },
      {
        amount: 105,
        localTypeId: 142,
        localTypeName: "2/4 ANGLIA 1,36 KG",
        localTypeWeight: 1.36,
        paletteId: 3,
        remoteTypeId: 0,
        remoteTypeName: null,
        remoteTypeWeight: 0,
        warehousePaletteStatus: "ON_STOCK"
      },
      {
        amount: 0,
        localTypeId: 142,
        localTypeName: "2/4 ANGLIA 1,36 KG",
        localTypeWeight: 1.36,
        paletteId: 234,
        remoteTypeId: 0,
        remoteTypeName: null,
        remoteTypeWeight: 0,
        warehousePaletteStatus: "ON_STOCK"
      },
      {amount: 105,
        localTypeId: 142,
        localTypeName: "2/4 ANGLIA 1,36 KG",
        localTypeWeight: 1.36,
        paletteId: 3,
        remoteTypeId: 0,
        remoteTypeName: null,
        remoteTypeWeight: 0,
        warehousePaletteStatus: "ola"}
    ];
    expect($scope.getPalettesAmount(palettes)).toEqual(3);
  });


});
