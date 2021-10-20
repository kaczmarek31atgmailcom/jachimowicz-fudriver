"use strict";

angular.module("frontendApp")
  .controller("CycleDetailsCtrl", ["$scope", "growl", "$routeParams", "CycleService", function ($scope, growl, $routeParams, CycleService) {

    $scope.cycleId = $routeParams.cycleId;
    $scope.firstPeriod = [];
    $scope.secondPeriod = [];
    $scope.thirdPeriod = [];

    var init = function () {
      CycleService.getSingleCycleDetails($scope.cycleId, function (days) {
        $scope.days = days;
        $scope.firstPeriod = getPeriod(days, "FIRST_PERIOD");
        $scope.secondPeriod = getPeriod(days, "SECOND_PERIOD");
        $scope.thirdPeriod = getPeriod(days, "THIRD_PERIOD");
        $scope.groupNames = getUniqTypeGroups($scope.firstPeriod, $scope.secondPeriod, $scope.thirdPeriod);
        $scope.groupNames = getTypeGroupTotals($scope.groupNames, $scope.firstPeriod);
        $scope.groupNames = getTypeGroupTotals($scope.groupNames, $scope.secondPeriod);
        $scope.groupNames = getTypeGroupTotals($scope.groupNames, $scope.thirdPeriod);
      });

      CycleService.getSingleCycleHeader($scope.cycleId, function (header) {
        $scope.header = header;
      })
    };
    init();

    function getPeriod(days, periodName) {
      for (var i = 0; i < days.length; i++) {
        if (days[i].periodName === periodName) {
          var result = angular.copy(days[i]);
          if (result.cycleDates !== undefined) result.cycleDates.sort(compareDays);
          if (result.typeGroups !== undefined) result.typeGroups.sort(compareGroups);
          return result;
        }
      }
    }

    $scope.getTypeGroupAmount = function (groupId, day, typeGroups) {
      if (typeGroups !== undefined) {
        for (var i = 0; i < typeGroups.length; i++) {
          if ((typeGroups[i].date === day) && (typeGroups[i].groupId === groupId)) {
            return typeGroups[i].totalG;
          }
        }
      }
      return 0;
    };

    $scope.getTypeGroupPeriodSummary = function (groupId, typeGroups) {
      var result = 0;
      typeGroups.forEach(function (item) {
        if (item.groupId === groupId) {
          result += item.totalG;
        }
      });
      return result;
    };

    function getTypeGroupTotals(groupNames, period) {
      if (groupNames) {
        groupNames.forEach(function (groupName) {
            if (period) {
              if (period.hasOwnProperty("typeGroups")) {
                period.typeGroups.forEach(function (typeGroup) {
                  if (typeGroup.groupId === groupName.groupId) {
                    groupName.total += typeGroup.totalG;
                  }
                })
              }
            }
          }
        )
      }
      return groupNames;
    }


    function getUniqTypeGroups(a, b, c) {
      var typeGroups = [];
      if ((a !== undefined) && (a.typeGroups !== undefined)) typeGroups = typeGroups.concat(a.typeGroups);
      if ((b !== undefined) && (b.typeGroups !== undefined)) typeGroups = typeGroups.concat(b.typeGroups);
      if ((c !== undefined) && (c.typeGroups !== undefined)) typeGroups = typeGroups.concat(c.typeGroups);
      var tmp = [];
      typeGroups.forEach(function (item) {
        var type = {};
        type.groupId = item.groupId;
        type.groupName = item.groupName;
        type.total = 0;
        tmp[item.groupId] = type;
      });
      var result = [];
      tmp.forEach(function (item) {
        if (item) {
          result.push(item);
        }
      });
      result.sort(compareGroups);
      return result;
    }


    function compareDays(d1, d2) {
      if (moment(d1.date).isBefore(moment(d2.date))) {
        return 1;
      }
      if (moment(d1.date).isAfter(moment(d2.date))) {
        return -1;
      }
      return 0;
    }

    function compareGroups(g1, g2) {
      if (g1.groupId > g2.groupId) {
        return 1;
      }
      if (g1.groupId < g2.groupId) {
        return -1;
      }
      return 0;
    }

  }])
;
