'use strict';

angular.module('frontendApp')
  .controller('reclassificationCtrl', ['$scope', 'reclassificationService', function ($scope, reclassificationService) {

    $scope.rodzajeSkup = {};
    $scope.rodzajeLocal = [];

    $scope.loadRodzaje = function () {
      reclassificationService.getSkupTypes(function (rodzajeSkup) {
        $scope.rodzajeSkup = removeInactiveTypes(rodzajeSkup);

        reclassificationService.getLocalTypes(function (rodzajeLocal) {
          $scope.rodzajeLocal = removeInactiveTypes(rodzajeLocal);
          //$scope.rodzajeLocalDropDown = removeAssignedTypes($scope.rodzajeLocal, $scope.rodzajeSkup);
        });
      });
    };

    $scope.getRodzajSkupById = function (rodzajLocal,rodzajeSkup) {
      for(var i = 0; i<rodzajeSkup.length; i++){
        if(parseInt(rodzajeSkup[i].id) === parseInt(rodzajLocal.remoteTypeId)){
          return rodzajeSkup[i].name + " " + rodzajeSkup[i].weight / 1000;
        }
      }
      return "---"
    };



    $scope.assignRodzajSkup = function (rodzajLocal,data) {
      var command = {};
      if(data !== undefined) {
        command.remoteTypeId = data;
        command.localTypeId = rodzajLocal.id;
        reclassificationService.assignLocalRodzaj(command);
      } else {
        command.localTypeId = rodzajLocal.id;
        reclassificationService.unassignLocalRodzaj(command);
      }
    };

    function removeInactiveTypes(allTypes) {
      var result = [];
      allTypes.forEach(function (type) {
        if (type.active === true) {
          result.push(type);
        }
      });
      return result.sort(compareTypes);
    }

    function compareTypes(a, b) {
      if (a.name > b.name) {
        return 1;
      }
      if (b.name < a.name) {
        return -1;
      }
      return 0;
    }

    $scope.$on('Rodzaj updated', function () {
      $scope.loadRodzaje();
    })
  }]);
