'use strict';

angular.module('frontendApp').controller('reclassifyCtrl',['$scope','$location','reclassifyService',function($scope,$location,reclassifyService){


  $scope.loading = false;
  $scope.dateTo = new Date();
  var dateFrom = new Date();
  dateFrom.setDate(dateFrom.getDate() - 1);
  $scope.dateFrom = dateFrom;



  $scope.getHeaders = function(){
    reclassifyService.getHeaders($scope.dateFrom, $scope.dateTo,function(headers){
      $scope.headers = headers;
    })
  };

  $scope.showDetails = function(nr){
    $location.url("/reclassifyDetails/" + nr);
  };

  $scope.$on('loading', function(){
    $scope.loading = true;
  });

  $scope.$on('loaded', function(){
    $scope.loading = false;
  });

}]);
