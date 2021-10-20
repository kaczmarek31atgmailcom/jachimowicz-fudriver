'use strict';

angular.module('frontendApp')
  .controller('ScannerManController', ['$scope', 'growl', 'ZarobkiService', function ($scope, growl, ZarobkiService) {

    $scope.startDate = moment().subtract(1, 'months');
    $scope.endDate = moment();

    $scope.getReport = function () {
      $scope.typeTotals = [];
      $scope.totalInne = 0;
      $scope.totalKraj = 0;
      $scope.totalExport = 0;
      $scope.totalInnePcs = 0;
      $scope.totalKrajPcs = 0;
      $scope.totalExportPcs = 0;
      $scope.totalTotalPcs = 0;
      $scope.totalTotal = 0;
      $scope.totalQuality = 0;
      $scope.loading = 1;
      ZarobkiService.getScannerManReport(moment($scope.startDate).format("YYYY-MM-DD"), moment($scope.endDate).format("YYYY-MM-DD"), function (report) {
        $scope.typeTotals = countTypeTotals(report);
        countTotals(report);
        $scope.report = getQuality(report);
        $scope.report = getPersonSum(report);
        $scope.loading = 0;
      })
    };
    $scope.getReport();


    function getPersonSum(report){
      report.forEach(function(person){
        person.innePcs = 0;
        person.krajPcs = 0;
        person.exportPcs = 0;
        person.totalKg = 0;
        person.totalKg += person.inne;
        person.totalKg += person.kraj;
        person.totalKg += person.export;
        if(person.hasOwnProperty('details')){
          person.details.forEach(function(type){
            switch (type.exportType) {
              case 'EXPORT':
                person.exportPcs += type.numberPcs;
                break;
              case 'KRAJ':
                person.krajPcs += type.numberPcs;
                break;
              case 'INNE':
                person.innePcs += type.numberPcs;
                break;
            }
          })
        }
      });
return report;
    }

    function getQuality(report) {
      report.forEach(function(item){
        if (!(item.kraj + item.export) > 0) {
          item.quality = 0;
        } else {
          item.quality = Math.round(((item.export * 100) / (item.kraj + item.export)));
        }
      });
      return report;
    }

    function countTypeTotals(report) {
      var totals = [];
      if (report.length > 0) {
        report[0].details.forEach(function (item) {
          var type = {};
          type.exportType = item.exportType;
          type.id = item.typeId;
          type.totalWeight = 0;
          type.totalPcs = 0;
          totals.push(type);
        });
      }
      totals.forEach(function (total) {
        report.forEach(function (report) {
          report.details.forEach(function (detail) {
            if (detail.typeId === total.id) {

              total.totalWeight += detail.totalWeight;
              total.totalPcs += detail.numberPcs;
            }
          })
        })
      });
      return totals;
    }

    function countTotals(report) {
      report.forEach(function (item) {
        $scope.totalInne += item.inne;
        $scope.totalKraj += item.kraj;
        $scope.totalExport += item.export;
        $scope.totalTotal += item.inne;
        $scope.totalTotal += item.kraj;
        $scope.totalTotal += item.export;
        $scope.totalTotalPcs += item.totalPcs;
          if (item.hasOwnProperty('details')) {
            item.details.forEach(function (type) {
              switch (type.exportType) {
                case 'EXPORT':
                  $scope.totalExportPcs += type.numberPcs;
                  break;
                case 'KRAJ':
                  $scope.totalKrajPcs += type.numberPcs;
                  break;
                case 'INNE':
                  $scope.totalInnePcs += type.numberPcs;
                  break;
              }
            })
          }
      });
      $scope.totalQuality = Math.round((($scope.totalExport * 100) / ($scope.totalKraj + $scope.totalExport)));
    }

    $scope.getClass = function (exportType) {
      switch (exportType) {
        case 'INNE':
          return 'text-right text-danger';
          break;
        case 'KRAJ':
          return 'text-right text-success';
          break;
        default:
          return 'text-right text-primary';
      }
    };

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
