"use strict";

angular.module("frontendApp")
  .controller("SizeByCycleCtrl", ["$scope", "growl", "TypeService", function ($scope, growl, TypeService) {

    $scope.endDate = moment();
    $scope.startDate = moment().subtract(2, 'months');
    $scope.loading = 1;

    $scope.init = function () {
      $scope.loading = 1;
      TypeService.getTypeSizesByCycle(moment($scope.startDate).format('YYYY-MM-DD'), moment($scope.endDate).format('YYYY-MM-DD'), function (report) {
        $scope.cycles = getCycles(report);
        $scope.sizes = getSizes(report);
        $scope.cyclesForExcel = getCyclesForExecel($scope.cycles);
        $scope.cycleTotals = getCycleTotals($scope.cycles, report);
        $scope.printTotals = getPrintableTotals($scope.cycleTotals);
        $scope.data = getData($scope.sizes, $scope.cycles, report);

        $scope.loading = 0;
      })
    };
    $scope.init();


    function getData(sizes, cycles, report) {
      var result = [];
      sizes.forEach(function (size) {
        size.data = [];
            cycles.forEach(function (cycle) {
              var amount = getAmount(size.id, cycle.id, report);
              var totalCycle = getCycleTotal(cycle.id);
              size.data.push((amount / 1000) + 'kg');
              size.data.push((Math.round((amount / totalCycle) * 10000) / 100) + '%');
          });
          result.push(size);
      });
      return result;
    }

    function getCycleTotal(cycleId) {
      for (var i = 0; i < $scope.cycleTotals.length; i++) {
        if ($scope.cycleTotals[i].id === cycleId) {
          return $scope.cycleTotals[i].total;
        }
      }
      return 0;
    }

    function getCycleTotals(cycles, report) {
      cycles.forEach(function (cycle) {
          cycle.total = 0;
          report.forEach(function (item) {
            if (cycle.id === item.cycleId) {
              cycle.total += item.amount;
            }
          })

      });
      return cycles;
    }


    function getAmount(sizeId, cycleId, report) {
      for (var i = 0; i < report.length; i++) {
        if ((report[i].sizeId === sizeId) && (report[i].cycleId === cycleId)) {
          return report[i].amount;
        }
      }
      return 0;
    }

    function getCycles(report) {
      var tmp = [];
      report.forEach(function (item) {
        var cycle = {};
        cycle.id = item.cycleId;
        cycle.name = item.cycleName;
        cycle.startDate = item.cycleStartDate;
        tmp[cycle.id] = cycle;
      });
      var cycles = Array.from(new Set(tmp));
      cycles = removeNulls(cycles);
      cycles.sort(compareCycles);
      return cycles;
    }

    function getSizes(report) {
      var tmp = [];
      report.forEach(function (item) {
        var size = {};
        size.id = item.sizeId;
        size.name = item.sizeName;
        tmp[size.id] = size;
      });
      var sizes = Array.from(new Set(tmp));
      sizes = removeNulls(sizes);
      sizes.sort(compareSizes);
      return sizes;
    }

    function getPrintableTotals(totals){
      var output = [];
      var totalTotal = 0;
      totals.forEach(function(total){
        totalTotal += total.total;
      });
      totals.forEach(function(total){
        output.push(total.total / 1000);
        output.push("");
      });
      return output;
    }

    function removeNulls(input){
      var output = [];
      input.forEach(function(item){
        if((item !== undefined) && (item !== null)){
          output.push(item);
        }
      });
    return output;
    }

    function getCyclesForExecel(cycles) {
      var output = [];
      cycles.forEach(function(item){
        output.push(item.name);
        output.push(item.startDate);
      });
      return output;
    }

    function compareCycles(a, b) {
      if (a.startDate < b.startDate) {
        return 1
      }
      if (a.startDate > b.startDate) {
        return -1
      }
      return 0;
    }

    function compareSizes(a, b) {
      if (a.name > b.name) {
        return 1;
      }
      if (a.name < b.name) {
        return -1;
      }
      return 0;
    }

    $scope.export = function () {
      $scope.export_action = 'excel';
      exportAction();
    };

    var exportAction = function () {
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

    $scope.$on('error',function(){
      growl.error("error",{ttl:5000});
    });

  }]);
