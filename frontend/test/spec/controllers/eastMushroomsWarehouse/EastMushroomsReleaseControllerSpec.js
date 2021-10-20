'use strict';

describe('EastMushroomsReleaseControllerSpec', function () {
  beforeEach(module('frontendApp'));

  var $controller;
  var rootScope;

  beforeEach(inject(function ($rootScope, _$controller_) {
    $controller = _$controller_;
    rootScope = $rootScope;
  }));


  it('should create warehouse palettes', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsReleaseCtrl', {$scope: $scope});
    var inputPalettes = [
      {paletteId: 4, localTypeId: 1, amount: 10},
      {paletteId: 4, localTypeId: 2, amount: 20},
      {paletteId: 5, localTypeId: 2, amount: 20}
    ];
    var tested = $scope.createPalettes(inputPalettes);
    expect(tested.length).toEqual(2);
    expect(tested[0].totalAmount).toEqual(30);
    expect(tested[0].types.length).toEqual(2);
    expect(tested[0].types[0].localTypeId).toEqual(1);
    expect(tested[0].types[1].localTypeId).toEqual(2);
  });


  it('should create warehouse palette', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsReleaseCtrl', {$scope: $scope});
    var inputPalette = {
      paletteId: 60,
      localTypeId: 179,
      localTypeName: "30-40mm 6*250g",
      localTypeWeight: 1.5,
      remoteTypeId: 1,
      remoteTypeName: "ala",
      remoteTypeWeight: 1.5,
      amount: 2,
      warehousePaletteStatus: "ON_STOCK"
    };
    var tested = $scope.createPalette(inputPalette);
    expect(tested.paletteId).toEqual(60);
    expect(tested.totalAmount).toEqual(2);
    expect(tested.types[0].localTypeId).toEqual(179);
    expect(tested.types[0].localTypeName).toEqual("30-40mm 6*250g");
    expect(tested.types[0].localTypeWeight).toEqual(1.5);
    expect(tested.types[0].remoteTypeId).toEqual(1);
    expect(tested.types[0].remoteTypeName).toEqual("ala");
    expect(tested.types[0].remoteTypeWeight).toEqual(1.5);
  });

});
