'use strict';

angular.module('frontendApp')
  .controller('cycleSettingsCtrl',['$scope','cycleService','cycleDictionaryServicePromise', function ($scope,cycleService,cycleDictionaryServicePromise) {

    $scope.details = {};

    cycleDictionaryServicePromise.then(function(cycleDictionaryService) {
      $scope.grzybnie = cycleDictionaryService.grzybnie;
      $scope.kompostownie = cycleDictionaryService.kompostownie;
      $scope.torfy = cycleDictionaryService.torfy;
      $scope.dok_pod = cycleDictionaryService.dok_pod;
      $scope.dok_okr = cycleDictionaryService.dok_okr;
      $scope.stopien_ubicia = cycleDictionaryService.stopien_ubicia;
      $scope.kaking = cycleDictionaryService.kaking;
    });

    $scope.getDetails = function(){
      var halaId = $scope.hala.id;
      cycleService.getDetails(halaId, function(data){
        $scope.details = data;
      });
    };
  }]);
