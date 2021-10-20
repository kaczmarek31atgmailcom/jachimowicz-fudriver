"use strict";

angular.module("frontendApp")
.controller("CyclesByBrigadesCtrl",["$scope","growl","CycleService",function($scope,growl,CycleService){
  $scope.startDate = moment().subtract(1,'months');
  $scope.endDate = moment();

  $scope.getReport = function(){
    $scope.loading = 1;
    CycleService.getCyclesByBrigades(moment($scope.startDate).format("YYYYMMDD"),moment($scope.endDate).format("YYYYMMDD"),function(cycles){
      var head = getHeaders(cycles);
      head = getBrigades(head,cycles);
      head = countQuality(head);
      head = countChamberTotals(head);
      $scope.headers = countChamberQuality(head);
      $scope.loading = 0;
    })
  };

  function getHeaders(cycles){
    var tmp = [];
    cycles.forEach(function(cycle){
      var header = {};
      header.cycleId = cycle.cycleId;
      header.chamberName = cycle.chamberName;
      header.startDate = moment(cycle.startDate,"YYYYMMDD").format("YYYY/MM/DD");

      header.inne = 0;
      header.kraj = 0;
      header.export = 0;
      header.total = 0;
      header.quality = 0;
      header.brigades = [];
      tmp[header.cycleId] = header;
    });
    var headers = [];
    tmp.forEach(function(item){
      headers.push(item);
    });
    headers.sort(compareHeaders);
    return headers;
  }

  function getBrigades(headers,cycles){
    headers.forEach(function(header){
      cycles.forEach(function(cycle){
        if(header.cycleId === cycle.cycleId){
          header.brigades.push(cycle);
        }
      });
    });
  return headers;
  }

  function countQuality(headers){
    headers.forEach(function(header) {
      if (header.quality > 0) {
        header.quality = parseInt((header.export * 10000) / header.total) / 100;
      } else {
        header.quality = 0;
      }
      header.brigades.forEach(function(brigade){
        if(brigade.total > 0){
          brigade.quality = parseInt((brigade.export * 10000) / brigade.total) / 100;
        } else {
          brigade.quality = 0;
        }
      });
      header.brigades.sort(compareBrigades);
    });
  return headers;
  }

  function countChamberTotals(headers){
    headers.forEach(function(header){
      header.brigades.forEach(function(brigade){
        header.inne += brigade.inne;
        header.kraj += brigade.kraj;
        header.export += brigade.export;
        header.total += brigade.total;
      })
    });
  return headers;
  }

  function countChamberQuality(headers){
    headers.forEach(function(header){
      if(header.total > 0){
        header.quality = parseInt((header.export * 10000) / header.total) /100;
      }
    });
    return headers;
  }

  function compareBrigades(a,b){
    if(a.quality > b.quality){
      return -1;
    }
    if(a.quality < b.quality){
      return 1;
    }
     else return 0;
  }

  function compareHeaders(a,b){
    if(a.cycleId > b.cycleId){
      return 1;
    }
    if(a.cycleId < b.cycleId){
      return -1;
    }
    return 0;
  }
}]);
