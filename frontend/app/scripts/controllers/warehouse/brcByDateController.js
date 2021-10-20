'use strict';

angular.module('frontendApp').
  controller('brcByDateCtrl',['$scope','$filter','$location','brcReportService',function($scope,$filter,$location,brcReportService){


  $scope.startDate = brcReportService.getStartDate();
  $scope.endDate = brcReportService.getEndDate();


    $scope.init = function(){
      brcReportService.getWarehouseHeaders($filter('date')($scope.startDate,'yyyyMMdd'), $filter('date')($scope.endDate,'yyyyMMdd'),function(headers){
        $scope.warehouseHeaders = headers;
      });
      brcReportService.getWzHeaders($filter('date')($scope.startDate,'yyyyMMdd'), $filter('date')($scope.endDate,'yyyyMMdd'),function(wzHeaders){
        $scope.wzHeaders = wzHeaders;
      });
    };

  $scope.getHeaders = function(){
    brcReportService.getWarehouseHeaders($filter('date')($scope.startDate,'yyyyMMdd'), $filter('date')($scope.endDate,'yyyyMMdd'),function(headers) {
      $scope.warehouseHeaders = headers;
    });

    brcReportService.getWzHeaders($filter('date')($scope.startDate,'yyyyMMdd'), $filter('date')($scope.endDate,'yyyyMMdd'),function(wzHeaders){
      $scope.wzHeaders = wzHeaders;
    });
  };

  $scope.stockDetails = function(header){
    brcReportService.setStartDate($scope.startDate);
    brcReportService.setEndDate($scope.endDate);
    brcReportService.setCycleId(header.cycleId);
    brcReportService.setDepotId(header.depotId);
    brcReportService.setTypeId(header.typeId);
    $location.url("/brcByDateStockDetails");
  };

  $scope.wzDetails = function(header){
    brcReportService.setStartDate($scope.startDate);
    brcReportService.setEndDate($scope.endDate);
    brcReportService.setHeaderId(header.headerId);
    brcReportService.setTypeId(header.typeId);
    brcReportService.setCycleId(header.cycleId);
    $location.url("/brcByWzDetails");
  }
}]);
