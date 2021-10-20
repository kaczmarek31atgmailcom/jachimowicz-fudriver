"use strict";

angular.module("frontendApp")
  .controller("SubsoilCtrl", ["$scope", "growl", "$modal", "SubsoilService", function ($scope, growl, $modal, SubsoilService) {

    $scope.subsoils = [];

    var init = function () {
      SubsoilService.getActiveSubsoils(function (subsoils) {
        $scope.subsoils = subsoils;
        $scope.subsoils.sort(compare);
      })
    };
    init();

    $scope.updateName = function (id, name) {
      var command = {};
      command.id = id;
      command.name = name;
      SubsoilService.updateSubsoilName(command);
    };

    $scope.deleteSubsoil = function (id) {
      SubsoilService.deleteSubsoil(id);
    };

    $scope.openCreateSubsoilModal = function () {
      var createSubsoilModal = $modal({
        scope: $scope,
        animation: $scope.animationsEnabled,
        templateUrl: 'views/settings/subsoil/createSubsoilModal.html',
        show: true
      })
    };


    $scope.$on("SubsoilCreated", function () {
      init();
      growl.success("settings.subsoil.message.subsoil-created", {ttl: 3000});
    });

    $scope.$on("SubsoilNameUpdated", function () {
      init();
      growl.success("settings.subsoil.message.name-updated", {ttl: 3000});
    });

    $scope.$on("SubsoilDeleted", function () {
      init();
      growl.success("settings.subsoil.message.subsoil.deleted", {ttl: 3000});
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
  .controller("CreateSubsoilCtrl", ["$scope", "SubsoilService", function ($scope, SubsoilService) {


    $scope.create = function () {
      var command = {};
      command.name = angular.copy($scope.name);
      SubsoilService.createSubsoil(command);
    };
  }]);

