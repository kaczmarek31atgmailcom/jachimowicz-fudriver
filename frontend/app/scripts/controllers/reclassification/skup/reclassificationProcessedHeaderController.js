'use strict';

angular.module('frontendApp')
    .controller('reclassificationProcessedHeaderCtrl',['$scope','reclassificationService',function($scope,reclassificationService){

        $scope.headers = [];
        $scope.showHeaders = false;

        $scope.getHeaders = function(){
            reclassificationService.getProcessedHeaders(function(headers){
                $scope.headers = headers;
                if($scope.headers.length > 0){
                    $scope.showHeaders = true;
                }
            })
        };

    }]);