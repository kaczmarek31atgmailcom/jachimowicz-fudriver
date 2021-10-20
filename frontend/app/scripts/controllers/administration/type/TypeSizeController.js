"use strict";

angular.module("frontendApp")
  .controller("TypeSizeCtrl", ["$scope", "$rootScope", "growl", "$modal", "TypeService",
    function ($scope, $rootScope, growl, $modal, TypeService) {

      $scope.sizes = [];

      var init = function () {
        TypeService.getActiveTypeSizes(function (sizes) {
          $scope.sizes = sizes;
        })
      };
      init();


      $scope.updateName = function (size, name) {
        if (size.name !== name) {
          var command = {};
          command.id = size.id;
          command.name = name;
          TypeService.updateTypeSizeName(command);
        }
      };

      $scope.openCreateTypeSizeModal = function () {
        var createTypeSizeModal = $modal({
          scope: $scope,
          animation: $scope.animationsEnabled,
          templateUrl: 'views/administration/type/createTypeSizeModal.html',
          show: true
        })
      };

      $scope.openDeleteModal = function (typeSize) {
        var scope = $rootScope.$new();
        scope.params = {typeSize: typeSize};
        var deleteModal = $modal({
          controller: "TypeSizeDeleteCtrl",
          scope: scope,
          animation: $scope.animationsEnabled,
          templateUrl: "views/administration/type/confirmTypeSizeDeleteModal.html"
        });
      };


      $scope.$on("TypeSizeCreated", function () {
        init();
        growl.success("admin.typeSize.message.typeSize-created", {ttl: 3000});
      });

      $scope.$on("TypeSizeNameUpdated", function () {
        init();
        growl.success("admin.typeSize.message.name-updated", {ttl: 3000});
      });

      $scope.$on("TypeSizeDeleted", function () {
        growl.success("admin.typeSize.message.deleted", {ttl: 3000});
        init();
      });

      $scope.$on("error", function () {
        init();
        growl.error("error", {ttl: 5000})
      });


    }]);
//////////////////////////////////////

angular.module("frontendApp")
  .controller("CreateTypeSizeCtrl", ["$scope", "TypeService", function ($scope, TypeService) {

    $scope.create = function () {
      var command = {};
      command.name = angular.copy($scope.name);
      TypeService.createTypeSize(command);
    };
  }]);

//////////////////////////////////////

angular.module("frontendApp")
  .controller("TypeSizeDeleteCtrl", ["$scope", "TypeService", function ($scope, TypeService) {
    $scope.typeSize = $scope.params.typeSize;

    $scope.deleteTypeSize = function (typeSize) {
      TypeService.deleteTypeSize(typeSize.id);
    };


  }]);

