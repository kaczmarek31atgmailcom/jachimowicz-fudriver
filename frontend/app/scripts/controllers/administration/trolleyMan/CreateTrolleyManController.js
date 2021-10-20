'use strict';

angular.module('frontendApp')
  .controller('CreateTrolleyManCtrl',['$scope','TrolleyManService',function($scope,TrolleyManService){

    $scope.create = function(name,surname){
      var command = {};
      command.name = name;
      command.surname = surname;
      TrolleyManService.createTrolleyMan(command);
    }

  }])
