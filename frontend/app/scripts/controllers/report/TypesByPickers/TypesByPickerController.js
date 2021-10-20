'use strict';

angular.module('frontendApp')
  .controller('TypesByPickerCtrl', ['$scope', '$location', 'growl', '$filter', '$routeParams', 'ReportsService',
    function ($scope, $location, growl, $filter, $routeParams, ReportsService) {

      $scope.loading = 0;

      function onlyUnique(value, index, self) {
        return self.indexOf(value) === index;
      }

      $scope.updateDates = function () {
        ReportsService.getTypesByPicker($scope.pickerId,
          moment($scope.pickerStartDate).format("YYYY-MM-DD"),
          moment($scope.pickerEndDate).format("YYYY-MM-DD"),
          function (pickerHarvest) {
          $scope.pickerHarvest = pickerHarvest;
            $scope.loading = 1;
            $scope.pickerHarvest = pickerHarvest;
            if ($scope.showPcs === true) {
              getPcsTable(pickerHarvest);
            } else {
              getWeightTable(pickerHarvest);
            }
            $scope.loading = 0;
          });
      };

      function getPcsTable(pickerHarvest){
        $scope.weightTable = [];
        $scope.headerWeightTable = [];
        $scope.footerWeightTable = [];
        var uniqueTypes = $scope.getUniqueTypes(pickerHarvest);
        $scope.headerWeightTable.push('');
        $scope.footerWeightTable.push($filter('translate')('report.types-by-picker.total'));

        uniqueTypes.forEach(function (type) {
          $scope.headerWeightTable.push(type.name + ' ' + type.weight);
          $scope.footerWeightTable.push($scope.getTypeTotalPcs(pickerHarvest, type));
        });

        $scope.headerWeightTable.push($filter('translate')('report.types-by-picker.quality'));
        $scope.headerWeightTable.push($filter('translate')('report.types-by-picker.total'));
        $scope.footerWeightTable.push($filter('dotToComma')($scope.getTotalQuality(pickerHarvest)) + '%');
        $scope.footerWeightTable.push($scope.getTotalPcs(pickerHarvest,uniqueTypes));
        var days = $scope.getUniqueDates(pickerHarvest);
        days.forEach(function (day) {
          var row = [];
          row.push(day);
          uniqueTypes.forEach(function (type) {
            row.push($scope.getDayTypePcs(pickerHarvest, type, day));
          });
          row.push($filter('dotToComma')($scope.getDayQuality(pickerHarvest, day)) + '%');
          row.push($scope.getDayTotalPcs(pickerHarvest, uniqueTypes,day));
          $scope.weightTable.push(row);
        });

      }

      function getWeightTable(pickerHarvest) {
        $scope.weightTable = [];
        $scope.headerWeightTable = [];
        $scope.footerWeightTable = [];
        var uniqueTypes = $scope.getUniqueTypes(pickerHarvest);
        $scope.headerWeightTable.push('');
        $scope.footerWeightTable.push($filter('translate')('report.types-by-picker.total'));

        uniqueTypes.forEach(function (type) {
          $scope.headerWeightTable.push(type.name + ' ' + type.weight);
          $scope.footerWeightTable.push($filter('dotToComma')($scope.getTypeTotalWeight(pickerHarvest, type.id)));
        });

        $scope.headerWeightTable.push($filter('translate')('report.types-by-picker.quality'));
        $scope.headerWeightTable.push($filter('translate')('report.types-by-picker.total'));
        $scope.footerWeightTable.push($filter('dotToComma')($scope.getTotalQuality(pickerHarvest)) + '%');
        $scope.footerWeightTable.push($filter('dotToComma')($scope.getTotalWeight(pickerHarvest)));
        var days = $scope.getUniqueDates(pickerHarvest);
        days.forEach(function (day) {
          var row = [];
          row.push(day);
          uniqueTypes.forEach(function (type) {
            row.push($filter('dotToComma')($scope.getDayTypeWeight(pickerHarvest, type.id, day)));
          });
          row.push($filter('dotToComma')($scope.getDayQuality(pickerHarvest, day)) + '%');
          row.push($filter('dotToComma')($scope.getDayTotalWeight(pickerHarvest, day)));
          $scope.weightTable.push(row);
        });
      }

      $scope.getDayTypeWeight = function (harvest, typeId, day) {
        var totalWeight = 0;
        harvest.forEach(function (item) {
          if ((item.typeId === typeId) && (item.date === day)) {
            totalWeight += item.totalWeight;
          }
        });
        return Math.round(totalWeight / 10) / 100;
      };

      $scope.getDayQuality = function (harvest, day) {
        var totalWeight = 0;
        var totalExport = 0;
        harvest.forEach(function (item) {
          if (item.date === day) {
            if (item.export === 2) {
              totalExport += item.totalWeight;
            }
            totalWeight += item.totalWeight;
          }
        });
        if (totalWeight !== 0) {
          return Math.round((totalExport * 10000) / totalWeight) / 100;
        }
        return 0;
      };

      $scope.getDayTotalWeight = function (harvest, day) {
        var totalWeight = 0;
        harvest.forEach(function (item) {
          if (item.date === day) {
            totalWeight += item.totalWeight;
          }
        });
        return Math.round(totalWeight / 10) / 100;
      };

      $scope.getTypeTotalWeight = function (harvest, typeId) {
        var totalWeight = 0;
        harvest.forEach(function (item) {
          if (item.typeId === typeId) {
            totalWeight += item.totalWeight;
          }
        });
        return Math.round(totalWeight / 10) / 100;
      };

      $scope.getTotalWeight = function (harvest) {
        var totalWeight = 0;
        harvest.forEach(function (item) {
          totalWeight += item.totalWeight;
        });
        return Math.round(totalWeight / 10) / 100;
      };

      $scope.getUniqueDates = function (harvest) {
        var tmp = [];
        harvest.forEach(function (item) {
          tmp.push(item.date);
        });
        var result = tmp.filter(onlyUnique);
        return result.sort(compareDates);
      };

      function compareDates(a, b) {
        if (moment(a).isAfter(moment(b))) {
          return 1;
        }
        if (moment(a).isBefore(moment(b))) {
          return -1;
        }
        return 0;
      }

      $scope.getUniqueTypes = function (harvest) {
        var tmp = [];
        harvest.forEach(function (item) {
          var type = {};
          type.id = item.typeId;
          type.name = item.typeName;
          type.export = item.export;
          type.weightG = item.typeWeight;
          type.weight = item.typeWeight / 1000;
          tmp[item.typeId] = type;
        });
        var result = tmp.filter(onlyUnique);
        return result.sort(compareTypes);
      };

      $scope.getTotalQuality = function (harvest) {
        var totalWeight = 0;
        var totalExport = 0;
        harvest.forEach(function (item) {
          if (item.export === 2) {
            totalExport += item.totalWeight;
          }
          totalWeight += item.totalWeight;
        });
        if (totalWeight !== 0) {
          return Math.round((totalExport * 10000) / totalWeight) / 100;
        }
        return 0;
      };


      function compareTypes(a, b) {
        if (a.export > b.export) {
          return 1;
        }
        if (a.export < b.export) {
          return -1;
        }
        if (a.export === b.export) {
          if (a.id > b.id) {
            return 1;
          }
          if (a.id < b.id) {
            return -1;
          }
        }
        return 0;
      }

      $scope.getClass = function (key, length) {
        if ((key + 2) > length) {
          return 'text-right font-bold';
        }
        return 'text-right'
      };


      $scope.updateDates = function(){
        $scope.loading = 1;
        ReportsService.getTypesByPicker($scope.pickerId,
          moment($scope.pickerStartDate).format("YYYY-MM-DD"),
          moment($scope.pickerEndDate).format("YYYY-MM-DD"),
          function (pickerHarvest) {
            $scope.pickerHarvest = pickerHarvest;
            if ($scope.showPcs === true) {
              getPcsTable($scope.pickerHarvest);
            } else {
              getWeightTable($scope.pickerHarvest);
            }
            $scope.loading = 0;
          })
      };

      $scope.getTypeTotalPcs = function(pickerHarvest, type){
        var totalWeight = 0;
        pickerHarvest.forEach(function(item){
          if(item.typeId === type.id){
            totalWeight += item.totalWeight;
          }
        });
        if(type.weightG !== 0){
          return Math.round(totalWeight/type.weightG);
        }
        return 0;
      };


      $scope.getDayTypePcs = function(harvest, type, day){
        var totalWeight = 0;
        harvest.forEach(function(item){
          if((item.typeId === type.id) &&(item.date === day)){
            totalWeight += item.totalWeight;
          }
        });
        if(type.totalWeight !== 0){
          return Math.round(totalWeight/type.weightG);
        }
      };

      $scope.getTotalPcs = function(harvest,types){
        var totalPcs = 0;
        harvest.forEach(function(item){
          totalPcs += (item.totalWeight / findType(item.typeId,types).weightG);
        });
        return Math.round(totalPcs);
      };

      $scope.getDayTotalPcs = function (harvest, types,day){
        var totalPcs = 0;
        harvest.forEach(function(item){
          if(item.date === day){
            totalPcs += (item.totalWeight / findType(item.typeId,types).weightG);
          }
        });
        return Math.round(totalPcs);
      };

      function findType(typeId,types) {
        if (types !== null) {
          for (var i = 0; i < types.length; i++){
            if(types[i].id === typeId){
              return types[i];
            }
          }
        }
      }


      $scope.onSwitchChange = function () {
        if ($scope.showPcs === true) {
          getPcsTable($scope.pickerHarvest);
        } else {
          getWeightTable($scope.pickerHarvest);
        }
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


      function init() {
        $scope.loading = 1;
        if (ReportsService.showPcs !== null) {
          $scope.showPcs = ReportsService.showPcs;
        }
        else {
          $scope.showPcs = true;
        }

        if (ReportsService.startDate !== null) {
          $scope.pickerStartDate = ReportsService.startDate
        }
        else {
          $scope.pickerStartDate = moment().startOf('month').format('YYYY-MM-DD');
        }
        if (ReportsService.endDate !== null) {
          $scope.pickerEndDate = ReportsService.endDate;
        }
        else {
          $scope.pickerEndDate = moment();
        }
        $scope.pickerId = $routeParams.pickerId;
        if (($scope.pickerId !== null) && (ReportsService.picker !== null) && (Number($scope.pickerId) !== Number(ReportsService.picker.id))) {
          $location.url("/report/typesByPickers")
        }
        $scope.picker = ReportsService.picker;
        if($scope.picker === null){
          ReportsService.getPicker($scope.pickerId,function(picker){
            $scope.picker = picker;
          })
        }
        ReportsService.getTypesByPicker($scope.pickerId,
          moment($scope.pickerStartDate).format("YYYY-MM-DD"),
          moment($scope.pickerEndDate).format("YYYY-MM-DD"),
          function (pickerHarvest) {
          $scope.pickerHarvest = pickerHarvest;
            $scope.loading = 1;
            getWeightTable(pickerHarvest);
            if ($scope.showPcs === true) {
              getPcsTable(pickerHarvest);
            } else {
              getWeightTable(pickerHarvest);
            }
            $scope.loading = 0;
          })
      }

      init();
    }]);
