'use strict';

angular.module('frontendApp')
  .controller('warehouseAgeCtrl', ['$scope', 'warehouseStatusService', function ($scope, warehouseStatusService) {

    $scope.stock = [];
    $scope.depotTotal = [];
    $scope.total = 0;
    $scope.days = [];

    $scope.init = function () {
      warehouseStatusService.getTypes(function (types) {
        $scope.types = types;
      });

      warehouseStatusService.getDepots(function (depots) {
        $scope.depots = depots;
      });

      warehouseStatusService.getAgedStock(function (stock) {
        $scope.stock = stock;
        addTypeName();
        countTotals();
        $scope.findUniqueDays($scope.stock);
        if($scope.days.length > 1) {
          $scope.days.sort($scope.dateComparator());
        }
        $scope.stockDays = getStockByDays($scope.days, $scope.stock);
        $scope.stockDays = countDayTotals($scope.stockDays);
      });
    };

    $scope.getAgeStyle = function (date) {
      var yellowDay = moment().subtract(2, 'days');
      var redDay = moment().subtract(5, 'days');
      if (moment(date).isSameOrBefore(redDay)) {
        return 'text text-danger';
      }
      if (moment(date).isSameOrBefore(yellowDay)) {
        return 'text text-warning';
      }
      return 'text text-default'
    };

    var countDayTotals = function (stockDays) {
      stockDays.forEach(function (stockByDay) {
        stockByDay.stock.forEach(function (item) {
          if (item.total === undefined) {
            item.total = item.amount;
          } else {
            item.total += item.amount;
          }
        })
      });
      return stockDays;
    };

    $scope.findStockValue = function (rodzajId, depotId, date) {
      var stockValue = 0;
      $scope.stockDays.forEach(function (stockDay) {
        stockDay.stock.forEach(function (item) {
          if (item.rodzajId == rodzajId && item.depotId == depotId && stockDay.date === date) {
            stockValue = item.amount;
          }
        })
      });
      return stockValue;
    };

    var addTypeName = function () {
      $scope.stock.forEach(function (item) {
        for (var i = 0; i < $scope.types.length; i++) {
          if ($scope.types[i].id === item.rodzajId) {
            item.typeName = $scope.types[i].name;
            item.typeWeight = $scope.types[i].weight;
          }
        }
      })
    };

    var countTotals = function () {
      $scope.stock.forEach(function (item) {
        $scope.depotTotal[item.depotId] = 0;

      });
      $scope.stock.forEach(function (item) {
        $scope.depotTotal[item.depotId] += item.amount;
        $scope.total += item.amount;
      })
    };

    $scope.findUniqueDays = function (stock) {
      var found = false;
      if (stock !== undefined && stock.length > 0) {
        stock.forEach(function (item) {
          for (var i = 0; i < $scope.days.length; i++) {
            if ($scope.days[i] === item.harvestDate) {
              found = true;
              break;
            }
          }
          if (found) {
            found = false;
          } else {
            $scope.days.push(item.harvestDate);
          }
        })
      }
    };

    $scope.dateComparator = function (date1, date2) {
      if (moment(date2).isBefore(date2)) {
        return -1;
      }
      if (moment(date1).isBefore(date2)) {
        return 1;
      }
      return 0;
    };


    var getStockByDays = function (days, stock) {
      var stockDays = [];
      days.forEach(function (day) {
        console.log(day);
        var stockDay = {};
        stockDay.date = day;
        stockDay.stock = [];
        stockDays.push(stockDay);
      });
      stockDays.forEach(function (stockDay) {
        stock.forEach(function (item) {
          if (item.harvestDate === stockDay.date) {
            stockDay.stock.push(item);
          }
        })
      });
      return stockDays;
    };


  }]);
