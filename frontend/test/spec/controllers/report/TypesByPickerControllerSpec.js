'use strict';

describe('TypesByPicker', function () {
  beforeEach(module('frontendApp'));

  var $controller;
  var rootScope;

  beforeEach(inject(function ($rootScope, _$controller_) {
    $controller = _$controller_;
    rootScope = $rootScope;
  }));

  describe('TypesByPicker controller:  ', function () {

    it('should get unique dates ', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickerCtrl', {$scope: $scope});
      var harvest = [
        {"date":"2018-01-01"},
        {"date":"2018-01-01"},
        {"date":"2018-01-02"},
        {"date":"2018-01-05"},
        {"date":"2018-01-02"},
        {"date":"2018-01-04"}
        ];
      expect($scope.getUniqueDates(harvest).length).toEqual(4);
      expect($scope.getUniqueDates(harvest)[0]).toEqual('2018-01-01');
      expect($scope.getUniqueDates(harvest)[1]).toEqual('2018-01-02');
      expect($scope.getUniqueDates(harvest)[2]).toEqual('2018-01-04');
      expect($scope.getUniqueDates(harvest)[3]).toEqual('2018-01-05');
    });

    it('should get unique types ', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickerCtrl', {$scope: $scope});
      var harvest = [
        {"typeId": 20, "export": 2},
        {"typeId": 21, "export": 1},
        {"typeId": 20, "export": 2},
        {"typeId": 201, "export": 2}
        ];
      expect($scope.getUniqueTypes(harvest).length).toEqual(3);
      expect($scope.getUniqueTypes(harvest)[0].id).toEqual(21);
      expect($scope.getUniqueTypes(harvest)[1].id).toEqual(20);
      expect($scope.getUniqueTypes(harvest)[2].id).toEqual(201);
    });

    it('should day quality ', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickerCtrl', {$scope: $scope});
      var harvest = [
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 21, "export": 1, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-02","totalWeight": 100000}
    ];
      expect($scope.getDayQuality(harvest,'2018-01-01')).toEqual(80);
    });

    it('should day total weight ', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickerCtrl', {$scope: $scope});
      var harvest = [
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 21, "export": 1, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-02","totalWeight": 100000}
      ];
      expect($scope.getDayTotalWeight(harvest,'2018-01-01')).toEqual(500);
    });

    it('should type total weight ', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickerCtrl', {$scope: $scope});
      var harvest = [
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 21, "export": 1, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-02","totalWeight": 150000}
      ];
      expect($scope.getTypeTotalWeight(harvest,20)).toEqual(550);
      expect($scope.getTypeTotalWeight(harvest,21)).toEqual(100);
    });

    it('should type total pcs ', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickerCtrl', {$scope: $scope});
      var harvest = [
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 21, "export": 1, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-02","totalWeight": 150000}
      ];
      var type20 = {};
      type20.id = 20;
      type20.weightG = 1000;
      var type21 = {};
      type21.id = 21;
      type21.weightG = 2000;
      expect($scope.getTypeTotalPcs(harvest,type20)).toEqual(550);
      expect($scope.getTypeTotalPcs(harvest,type21)).toEqual(50);
    });

    it('should total pcs ', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickerCtrl', {$scope: $scope});
      var harvest = [
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 21, "export": 1, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-02","totalWeight": 150000}
      ];
      var type20 = {};
      type20.id = 20;
      type20.weightG = 1000;
      var type21 = {};
      type21.id = 21;
      type21.weightG = 2000;
      var types = [];
      types.push(type20);
      types.push(type21);
      expect($scope.getTotalPcs(harvest,types)).toEqual(600);
    });


    it('should return day type pcs ', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickerCtrl', {$scope: $scope});
      var harvest = [
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 21, "export": 1, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-02","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-02","totalWeight": 150000}
      ];
      var type20 = {};
      type20.id = 20;
      type20.weightG = 1000;
      var type21 = {};
      type21.id = 21;
      type21.weightG = 2000;
      expect($scope.getDayTypePcs(harvest,type20,'2018-01-01')).toEqual(300);
      expect($scope.getDayTypePcs(harvest,type21,'2018-01-01')).toEqual(50);
      expect($scope.getDayTypePcs(harvest,type20,'2018-01-02')).toEqual(250);
    });


    it('should return day total pcs ', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickerCtrl', {$scope: $scope});
      var harvest = [
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 21, "export": 1, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-02","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-02","totalWeight": 150000}
      ];
      var type20 = {};
      type20.id = 20;
      type20.weightG = 1000;
      var type21 = {};
      type21.id = 21;
      type21.weightG = 2000;
      var types= [];
      types.push(type20);
      types.push(type21);
      expect($scope.getDayTotalPcs(harvest,types,'2018-01-01')).toEqual(350);
      expect($scope.getDayTotalPcs(harvest,types,'2018-01-02')).toEqual(250);
    });


    it('should total quality ', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickerCtrl', {$scope: $scope});
      var harvest = [
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 21, "export": 1, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-02","totalWeight": 150000}
      ];
      expect($scope.getTotalQuality(harvest)).toEqual(84.62);
    });


    it('should total weight ', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickerCtrl', {$scope: $scope});
      var harvest = [
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 21, "export": 1, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-02","totalWeight": 150000}
      ];
      expect($scope.getTotalWeight(harvest)).toEqual(650);
    });

    it('should day type  weight ', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickerCtrl', {$scope: $scope});
      var harvest = [
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 21, "export": 1, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-01","totalWeight": 100000},
        {"typeId": 20, "export": 2, "date":"2018-01-02","totalWeight": 150000}
      ];
      expect($scope.getDayTypeWeight(harvest,20,'2018-01-01')).toEqual(400);
    });



  });
});
