"use strict";

angular.module("frontendApp")
  .controller("ChamberListCtrl", ["$rootScope", "$scope", "growl", "$modal", "ChamberService", "DepotService","CompanyDataService",
    function ($rootScope, $scope, growl, $modal, ChamberService, DepotService, companyDataService) {

      $scope.chambers = [];
      $scope.animationsEnabled = true;
      var init = function () {
        ChamberService.getActiveChambers(function (chambers) {
          $scope.chambers = chambers;
          $scope.chambers.sort(compare)
        });
        DepotService.getActiveDepots(function (depots) {
          $scope.depots = depots;
          $scope.depots.sort(compare);
        });

        companyDataService.getCompaniesData(function(companies){
          $scope.companies = companies;
          $scope.companies.sort(compare);
        })
      };
      init();

      $scope.updateDepot = function (chamberId, depotId) {
        var command = {};
        command.id = chamberId;
        command.depotId = depotId;
        ChamberService.updateDepot(command);
      };

      $scope.updateCompany = function (chamberId, companyId) {
        var command = {};
        command.id = chamberId;
        command.companyId = companyId;
        ChamberService.updateCompany(command);
      };

      $scope.updateName = function (chamberId, name) {
        var command = {};
        command.id = chamberId;
        command.name = name;
        ChamberService.updateName(command);
      };

      $scope.updateArea = function (chamberId, area) {
        var command = {};
        command.id = chamberId;
        command.area = area;
        ChamberService.updateArea(command);
      };

      $scope.openConfirmDeletePrompt = function (chamberId, chamberName) {
        var scope = $rootScope.$new();
        scope.params = {chamberId: chamberId, chamberName: chamberName};
        var confirmDeleteModal = $modal({
          controller: "ChamberDeleteCtrl",
          scope: scope,
          animation: $scope.animationsEnabled,
          templateUrl: "views/settings/chamber/confirmDeleteChamberModal.html"
        });
      };

      $scope.openCreateChamberModal = function () {
        var createChamberModal = $modal({
          controller: "ChamberCreateCtrl",
          animation: $scope.animationsEnabled,
          templateUrl: "views/settings/chamber/createChamberModal.html"
        });
      };


      $scope.$on("ChamberDepotUpdated", function () {
        init();
        growl.success("settings.chamber.message-depot.updated", {ttl: 3000});
      });

      $scope.$on("ChamberCompanyUpdated", function () {
        init();
        growl.success("settings.chamber.message-company.updated", {ttl: 3000});
      });


      $scope.$on("ChamberNameUpdated", function () {
        init();
        growl.success("settings.chamber.message-name.updated", {ttl: 3000});
      });

      $scope.$on("ChamberAreaUpdated", function () {
        init();
        growl.success("settings.chamber.message-area.updated", {ttl: 3000});
      });

      $scope.$on("ChamberDeleted", function () {
        init();
        growl.success("settings.chamber.message-chamber.deleted", {ttl: 3000});
      });

      $scope.$on("ChamberCreated", function () {
        init();
        growl.success("settings.chamber.message-chamber.created", {ttl: 3000});
      });


      $scope.$on("error", function () {
        init();
        growl.error("error", {ttl: 5000});
      });


      function compare(a, b) {
        if (a.id > b.id) {
          return 1;
        }
        if (a.id < b.id) {
          return -1;
        }
        return 0;
      }


    }]);

////////////////////////////////////
angular.module("frontendApp")
  .controller("ChamberDeleteCtrl", ["$scope", "ChamberService", function ($scope, ChamberService) {

    $scope.deleteChamber = function () {
      ChamberService.deleteChamber($scope.params.chamberId);
    }
  }]);


////////////////////////////////////
angular.module("frontendApp")
  .controller("ChamberCreateCtrl", ["$scope", "ChamberService", "DepotService","CompanyDataService", function ($scope, ChamberService, DepotService, CompanyDataService) {

    $scope.depots = [];
    var init = function () {
      DepotService.getActiveDepots(function (depots) {
        $scope.depots = depots;
        $scope.depots = $scope.depots.sort(compare);
        $scope.depotId = $scope.depots[0].id;
      });
      CompanyDataService.getCompaniesData(function(companies){
        $scope.companies = companies;
        $scope.companies.sort(compare);
        $scope.companyId = $scope.companies[0].id;
      })
    };
    init();

    $scope.createChamber = function () {
      var command = {};
      command.name = $scope.name;
      command.area = $scope.area;
      command.depotId = $scope.depotId;
      command.companyId = $scope.companyId;
      ChamberService.createChamber(command);
    };


    function compare(a, b) {
      if (a.name > b.name) {
        return 1;
      }
      if (a.name < b.name) {
        return -1;
      }
      return 0;
    }


  }]);
