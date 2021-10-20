'use strict';

describe('NewHotelReservationCtrlSpec', function () {
  beforeEach(module('frontendApp'));

  var $controller;
  var rootScope;

  beforeEach(inject(function ($rootScope, _$controller_) {
    $controller = _$controller_;
    rootScope = $rootScope;
  }));


  it('should find biggest start period date ', function () {
    var $scope = rootScope.$new();
    $controller('NewHotelReservationCtrl', {$scope: $scope});
    var occupancy = [{bedId:1,startDate:'2018-01-03',endDate:'2018-01-05'},
      {bedId:1,startDate:'2018-01-06',endDate:'2018-01-10'},
      {bedId:1,startDate:'2018-01-01',endDate:'2018-01-02'}];
    expect($scope.findMinNewPeriodDate(occupancy,'2018-01-20','2018-01-01')).toEqual('2018-01-10');
  });

  it('should find biggest start period date when no occupancies', function () {
    var $scope = rootScope.$new();
    $controller('NewHotelReservationCtrl', {$scope: $scope});
    var occupancy =[];
    expect($scope.findMinNewPeriodDate(occupancy,'2018-01-20','2018-01-01')).toEqual('2017-12-31');
  });

  it('should find biggest end period date ', function () {
    var $scope = rootScope.$new();
    $controller('NewHotelReservationCtrl', {$scope: $scope});
    var occupancy = [{bedId:1,startDate:'2018-01-03',endDate:'2018-01-05'},
      {bedId:1,startDate:'2018-01-22',endDate:'2018-01-23'},
      {bedId:1,startDate:'2018-01-24',endDate:'2018-01-26'}];
    expect($scope.findMaxNewPeriodDate(occupancy,'2018-01-10','2018-01-31')).toEqual('2018-01-21');
  });

  it('should find biggest end period date 2', function () {
    var $scope = rootScope.$new();
    $controller('NewHotelReservationCtrl', {$scope: $scope});
    var occupancy = [{bedId:1,startDate:'2018-01-24',endDate:'2018-01-26'}];
    expect($scope.findMaxNewPeriodDate(occupancy,'2018-01-10','2018-01-31')).toEqual('2018-01-23');
  });


  it('should find biggest end period date when no occupancies', function () {
    var $scope = rootScope.$new();
    $controller('NewHotelReservationCtrl', {$scope: $scope});
    var occupancy =[];
    expect($scope.findMaxNewPeriodDate(occupancy,'2018-01-20','2018-01-31')).toEqual('2018-01-31');
  });

});

