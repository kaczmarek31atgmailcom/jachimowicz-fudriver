'use strict';

describe('HotelBookingControllerSpec', function () {
  beforeEach(module('frontendApp'));

  var $controller;
  var rootScope;

  beforeEach(inject(function ($rootScope, _$controller_) {
    $controller = _$controller_;
    rootScope = $rootScope;
  }));


  it('should get unique days', function () {
    var $scope = rootScope.$new();
    $controller('HotelBookingCtrl', {$scope: $scope});
    var startDate = '2018-01-01';
    var endDate = '2018-01-10';
    var tested = $scope.getUniqueDays(startDate,endDate);
    expect(tested.length).toEqual(10);
    expect(tested[0].date).toEqual('2018-01-01');
    expect(tested[1].date).toEqual('2018-01-02');
    expect(tested[2].date).toEqual('2018-01-03');
    expect(tested[3].date).toEqual('2018-01-04');
    expect(tested[4].date).toEqual('2018-01-05');
    expect(tested[5].date).toEqual('2018-01-06');
    expect(tested[6].date).toEqual('2018-01-07');
    expect(tested[7].date).toEqual('2018-01-08');
    expect(tested[8].date).toEqual('2018-01-09');
    expect(tested[9].date).toEqual('2018-01-10');
  });

  it('should get occupancies for given bed', function () {
    var $scope = rootScope.$new();
    $controller('HotelBookingCtrl', {$scope: $scope});
    var occupancy = [{bedId:1,startDate:'2018-01-03',endDate:'2018-01-05'},
      {bedId:1,startDate:'2018-01-06',endDate:'2018-01-10'},
      {bedId:2,startDate:'2018-01-03',endDate:'2018-01-11'}];
    var tested = $scope.getBedOccupancyDaysOnly(1,occupancy);
    expect(tested.length).toEqual(2);
    expect(tested[0].startDate).toEqual('2018-01-03');
    expect(tested[0].endDate).toEqual('2018-01-05');
    expect(tested[0].bedId).toEqual(1);
    expect(tested[1].startDate).toEqual('2018-01-06');
    expect(tested[1].endDate).toEqual('2018-01-10');
    expect(tested[1].bedId).toEqual(1);
  });


  it('should get utilization for three days', function () {
    var $scope = rootScope.$new();
    $controller('HotelBookingCtrl', {$scope: $scope});
    var occupancy = [{bedId:1,startDate:'2018-01-03',endDate:'2018-01-05'},
      {bedId:1,startDate:'2018-01-06',endDate:'2018-01-10'},
      {bedId:2,startDate:'2018-01-03',endDate:'2018-01-11'}];
    var days = [{date:'2018-01-06'},{date:'2018-01-07'},{date:'2018-02-06'}];
    var tested = $scope.getDailyUtilization(days,occupancy);
    expect(tested.length).toEqual(3);
    expect(tested[0].utilization).toEqual(2);
    expect(tested[1].utilization).toEqual(2);
    expect(tested[2].utilization).toEqual(0);
  });

});

