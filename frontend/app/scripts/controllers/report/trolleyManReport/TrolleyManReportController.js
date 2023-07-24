'use strict';

angular.module('frontendApp')
  .controller('TrolleyManReportCtrl', ['$scope', 'growl', 'TrolleyManService', function ($scope, growl, TrolleyManService) {

    $scope.loading = 0;
    $scope.startDate = moment().subtract(1, 'month');
    $scope.endDate = moment();
    $scope.totalReclassified = 0;

    function compareTypes(a, b) {
      if (a.name > b.name) {
        return 1;
      }
      if (a.name < b.name) {
        return -1;
      }
      return 0;
    }

    $scope.getUniqueTypes = function (report) {
      var tmp = [];
      report.forEach(function (trolleyMan) {
        trolleyMan.deliverables.forEach(function (item) {
          var type = {};
          type.id = item.typeId;
          type.name = item.typeName;
          type.weight = item.typeWeight;
          tmp[type.id] = type;
        });
      });
      var result = [];
      tmp.forEach(function (item) {
        result.push(item);
      })
      return result.sort(compareTypes);
    };

    $scope.getUniqueTrolleyMen = function (report) {
      var tmp = [];
      report.forEach(function (item) {
        var trolleyMan = {};
        trolleyMan.id = item.trolleyManDto.id;
        trolleyMan.name = item.trolleyManDto.name;
        trolleyMan.surname = item.trolleyManDto.surname;
        tmp[trolleyMan.id] = trolleyMan;
      })
      var result = [];
      tmp.forEach(function (item) {
        result.push(item);
      })
      return result;
    }

    function findTrolleyManTypeResult(trolleyManId, typeId, report) {
      var result = 0;
      report.forEach(function (item) {
        if ((item.trolleyManDto.id === trolleyManId)) {
          item.deliverables.forEach(function (deliverable) {
            if (deliverable.typeId === typeId) {
              result += deliverable.totalWeight;
            }
          })
        }
      })
      return result;
    }

    $scope.getReport = function (trolleyMen, types, report) {
      var result = [];
      trolleyMen.forEach(function (item) {
        var trolleyMan = {};
        trolleyMan.id = item.id;
        trolleyMan.name = item.name;
        trolleyMan.surname = item.surname;
        trolleyMan.typesWeights = [];
        trolleyMan.reclassified = $scope.findTrolleyManTotalReclassified(report,trolleyMan.id);
        types.forEach(function (type) {
          trolleyMan.typesWeights.push(findTrolleyManTypeResult(trolleyMan.id, type.id, report));
        })
        result.push(trolleyMan);
      })
      return result;
    }

    $scope.addTrolleyManTotals = function (report) {
      report.forEach(function (trolleyMan) {
        trolleyMan.totalWeight = 0;
        trolleyMan.typesWeights.forEach(function (typeWeight) {
          trolleyMan.totalWeight += typeWeight;
        })
      })
      return report;
    }

    $scope.findTypeTotals = function (report, uniqueTypes) {
      uniqueTypes.forEach(function (type) {
        type.totalWeight = 0;
        report.forEach(function(trolleyMan){
          trolleyMan.deliverables.forEach(function(item){
            if(item.typeId === type.id){
              type.totalWeight += item.totalWeight;
            }
          })

        })
      })
      return uniqueTypes;
    }

    $scope.findTrolleyManTotalReclassified = function(report,trolleyManId){
      var result = 0;
      report.forEach(function(trolleyMan){
        if(trolleyMan.trolleyManDto.id === trolleyManId) {
          trolleyMan.deliverables.forEach(function (item) {
            result += item.reclassified;
          })
        }
      })
      $scope.totalReclassified += result;
      return result;
    }

    $scope.findTotalWeight = function(report){
      var result = 0;
      report.forEach(function(trolleyMan){
        trolleyMan.deliverables.forEach(function(item){
          result += item.totalWeight;
        })
      })
      return result;
    }

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

    $scope.export = function () {
      $scope.export_action = 'excel';
      exportAction();
    };


    $scope.init = function () {
      $scope.loading = 1;
      TrolleyManService.getTrolleyManReport(moment($scope.startDate).format("YYYY-MM-DD"), moment($scope.endDate).format("YYYY-MM-DD"), function (report) {
        var uniqueTypes = $scope.getUniqueTypes(report)
        var uniqueTrolleyMen = $scope.getUniqueTrolleyMen(report);
        var trolleyMen = $scope.getReport(uniqueTrolleyMen, uniqueTypes, report);
        $scope.trolleyMen = $scope.addTrolleyManTotals(trolleyMen);
        $scope.uniqueTypes = $scope.findTypeTotals(report, uniqueTypes);
        $scope.totalWeight = $scope.findTotalWeight(report);
        $scope.loading = 0;
      })
    }
    $scope.init();
  }]);
