'use strict';
angular.module('frontendApp').controller('warehouseStatusCtrl', ['$scope', '$filter', 'warehouseStatusService',
  function ($scope, $filter, warehouseStatusService) {


    $scope.stock = [];
    $scope.depotTotal = [];
    $scope.rodzajTotal = [];
    $scope.total = 0;
    $scope.depots = [];


    $scope.init = function () {
      warehouseStatusService.getTypes(function (types) {
        $scope.types = types;
      });

      warehouseStatusService.getDepots(function (depots) {

        var trolley = {};
        trolley.id = 0;
        trolley.name = $filter('translate')('warehouse.status.trolley');
        $scope.depots[0] = trolley;
        depots.forEach(function (depot) {
          $scope.depots.push(depot);
        })
      });


      warehouseStatusService.getStock(function (stock) {
        warehouseStatusService.getTotalTrolleys(function (trolleys) {
          $scope.stock = addTrolleysToStock(stock, trolleys);
          countTotals(stock);
        });
      });
    };

    $scope.findStockValue = function (rodzajId, depotId) {
      var stockValue = 0;
      $scope.stock.forEach(function (item) {
        if (item.rodzajId == rodzajId && item.depotId == depotId) {
          stockValue = item.amount;
        }
      });
      return stockValue;
    };

    $scope.findStockPcs = function (typeId, depotId) {
      var result;
      $scope.stock.forEach(function (item) {
        if (item.rodzajId === typeId && item.depotId === depotId) {
          result = item.pcs;
        }
      });
      return result;
    };


    var addTrolleysToStock = function (stock, trolleys) {
      trolleys.forEach(function (trolley) {
        var stockItem = {};
        stockItem.depotId = 0;
        stockItem.rodzajId = trolley.typeId;
        stockItem.pcs = trolley.amount;
        stockItem.amount = (trolley.amount * parseInt(trolley.typeWeight * 1000)) / 1000;
        stock.push(stockItem);
      });
      return stock;
    };

    var countTotals = function (stock) {
      var total = 0;
      stock.forEach(function (item) {
        $scope.depotTotal[item.depotId] = 0;
        $scope.rodzajTotal[item.rodzajId] = 0;
      });

      stock.forEach(function (item) {
        $scope.depotTotal[item.depotId] += parseInt(item.amount * 1000);
        $scope.rodzajTotal[item.rodzajId] += parseInt(item.amount * 1000);
        total += parseInt(item.amount * 1000);
      });

      for (var i in $scope.depotTotal) {
        $scope.depotTotal[i] /= 1000;
      }

      for (var j in $scope.rodzajTotal) {
        $scope.rodzajTotal[j] /= 1000;
      }
      $scope.total = total / 1000;
    };
  }]);
