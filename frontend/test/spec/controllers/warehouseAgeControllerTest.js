'use strict';

describe('warehouseAgeCtrl', function () {
  beforeEach(module('frontendApp'));

  var $controller;
  var rootScope;

  beforeEach(inject(function ($rootScope, _$controller_) {
    $controller = _$controller_;
    rootScope = $rootScope;
  }));

  describe('Find and sort harvest dates on stock', function () {
    it('should find table of uniq dates', function () {
      var $scope = rootScope.$new();
      var controller = $controller('warehouseAgeCtrl', {$scope: $scope});
      var stock = [];
      $scope.days = [];
      stock.push({'harvestDate': '2016-01-01'});
      $scope.findUniqueDays(stock);
      expect($scope.days.length).toEqual(1);
      stock.push({'harvestDate': '2016-01-01'});
      $scope.findUniqueDays(stock);
      expect($scope.days.length).toEqual(1);
      stock.push({'harvestDate': '2016-01-02'});
      $scope.findUniqueDays(stock);
      expect($scope.days.length).toEqual(2);
    });

    it('should return 1 on older to newer date compare', function () {
      var $scope = rootScope.$new();
      var controller = $controller('warehouseAgeCtrl', {$scope: $scope});
      var date1 = '2016-10-01';
      var date2 = '2016-10-02';
      expect($scope.dateComparator(date1, date2)).toEqual(1);
    });
/*
    it('should return -1 on newer to older date compare', function () {
      var $scope = rootScope.$new();
      var controller = $controller('warehouseAgeCtrl', {$scope: $scope});
      var date1 = '2016-10-01';
      var date2 = '2016-10-02';
      expect($scope.dateComparator(date2, date1)).toEqual(-1);
    });
*/
    it('should return 0 on the same dates compare', function () {
      var $scope = rootScope.$new();
      var controller = $controller('warehouseAgeCtrl', {$scope: $scope});
      var date1 = '2016-10-01';
      expect($scope.dateComparator(date1, date1)).toEqual(0);
    });
  });


  describe('Get proper text style depending on stock age', function () {
    it('should return text-danger', function () {
      var $scope = rootScope.$new();
      var controller = $controller('warehouseAgeCtrl', {$scope: $scope});
      var day = moment().subtract(5, 'days');
      expect($scope.getAgeStyle(day)).toEqual('text text-danger')
    });

    it('should return text-warning', function () {
      var $scope = rootScope.$new();
      var controller = $controller('warehouseAgeCtrl', {$scope: $scope});
      var day = moment().subtract(2, 'days');
      expect($scope.getAgeStyle(day)).toEqual('text text-warning')
    });

    it('should return text-warning', function () {
      var $scope = rootScope.$new();
      var controller = $controller('warehouseAgeCtrl', {$scope: $scope});
      var day = moment().subtract(1, 'days');
      expect($scope.getAgeStyle(day)).toEqual('text text-default')
    });

  });


});








