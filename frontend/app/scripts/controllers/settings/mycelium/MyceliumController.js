"use strict";

angular.module("frontendApp")
  .controller("MyceliumCtrl", ["$scope", "growl", "$modal", "MyceliumService", function ($scope, growl, $modal, MyceliumService) {

    $scope.myceliums = [];

    var init = function () {
      MyceliumService.getActiveMyceliums(function (myceliums) {
        $scope.myceliums = myceliums;
        $scope.myceliums.sort(compare);
      })
    };
    init();

    $scope.updateName = function (id, name) {
      var command = {};
      command.id = id;
      command.name = name;
      MyceliumService.updateMyceliumName(command);
    };

    $scope.deleteMycelium = function (id) {
      MyceliumService.deleteMycelium(id);
    };

    $scope.openCreateMyceliumModal = function () {
      var createMyceliumModal = $modal({
        scope: $scope,
        animation: $scope.animationsEnabled,
        templateUrl: 'views/settings/mycelium/createMyceliumModal.html',
        show: true
      })
    };


    $scope.$on("MyceliumCreated", function () {
      init();
      growl.success("settings.mycelium.message.mycelium-created", {ttl: 3000});
    });

    $scope.$on("MyceliumNameUpdated", function () {
      init();
      growl.success("settings.mycelium.message.name-updated", {ttl: 3000});
    });

    $scope.$on("MyceliumDeleted", function () {
      init();
      growl.success("settings.mycelium.message.mycelium.deleted", {ttl: 3000});
    });

    $scope.$on("error", function () {
      init();
      growl.error("error", {ttl: 5000})
    });

    function compare(a, b) {
      if (a.name > b.name) {
        return 1
      }
      if (a.name < b.name) {
        return -1
      }
      return 0;
    }

  }]);

//////////////////////////////////////

angular.module("frontendApp")
  .controller("CreateMyceliumCtrl", ["$scope", "MyceliumService", function ($scope, MyceliumService) {

    $scope.create = function () {
      var command = {};
      command.name = angular.copy($scope.name);
      MyceliumService.createMycelium(command);
    };
  }]);

