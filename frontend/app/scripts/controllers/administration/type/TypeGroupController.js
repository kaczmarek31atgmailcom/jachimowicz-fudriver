"use strict";

angular.module("frontendApp")
  .controller("TypeGroupCtrl", ["$scope", "$rootScope", "growl", "$modal", "TypeService", function ($scope, $rootScope, growl, $modal, TypeService) {

    var init = function () {
      TypeService.getActiveGroups(function (groups) {
        $scope.groups = groups;
        $scope.groups.sort(compareTypeGroups);
      })
    };
    init();

    $scope.updateName = function (id, name) {
      var command = {};
      command.id = id;
      command.name = name;
      TypeService.updateTypeGroupName(command);
    };

    $scope.openCreateTypeGroupModal = function () {
      var createTypeGroupModal = $modal({
        scope: $scope,
        animation: $scope.animationsEnabled,
        templateUrl: 'views/administration/type/createTypeGroupModal.html',
        show: true
      })
    };

    $scope.openDeleteModal = function (typeGroup) {
      var scope = $rootScope.$new();
      scope.params = {typeGroup: typeGroup};
      var deleteModal = $modal({
        controller: "TypeGroupDeleteCtrl",
        scope: scope,
        animation: $scope.animationsEnabled,
        templateUrl: "views/administration/type/confirmTypeGroupDeleteModal.html"
      });
    };


    $scope.$on("TypeGroupCreated", function () {
      init();
      growl.success("admin.typeGroup.message.typeGroup-created", {ttl: 3000});
    });

    $scope.$on("TypeGroupNameUpdated", function () {
      init();
      growl.success("admin.typeGroup.message.name-updated", {ttl: 3000});
    });

    $scope.$on("TypeGroupDeleted", function () {
      growl.success("admin.typeGroup.message.deleted", {ttl: 3000});
      init();
    });

    $scope.$on("error", function () {
      init();
      growl.error("error", {ttl: 5000})
    });

    function compareTypeGroups(a, b) {
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
  .controller("CreateTypeGroupCtrl", ["$scope", "TypeService", function ($scope, TypeService) {

    $scope.create = function () {
      var command = {};
      command.name = angular.copy($scope.name);
      TypeService.createTypeGroup(command);
    };
  }]);

//////////////////////////////////////

angular.module("frontendApp")
  .controller("TypeGroupDeleteCtrl", ["$scope", "TypeService", function ($scope, TypeService) {
    $scope.typeGroup = $scope.params.typeGroup;
    $scope.deleteTypeGroup = function (typeGroup) {
      TypeService.deleteTypeGroup(typeGroup.id);
    };


  }]);

