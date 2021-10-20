'use strict';

angular.module('frontendApp')
.controller('reclassificationHeaderCtrl',['$scope','reclassificationService',function($scope,reclassificationService){

    $scope.headers = [];
    $scope.showHeaders = false;

    $scope.getHeaders = function(){
        reclassificationService.getNotProcessedHeaders(function(headers){
            $scope.headers = headers;
            if($scope.headers.length > 0){
                $scope.showHeaders = true;
            }
        })
    };
}]);