"use strict";

angular.module("frontendApp")
  .controller("CycleDatesCtrl", ["$rootScope", "$scope", "growl", "$filter", "$modal", "CycleService", "MyceliumService", "userService", "SubsoilService",
    function ($rootScope, $scope, growl, $filter, $modal, CycleService, MyceliumService, userService, SubsoilService) {

      $scope.cycles = [];

      var getCycles = function () {
        CycleService.getDates(function (cycles) {
          cycles = convertIntToDate(cycles);
          $scope.cycles = cycles;
          $scope.cycles.sort(compare);

          MyceliumService.getActiveMyceliums(function (myceliums) {
            $scope.myceliums = checkIfCurrentMyceliumIsInTheList(myceliums, cycles);
          });

          SubsoilService.getActiveSubsoils(function (subsoils) {
            $scope.subsoils = checkIfCurrentSubsoilIsInTheList(subsoils, cycles);
          });

          userService.getActiveUsers(function (users) {
            $scope.technologists = checkIfCurrentTechnologistIsInTheList(users, cycles);
          })
        });
      };
      getCycles();


      var checkIfCurrentMyceliumIsInTheList = function (myceliums, cycles) {
        cycles.forEach(function (cycle) {
          if ((cycle.myceliumId !== null) && (!checkIfMyceliumIsInTheList(cycle.myceliumId, myceliums))) {
            var mycelium = {};
            mycelium.id = cycle.myceliumId;
            mycelium.name = (cycle.myceliumName !== null) ? cycle.myceliumName : "";
            myceliums.push(mycelium);
          }
        });
        return myceliums;
      };

      var checkIfMyceliumIsInTheList = function (myceliumId, myceliums) {
        for (var i = 0; i < myceliums.length; i++) {
          if (myceliums[i].id === myceliumId) {
            return true;
          }
        }
        return false;
      };

      var checkIfCurrentSubsoilIsInTheList = function (subsoils, cycles) {
        cycles.forEach(function (cycle) {
          if ((cycle.subsoilId !== null) && (!checkIfSubsoilIsInTheList(cycle.subsoilId, subsoils))) {
            var subsoil = {};
            subsoil.id = cycle.subsoilId;
            subsoil.name = (cycle.subsoilName !== undefined) ? cycle.subsoilName : "";
            subsoils.push(subsoil);
          }
        });
        return subsoils;
      };

      var checkIfSubsoilIsInTheList = function (subsoilId, subsoils) {
        for (var i = 0; i < subsoils.length; i++) {
          if (subsoils[i].id === subsoilId) {
            return true;
          }
        }
        return false;
      };

      var checkIfCurrentTechnologistIsInTheList = function (technologists, cycles) {
        cycles.forEach(function (cycle) {
          if ((cycle.technologistId !== null) && (!checkIfTechnologistIsInTheList(cycle.technologistId, technologists))) {
            var technologist = {};
            technologist.id = cycle.technologistId;
            technologist.login = cycle.technologistLogin;
            technologist.name = cycle.technoglogistName;
            technologist.surname = cycle.technoglogistSurname;
            technologists.push(technologist);
          }
        });
        return technologists;
      };

      var checkIfTechnologistIsInTheList = function (technologistId, technologists) {
        for (var i = 0; i < technologists.length; i++) {
          if (technologists[i].id === technologistId) {
            return true;
          }
        }
        return false;
      };


      var convertIntToDate = function (cycles) {
        cycles.forEach(function (cycle) {
          cycle = convertIntToDateForSingleCycle(cycle);
        });
        return cycles;
      };

      var convertIntToDateForSingleCycle = function (cycle) {
        if (cycle.initDate > 0) {
          cycle.initDate = moment(cycle.initDate, "YYYYMMDD");
        } else {
          cycle.initDate = null;
        }


        if (cycle.startFirstPeriod > 0) {
          cycle.startFirstPeriod = moment(cycle.startFirstPeriod, "YYYYMMDD");
        } else {
          cycle.startFirstPeriod = null;
        }

        if (cycle.startSecondPeriod > 0) {
          cycle.startSecondPeriod = moment(cycle.startSecondPeriod, "YYYYMMDD");
        } else {
          cycle.startSecondPeriod = null;
        }

        if (cycle.startThirdPeriod > 0) {
          cycle.startThirdPeriod = moment(cycle.startThirdPeriod, "YYYYMMDD");
        } else {
          cycle.startThirdPeriod = null;
        }

        return cycle;
      };


      function compare(a, b) {
        if (a.chamberId > b.chamberId) {
          return 1;
        }
        if (a.chamberId < b.chamberId) {
          return -1;
        }
        return 0;
      }

      $scope.createCycle = function (cycle) {
        var command;
        if (cycle.cycleId) {
          if ((cycle.startFirstPeriod !== null) && (cycle.initDate > cycle.startFirstPeriod)) {
            growl.error("cycle.dates.message.startDate-after-startSecondPeriod", {ttl: 5000});
            updateSingleCycle(cycle.cycleId);
            return;
          }
          command = {};
          command.cycleId = cycle.cycleId;
          command.startDate = moment(cycle.initDate).format("YYYYMMDD");
          command.version = cycle.version;
          CycleService.updateCycleStartDate(command);
        } else {
          command = {};
          command.chamberId = cycle.chamberId;
          command.startDate = moment(cycle.initDate).format("YYYYMMDD");
          CycleService.createCycle(command);
        }
      };

      $scope.updateStartFirstPeriod = function (cycle) {
        if (cycle.startFirstPeriod < cycle.initDate) {
          growl.error("cycle.dates.message.startFirstPeriod-before-startDate", {ttl: 5000});
          updateSingleCycle(cycle.cycleId);
          return;
        }
        if ((cycle.startSecondPeriod) && (cycle.startFirstPeriod > cycle.startSecondPeriod)) {
          growl.error("cycle.dates.message.startFirstPeriod-after-startSecondPeriod", {ttl: 5000});
          updateSingleCycle(cycle.cycleId);
          return;
        }
        var command = {};
        command.cycleId = angular.copy(cycle.cycleId);
        command.startFirstPeriod = moment(cycle.startFirstPeriod).format("YYYYMMDD");
        command.version = cycle.version;
        CycleService.updateCycleStartFirstPeriodDate(command);
      };



      $scope.updateStartSecondPeriod = function (cycle) {
        if (cycle.startSecondPeriod < cycle.startFirstPeriod) {
          growl.error("cycle.dates.message.startSecondPeriod-before-startDate", {ttl: 5000});
          updateSingleCycle(cycle.cycleId);
          return;
        }

        if ((cycle.startThirdPeriod) && (cycle.startSecondPeriod > cycle.startThirdPeriod)) {
          growl.error("cycle.dates.message.startSecondPeriod-after-startThirdPeriod", {ttl: 5000});
          updateSingleCycle(cycle.cycleId);
          return;
        }
        var command = {};
        command.cycleId = angular.copy(cycle.cycleId);
        command.startSecondPeriod = moment(cycle.startSecondPeriod).format("YYYYMMDD");
        command.version = cycle.version;
        CycleService.updateCycleStartSecondPeriodDate(command);
      };

      $scope.updateStartThirdPeriod = function (cycle) {
        if (cycle.startThirdPeriod < cycle.startSecondPeriod) {
          growl.error("cycle.dates.message.startThirdPeriod-before-startSecondPeriod", {ttl: 5000});
          updateSingleCycle(cycle.cycleId);
          return;
        }
        var command = {};
        command.cycleId = angular.copy(cycle.cycleId);
        command.startThirdPeriod = moment(cycle.startThirdPeriod).format("YYYYMMDD");
        command.version = cycle.version;
        CycleService.updateCycleStartThirdPeriodDate(command);
      };

      $scope.updateArea = function (cycle) {
        if (!(parseInt(cycle.area) > 0)) {
          growl.error("cycle.dates.message.area.below.zero", {ttl: 5000});
          updateSingleCycle(cycle.cycleId);
          return;
        }
        var command = {};
        command.cycleId = angular.copy(cycle.cycleId);
        command.area = parseInt(cycle.area);
        command.version = cycle.version;
        CycleService.updateCycleArea(command);
      };

      $scope.updateWeight = function (cycle) {
        if (!(parseInt(cycle.weight) > 0)) {
          growl.error("cycle.dates.message.weight.below.zero", {ttl: 5000});
          updateSingleCycle(cycle.cycleId);
          return;
        }
        var command = {};
        command.cycleId = angular.copy(cycle.cycleId);
        command.weight = parseInt(cycle.weight);
        command.version = cycle.version;
        CycleService.updateCycleWeight(command);
      };

      $scope.updateHumidity = function (cycle) {
        if ((cycle.humidity === undefined) || (parseInt(cycle.humidity) < 0) || (parseInt(cycle.humidity) > 100)) {
          growl.error("cycle.dates.message.weight.invalid.humidity", {ttl: 5000});
          updateSingleCycle(cycle.cycleId);
          return;
        }
        var command = {};
        command.cycleId = angular.copy(cycle.cycleId);
        command.humidity = parseInt(cycle.humidity);
        command.version = cycle.version;
        CycleService.updateCycleHumidity(command);
      };

      $scope.updateMycelium = function (cycle) {
        if (cycle.myceliumId > 0) {
          var command = {};
          command.cycleId = angular.copy(cycle.cycleId);
          command.myceliumId = parseInt(cycle.myceliumId);
          command.version = cycle.version;
          CycleService.updateCycleMycelium(command);
        }
      };

      $scope.updateSubsoil = function (cycle) {
        if (cycle.subsoilId > 0) {
          var command = {};
          command.cycleId = angular.copy(cycle.cycleId);
          command.subsoilId = parseInt(cycle.subsoilId);
          command.version = cycle.version;
          CycleService.updateCycleSubsoil(command);
        }
      };

      $scope.updateTechnologist = function (cycle) {
        if (cycle.technologistId > 0) {
          var command = {};
          command.cycleId = angular.copy(cycle.cycleId);
          command.technologistId = parseInt(cycle.technologistId);
          command.version = cycle.version;
          CycleService.updateCycleTechnologist(command);
        }
      };

      $scope.openCloseCycleConfirm = function (cycle) {
        var scope = $rootScope.$new();
        scope.params = {cycleId: cycle.cycleId, version: cycle.version, endDate: cycle.endDate, chamberName: cycle.chamberName};
        var confirmCloseModal = $modal({
          controller: "CycleCloseCtrl",
          scope: scope,
          animation: $scope.animationsEnabled,
          templateUrl: "views/cycles/confirmCloseCycleModal.html"
        });
      };

      $scope.$on("CycleCreated", function (response, data) {
        updateCreatedCycle(data.entityId);
        growl.success("cycle.dates.message.cycle-created", {ttl: 3000})
      });

      $scope.$on("CycleUpdated", function (response, data) {
        updateSingleCycle(data.entityId);
        growl.success("cycle.dates.message.cycle-updated", {ttl: 3000})
      });

      $scope.$on("CycleClosed", function (response, data) {
        removeSingleCycle(data.entityId);
        growl.success("cycle.dates.message.cycle-closed", {ttl: 3000})
      });


      $scope.$on("error", function (result, data) {
        if (data.body === "concurrentWrite") {
          growl.error("error.concurrentWrite", {ttl: 5000});
          return;
        }

        if (data.body === "FirstCycleDayAfterHarvest") {
          growl.error($filter('translate')('cycle.dates.message.first-day.after-harvest') + moment(data.date, "YYYYMMDD").format("YYYY-MM-DD"), {ttl: 5000});
          updateSingleCycle(data.entityId);
          return;
        }


        growl.error("error", {ttl: 5000});
      });

      var updateSingleCycle = function (cycleId) {
        CycleService.getSingleCycleDatesById(cycleId, function (cycleData) {
          var cycle = convertIntToDateForSingleCycle(cycleData);
          for (var i = 0; i < $scope.cycles.length; i++) {
            if ($scope.cycles[i].cycleId === cycle.cycleId) {
              $scope.cycles[i] = cycle;
              break;
            }
          }
        })
      };

      var updateCreatedCycle = function (cycleId) {
        CycleService.getSingleCycleDatesById(cycleId, function (cycleData) {
          var cycle = convertIntToDateForSingleCycle(cycleData);
          for (var i = 0; i < $scope.cycles.length; i++) {
            if ($scope.cycles[i].chamberId === cycle.chamberId) {
              $scope.cycles[i] = cycle;
              break;
            }
          }
        })
      };

      var removeSingleCycle = function (cycleId) {
        var chamberId = findChamberIdByCycleId($scope.cycles, cycleId);
        CycleService.getSingleCycleDatesByChamberId(chamberId, function (chamberData) {
          var chamber = convertIntToDateForSingleCycle(chamberData);
          for (var i = 0; i < $scope.cycles.length; i++) {
            if ($scope.cycles[i].chamberId === chamberId) {
              $scope.cycles[i] = chamber;
              $scope.cycles[i].isActive = false;
              break;
            }
          }
        })
      };

      var findChamberIdByCycleId = function (cycles, cycleId) {
        for (var i = 0; i < cycles.length; i++) {
          if (cycles[i].cycleId === cycleId) {
            return cycles[i].chamberId;
          }
        }
      };

      $scope.getBarcode = function (id) {
        var chamberId = String(id);
        for (var i = chamberId.length; i < 5; i++) {
          chamberId = "0" + chamberId;
        }
        return "99900001" + chamberId;
      };

    }]);
//////////////////////////////////////////////
angular.module("frontendApp")
  .controller("CycleCloseCtrl", ["$scope", "CycleService", function ($scope, CycleService) {

    $scope.closeCycle = function () {
      var command = {};
      command.cycleId = $scope.params.cycleId;
      command.version = $scope.params.version;
      command.closeDate = parseInt(moment($scope.params.endDate).format("YYYYMMDD"));
      CycleService.closeCycle(command);
    }
  }]);
