'use strict';

angular.module('frontendApp')
  .controller('TypesByPickersCtrl', ['$scope', 'growl', '$filter', '$location','ReportsService',
    function ($scope, growl, $filter, $location, ReportsService) {

    $scope.loading = 0;
    if (ReportsService.showPcs) {
      $scope.showPcs = ReportsService.showPcs;
    }
    else {
      $scope.showPcs = true;
    }

    $scope.updateDates = function () {
      $scope.loading = 1;
      ReportsService.getTypesByPickers(moment($scope.startDate).format('YYYY-MM-DD'), moment($scope.endDate).format('YYYY-MM-DD'), function (harvest) {
        $scope.harvest = harvest;
        if ($scope.showPcs === true) {
          $scope.createPcsTable(harvest);
        } else {
          $scope.createWeightTable(harvest);
        }
        $scope.loading = 0;
      });
    };

    $scope.onSwitchChange = function () {
      if ($scope.showPcs === true) {
        $scope.createPcsTable($scope.harvest);
      } else {
        $scope.createWeightTable($scope.harvest);
      }
    };

    $scope.createPcsTable = function (harvest) {
      $scope.weightTable = [];
      $scope.headerWeightTable = [];
      $scope.footerWeightTable = [];
      var uniqueTypes = $scope.getUniqueTypes(harvest);
      $scope.headerWeightTable.push('');
      $scope.footerWeightTable.push($filter('translate')('report.types-by-pickers.total'));

      uniqueTypes.forEach(function (type) {
        $scope.headerWeightTable.push(type.name + ' ' + type.weight);
        $scope.footerWeightTable.push($filter('dotToComma')($scope.getTypeTotalPcs(harvest, type)));
      });

      $scope.headerWeightTable.push($filter('translate')('report.types-by-pickers.quality'));
      $scope.headerWeightTable.push($filter('translate')('report.types-by-pickers.total'));
      $scope.footerWeightTable.push($filter('dotToComma')($scope.getTotalQuality(harvest)) + '%');
      $scope.footerWeightTable.push($scope.getTotalPcs(harvest, uniqueTypes));
      var uniquePickers = $scope.getUniquePickers(harvest);
      uniquePickers.forEach(function (picker) {
        var row = [];
        row.picker = picker;
        row.harvest = [];
        uniqueTypes.forEach(function (type) {
          row.harvest.push($scope.getPickerTypePcs(harvest, picker.id, type));
        });
        row.harvest.push($filter('dotToComma')($scope.getPickerQuality(harvest, picker.id)) + '%');
        row.harvest.push($scope.getPickerTotalPcs(harvest, picker.id, uniqueTypes));
        $scope.weightTable.push(row);
      });

    };


    $scope.createWeightTable = function (harvest) {
      $scope.weightTable = [];
      $scope.headerWeightTable = [];
      $scope.footerWeightTable = [];
      var uniqueTypes = $scope.getUniqueTypes(harvest);
      $scope.headerWeightTable.push('');
      $scope.footerWeightTable.push($filter('translate')('report.types-by-pickers.total'));

      uniqueTypes.forEach(function (type) {
        $scope.headerWeightTable.push(type.name + ' ' + type.weight);
        $scope.footerWeightTable.push($filter('dotToComma')($scope.getTypeTotalWeight(harvest, type.id)));
      });

      $scope.headerWeightTable.push($filter('translate')('report.types-by-pickers.quality'));
      $scope.headerWeightTable.push($filter('translate')('report.types-by-pickers.total'));
      $scope.footerWeightTable.push($filter('dotToComma')($scope.getTotalQuality(harvest)) + '%');
      $scope.footerWeightTable.push($filter('dotToComma')($scope.getTotalWeight(harvest)));
      var uniquePickers = $scope.getUniquePickers(harvest);
      uniquePickers.forEach(function (picker) {
        var row = [];
        row.picker = picker;
        row.harvest = [];
        uniqueTypes.forEach(function (type) {
          row.harvest.push($filter('dotToComma')($scope.getPickerTypeWeight(harvest, picker.id, type.id)));
        });
        row.harvest.push($filter('dotToComma')($scope.getPickerQuality(harvest, picker.id)) + '%');
        row.harvest.push($filter('dotToComma')($scope.getPickerTotalWeight(harvest, picker.id)));
        $scope.weightTable.push(row);
      });
    };

    $scope.showPicker = function (picker) {
      ReportsService.startDate = $scope.startDate;
      ReportsService.endDate = $scope.endDate;
      ReportsService.harvest = $scope.harvest;
      ReportsService.picker = picker;
      ReportsService.showPcs = $scope.showPcs;
      $location.url("/report/typesByPicker/" + picker.id);
    };

    $scope.getPickerTypeWeight = function (harvest, pickerId, typeId) {
      var totalWeight = 0;
      harvest.forEach(function (item) {
        if ((item.pickerId === pickerId) && (item.typeId === typeId)) {
          totalWeight += item.totalWeight;
        }
      });
      return totalWeight / 1000;
    };

    $scope.getUniquePickers = function (harvest) {
      var tmp = [];
      harvest.forEach(function (item) {
        var picker = {};
        picker.id = item.pickerId;
        picker.nr = item.pickerNr;
        picker.name = item.pickerName;
        picker.surname = item.pickerSurname;
        tmp[item.pickerId] = picker;
      });
      var result = tmp.filter(onlyUnique);
      return result.sort(sortPickers);
    };


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
      return result.sort(sortTypes);
    };

    $scope.getPickerTotalWeight = function (harvest, pickerId) {
      var totalWeight = 0;
      harvest.forEach(function (item) {
        if (item.pickerId === pickerId) {
          totalWeight += item.totalWeight;
        }
      });
      return totalWeight / 1000;
    };

    $scope.getPickerQuality = function (harvest, pickerId) {
      var totalWeight = 0;
      var totalExport = 0;
      harvest.forEach(function (item) {
        if (item.pickerId === pickerId) {
          totalWeight += item.totalWeight;
          if (item.export === 2) {
            totalExport += item.totalWeight;
          }
        }
      });
      if (totalWeight !== 0) {
        return Math.round((totalExport * 10000) / totalWeight) / 100;
      }
      return 0;
    };

    $scope.getTotalQuality = function (harvest) {
      var totalWeight = 0;
      var totalExport = 0;
      harvest.forEach(function (item) {
        totalWeight += item.totalWeight;
        if (item.export === 2) {
          totalExport += item.totalWeight;
        }
      });
      if (totalWeight !== 0) {
        return Math.round((totalExport * 10000) / totalWeight) / 100;
      }
      return 0;
    };


    $scope.getTypeTotalWeight = function (harvest, typeId) {
      var totalWeight = 0;
      harvest.forEach(function (item) {
        if (item.typeId === typeId) {
          totalWeight += item.totalWeight;
        }
      });
      return totalWeight / 1000;
    };

    $scope.getTotalWeight = function (harvest) {
      var totalWeight = 0;
      harvest.forEach(function (item) {
        totalWeight += item.totalWeight;
      });
      return totalWeight / 1000;
    };


    function sortPickers(a, b) {
      if (a.id > b.id) {
        return 1;
      }
      if (a.id < b.id) {
        return -1;
      }
      return 0;
    }

    function sortTypes(a, b) {
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

    $scope.getTypeTotalPcs = function (harvest, type) {
      var totalWeight = 0;
      harvest.forEach(function (item) {
        if (item.typeId === type.id) {
          totalWeight += item.totalWeight;
        }
      });
      if (type.weightG !== 0) {
        return Math.round(totalWeight / type.weightG);
      }
      return 0;
    };


    $scope.getTotalPcs = function (harvest, types) {
      var totalPcs = 0;
      harvest.forEach(function (item) {
        totalPcs += Math.round(item.totalWeight / findType(item.typeId, types).weightG);
      });
      return totalPcs;
    };

    function findType(id, types) {
      for (var i = 0; i < types.length; i++) {
        if (types[i].id === id) {
          return types[i];
        }
      }
    }

    $scope.getPickerTypePcs = function (harvest, pickerId, type) {
      var totalWeight = 0;
      harvest.forEach(function (item) {
        if ((item.typeId === type.id) && (item.pickerId === pickerId)) {
          totalWeight += item.totalWeight;
        }
      });
      if (type.weightG !== 0) {
        return Math.round(totalWeight / type.weightG);
      }
      return 0;
    };

    $scope.getPickerTotalPcs = function (harvest, pickerId, types) {
      var totalPcs = 0;

      harvest.forEach(function (item) {
        if (item.pickerId === pickerId) {
          var typeWeightG = findType(item.typeId, types).weightG;
          if (typeWeightG !== 0) {
            totalPcs += (item.totalWeight / typeWeightG);
          }
        }
      });
      return Math.round(totalPcs);
    };

    $scope.getClass = function (key, length) {
      if ((key + 2) > length) {
        return 'text-right font-bold';
      }
      return 'text-right'
    };

    function onlyUnique(value, index, self) {
      return self.indexOf(value) === index;
    }


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
      if (ReportsService.startDate !== null) {
        $scope.startDate = ReportsService.startDate
      }
      else {
        $scope.startDate = moment().startOf('month').format('YYYY-MM-DD');
      }
      if (ReportsService.endDate !== null) {
        $scope.endDate = ReportsService.endDate;
      }
      else {
        $scope.endDate = moment();
      }
      if (ReportsService.harvest !== null) {
        $scope.harvest = ReportsService.harvest;
        if ($scope.showPcs === true) {
          $scope.createPcsTable($scope.harvest);
        } else {
          $scope.createWeightTable($scope.harvest);
        }
        $scope.loading = 0;
      } else {
        ReportsService.getTypesByPickers(moment($scope.startDate).format('YYYY-MM-DD'), moment($scope.endDate).format('YYYY-MM-DD'), function (harvest) {
          $scope.harvest = harvest;
          if ($scope.showPcs === true) {
            $scope.createPcsTable($scope.harvest);
          } else {
            $scope.createWeightTable($scope.harvest);
          }
          $scope.loading = 0;
        })
      }
    }

    init();


  }]);
