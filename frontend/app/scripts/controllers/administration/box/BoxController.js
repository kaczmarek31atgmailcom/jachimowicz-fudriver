"use strict";

angular.module("frontendApp")
  .controller("BoxCtrl", ["$scope", "$rootScope", "growl", "$modal", "BoxService", function ($scope, $rootScope, growl, $modal, BoxService) {

    var init = function () {
      BoxService.getActiveBoxes(function (boxes) {
        $scope.boxes = boxes;
        $scope.boxes.sort(compareBoxes);
      })
    };
    init();

    $scope.updateName = function (id, name) {
      var command = {};
      command.id = id;
      command.name = name;
      BoxService.updateBoxName(command);
    };

    $scope.openCreateBoxModal = function () {
      var createBoxModal = $modal({
        scope: $scope,
        animation: $scope.animationsEnabled,
        templateUrl: 'views/administration/box/createBoxModal.html',
        show: true
      })
    };

    $scope.openDeleteModal = function (box) {
      var scope = $rootScope.$new();
      scope.params = {box: box};
      var deleteModal = $modal({
        controller: "BoxDeleteCtrl",
        scope: scope,
        animation: $scope.animationsEnabled,
        templateUrl: "views/administration/box/confirmBoxDeleteModal.html"
      });
    };


    $scope.$on("BoxCreated", function () {
      init();
      growl.success("admin.box.message.box-created", {ttl: 3000});
    });

    $scope.$on("BoxNameUpdated", function () {
      init();
      growl.success("admin.box.message.name-updated", {ttl: 3000});
    });

    $scope.$on("BoxDeleted", function () {
      growl.success("admin.box.message.deleted", {ttl: 3000});
      init();
    });

    $scope.$on("error", function () {
      init();
      growl.error("error", {ttl: 5000})
    });

    function compareBoxes(a, b) {
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
  .controller("CreateBoxCtrl", ["$scope", "BoxService", function ($scope, BoxService) {

    $scope.create = function () {
      var command = {};
      command.name = angular.copy($scope.name);
      BoxService.createBox(command);
    };
  }]);

//////////////////////////////////////

angular.module("frontendApp")
  .controller("BoxDeleteCtrl", ["$scope", "BoxService", function ($scope, BoxService) {
    $scope.box = $scope.params.box;
    $scope.deleteBox = function (box) {
      BoxService.deleteBox(box.id);
    };


  }]);

