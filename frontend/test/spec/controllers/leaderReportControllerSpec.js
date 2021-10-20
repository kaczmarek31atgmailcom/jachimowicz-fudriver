'use strict';
/*
describe(' leaderReportCtrl ', function () {
  beforeEach(module('frontendApp'));
  var $controller;
  var rootScope;
  var $httpBackend;
  var location;

  beforeEach(inject(function ($rootScope, _$controller_, $injector, $location) {
    $controller = _$controller_;
    rootScope = $rootScope;
    $httpBackend = $injector.get('$httpBackend');
    location = $location;
  }));

  afterEach(function () {
    $httpBackend.verifyNoOutstandingExpectation();
    $httpBackend.verifyNoOutstandingRequest();
  });

  describe(' Should load report ', function () {
    it(' should update warehouseOnly value', function () {
      var $scope = rootScope.$new();
      var controller = $controller('LeaderReportCtrl', {$scope: $scope});
      var startDay = moment().subtract(14, 'days').format("YYYY-MM-DD");
      var endDay = moment().format("YYYY-MM-DD");
      $httpBackend.expectGET('/fudriver/rest/zarobki/reportForLeader/' + startDay + '/' + endDay)
        .respond([{
          "id": 1001,
          "nr": 0,
          "name": "",
          "surname": "",
          "groupName": "Бригада 1",
          "inne": 1000,
          "kraj": 2000,
          "export": 3000,
          "minutes": 445,
          "checked": 3000,
          "rejected": 1000,
          "totalByGroups":[
            {
              "groupId":1,
              "groupName":"Grupa A",
              "groupTotal":1324100
            },
            {
              "groupId":2,
              "groupName":"Grupa 2",
              "groupTotal":13000
            },
            {
              "groupId":5,
              "groupName":"Grupa D",
              "groupTotal":81000
            }
          ]
        }, {
          "id": 1002,
          "nr": 0,
          "name": "",
          "surname": "",
          "groupName": "Бригада 1",
          "inne": 1500,
          "kraj": 2500,
          "export": 3500,
          "minutes": 123,
          "checked": 3000,
          "rejected": 300,
          "totalByGroups":[
            {
              "groupId":1,
              "groupName":"Grupa A",
              "groupTotal":1444500
            },
            {
              "groupId":2,
              "groupName":"Grupa 2",
              "groupTotal":44000
            },
            {
              "groupId":3,
              "groupName":"Grupa B",
              "groupTotal":150000
            },
            {
              "groupId":5,
              "groupName":"Grupa D",
              "groupTotal":126000
            }
          ]
        }]);
      $httpBackend.flush();
    });

    it(' should count additional values ', function () {
      var $scope = rootScope.$new();
      var controller = $controller('LeaderReportCtrl', {$scope: $scope});
      var startDay = moment().subtract(14, 'days').format("YYYY-MM-DD");
      var endDay = moment().format("YYYY-MM-DD");
      $httpBackend.expectGET('views/main.html').respond('200');
      $httpBackend.expectGET('/fudriver/rest/zarobki/reportForLeader/' + startDay + '/' + endDay)
        .respond([{
          "id": 1001,
          "nr": 0,
          "name": "",
          "surname": "",
          "groupName": "Бригада 1",
          "inne": 1000,
          "kraj": 2000,
          "export": 3000,
          "minutes": 445,
          "checked": 3000,
          "rejected": 1000,
          "totalByGroups":[
            {
              "groupId":1,
              "groupName":"Grupa A",
              "groupTotal":4000
            },
            {
              "groupId":2,
              "groupName":"Grupa 2",
              "groupTotal":2000
            },
            {
              "groupId":5,
              "groupName":"Grupa D",
              "groupTotal":1000
            }
          ]
        }, {
          "id": 1002,
          "nr": 0,
          "name": "",
          "surname": "",
          "groupName": "Бригада 1",
          "inne": 1500,
          "kraj": 2500,
          "export": 3500,
          "minutes": 123,
          "checked": 3000,
          "rejected": 300,
          "totalByGroups":[
            {
              "groupId":1,
              "groupName":"Grupa A",
              "groupTotal":500
            },
            {
              "groupId":2,
              "groupName":"Grupa 2",
              "groupTotal":2000
            },
            {
              "groupId":3,
              "groupName":"Grupa B",
              "groupTotal":4000
            },
            {
              "groupId":5,
              "groupName":"Grupa D",
              "groupTotal":10000
            }
          ]
        }]);
      $httpBackend.flush();
      expect($scope.report[0].summary).toEqual(6000);
      expect($scope.report[0].exportPercent).toEqual(50);
      expect($scope.report[0].checkedPercent).toEqual(50);
      expect($scope.report[0].rejectedPercent).toEqual(16);
      expect($scope.report[0].kgPerHour).toEqual(808.98);

      expect($scope.report[1].summary).toEqual(7500);
      expect($scope.report[1].exportPercent).toEqual(46);
      expect($scope.report[1].checkedPercent).toEqual(40);
      expect($scope.report[1].rejectedPercent).toEqual(4);
      expect($scope.report[1].kgPerHour).toEqual(3658.53);

      expect($scope.totalKraj).toEqual(4500);
      expect($scope.totalExport).toEqual(6500);
      expect($scope.totalInne).toEqual(2500);
      expect($scope.averageKgPerHour).toEqual(1426.05);
      expect($scope.totalSummary).toEqual(13500);
      expect($scope.totalMinutes).toEqual(568);
      expect($scope.averageExportPercent).toEqual(48);
      expect($scope.totalChecked).toEqual(6000);
      expect($scope.totalRejected).toEqual(1300);
      expect($scope.averageChecked).toEqual(44);
      expect($scope.averageRejected).toEqual(9);
      expect($scope.pickerCount).toEqual(2);
      expect($scope.averageInne).toEqual(1250);
      expect($scope.averageKraj).toEqual(2250);
      expect($scope.averageExport).toEqual(3250);
      expect($scope.averageSummary).toEqual(6750);
      expect($scope.groups[0].name).toEqual('Grupa A');
      expect($scope.groups[1].name).toEqual('Grupa 2');
      expect($scope.groups[2].name).toEqual('Grupa B');
      expect($scope.groups[3].name).toEqual('Grupa D');
      expect($scope.groups[0].id).toEqual(1);
      expect($scope.groups[1].id).toEqual(2);
      expect($scope.groups[2].id).toEqual(3);
      expect($scope.groups[3].id).toEqual(5);
    });

  });
});
*/
