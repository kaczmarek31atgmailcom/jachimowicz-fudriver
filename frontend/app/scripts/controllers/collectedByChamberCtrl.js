'use strict';

angular.module('frontendApp')
  .controller('collectedByChamberCtrl', ['$scope', 'collectedByChamberService', function ($scope, collectedByChamberService) {

    $scope.startDate = moment().subtract(1, 'month');
    $scope.endDate = moment();
    $scope.report = [];
    $scope.report.export = [];
    $scope.report.kraj = [];
    $scope.report.inne = [];

    $scope.init = function () {
      $scope.loading = 1;
      collectedByChamberService.getHarvestByChamber(moment($scope.startDate).format("YYYYMMDD"), moment($scope.endDate).format("YYYYMMDD"), function (data) {
        $scope.exportTotals = [];
        $scope.krajTotals = [];
        $scope.innetTotals = [];
        $scope.krajTypes = getUniqTypes(data, 1);
        $scope.exportTypes = getUniqTypes(data, 2);
        $scope.inneTypes = getUniqTypes(data, 3);
        $scope.cycles = getUniqCycles(data);
        $scope.allTotal = getAllTotal(data);
        $scope.cycles.forEach(function (cycle) {
          cycle.inne = setHarvestValues(data, $scope.inneTypes, cycle.id);
          cycle.kraj = setHarvestValues(data, $scope.krajTypes, cycle.id);
          cycle.export = setHarvestValues(data, $scope.exportTypes, cycle.id);
        });
        $scope.cycles = setSummaries($scope.cycles, data);
        $scope.cycles = setTotalsForCycles($scope.cycles,data);
        $scope.allTotal = getAllTotal(data);
        $scope.exportTotals = getTypeTotals(data, $scope.exportTypes);
        $scope.krajTotals = getTypeTotals(data, $scope.krajTypes);
        $scope.inneTotals = getTypeTotals(data, $scope.inneTypes);
        $scope.exportSummary = getSummary($scope.exportTotals);
        $scope.krajSummary = getSummary($scope.krajTotals);
        $scope.inneSummary = getSummary($scope.inneTotals);
        $scope.totalExportRatio = getTotalExportRatio($scope.exportSummary, $scope.krajSummary);
        $scope.loading = 0;
      });
    };


    var getAllTotal = function(data){
      var result = 0;
      data.forEach(function(item){
        result += item.exportTotal;
        result += item.krajTotal;
        result += item.inneTotal;
      });
      return result;
    };

    var setTotalsForCycles = function(cycles, data){
      cycles.forEach(function(cycle){
        cycle.total = 0;
        for(var i = 0; i< data.length; i++){
          if(data[i].cycleId === cycle.id){
            cycle.total = data[i].exportTotal + data[i].krajTotal + data[i].inneTotal;
            break;
          }
        }
      });
      return cycles;
    };

    var getTotalExportRatio = function(totalExport, totalKraj){
      var result = 0;
      if(totalExport > 0){
        result = (totalExport / (totalExport + totalKraj)) * 100;
      }
      return result;
    };

    var getSummary = function (totalsArray) {
      var result = 0;
      totalsArray.forEach(function (item) {
        result += item;
      });
      return result;
    };

    var getTypeTotals = function (data, types) {
      var totals = [];
      types.forEach(function (type) {
        var typeTotalWeight = 0;
        data.forEach(function (cycle) {
          cycle.types.forEach(function (cycleType) {
            if (cycleType.id === type.id) {
              typeTotalWeight += cycleType.totalWeight;
            }
          })
        });
        totals.push(typeTotalWeight);
      });
      return totals;
    };

    var setSummaries = function (cycles, data) {
      cycles.forEach(function (cycle) {
        for (var i = 0; i < data.length; i++) {
          if (data[i].cycleId === cycle.id) {
            cycle.krajSummary = data[i].krajTotal;
            cycle.exportSummary = data[i].exportTotal;
            cycle.inneSummary = data[i].inneTotal;
            cycle.exportRatio = data[i].exportRatio;
            break;
          }
        }
      });
      return cycles;
    };

    var getUniqTypes = function (chamberData, exportType) {
      var typesTmp = [];
      var types = [];
      chamberData.forEach(function (chamber) {
        chamber.types.forEach(function (type) {
          if (type.exportType === exportType) {
            var uniqType = {};
            uniqType.id = type.id;
            uniqType.name = type.name;
            uniqType.weight = type.weight;
            typesTmp[type.id] = uniqType;
          }
        })
      });
      typesTmp.forEach(function (type) {
        types.push(type);
      });
      return types;
    };

    var getUniqCycles = function (data) {
      var cyclesTmp = [];
      var cycles = [];
      data.forEach(function (item) {
        var cycle = {};
        cycle.id = item.cycleId;
        cycle.name = item.chamberName;
        cycle.startDate = item.startDate;
        cyclesTmp[item.cycleId] = cycle;
      });
      cyclesTmp.forEach(function (cycle) {
        cycles.push(cycle);
      });
      return cycles;
    };

    var setHarvestValues = function (data, types, cycleId) {
      var result = [];
      types.forEach(function (type) {
        var harvest = {};
        harvest.typeId = type.id;
        harvest.typeName = type.name;
        harvest.typeWeight = type.weight;
        harvest.value = getHarvestValue(cycleId, type.id, data);
        result.push(harvest);
      });
      return result;
    };

    var getHarvestValue = function (cycleId, typeId, data) {
      var result = 0;
      for (var i = 0; i < data.length; i++) {
        if (data[i].cycleId === cycleId) {
          for (var j = 0; j < data[i].types.length; j++) {
            if (data[i].types[j].id === typeId) {
              result = data[i].types[j].totalWeight;
              break;
            }
          }
          break;
        }
      }
      return result;
    };

    $scope.export = function () {
      $scope.export_action = 'excel';
      $scope.exportAction();
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


  }]);
