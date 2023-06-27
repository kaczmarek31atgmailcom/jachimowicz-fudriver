'use strict';

angular.module('frontendApp')
  .controller('LeaderReportCtrl', ['$scope', '$rootScope', '$http','growl', 'leaderReportService',
    function ($scope, $rootScope, $http, growl, leaderReportService) {

    $scope.startDay = moment().subtract(3, 'days');
    $scope.endDay = moment();
    $scope.loading = 0;
    $scope.report = [];
    $scope.totalKraj = 0;
    $scope.totalExport = 0;
    $scope.totalInne = 0;
    $scope.totalSummary = 0;
    $scope.totalMinutes = 0;
    $scope.averageExportPercent = 0;
    $scope.averageKgPerHour = 0;
    $scope.totalChecked = 0;
    $scope.averageTested = 0;
    $scope.totalRejected = 0;
    $scope.averageRejected = 0;
    $scope.pickerCount = 0;
    $scope.groups = [];
    $scope.sortReverse = true;

    var temporaryGroups = [];


    $scope.getReport = function () {
      if(moment($scope.endDay).isBefore($scope.startDay,'day')){
        growl.error("leader_report.end-date.before.start-date",{ttl:5000});
      } else {
        $scope.loading = 1;
        leaderReportService.getReport(moment($scope.startDay).format("YYYY-MM-DD"), moment($scope.endDay).format("YYYY-MM-DD"), function (report) {
          $scope.report = [];
          $scope.totalKraj = 0;
          $scope.totalExport = 0;
          $scope.totalInne = 0;
          $scope.totalSummary = 0;
          $scope.totalMinutes = 0;
          $scope.averageExportPercent = 0;
          $scope.averageKgPerHour = 0;
          $scope.totalChecked = 0;
          $scope.averageTested = 0;
          $scope.totalRejected = 0;
          $scope.averageRejected = 0;
          $scope.report = report;
          countAdditionalValues();
          rewriteGroupsIndexes();
          $scope.loading = 0;
        });
      }
    };

    var countAdditionalValues = function () {
      $scope.pickerCount = 0;
      if ($scope.report !== undefined) {
        $scope.report.forEach(function (item) {
          item.summary = item.kraj + item.export + item.inne;
          $scope.totalKraj += parseInt(item.kraj);
          $scope.totalExport += parseInt(item.export);
          $scope.totalInne += parseInt(item.inne);
          $scope.totalSummary += item.summary;
          $scope.totalMinutes += item.minutes;
          $scope.totalChecked += item.checked;
          $scope.totalRejected += item.rejected;
          $scope.pickerCount++;
          if (item.minutes > 0 && item.summary > 0) {
            item.kgPerHour = ((parseInt(((item.summary/1000) / (item.minutes / 60)) * 100)) / 100)
          } else {
            item.kgPerHour = 0;
          }
          item.exportPercent = getPercent(item.summary, item.export);
          item.checkedPercent = getPercent(item.summary, item.checked);
          item.rejectedPercent = getPercent(item.summary, item.rejected);
          extractGroupNames(item.totalByGroups);
        });
        if ($scope.totalMinutes > 0 && $scope.totalSummary > 0) {
          $scope.averageKgPerHour = parseInt(((($scope.totalSummary/1000) / ($scope.totalMinutes / 60))/$scope.pickerCount) * 100) / 100;
        } else {
          $scope.averageKgPerHour = 0;
        }
        $scope.averageMinutes = $scope.totalMinutes/$scope.pickerCount;
        $scope.averageInne = parseInt(($scope.totalInne / $scope.pickerCount) * 100) / 100;
        $scope.averageKraj = parseInt(($scope.totalKraj / $scope.pickerCount) * 100) / 100;
        $scope.averageExport = parseInt(($scope.totalExport / $scope.pickerCount) * 100) / 100;
        $scope.averageSummary = parseInt(($scope.totalSummary / $scope.pickerCount) * 100) / 100;
        $scope.averageExportPercent = getPercent($scope.totalSummary, $scope.totalExport);
        $scope.averageChecked = getPercent($scope.totalSummary, $scope.totalChecked);
        $scope.averageRejected = getPercent($scope.totalSummary, $scope.totalRejected);
      }
    };

    var extractGroupNames = function (pickerGroups) {
      if((pickerGroups !== undefined) && (pickerGroups !== null)) {
        pickerGroups.forEach(function (group) {
          temporaryGroups[group.groupId] = {};
          temporaryGroups[group.groupId].id = group.groupId;
          temporaryGroups[group.groupId].name = group.groupName;
        });
      }
    };

    var rewriteGroupsIndexes = function () {
      $scope.groups = [];
      temporaryGroups.forEach(function (group) {
        $scope.groups.push(group);
      });
    };


    $scope.getGroupOutcome = function (pickerId, groupId) {
      var result = 0;
      for (var i = 0; i < $scope.report.length; i++) {
        if ($scope.report[i].id === pickerId) {
          if(($scope.report[i].totalByGroups !== undefined) && ($scope.report[i].totalByGroups !== null)) {
            for (var j = 0; j < $scope.report[i].totalByGroups.length; j++) {
              if ($scope.report[i].totalByGroups[j].groupId === groupId) {
                return $scope.report[i].totalByGroups[j].groupTotal;
              }
            }
          }
         return 0;
        }
      }
      return 0;
    };

    $scope.getGroupTotal = function(groupId){
      var result = 0;
      $scope.report.forEach(function(item){
        if((item.totalByGroups !== undefined) && (item.totalByGroups !== null)) {
          item.totalByGroups.forEach(function (group) {
            if (group.groupId === groupId) {
              result += group.groupTotal;
            }
          })
        }
      });
    return result;
    };

    var getPercent = function (all, part) {
      if (all === undefined || part === undefined || all <= 0 || part <= 0) {
        return 0;
      } else {
        return (parseInt((part / all) * 10000))/100;
      }
    };

    var init = function () {
      $scope.getReport();
    };
    init();

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

  }]);

