"use strict";

angular.module("frontendApp")
.controller("DepotCtrl",["$scope","growl","$modal","DepotService",function($scope,growl,$modal,DepotService){

  $scope.depots = [];

  var init = function(){
    DepotService.getActiveDepots(function(depots){
      $scope.depots = depots;
      $scope.depots.sort(compareDepots);
    })
  };
  init();

  $scope.updateName = function(id,name){
    var command = {};
    command.id = id;
    command.name = name;
    DepotService.updateDepotName(command);
  };

  $scope.deleteDepot = function(id){
    DepotService.deleteDepot(id);
  };

  $scope.openCreateDepotModal = function () {
    //$scope.createCustomer = {};
    var createDepotModal = $modal({
      scope: $scope,
      animation: $scope.animationsEnabled,
      templateUrl: 'views/settings/depot/createDepotModal.html',
      show: true
    })
  };


  $scope.$on("DepotCreated",function(){
    init();
    growl.success("settings.depot.message.depot-created", {ttl:3000});
  });

  $scope.$on("DepotNameUpdated",function(){
    init();
    growl.success("settings.depot.message.name-updated", {ttl:3000});
  });

  $scope.$on("DepotDeleted",function(){
    init();
  });

  $scope.$on("error",function(){
    init();
    growl.error("error" , {ttl:5000})
  });

  function compareDepots(a,b){
    if(a.name > b.name){
      return 1
    }
    if(a.name < b.name){
      return -1
    }
    return 0;
  }

}]);

//////////////////////////////////////

angular.module("frontendApp")
.controller("CreateDepotCtrl",["$scope","DepotService",function($scope,DepotService){

  $scope.create = function(){
    var command = {};
    command.name = angular.copy($scope.name);
    DepotService.createDepot(command);
  };
}]);
