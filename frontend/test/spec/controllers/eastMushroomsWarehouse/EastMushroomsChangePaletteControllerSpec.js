'use strict';

describe('EastMushroomsChangePaletteControllerSpec', function () {
  beforeEach(module('frontendApp'));

  var $controller;
  var rootScope;

  beforeEach(inject(function ($rootScope, _$controller_) {
    $controller = _$controller_;
    rootScope = $rootScope;
  }));


  it('should create source palettes', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsChangePaletteCtrl', {$scope: $scope});

    var palettes = [{
      "paletteId": 1,
      "localTypeId": 90,
      "localTypeName": "1/2 MINI 3 KG ",
      "localTypeWeight": 3,
      "remoteTypeId": 0,
      "remoteTypeName": null,
      "remoteTypeWeight": 0,
      "amount": 1,
      "warehousePaletteStatus": "ON_STOCK"
    }, {
      "paletteId": 1,
      "localTypeId": 155,
      "localTypeName": "2/3,5 ANGLIA 0,17",
      "localTypeWeight": 0.68,
      "remoteTypeId": 0,
      "remoteTypeName": null,
      "remoteTypeWeight": 0,
      "amount": 1,
      "warehousePaletteStatus": "ON_STOCK"
    }, {
      "paletteId": 1,
      "localTypeId": 286,
      "localTypeName": "2,5/4 BRAZ 0,250 ",
      "localTypeWeight": 1,
      "remoteTypeId": 0,
      "remoteTypeName": null,
      "remoteTypeWeight": 0,
      "amount": 1,
      "warehousePaletteStatus": "ON_STOCK"
    }, {
      "paletteId": 2,
      "localTypeId": 372,
      "localTypeName": "2/3,5ASDA0,200T013",
      "localTypeWeight": 0.8,
      "remoteTypeId": 0,
      "remoteTypeName": null,
      "remoteTypeWeight": 0,
      "amount": 2,
      "warehousePaletteStatus": "ON_STOCK"
    }];
    var tested = $scope.createSourcePalettes(palettes);
    expect(tested.length).toEqual(2);
    expect(tested[0].paletteId).toEqual(1);
    expect(tested[0].types.length).toEqual(3);
    expect(tested[0].totalAmount).toEqual(3);
    expect(tested[1].paletteId).toEqual(2);
    expect(tested[1].totalAmount).toEqual(2);
  });



});
