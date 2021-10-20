'use strict';

angular.module('frontendApp')
  .controller('ReclassifyReportCtrl', ['$scope', 'ReclassifyReportService','settingsPromiseService', function ($scope, ReclassifyReportService,settingsPromiseService) {

    $scope.startDate = moment().subtract(1, 'days');
    $scope.endDate = moment();

    settingsPromiseService.then(function (settingsService) {
      $scope.eastMode = settingsService.eastMushroomsMode;
    });


    $scope.init = function () {
      $scope.getReclassifications();
    };

    $scope.getReclassifications = function () {
      ReclassifyReportService.getLocalReclassifications(moment($scope.startDate).startOf('day').format("YYYYMMDDHHmmss"), moment($scope.endDate).endOf('day').format("YYYYMMDDHHmmss"), function (reclassifications) {
        $scope.reclassifications = reclassifications;
      });
    };

    $scope.getBarcode = function (supplierId,uniqId, pickerId) {
      return addLeadingZeroes(3, supplierId) + addLeadingZeroes(5, uniqId) + addLeadingZeroes(5, pickerId);
    };

    $scope.getBarcodeEastMushrooms = function (supplierId,uniqId, pickerNr) {
      console.log(addLeadingZeroes(5,uniqId));
      return '002' +  addLeadingZeroes(5, uniqId) + addLeadingZeroes(2,pickerNr) + addLeadingZeroes(3, supplierId);
    };


    var addLeadingZeroes = function (length, item) {
      var result = item.toString();
      for (var i = result.length; i < length; i++) {
        result = '0' + result;
      }
      return result;
    };


  }]);
