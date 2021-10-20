'use strict';

describe('EastMushroomsWzControllerSpec', function () {
  beforeEach(module('frontendApp'));

  var $controller;
  var rootScope;

  beforeEach(inject(function ($rootScope, _$controller_) {
    $controller = _$controller_;
    rootScope = $rootScope;
  }));


  it('should create types summary', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsWzCtrl', {$scope: $scope});
    var wz = {
      "wzId": 8,
      "wzNr": 4,
      "companyName": "Pieczarki sp.z o.o.",
      "companyCity": "11-223 Warszawa",
      "companyStreet": "Al Jerozolimskie 12",
      "companyNip": "123456",
      "companyGGN": "12354-234234",
      "customerName": "East Mushrooms",
      "date": "2018-11-27",
      "creatorLogin": "marcin",
      "creatorName": "Marcin",
      "creatorSurname": "Kaczmarek",
      "types": [{
        "localTypeId": 14,
        "localTypeName": "50mm 3kg",
        "localTypeWeight": 3,
        "remoteTypeId": 3,
        "remoteTypeName": "10-30mm 2,4kg",
        "remoteTypeWeight": 2.4,
        "warehousePaletteId": 5,
        "amount": 3
      }, {
        "localTypeId": 15,
        "localTypeName": "60+ Rizen I kl. 2kg",
        "localTypeWeight": 2,
        "remoteTypeId": 4,
        "remoteTypeName": "60+ Rizen I kl. 2kg",
        "remoteTypeWeight": 2,
        "warehousePaletteId": 5,
        "amount": 30
      }, {
          "localTypeId": 15,
          "localTypeName": "60+ Rizen I kl. 2kg",
          "localTypeWeight": 2,
          "remoteTypeId": 4,
          "remoteTypeName": "60+ Rizen I kl. 2kg",
          "remoteTypeWeight": 2,
          "warehousePaletteId": 6,
          "amount": 20
        }, {
        "localTypeId": 15,
        "localTypeName": "60+ Rizen I kl. 2kg",
        "localTypeWeight": 2,
        "remoteTypeId": 4,
        "remoteTypeName": "60+ Rizen I kl. 2kg",
        "remoteTypeWeight": 2,
        "warehousePaletteId": 7,
        "amount": 30
      }]
    };
    var tested = $scope.getTypes(wz);
    expect(tested[0].amount).toEqual(3);
    expect(tested[1].amount).toEqual(80);
  });

  it('should create palettes summary', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsWzCtrl', {$scope: $scope});
    var wz = {
      "wzId": 8,
      "wzNr": 4,
      "companyName": "Pieczarki sp.z o.o.",
      "companyCity": "11-223 Warszawa",
      "companyStreet": "Al Jerozolimskie 12",
      "companyNip": "123456",
      "companyGGN": "12354-234234",
      "customerName": "East Mushrooms",
      "date": "2018-11-27",
      "creatorLogin": "marcin",
      "creatorName": "Marcin",
      "creatorSurname": "Kaczmarek",
      "types": [{
        "localTypeId": 14,
        "localTypeName": "50mm 3kg",
        "localTypeWeight": 3,
        "remoteTypeId": 3,
        "remoteTypeName": "10-30mm 2,4kg",
        "remoteTypeWeight": 2.4,
        "warehousePaletteId": 5,
        "amount": 3
      }, {
        "localTypeId": 15,
        "localTypeName": "60+ Rizen I kl. 2kg",
        "localTypeWeight": 2,
        "remoteTypeId": 4,
        "remoteTypeName": "60+ Rizen I kl. 2kg",
        "remoteTypeWeight": 2,
        "warehousePaletteId": 5,
        "amount": 30
      }, {
        "localTypeId": 15,
        "localTypeName": "60+ Rizen I kl. 2kg",
        "localTypeWeight": 2,
        "remoteTypeId": 4,
        "remoteTypeName": "60+ Rizen I kl. 2kg",
        "remoteTypeWeight": 2,
        "warehousePaletteId": 6,
        "amount": 20
      }, {
        "localTypeId": 15,
        "localTypeName": "60+ Rizen I kl. 2kg",
        "localTypeWeight": 2,
        "remoteTypeId": 4,
        "remoteTypeName": "60+ Rizen I kl. 2kg",
        "remoteTypeWeight": 2,
        "warehousePaletteId": 7,
        "amount": 30
      }]
    };
    var tested = $scope.getPalettes(wz,17);
    expect(tested[0].amount).toEqual(33);
    expect(tested[1].amount).toEqual(20);
    expect(tested[2].amount).toEqual(30);
    expect(tested[0].types.length).toEqual(2);
  });

  it('should add leading zeros', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsWzCtrl', {$scope: $scope});
    expect($scope.addLeadingZeroes(3,17)).toEqual('017');
    expect($scope.addLeadingZeroes(10,4)).toEqual('0000000004');
    expect($scope.addLeadingZeroes(3,123)).toEqual('123');
  });

  it('should get total amount', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsWzCtrl', {$scope: $scope});
    var wz = {
      "wzId": 8,
      "wzNr": 4,
      "companyName": "Pieczarki sp.z o.o.",
      "companyCity": "11-223 Warszawa",
      "companyStreet": "Al Jerozolimskie 12",
      "companyNip": "123456",
      "companyGGN": "12354-234234",
      "customerName": "East Mushrooms",
      "date": "2018-11-27",
      "creatorLogin": "marcin",
      "creatorName": "Marcin",
      "creatorSurname": "Kaczmarek",
      "types": [{
        "localTypeId": 14,
        "localTypeName": "50mm 3kg",
        "localTypeWeight": 3,
        "remoteTypeId": 3,
        "remoteTypeName": "10-30mm 2,4kg",
        "remoteTypeWeight": 2.4,
        "warehousePaletteId": 5,
        "amount": 3
      }, {
        "localTypeId": 15,
        "localTypeName": "60+ Rizen I kl. 2kg",
        "localTypeWeight": 2,
        "remoteTypeId": 4,
        "remoteTypeName": "60+ Rizen I kl. 2kg",
        "remoteTypeWeight": 2,
        "warehousePaletteId": 5,
        "amount": 30
      }, {
        "localTypeId": 15,
        "localTypeName": "60+ Rizen I kl. 2kg",
        "localTypeWeight": 2,
        "remoteTypeId": 4,
        "remoteTypeName": "60+ Rizen I kl. 2kg",
        "remoteTypeWeight": 2,
        "warehousePaletteId": 6,
        "amount": 20
      }, {
        "localTypeId": 15,
        "localTypeName": "60+ Rizen I kl. 2kg",
        "localTypeWeight": 2,
        "remoteTypeId": 4,
        "remoteTypeName": "60+ Rizen I kl. 2kg",
        "remoteTypeWeight": 2,
        "warehousePaletteId": 7,
        "amount": 30
      }]
    };
    var tested = $scope.getTotalAmount(wz);
    expect(tested).toEqual(83);
  });


  it('should get total weight', function () {
    var $scope = rootScope.$new();
    $controller('EastMushroomsWzCtrl', {$scope: $scope});
    var wz = {
      "wzId": 8,
      "wzNr": 4,
      "companyName": "Pieczarki sp.z o.o.",
      "companyCity": "11-223 Warszawa",
      "companyStreet": "Al Jerozolimskie 12",
      "companyNip": "123456",
      "companyGGN": "12354-234234",
      "customerName": "East Mushrooms",
      "date": "2018-11-27",
      "creatorLogin": "marcin",
      "creatorName": "Marcin",
      "creatorSurname": "Kaczmarek",
      "types": [{
        "localTypeId": 14,
        "localTypeName": "50mm 3kg",
        "localTypeWeight": 3,
        "remoteTypeId": 3,
        "remoteTypeName": "10-30mm 2,4kg",
        "remoteTypeWeight": 2.4,
        "warehousePaletteId": 5,
        "amount": 3
      }, {
        "localTypeId": 15,
        "localTypeName": "60+ Rizen I kl. 2kg",
        "localTypeWeight": 2,
        "remoteTypeId": 4,
        "remoteTypeName": "60+ Rizen I kl. 2kg",
        "remoteTypeWeight": 2,
        "warehousePaletteId": 5,
        "amount": 30
      }, {
        "localTypeId": 15,
        "localTypeName": "60+ Rizen I kl. 2kg",
        "localTypeWeight": 2,
        "remoteTypeId": 4,
        "remoteTypeName": "60+ Rizen I kl. 2kg",
        "remoteTypeWeight": 2,
        "warehousePaletteId": 6,
        "amount": 20
      }, {
        "localTypeId": 15,
        "localTypeName": "60+ Rizen I kl. 2kg",
        "localTypeWeight": 2,
        "remoteTypeId": 4,
        "remoteTypeName": "60+ Rizen I kl. 2kg",
        "remoteTypeWeight": 2,
        "warehousePaletteId": 7,
        "amount": 30
      }]
    };
    var tested = $scope.getTotalWeight(wz);
    expect(tested).toEqual(169);
  });



});
