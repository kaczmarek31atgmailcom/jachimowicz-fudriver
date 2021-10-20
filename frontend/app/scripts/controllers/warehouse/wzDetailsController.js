'use strict';

angular.module('frontendApp').controller('wzDetailsCtrl', ['$scope', '$route', 'wzService', function ($scope, $route, wzService) {
  $scope.details = [];
  $scope.palettes = [];
  $scope.totalWeight = 0;
  $scope.totalAmount = 0;
  $scope.showPrices= false;


  $scope.init = function () {
    wzService.getDetails($route.current.params.wzId, function (details) {
      $scope.details = details;
      $scope.showPrices = checkIfShowPrices(details);
      findTotals();
    });
    wzService.getPalettes($route.current.params.wzId, function (palettes) {
      $scope.palettes = palettes;
    });

    $scope.header = wzService.getHeader();
    if ($scope.header == undefined) {
      wzService.getHeader($route.current.params.wzId, function (header) {
        $scope.header = header;
      })
    }
  };

  var checkIfShowPrices = function (details) {
    var result = false;
    for (var i = 0; i < details.length; i++) {
      if ((details[i].price != undefined) && (details[i].price > 0)){
        result = true;
        break;
      }
    }
    return result;
  };

  var findTotals = function () {
    $scope.details.forEach(function (item) {
      $scope.totalWeight += item.totalWeight;
      $scope.totalAmount += item.totalAmount;
    })
  }

}]);
