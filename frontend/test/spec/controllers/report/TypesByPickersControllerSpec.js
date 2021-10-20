'use strict';

describe('TypesByPickers', function () {
  beforeEach(module('frontendApp'));

  var $controller;
  var rootScope;

  beforeEach(inject(function ($rootScope, _$controller_) {
    $controller = _$controller_;
    rootScope = $rootScope;
  }));

  describe('TypesByPickers controller:  ', function () {

    it('should get unique types ', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickersCtrl', {$scope: $scope});
      var harvest = [
        {"typeId": 20, "export": 2},
        {"typeId": 21, "export": 1},
        {"typeId": 20, "export": 2},
        {"typeId": 201, "export": 2}];
      expect($scope.getUniqueTypes(harvest).length).toEqual(3);
      expect($scope.getUniqueTypes(harvest)[0].id).toEqual(21);
      expect($scope.getUniqueTypes(harvest)[1].id).toEqual(20);
      expect($scope.getUniqueTypes(harvest)[2].id).toEqual(201);

    });

    it('should get unique pickers ', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickersCtrl', {$scope: $scope});
      var harvest = [
        {"pickerId": 20}, {"pickerId": 21}, {"pickerId": 20}, {"pickerId": 22}, {"pickerId": 20}, {"pickerId": 21}
      ];
      expect($scope.getUniquePickers(harvest).length).toEqual(3);
      expect($scope.getUniquePickers(harvest)[0].id).toEqual(20);
      expect($scope.getUniquePickers(harvest)[1].id).toEqual(21);
      expect($scope.getUniquePickers(harvest)[2].id).toEqual(22);
    });

    it('should get type - picker weight ', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickersCtrl', {$scope: $scope});
      var harvest = [
        {"pickerId": 20, "typeId": 1, "totalWeight": 1200}, {"pickerId": 20, "typeId": 1, "totalWeight": 1200}, {"pickerId": 21, "typeId": 1, "totalWeight": 100}, {
          "pickerId": 20,
          "typeId": 2,
          "totalWeight": 100
        }
      ];
      expect($scope.getPickerTypeWeight(harvest, 20, 1)).toEqual(2.4);
    });

    it('should get picker total  weight ', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickersCtrl', {$scope: $scope});
      var harvest = [
        {"pickerId": 20, "typeId": 1, "totalWeight": 1200}, {"pickerId": 20, "typeId": 1, "totalWeight": 1200}, {"pickerId": 21, "typeId": 1, "totalWeight": 100}, {
          "pickerId": 20,
          "typeId": 2,
          "totalWeight": 100
        }
      ];
      expect($scope.getPickerTotalWeight(harvest, 20)).toEqual(2.5);
    });

    it('should get picker quality ', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickersCtrl', {$scope: $scope});
      var harvest = [
        {"pickerId": 20, "typeId": 1, "export": 2, "totalWeight": 12000},
        {"pickerId": 20, "typeId": 1, "export":2, "totalWeight": 10000},
        {"pickerId": 21, "typeId": 1, "export":2, "totalWeight": 1000},
        {"pickerId": 20,"typeId": 3,"export":1,"totalWeight": 1000}
      ];
      expect($scope.getPickerQuality(harvest, 20)).toEqual(95.65);
    });

    it('should get picker quality if totalWeight = 0', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickersCtrl', {$scope: $scope});
      var harvest = [
        {"pickerId": 20, "typeId": 1, "export": 2, "totalWeight": 12000},
        {"pickerId": 20, "typeId": 1, "export":2, "totalWeight": 10000},
        {"pickerId": 21, "typeId": 1, "export":2, "totalWeight": 1000},
        {"pickerId": 20,"typeId": 3,"export":1,"totalWeight": 1000}
      ];
      expect($scope.getPickerQuality(harvest, 22)).toEqual(0);
    });

    it('should get average quality', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickersCtrl', {$scope: $scope});
      var harvest = [
        {"pickerId": 20, "typeId": 1, "export": 2, "totalWeight": 12000},
        {"pickerId": 20, "typeId": 1, "export":2, "totalWeight": 10000},
        {"pickerId": 21, "typeId": 1, "export":2, "totalWeight": 1000},
        {"pickerId": 20,"typeId": 3,"export":1,"totalWeight": 10000}
      ];
      expect($scope.getTotalQuality(harvest)).toEqual(69.7);
    });

    it('should get total type weight', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickersCtrl', {$scope: $scope});
      var harvest = [
        {"pickerId": 20, "typeId": 1, "export": 2, "totalWeight": 12500},
        {"pickerId": 20, "typeId": 1, "export":2, "totalWeight": 10000},
        {"pickerId": 21, "typeId": 1, "export":2, "totalWeight": 1000},
        {"pickerId": 20,"typeId": 3,"export":1,"totalWeight": 1000}
      ];
      expect($scope.getTypeTotalWeight(harvest, 1)).toEqual(23.5);
    });

    it('should get total type pcs', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickersCtrl', {$scope: $scope});
      var harvest = [
        {"pickerId": 20, "typeId": 1, "export": 2, "totalWeight": 12500},
        {"pickerId": 20, "typeId": 1, "export":2, "totalWeight": 10000},
        {"pickerId": 21, "typeId": 1, "export":2, "totalWeight": 1000},
        {"pickerId": 20,"typeId": 3,"export":1,"totalWeight": 1000}
      ];
      var type = {};
      type.id = 1;
      type.weightG = 500;
      expect($scope.getTypeTotalPcs(harvest, type)).toEqual(47);
    });

    it('should get total pcs', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickersCtrl', {$scope: $scope});
      var harvest = [
        {"pickerId": 20, "typeId": 1, "export": 2, "totalWeight": 12500},
        {"pickerId": 20, "typeId": 1, "export":2, "totalWeight": 10000},
        {"pickerId": 21, "typeId": 1, "export":2, "totalWeight": 1000},
        {"pickerId": 20,"typeId": 3,"export":1,"totalWeight": 1000}
      ];
      var type1 = {};
      type1.id = 1;
      type1.weightG = 500;
      var type2 = {};
      type2.id = 3;
      type2.weightG = 1000;
      var types = [];
      types.push(type1);
      types.push(type2);
      expect($scope.getTotalPcs(harvest, types)).toEqual(48);
    });


    it('should get picker type pcs', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickersCtrl', {$scope: $scope});
      var harvest = [
        {"pickerId": 20, "typeId": 1, "export": 2, "totalWeight": 12500},
        {"pickerId": 20, "typeId": 1, "export":2, "totalWeight": 10000},
        {"pickerId": 21, "typeId": 1, "export":2, "totalWeight": 1000},
        {"pickerId": 20,"typeId": 3,"export":1,"totalWeight": 1000}
      ];
      var type1 = {};
      type1.id = 1;
      type1.weightG = 500;
      var type2 = {};
      type2.id = 3;
      type2.weightG = 1000;
      var types = [];
      types.push(type1);
      types.push(type2);

      expect($scope.getPickerTypePcs(harvest, 20,type1)).toEqual(45);
    });

    it('should get picker total pcs', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickersCtrl', {$scope: $scope});
      var harvest = [
        {"pickerId": 20, "typeId": 1, "export": 2, "totalWeight": 12500},
        {"pickerId": 20, "typeId": 1, "export":2, "totalWeight": 10000},
        {"pickerId": 21, "typeId": 1, "export":2, "totalWeight": 1000},
        {"pickerId": 20,"typeId": 3,"export":1,"totalWeight": 1000}
      ];
      var type1 = {};
      type1.id = 1;
      type1.weightG = 500;
      var type2 = {};
      type2.id = 3;
      type2.weightG = 1000;
      var types = [];
      types.push(type1);
      types.push(type2);

      expect($scope.getPickerTotalPcs(harvest,20,types)).toEqual(46);
    });


    it('should get total  weight', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickersCtrl', {$scope: $scope});
      var harvest = [
        {"pickerId": 20, "typeId": 1, "export": 2, "totalWeight": 12500},
        {"pickerId": 20, "typeId": 1, "export":2, "totalWeight": 10000},
        {"pickerId": 21, "typeId": 1, "export":2, "totalWeight": 1000},
        {"pickerId": 20,"typeId": 3,"export":1,"totalWeight": 1000}
      ];
      expect($scope.getTotalWeight(harvest, 1)).toEqual(24.5);
    });


    it('should get valid class', function () {
      var $scope = rootScope.$new();
      $controller('TypesByPickersCtrl', {$scope: $scope});
      expect($scope.getClass(1,10)).toEqual('text-right');
      expect($scope.getClass(2,10)).toEqual('text-right');
      expect($scope.getClass(3,10)).toEqual('text-right');
      expect($scope.getClass(4,10)).toEqual('text-right');
      expect($scope.getClass(5,10)).toEqual('text-right');
      expect($scope.getClass(6,10)).toEqual('text-right');
      expect($scope.getClass(7,10)).toEqual('text-right');
      expect($scope.getClass(8,10)).toEqual('text-right');
      expect($scope.getClass(9,10)).toEqual('text-right font-bold');
      expect($scope.getClass(10,10)).toEqual('text-right font-bold');
    });




  });
});
