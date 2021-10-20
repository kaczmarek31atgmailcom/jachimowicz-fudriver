'use strict';

describe('ProductionOrderControllerSpec', function () {
  beforeEach(module('frontendApp'));

  var $controller;
  var rootScope;

  beforeEach(inject(function ($rootScope, _$controller_) {
    $controller = _$controller_;
    rootScope = $rootScope;
  }));


  it('powinien zwrócić dni pomiędzy datami', function () {
    var $scope = rootScope.$new();
    $controller('ProductionOrderCtrl', {$scope: $scope});
    var startDate = '2018-01-01';
    var endDate = '2018-01-05';
    var tested = $scope.getDaysBetween(startDate,endDate);
    expect(tested.length).toEqual(5);
    expect(tested[0]).toEqual('2018-01-01');
    expect(tested[1]).toEqual('2018-01-02');
    expect(tested[2]).toEqual('2018-01-03');
    expect(tested[3]).toEqual('2018-01-04');
    expect(tested[4]).toEqual('2018-01-05');
  });

  it('powinien znaleźć zamówienie które istnieje', function () {
    var $scope = rootScope.$new();
    $controller('ProductionOrderCtrl', {$scope: $scope});
    var orders = [{"warehouseOrderId":18,"creationDate":"2020-04-14","dueDate":"2020-04-14","warehouseTypeId":264,"warehouseTypeName":"Ansław Pieczarka Flat 7/9 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":432,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":19,"creationDate":"2020-04-14","dueDate":"2020-04-15","warehouseTypeId":264,"warehouseTypeName":"Ansław Pieczarka Flat 7/9 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":423,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":20,"creationDate":"2020-04-14","dueDate":"2020-04-16","warehouseTypeId":264,"warehouseTypeName":"Ansław Pieczarka Flat 7/9 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":423,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":21,"creationDate":"2020-04-14","dueDate":"2020-04-17","warehouseTypeId":264,"warehouseTypeName":"Ansław Pieczarka Flat 7/9 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":423,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":23,"creationDate":"2020-04-14","dueDate":"2020-04-14","warehouseTypeId":263,"warehouseTypeName":"Ansław Pieczarka Flat 8/10 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":44,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":24,"creationDate":"2020-04-14","dueDate":"2020-04-15","warehouseTypeId":263,"warehouseTypeName":"Ansław Pieczarka Flat 8/10 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":44,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":25,"creationDate":"2020-04-14","dueDate":"2020-04-16","warehouseTypeId":263,"warehouseTypeName":"Ansław Pieczarka Flat 8/10 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":123,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":26,"creationDate":"2020-04-14","dueDate":"2020-04-17","warehouseTypeId":263,"warehouseTypeName":"Ansław Pieczarka Flat 8/10 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":55,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":28,"creationDate":"2020-04-14","dueDate":"2020-04-14","warehouseTypeId":191,"warehouseTypeName":"Asda AS Pieczarka 2/4,5 4*250g T 014","warehouseTypeWeight":1000,"localTypeId":373,"localTypeName":"2/4,5 ASDA 4*250 T014","localTypeWeight":1000,"dueAmount":678,"deliveredAmount":20,"description":null},
      {"warehouseOrderId":29,"creationDate":"2020-04-14","dueDate":"2020-04-15","warehouseTypeId":191,"warehouseTypeName":"Asda AS Pieczarka 2/4,5 4*250g T 014","warehouseTypeWeight":1000,"localTypeId":373,"localTypeName":"2/4,5 ASDA 4*250 T014","localTypeWeight":1000,"dueAmount":111,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":30,"creationDate":"2020-04-14","dueDate":"2020-04-16","warehouseTypeId":191,"warehouseTypeName":"Asda AS Pieczarka 2/4,5 4*250g T 014","warehouseTypeWeight":1000,"localTypeId":373,"localTypeName":"2/4,5 ASDA 4*250 T014","localTypeWeight":1000,"dueAmount":777,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":31,"creationDate":"2020-04-14","dueDate":"2020-04-17","warehouseTypeId":191,"warehouseTypeName":"Asda AS Pieczarka 2/4,5 4*250g T 014","warehouseTypeWeight":1000,"localTypeId":373,"localTypeName":"2/4,5 ASDA 4*250 T014","localTypeWeight":1000,"dueAmount":1000,"deliveredAmount":0,"description":null}
      ]
    var tested = $scope.findOrder('2020-04-14',191,orders);
    expect(tested).toEqual(678);
  });

  it('powinien zwrocic 0/0 jeśli zamowienie nie istnieje', function () {
    var $scope = rootScope.$new();
    $controller('ProductionOrderCtrl', {$scope: $scope});
    var orders = [{"warehouseOrderId":18,"creationDate":"2020-04-14","dueDate":"2020-04-14","warehouseTypeId":264,"warehouseTypeName":"Ansław Pieczarka Flat 7/9 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":432,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":19,"creationDate":"2020-04-14","dueDate":"2020-04-15","warehouseTypeId":264,"warehouseTypeName":"Ansław Pieczarka Flat 7/9 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":423,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":20,"creationDate":"2020-04-14","dueDate":"2020-04-16","warehouseTypeId":264,"warehouseTypeName":"Ansław Pieczarka Flat 7/9 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":423,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":21,"creationDate":"2020-04-14","dueDate":"2020-04-17","warehouseTypeId":264,"warehouseTypeName":"Ansław Pieczarka Flat 7/9 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":423,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":23,"creationDate":"2020-04-14","dueDate":"2020-04-14","warehouseTypeId":263,"warehouseTypeName":"Ansław Pieczarka Flat 8/10 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":44,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":24,"creationDate":"2020-04-14","dueDate":"2020-04-15","warehouseTypeId":263,"warehouseTypeName":"Ansław Pieczarka Flat 8/10 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":44,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":25,"creationDate":"2020-04-14","dueDate":"2020-04-16","warehouseTypeId":263,"warehouseTypeName":"Ansław Pieczarka Flat 8/10 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":123,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":26,"creationDate":"2020-04-14","dueDate":"2020-04-17","warehouseTypeId":263,"warehouseTypeName":"Ansław Pieczarka Flat 8/10 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":55,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":28,"creationDate":"2020-04-14","dueDate":"2020-04-14","warehouseTypeId":191,"warehouseTypeName":"Asda AS Pieczarka 2/4,5 4*250g T 014","warehouseTypeWeight":1000,"localTypeId":373,"localTypeName":"2/4,5 ASDA 4*250 T014","localTypeWeight":1000,"dueAmount":678,"deliveredAmount":20,"description":null},
      {"warehouseOrderId":29,"creationDate":"2020-04-14","dueDate":"2020-04-15","warehouseTypeId":191,"warehouseTypeName":"Asda AS Pieczarka 2/4,5 4*250g T 014","warehouseTypeWeight":1000,"localTypeId":373,"localTypeName":"2/4,5 ASDA 4*250 T014","localTypeWeight":1000,"dueAmount":111,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":30,"creationDate":"2020-04-14","dueDate":"2020-04-16","warehouseTypeId":191,"warehouseTypeName":"Asda AS Pieczarka 2/4,5 4*250g T 014","warehouseTypeWeight":1000,"localTypeId":373,"localTypeName":"2/4,5 ASDA 4*250 T014","localTypeWeight":1000,"dueAmount":777,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":31,"creationDate":"2020-04-14","dueDate":"2020-04-17","warehouseTypeId":191,"warehouseTypeName":"Asda AS Pieczarka 2/4,5 4*250g T 014","warehouseTypeWeight":1000,"localTypeId":373,"localTypeName":"2/4,5 ASDA 4*250 T014","localTypeWeight":1000,"dueAmount":1000,"deliveredAmount":0,"description":null}
    ]
    var tested = $scope.findOrder('2020-04-14',19,orders);
    expect(tested).toEqual('0');
  });


  it('powinien zwrocic listę rodzai grzybow', function () {
    var $scope = rootScope.$new();
    $controller('ProductionOrderCtrl', {$scope: $scope});
    var orders = [{"warehouseOrderId":18,"creationDate":"2020-04-14","dueDate":"2020-04-14","warehouseTypeId":264,"warehouseTypeName":"Ansław Pieczarka Flat 7/9 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":432,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":19,"creationDate":"2020-04-14","dueDate":"2020-04-15","warehouseTypeId":264,"warehouseTypeName":"Ansław Pieczarka Flat 7/9 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":423,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":20,"creationDate":"2020-04-14","dueDate":"2020-04-16","warehouseTypeId":264,"warehouseTypeName":"Ansław Pieczarka Flat 7/9 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":423,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":21,"creationDate":"2020-04-14","dueDate":"2020-04-17","warehouseTypeId":264,"warehouseTypeName":"Ansław Pieczarka Flat 7/9 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":423,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":23,"creationDate":"2020-04-14","dueDate":"2020-04-14","warehouseTypeId":263,"warehouseTypeName":"Ansław Pieczarka Flat 8/10 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":44,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":24,"creationDate":"2020-04-14","dueDate":"2020-04-15","warehouseTypeId":263,"warehouseTypeName":"Ansław Pieczarka Flat 8/10 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":44,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":25,"creationDate":"2020-04-14","dueDate":"2020-04-16","warehouseTypeId":263,"warehouseTypeName":"Ansław Pieczarka Flat 8/10 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":123,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":26,"creationDate":"2020-04-14","dueDate":"2020-04-17","warehouseTypeId":263,"warehouseTypeName":"Ansław Pieczarka Flat 8/10 1,81 kg /czarna/","warehouseTypeWeight":1810,"localTypeId":0,"localTypeName":null,"localTypeWeight":0,"dueAmount":55,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":28,"creationDate":"2020-04-14","dueDate":"2020-04-14","warehouseTypeId":191,"warehouseTypeName":"Asda AS Pieczarka 2/4,5 4*250g T 014","warehouseTypeWeight":1000,"localTypeId":373,"localTypeName":"2/4,5 ASDA 4*250 T014","localTypeWeight":1000,"dueAmount":678,"deliveredAmount":20,"description":null},
      {"warehouseOrderId":29,"creationDate":"2020-04-14","dueDate":"2020-04-15","warehouseTypeId":191,"warehouseTypeName":"Asda AS Pieczarka 2/4,5 4*250g T 014","warehouseTypeWeight":1000,"localTypeId":373,"localTypeName":"2/4,5 ASDA 4*250 T014","localTypeWeight":1000,"dueAmount":111,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":30,"creationDate":"2020-04-14","dueDate":"2020-04-16","warehouseTypeId":191,"warehouseTypeName":"Asda AS Pieczarka 2/4,5 4*250g T 014","warehouseTypeWeight":1000,"localTypeId":373,"localTypeName":"2/4,5 ASDA 4*250 T014","localTypeWeight":1000,"dueAmount":777,"deliveredAmount":0,"description":null},
      {"warehouseOrderId":31,"creationDate":"2020-04-14","dueDate":"2020-04-17","warehouseTypeId":191,"warehouseTypeName":"Asda AS Pieczarka 2/4,5 4*250g T 014","warehouseTypeWeight":1000,"localTypeId":373,"localTypeName":"2/4,5 ASDA 4*250 T014","localTypeWeight":1000,"dueAmount":1000,"deliveredAmount":0,"description":null}
    ]
    var tested = $scope.getTypes(orders);
    expect(tested.length).toEqual(3);
    expect(tested[0].warehouseTypeId).toEqual(264);
    expect(tested[1].warehouseTypeId).toEqual(263);
    expect(tested[2].warehouseTypeId).toEqual(191);
  });


});
