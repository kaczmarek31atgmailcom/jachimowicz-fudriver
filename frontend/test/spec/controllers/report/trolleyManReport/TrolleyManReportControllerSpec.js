'use strict';

describe('TrolleyManReportController', function () {
  beforeEach(module('frontendApp'));

  var $controller;
  var rootScope;

  beforeEach(inject(function ($rootScope, _$controller_) {
    $controller = _$controller_;
    rootScope = $rootScope;
  }));

  describe('TrolleyManController:  ', function () {

    it('powinien znalezc unikalne rodzaj ', function () {
      var $scope = rootScope.$new();
      $controller('TrolleyManReportCtrl', {$scope: $scope});
      var report = [{
        "trolleyManDto": {"id": 1, "name": "Marcin", "surname": "Kaczmarek", "active": false},
        "deliverables": [{"typeId": 86, "typeWeight": 2400, "typeName": "B", "totalWeight": 4800, "date": "2021-09-24"}, {
          "typeId": 86,
          "typeWeight": 2400,
          "typeName": "B",
          "totalWeight": 2400,
          "date": "2021-09-28"
        }]
      }, {
        "trolleyManDto": {"id": 2, "name": "Katarzyna - Królowa", "surname": "Kaczmarek", "active": false},
        "deliverables": [{"typeId": 86, "typeWeight": 2400, "typeName": "B", "totalWeight": 4800, "date": "2021-09-24"}, {
          "typeId": 55,
          "typeWeight": 2500,
          "typeName": "A",
          "totalWeight": 2500,
          "date": "2021-09-24"
        }, {"typeId": 88, "typeWeight": 2200, "typeName": "C", "totalWeight": 2200, "date": "2021-09-28"}]
      }];

      var tested = $scope.getUniqueTypes(report);
      expect(tested.length).toEqual(3);
      expect(tested[0].id).toEqual(55);
      expect(tested[0].name).toEqual('A');
      expect(tested[0].weight).toEqual(2500);
      expect(tested[1].id).toEqual(86);
      expect(tested[1].name).toEqual('B');
      expect(tested[1].weight).toEqual(2400);
      expect(tested[2].id).toEqual(88);
      expect(tested[2].name).toEqual('C');
      expect(tested[2].weight).toEqual(2200);
    });

    it('powinien znalezc unikalnych wózkowych ', function () {
      var $scope = rootScope.$new();
      $controller('TrolleyManReportCtrl', {$scope: $scope});
      var report = [{
        "trolleyManDto": {"id": 1, "name": "A", "surname": "AS", "active": false},
        "deliverables": [{"typeId": 86, "typeWeight": 2400, "typeName": "B", "totalWeight": 4800, "date": "2021-09-24"}, {
          "typeId": 86,
          "typeWeight": 2400,
          "typeName": "B",
          "totalWeight": 2400,
          "date": "2021-09-28"
        }]
      }, {
        "trolleyManDto": {"id": 2, "name": "B", "surname": "BS", "active": false},
        "deliverables": [{"typeId": 86, "typeWeight": 2400, "typeName": "B", "totalWeight": 4800, "date": "2021-09-24"}, {
          "typeId": 55,
          "typeWeight": 2500,
          "typeName": "A",
          "totalWeight": 2500,
          "date": "2021-09-24"
        }, {"typeId": 88, "typeWeight": 2200, "typeName": "C", "totalWeight": 2200, "date": "2021-09-28"}]
      }];

      var tested = $scope.getUniqueTrolleyMen(report);
      expect(tested.length).toEqual(2);
      expect(tested[0].id).toEqual(1);
      expect(tested[0].name).toEqual('A');
      expect(tested[0].surname).toEqual('AS');
      expect(tested[1].id).toEqual(2);
      expect(tested[1].name).toEqual('B');
      expect(tested[1].surname).toEqual('BS');
    });


    it('powinien wygenerować tablicę z ilościami wg radzajów i wózkowych ', function () {
      var $scope = rootScope.$new();
      $controller('TrolleyManReportCtrl', {$scope: $scope});
      var report = [{
        "trolleyManDto": {"id": 1, "name": "A", "surname": "AS", "active": false},
        "deliverables": [{"typeId": 86, "typeWeight": 2400, "typeName": "B", "totalWeight": 4800, "date": "2021-09-24"}, {
          "typeId": 86,
          "typeWeight": 2400,
          "typeName": "B",
          "totalWeight": 2400,
          "date": "2021-09-28"
        }]
      }, {
        "trolleyManDto": {"id": 2, "name": "B", "surname": "BS", "active": false},
        "deliverables": [{"typeId": 86, "typeWeight": 2400, "typeName": "B", "totalWeight": 4800, "date": "2021-09-24"}, {
          "typeId": 55,
          "typeWeight": 2500,
          "typeName": "A",
          "totalWeight": 2500,
          "date": "2021-09-24"
        }, {"typeId": 88, "typeWeight": 2200, "typeName": "C", "totalWeight": 8000, "date": "2021-09-28"}]
      }];

      var uniqueTypes = [{id: 55, name: 'A', weight: 2500}, {id: 86, name: 'B', weight: 2400}, {id: 88, name: 'C', weight: 2200}];
      var uniqueTrolleMen = [{id: 1, name: 'A', surname: 'AS'}, {id: 2, name: 'B', surname: 'BS'}];

      var tested = $scope.getReport(uniqueTrolleMen, uniqueTypes, report);
      expect(tested.length).toEqual(2);
      expect(tested[0].name).toEqual("A");
      expect(tested[0].typesWeights.length).toEqual(3);
      expect(tested[0].typesWeights[0]).toEqual(0);
      expect(tested[0].typesWeights[1]).toEqual(7200);
      expect(tested[0].typesWeights[2]).toEqual(0);
      expect(tested[1].typesWeights[0]).toEqual(2500);
      expect(tested[1].typesWeights[1]).toEqual(4800);
      expect(tested[1].typesWeights[2]).toEqual(8000);
    });

    it('powinien dodać sumy zbiorów dla wózkowych ', function () {
      var $scope = rootScope.$new();
      $controller('TrolleyManReportCtrl', {$scope: $scope});

      var report = [
        {"id": 1, "name": "Marcin", "surname": "Kaczmarek", "typesWeights": [7200, 0]},
        {"id": 2, "name": "Katarzyna - Królowa", "surname": "Kaczmarek", "typesWeights": [7200, 2500]}
      ];

      var tested = $scope.addTrolleyManTotals(report);
      expect(tested.length).toEqual(2);
      expect(tested[0].totalWeight).toEqual(7200)
      expect(tested[1].totalWeight).toEqual(9700)
    });

    it('powinien obliczyć sumy zbiorów dla rodzai ', function () {
      var $scope = rootScope.$new();
      $controller('TrolleyManReportCtrl', {$scope: $scope});

      var report = [{
        "trolleyManDto": {"id": 1, "name": "A", "surname": "AS", "active": false},
        "deliverables": [{"typeId": 86, "typeWeight": 2400, "typeName": "B", "totalWeight": 4800, "date": "2021-09-24"}, {
          "typeId": 86,
          "typeWeight": 2400,
          "typeName": "B",
          "totalWeight": 2400,
          "date": "2021-09-28"
        }]
      }, {
        "trolleyManDto": {"id": 2, "name": "B", "surname": "BS", "active": false},
        "deliverables": [{"typeId": 86, "typeWeight": 2400, "typeName": "B", "totalWeight": 4800, "date": "2021-09-24"}, {
          "typeId": 55,
          "typeWeight": 2500,
          "typeName": "A",
          "totalWeight": 2500,
          "date": "2021-09-24"
        }, {"typeId": 88, "typeWeight": 2200, "typeName": "C", "totalWeight": 8000, "date": "2021-09-28"}]
      }];


      var uniqueTypes = [
        {id: 86, name: 'A 2,4kg luz', weight: 2400},
        {id: 55, name: 'A 2,5kg luz', weight: 2500},
        {id: 88, name: 'A 2,5kg luz', weight: 2500}
      ];

      var tested = $scope.findTypeTotals(report, uniqueTypes);
      expect(tested.length).toEqual(3);
      expect(tested[0].totalWeight).toEqual(12000);
      expect(tested[1].totalWeight).toEqual(2500);
      expect(tested[2].totalWeight).toEqual(8000);
    });

    it('powinien obliczyć ogólną sumę zbiorów ', function () {
      var $scope = rootScope.$new();
      $controller('TrolleyManReportCtrl', {$scope: $scope});

      var report = [{
        "trolleyManDto": {"id": 1, "name": "A", "surname": "AS", "active": false},
        "deliverables": [{"typeId": 86, "typeWeight": 2400, "typeName": "B", "totalWeight": 4800, "date": "2021-09-24"}, {
          "typeId": 86,
          "typeWeight": 2400,
          "typeName": "B",
          "totalWeight": 2400,
          "date": "2021-09-28"
        }]
      }, {
        "trolleyManDto": {"id": 2, "name": "B", "surname": "BS", "active": false},
        "deliverables": [{"typeId": 86, "typeWeight": 2400, "typeName": "B", "totalWeight": 4800, "date": "2021-09-24"}, {
          "typeId": 55,
          "typeWeight": 2500,
          "typeName": "A",
          "totalWeight": 2500,
          "date": "2021-09-24"
        }, {"typeId": 88, "typeWeight": 2200, "typeName": "C", "totalWeight": 8000, "date": "2021-09-28"}]
      }];

      var tested = $scope.findTotalWeight(report);
      expect(tested).toEqual(22500);

    });


  });
});
