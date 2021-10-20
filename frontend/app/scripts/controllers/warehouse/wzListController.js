'use strict';

angular.module('frontendApp').
  controller('wzListCtrl',['$scope','$filter','$location','wzService',function($scope,$filter,$location,wzService){

  $scope.endDate = moment();
  $scope.startDate = moment().subtract(1,'months');
  $scope.headers = [];
  $scope.loading = 1;

  $scope.init = function(){
    $scope.loading=1;
    wzService.getHeaders(moment($scope.startDate).format('YYYYMMDD'), moment($scope.endDate).format('YYYYMMDD'),function(headers){
      $scope.headers = headers;
      $scope.loading=0;
    })
  };

  $scope.showDetails = function(header){
    wzService.setHeader(header);
    $location.url("/wzDetail/" + header.id);
  }

}]);
