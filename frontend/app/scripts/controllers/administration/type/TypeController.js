"use strict";

angular.module("frontendApp")
  .controller("TypeCtrl", ["$scope", "$rootScope", "$modal", "growl", "TypeService", "BoxService",
    function ($scope, $rootScope, $modal, growl, TypeService, BoxService) {

      $scope.types = [];
      $scope.boxes = [];
      $scope.groups = [];
      $scope.sizes = [];

      var init = function () {
        TypeService.getActiveTypes(function (types) {
          $scope.types = types;

          TypeService.getActiveGroups(function (groups) {
            $scope.groups = groups;
          });

          TypeService.getActiveTypeSizes(function (sizes) {
            $scope.sizes = sizes;
          });

          BoxService.getActiveBoxes(function (boxes) {
            $scope.boxes = boxes;
          })

        })
      };
      init();

      $scope.changeBox = function (data, type) {
        if(data !== type.boxId) {
        var command = {};
        command.typeId = type.id;
        command.boxId = parseInt(data);
        if ((command.boxId > 0) && (command.typeId > 0)) {
          TypeService.updateTypeBoxAssignment(command);
        }
          }
        return false;
      };

      $scope.changeGroup = function (data, type) {
        if(data !== type.groupId) {
          var command = {};
          command.typeId = type.id;
          command.groupId = parseInt(data);
          if ((command.groupId > 0) && (command.typeId > 0)) {
            TypeService.updateTypeGroupAssignment(command);
          }
        }
        return false;
      };

      $scope.changeSize = function (data, type) {
        if(data !== type.sizeId) {
          var command = {};
          command.typeId = type.id;
          command.sizeId = parseInt(data);
          if ((command.sizeId > 0) && (command.sizeId > 0)) {
            TypeService.updateTypeSizeAssignment(command);
          }
        }
        return false;
      };


      $scope.updateTypeName = function (type, name) {
        if(name !== type.name) {
          var command = {};
          command.id = type.id;
          command.name = name;
          TypeService.updateTypeName(command);
        }
        return false;
      };

      $scope.addZeroes = function (id, number) {
        var result = id.toString();
        while (result.length < number) {
          result = '0' + result;
        }
        return result;
      };

      $scope.openCreateTypeModal = function () {
        var scope = $rootScope.$new();
        scope.params = {groups: $scope.groups, boxes: $scope.boxes, sizes: $scope.sizes};
        var createModal = $modal({
          controller: "TypeCreateCtrl",
          scope: scope,
          animation: $scope.animationsEnabled,
          templateUrl: "views/administration/type/createTypeModal.html"
        });
      };

      $scope.openDeleteTypeModal = function (type) {
        var scope = $rootScope.$new();
        scope.params = {type: type};
        var deleteModal = $modal({
          controller: "TypeDeleteCtrl",
          scope: scope,
          animation: $scope.animationsEnabled,
          templateUrl: "views/administration/type/confirmDeleteTypePrompt.html"
        });
      };


      $scope.$on('TypeCreated', function () {
        growl.success("admin.type.message.type-created", {ttl: 3000});
        init();
      });


      $scope.$on('NewBoxAssigned', function () {
        growl.success("admin.type.message.box-assigned", {ttl: 3000});
        init();
      });

      $scope.$on('NewTypeSizeAssigned', function () {
        growl.success("admin.type.message.size-assigned", {ttl: 3000});
        init();
      });


      $scope.$on('NewTypeGroupAssigned', function () {
        growl.success("admin.type.message.group-assigned", {ttl: 3000});
        init();
      });

      $scope.$on('TypeNameUpdated', function () {
        growl.success("admin.type.message.name-updated", {ttl: 3000});
        init();
      });

      $scope.$on('TypeDeleted', function () {
        growl.success("admin.type.message.deleted", {ttl: 3000});
        init();
      });

      $scope.$on('error', function () {
        growl.error("error", {ttl: 5000});
      })

    }]);

//////////////////////////////////////

angular.module("frontendApp")
  .controller("TypeCreateCtrl", ["$scope", "TypeService", function ($scope, TypeService) {

    $scope.boxes = $scope.params.boxes;
    $scope.groups = $scope.params.groups;
    $scope.sizes = $scope.params.sizes;
    $scope.type = {};
    $scope.exportTypes = ["export", "kraj", "inne"];
    $scope.type.exportType = $scope.exportTypes[0];

    $scope.create = function () {
      var command = {};
      command.name = angular.copy($scope.type.name);
      command.weight = Math.round($scope.type.weight.toString().replace(",", ".") * 1000);
      command.boxId = $scope.type.boxId;
      command.groupId = $scope.type.groupId;
      command.sizeId = $scope.type.sizeId;
      command.exportType = $scope.type.exportType.toUpperCase();
      TypeService.createType(command);
    };
  }]);

//////////////////////////////////////

angular.module("frontendApp")
  .controller("TypeDeleteCtrl", ["$scope", "TypeService", function ($scope, TypeService) {
    $scope.type = $scope.params.type;
    $scope.delete = function () {
      TypeService.deleteType($scope.type.id);
    };
  }]);
