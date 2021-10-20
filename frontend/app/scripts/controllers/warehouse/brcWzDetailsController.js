'use strict';

angular.module('frontendApp')
  .controller('brcWzDetailsCtrl',['$scope','$filter','brcReportService',function($scope,$filter,brcReportService){

var supplierId = '';


    $scope.init = function(){
      var startDate = brcReportService.getStartDate();
      var endDate = brcReportService.getEndDate();
      var cycleId = brcReportService.getCycleId();
      var typeId = brcReportService.getTypeId();
      var headerId = brcReportService.getHeaderId();
      brcReportService.getWzDetails($filter('date')(startDate,'yyyyMMdd'),$filter('date')(endDate,'yyyyMMdd'),typeId,cycleId,headerId,function(details){
        $scope.details = details;
      });
      brcReportService.getSupplierId(function(data){
        supplierId = data;
      })

    };


    $scope.findBarcode = function(uniqId, pickerId){
      return addHeadingZeroes(3,supplierId) + addHeadingZeroes(5,uniqId) + addHeadingZeroes(5,pickerId);
    };

    var addHeadingZeroes = function(requiredLength, input){
      var inputString = input.toString();
      while(inputString.length < requiredLength){
        inputString = '0' + inputString;
      }
      return inputString;
    }


  }]);
