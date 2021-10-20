"use strict";

angular.module("frontendApp")
  .controller("CycleHeadersCtrl", ["$scope", "growl", "$location", "CycleService",
    function ($scope, growl, $location, CycleService) {

      $scope.startDay = moment().subtract(2, 'MONTHS');
      $scope.endDay = moment();
      $scope.orderByField = "startDate";
      $scope.reverseSort = true;

      $scope.getCycles = function () {
        $scope.loading = 1;
        CycleService.getAllCycles(moment($scope.startDay).format("YYYYMMDD"), moment($scope.endDay).format("YYYYMMDD"), function (cycles) {
          cycles = countDailyHarvest(cycles);
          $scope.cycles = cycles;
          $scope.totalAmount = findTotalAmount(cycles);
          $scope.totalExport = findTotalExport(cycles);
          $scope.totalArea = findTotalArea(cycles);
          $scope.totalWeight = findTotalWeight(cycles);
          $scope.totalDays = findTotalDays(cycles);
          $scope.totalDryWeight = findTotalDryWeight(cycles);
          $scope.avgTotalAmount = (cycles.length > 0) ? parseInt($scope.totalAmount / cycles.length): 0;
          $scope.avgQuality = ($scope.totalAmount > 0) ? parseInt(($scope.totalExport / $scope.totalAmount) * 10000) : 0;
          $scope.avgAmountPerMeter = ($scope.totalArea > 0) ? parseInt($scope.totalAmount / $scope.totalArea) : 0;
          $scope.avgAmountPerTon = ($scope.totalWeight > 0) ? parseInt($scope.totalAmount / $scope.totalWeight) : 0;
          $scope.avgAmountPerDryTon = ($scope.totalDryWeight > 0 ) ? parseInt(($scope.totalAmount / $scope.totalDryWeight * 100)) / 100 : 0;
          $scope.avgHumidity = ($scope.totalWeight > 0) ? parseInt(100 - (($scope.totalDryWeight * 100) / $scope.totalWeight)) : 0;
          $scope.totalAvgDaily = ($scope.totalDays > 0 ) ? parseInt((($scope.totalAmount /1000) / $scope.totalDays) * 100) / 100 : 0;
          $scope.avgArea = (cycles.length > 0) ? parseInt(($scope.totalArea / cycles.length) * 100) / 100 : 0;
          $scope.avgWeight = (cycles.length > 0) ? parseInt($scope.totalWeight / cycles.length) : 0;
          $scope.loading = 0;
        })
      };
      $scope.getCycles();


      var countDailyHarvest = function (cycles) {
        cycles.forEach(function (cycle) {
          if (cycle.activeDaysAmount > 0) {
            cycle.avgDailyHarvest = parseInt(((cycle.total / 1000) / cycle.activeDaysAmount) * 100 ) / 100;
          } else {
            cycle.avgDailyHarvest = 0;
          }
        });
        return cycles;
      };

      var findTotalDays = function (cycles) {
        var totalNumberOfDays = 0;
        cycles.forEach(function (cycle) {
          totalNumberOfDays += cycle.activeDaysAmount;
        });
        return totalNumberOfDays;
      };

      var findTotalAmount = function (cycles) {
        var totalAmount = 0;
        cycles.forEach(function (cycle) {
          totalAmount += cycle.total;
        });
        return totalAmount;
      };

      var findTotalExport = function (cycles) {
        var totalExport = 0;
        cycles.forEach(function (cycle) {
          totalExport += cycle.export;
        });
        return totalExport;
      };

      var findTotalArea = function (cycles) {
        var totalArea = 0;
        cycles.forEach(function (cycle) {
          totalArea += cycle.area;
        });
        return totalArea;
      };

      var findTotalWeight = function (cycles) {
        var totalWeight = 0;
        cycles.forEach(function (cycle) {
          totalWeight += cycle.weight;
        });
        return totalWeight / 1000;
      };

      var findTotalDryWeight = function (cycles) {
        var totalDryWeight = 0;
        cycles.forEach(function (cycle) {
          totalDryWeight += (cycle.weight - (cycle.weight * (cycle.humidity / 100)));
        });
        return totalDryWeight / 1000;
      };

      $scope.exportAction = function () {
        switch ($scope.export_action) {
          case 'pdf':
            $scope.$broadcast('export-pdf', {});
            break;
          case 'excel':
            $scope.$broadcast('export-excel', {});
            break;
          case 'doc':
            $scope.$broadcast('export-doc', {});
            break;
          default:
            console.log('no event caught');
        }
      };

      $scope.export = function () {
        $scope.export_action = 'excel';
        $scope.exportAction();
      };

      $scope.getClassByResultCompare = function (detail, total) {
        var result = 'text-info';
        if (detail > total) {
          result = 'text-success';
        }
        if (detail < total) {
          result = 'text-danger';
        }
        return result;
      };

      $scope.getClassByResultCompareRev = function (detail, total) {
        var result = 'text-info';
        if (detail < total) {
          result = 'text-success';
        }
        if (detail > total) {
          result = 'text-danger';
        }
        return result;
      };


      $scope.showCycleDetails = function (cycleId) {
        $location.url("/cycle-details/" + cycleId);
      };

    }]);
