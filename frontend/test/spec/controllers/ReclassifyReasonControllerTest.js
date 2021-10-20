'use strict';

describe('reclassifyReasonCtrl', function () {
  beforeEach(module('frontendApp'));

  var $controller;
  var rootScope;

  beforeEach(inject(function ($rootScope, _$controller_) {
    $controller = _$controller_;
    rootScope = $rootScope;
  }));

  describe('Add new items to the reason list', function () {

    it('should return the same list', function () {
      var $scope = rootScope.$new();
      var controller = $controller('reclassifyReasonCtrl', {$scope: $scope});
      var reason1 = {};
      var reason2 = {};
      reason1.id = 1;
      reason1.description = 'jeden';
      reason1.id = 2;
      reason1.description = 'dwa';

      $scope.reasons = [];
      $scope.reasons.push(reason1);
      $scope.reasons.push(reason2);

      var updatedReasons = [];
      updatedReasons.push(reason1);
      updatedReasons.push(reason2)

      var result = $scope.refreshReasonList(updatedReasons)
      expect(result).toEqual($scope.reasons);
    });

    it('should add element to the list', function () {
      var $scope = rootScope.$new();
      var controller = $controller('reclassifyReasonCtrl', {$scope: $scope});
      var reason1 = {};
      var reason2 = {};
      var reason3 = {};

      reason1.id = 1;
      reason1.description = 'jeden';
      reason2.id = 2;
      reason2.description = 'dwa';
      reason3.id = 3;
      reason3.description = 'trzy';

      $scope.reasons = [];
      $scope.reasons.push(reason1);
      $scope.reasons.push(reason2);

      var updatedReasons = [];
      updatedReasons.push(reason1);
      updatedReasons.push(reason2);
      updatedReasons.push(reason3);

      var result = $scope.refreshReasonList(updatedReasons)
      expect(result.length).toEqual(3);
    });


    it('should remove element to the list', function () {
      var $scope = rootScope.$new();
      var controller = $controller('reclassifyReasonCtrl', {$scope: $scope});
      var reason1 = {};
      var reason2 = {};
      var reason3 = {};

      reason1.id = 1;
      reason1.description = 'jeden';
      reason2.id = 2;
      reason2.description = 'dwa';
      reason3.id = 3;
      reason3.description = 'trzy';

      $scope.reasons = [];
      $scope.reasons.push(reason1);
      $scope.reasons.push(reason2);
      $scope.reasons.push(reason3);

      var updatedReasons = [];
      updatedReasons.push(reason1);
      updatedReasons.push(reason2);


      var result = $scope.refreshReasonList(updatedReasons)
      expect(result.length).toEqual(2);
    });


  });

});
