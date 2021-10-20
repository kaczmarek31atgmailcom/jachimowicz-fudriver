'use strict';

describe('EastMushroomsShipmentDetailsControllerSpec', function () {
  beforeEach(module('frontendApp'));

  var $controller;
  var rootScope;

  beforeEach(inject(function ($rootScope, _$controller_) {
    $controller = _$controller_;
    rootScope = $rootScope;
  }));


  it('should count total weight palette with single type', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsShipmentDetailsCtrl', {$scope: $scope});
    var palettes = [];
    var palette1 = {types:[{typeId:1,typeName:"typ1",typeWeight:1500,amount:200,weight:300000}]};
    palettes.push(palette1);
    var tested = $scope.getTypesSummary(palettes);
    expect(tested[0].weight).toEqual(300000);
    expect(tested[0].amount).toEqual(200);
  });

  it('powinno zsumować dwie linie tego samego rodzaju', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsShipmentDetailsCtrl', {$scope: $scope});
    var palettes = [];
    var palette1 = {types:[{typeId:1,typeName:"typ1",typeWeight:1500,amount:200,weight:300000}]};
    var palette2 = {types:[{typeId:1,typeName:"typ1",typeWeight:1500,amount:200,weight:300000}]};
    palettes.push(palette1);
    palettes.push(palette2);
    var tested = $scope.getTypesSummary(palettes);
    expect(tested.length).toEqual(1);
    expect(tested[0].amount).toEqual(400);
    expect(tested[0].weight).toEqual(600000);
  });

  it('powinno zsumować po dwie linie dwóch rodzajów', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsShipmentDetailsCtrl', {$scope: $scope});
    var palettes = [];
    var palette1 = {types:[{typeId:1,typeName:"typ1",typeWeight:1500,amount:200,weight:300000}]};
    var palette2 = {types:[{typeId:2,typeName:"typ2",typeWeight:100,amount:100,weight:10000}]};
    var palette3 = {types:[{typeId:1,typeName:"typ1",typeWeight:1500,amount:200,weight:300000}]};
    var palette4 = {types:[{typeId:2,typeName:"typ2",typeWeight:100,amount:100,weight:10000}]};
    palettes.push(palette1);
    palettes.push(palette2);
    palettes.push(palette3);
    palettes.push(palette4);
    var tested = $scope.getTypesSummary(palettes);
    expect(tested.length).toEqual(2);
    expect(tested[0].amount).toEqual(400);
    expect(tested[0].weight).toEqual(600000);
    expect(tested[1].amount).toEqual(200);
    expect(tested[1].weight).toEqual(20000);
  });

  it('Powinien obliczyć sumę rodzajów palet przy dwóch takich samych paletach', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsShipmentDetailsCtrl', {$scope: $scope});
    var palettes = [];
    var palette1 = {"paletteTypeId":1, "paletteTypeName":"Euro"};
    var palette2 = {"paletteTypeId":1, "paletteTypeName":"Euro"};
    palettes.push(palette1);
    palettes.push(palette2);
    var tested = $scope.getPaletteTypeSummary(palettes);
    expect(tested.length).toEqual(1);
    expect(tested[0].amount).toEqual(2);
  });


  it('Powinien obliczyć sumę rodzajów palet przy dwóch typach palet', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsShipmentDetailsCtrl', {$scope: $scope});
    var palettes = [];
    var palette1 = {"paletteTypeId":1, "paletteTypeName":"Euro"};
    var palette2 = {"paletteTypeId":2, "paletteTypeName":"Triplet"};
    var palette3 = {"paletteTypeId":1, "paletteTypeName":"Euro"};
    var palette4 = {"paletteTypeId":2, "paletteTypeName":"Triplet"};
    var palette5 = {"paletteTypeId":2, "paletteTypeName":"Triplet"};
    palettes.push(palette1);
    palettes.push(palette2);
    palettes.push(palette3);
    palettes.push(palette4);
    palettes.push(palette5);
    var tested = $scope.getPaletteTypeSummary(palettes);
    expect(tested.length).toEqual(2);
    expect(tested[0].amount).toEqual(2);
    expect(tested[1].amount).toEqual(3);
  });

  it('powinno zliczyć ilość sztuk w pustym shipmencie', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsShipmentDetailsCtrl', {$scope: $scope});
    var palettes = [];
    var tested = $scope.getTotalNumberOfBoxes(palettes);
    expect(tested).toEqual(0);
  });


  it('powinno zliczyć ilość sztuk w shipmencie', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsShipmentDetailsCtrl', {$scope: $scope});
    var palettes = [];
    var palette1 = {types:[{typeId:1,typeName:"typ1",typeWeight:1500,amount:200,weight:300000}]};
    var palette2 = {types:[{typeId:1,typeName:"typ1",typeWeight:1500,amount:200,weight:300000}]};
    var palette3 = {types:[{typeId:1,typeName:"typ1",typeWeight:1500,amount:200,weight:300000},{typeId:1,typeName:"typ1",typeWeight:1500,amount:200,weight:300000}]};
    var tested = $scope.getTotalNumberOfBoxes(palettes);
    expect(tested).toEqual(0);
    palettes.push(palette1);
    palettes.push(palette2);
    palettes.push(palette3);
    var tested = $scope.getTotalNumberOfBoxes(palettes);
    expect(tested).toEqual(800);
  });

  it('powinno zliczyć łączną wagę', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsShipmentDetailsCtrl', {$scope: $scope});
    var palettes = [];
    var palette1 = {types:[{typeId:1,typeName:"typ1",typeWeight:1500,amount:200,weight:300000}]};
    var palette2 = {types:[{typeId:1,typeName:"typ1",typeWeight:1500,amount:200,weight:300000}]};
    var palette3 = {types:[{typeId:1,typeName:"typ1",typeWeight:1500,amount:200,weight:300000},{typeId:1,typeName:"typ1",typeWeight:1500,amount:200,weight:300000}]};
    var tested = $scope.getTotalWeight(palettes);
    expect(tested).toEqual(0);
    palettes.push(palette1);
    palettes.push(palette2);
    palettes.push(palette3);
    var tested = $scope.getTotalWeight(palettes);
    expect(tested).toEqual(1200000);
  });

  it('powinno zliczyć łączną wagę ułamków', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsShipmentDetailsCtrl', {$scope: $scope});
    var palettes = [];
    var palette1 = {types:[{typeId:1,typeName:"typ1",typeWeight:1500,amount:200,weight:300000.02}]};
    var palette2 = {types:[{typeId:1,typeName:"typ1",typeWeight:1500,amount:200,weight:300000.10}]};

    var tested = $scope.getTotalWeight(palettes);
    expect(tested).toEqual(0);
    palettes.push(palette1);
    palettes.push(palette2);

    var tested = $scope.getTotalWeight(palettes);
    expect(tested).toEqual(600000.12);
  });




});
