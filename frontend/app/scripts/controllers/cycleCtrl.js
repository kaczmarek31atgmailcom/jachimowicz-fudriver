'use strict';

angular.module('frontendApp')
  .controller('cycleCtrl',['$scope','$rootScope','$modal','$http','cycleService', function ($scope,$rootScope,$modal,$http,cycleService) {

    $scope.getHeaders = function(){
      cycleService.getHeaders(function(headers){
        $scope.cycleHeaders = headers;
      })
    };

    $scope.getHale = function(){
      cycleService.getHale(function(hale){
        $scope.hale = hale;
      })
    };

    $scope.launchCycleSettings = function(hala){
      $scope.hala = hala;
      var myModal = $modal({
        scope:$scope,
        controller:'cycleSettingsCtrl',
        templateUrl:"views/modals/cycleSettings.html",
        show: true
      });
    };


  }]);
