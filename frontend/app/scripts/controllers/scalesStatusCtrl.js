'use strict';

angular.module('frontendApp')
  .controller('scalesStatusCtrl',['$scope','growl','scalesStatusService', function ($scope,growl,scalesStatusService){

    $scope.statusInfo = {};
    $scope.chumbers = [];

    $scope.getStatus = function () {
      scalesStatusService.getStatus(function(status){
        $scope.statusInfo = status;
      });
      $scope.$on('scalesStatusLoaded',function(){
        getChumbers();
      });
    };

    var getChumbers = function () {
      var chumbers = [];
      for (var key in $scope.statusInfo) {
        chumbers.push({
          halaId: $scope.statusInfo[key].halaId,
          halaName: $scope.statusInfo[key].halaName
        });
      }
      $scope.chumbers = getUniqChumbers(chumbers);
      //$scope.chumbers.sort(compareChambersById);
    };

    $scope.getTotalAmountInChumber = function(halaId){
      var total = 0;
      for (var key in $scope.statusInfo){
        if($scope.statusInfo[key].halaId === halaId){
          total += $scope.statusInfo[key].amount;
        }
      }
      return total;
    };

    $scope.getTotalWeightInChumber = function(halaId){
      var total = 0;
      $scope.statusInfo.forEach(function(item){
        if(item.halaId === halaId){
          total += (item.amount * parseInt(item.typeWeight * 1000))/1000;
        }
      });
      return total;
    };

    $scope.getTotalAmount = function(){
      var total = 0;
      for (var key in $scope.statusInfo){
        total += $scope.statusInfo[key].amount;
      }
      return total;
    };

    function getUniqChumbers(chumbers) {
      var result = [];
      var exists = false;
      for (var key in chumbers) {
        for (var resultKey in result) {
          if (chumbers[key].halaId === result[resultKey].halaId) {
            exists = true;
          }
        }
        if (exists === false) {
          result.push({
            halaId: chumbers[key].halaId,
            halaName: chumbers[key].halaName
          });
        }
        exists = false;
      }
      return result;
    }

    $scope.getSummaryWeight = function(typeWeight, amount){
      return (amount * parseInt(typeWeight * 1000))/1000;
    };

    $scope.$on('error',function(){
      growl.error('error',{ttl:5000})
    })
  }]);


