'use strict';

describe('ScannerSummaryControllerSpec', function () {
  beforeEach(module('frontendApp'));

  var $controller;
  var rootScope;

  beforeEach(inject(function ($rootScope, _$controller_) {
    $controller = _$controller_;
    rootScope = $rootScope;
  }));


  it('should count total amount', function () {
    var $scope = rootScope.$new();
    $controller('scannerSummaryCtrl', {$scope: $scope});
    var headers = [
      {
        "rodzajId": 90,
        "rodzajName": "235ASDA0200T013",
        "rodzajWeight": 3,
        "spaceId": 0,
        "totalPcs": 2,
        "totalWeight": 6,
        "wozekNr": 103355
  },
      {
        "rodzajId": 123,
        "rodzajName": "235ASDA0200T013",
        "rodzajWeight": 0.8,
        "spaceId": 0,
        "totalPcs": 1,
        "totalWeight": 0.8,
        "wozekNr": 103356
      },
      {
        "rodzajId": 123,
        "rodzajName": "235ASDA0200T013",
        "rodzajWeight": 0.8,
        "spaceId": 3,
        "totalPcs": 1,
        "totalWeight": 0.8,
        "wozekNr": 103357
      }
  ];
    var tested = $scope.countTotalAmount(headers);
    expect(tested).toEqual(3);
  });

});
