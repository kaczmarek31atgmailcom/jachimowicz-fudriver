'use strict';

describe('EastMushroomsCommitHarvestPaletteToWarehouseControllerSpec', function () {
  beforeEach(module('frontendApp'));

  var $controller;
  var rootScope;

  beforeEach(inject(function ($rootScope, _$controller_) {
    $controller = _$controller_;
    rootScope = $rootScope;
  }));


  it('should create types summary', function () {
    var $scope = rootScope.$new();
    $controller('CommitHarvestPaletteToWarehouseCtrl', {$scope: $scope});
    var headers = [
      {totalPcs: 1, totalWeight: 3, checked: true},
      {totalPcs: 1, totalWeight: 3, checked: true},
      {totalPcs: 2, totalWeight: 2, checked: true},
      {totalPcs: 1, totalWeight: 3, checked: ''},
      {totalPcs: 3, totalWeight: 10, checked: true},
      {totalPcs: 12, totalWeight: 1.1, checked: true}
    ];
    expect($scope.countCheckedAmount(headers)).toEqual(19);
    expect($scope.countCheckedWeight(headers)).toEqual(19.1);

  });
});
