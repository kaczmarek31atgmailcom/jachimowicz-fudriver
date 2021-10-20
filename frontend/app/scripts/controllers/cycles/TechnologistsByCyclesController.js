"use strict";

angular.module("frontendApp")
  .controller("TechnologistsByCyclesCtrl", ["$scope", "growl", "CycleService", function ($scope, growl, CycleService) {

    $scope.startDate = moment().subtract(1, 'months').toDate();
    $scope.endDate = moment().toDate();

    $scope.getReport = function () {
      $scope.loading = 1;
      CycleService.getTechnologistsHeadersByCycles(moment($scope.startDate).format("YYYYMMDD"), moment($scope.endDate).format("YYYYMMDD"), function (header) {
        $scope.header = header;
        var days = extractDays(header.details);
        days = addTechnologistsToDays(days, $scope.header);
        days = countDaysTotals(days);
        days = countDayAverages(days);
        $scope.days = roundTechnologistDaysTotals(days);
        $scope.loading = 0;
      });
    };

    function extractDays(details) {
      var days = [];
      details.forEach(function (detail) {
        var day = [];
        day.date = detail.date;
        day.inne = 0;
        day.kraj = 0;
        day.export = 0;
        day.total = 0;
        day.quality = 0;
        day.kgT = 0;
        day.kgM = 0;
        day.totalArea = detail.totalArea;
        day.totalWeight = detail.totalWeight;
        day.technologists = [];
        if (!isDayExisting(day, days)) {
          days.push(day);
        }
      });
      return days;
    }

    function isDayExisting(day, days) {
      for (var i = 0; i < days.length; i++) {
        if ((days[i].hasOwnProperty("date")) && (days[i].date === day.date)) {
          return true;
        }
      }
      return false;
    }

    function addTechnologistsToDays(days, header) {
      if (header.hasOwnProperty("details")) {
        days.forEach(function (day) {
          header.details.forEach(function (detail) {
            if (day.date === detail.date) {
              day.technologists.push(detail);
            }
          })
        });
        return days;
      }
    }

    function countDaysTotals(days) {
      days.forEach(function (day) {
        day.technologists.forEach(function (tech) {
          day.inne += tech.totalInne;
          day.kraj += tech.totalKraj;
          day.export += tech.totalExport;
          day.total += tech.total;
        })
      });
      return days;
    }

    function countDayAverages(days){
      days.forEach(function(day){
        day.quality = (day.total > 0) ? parseInt((day.export * 10000) / day.total)/100 :0;
        day.kgT = (day.totalWeight > 0) ? parseInt((day.total * 1000)  / day.totalWeight)/1000  : 0;
        day.kgM = (day.totalArea > 0) ? parseInt(day.total  / day.totalArea) / 1000: 0;
      });
      return days;
    }

    function roundTechnologistDaysTotals(days){
      days.forEach(function(day){
        day.technologists.forEach(function(item){
          console.log(item.kgT);
          item.kgT = parseInt(item.kgT / 100) / 1000;
          item.kgM = item.kgM / 1000;
        })
      });
      return days;
    }

  }]);
