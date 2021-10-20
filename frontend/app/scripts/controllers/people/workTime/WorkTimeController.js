'use strict';

angular.module('frontendApp')
  .controller('WorkTimeCtrl', ['$scope', '$location', 'peopleService', 'EventService',
    function ($scope, $location, peopleService, EventService) {

      $scope.people = [];

      var init = function () {
        peopleService.getPeople(true, function (people) {
          $scope.people = people;
        })
      };
      init();

      $scope.event = EventService.getEvents(function (response) {
        var response = JSON.parse(response);
        var message = response.source;

        if (message === 'EMPLOYEE_WORK_STARTED') {
          updateWorkStatus(response.id, true);
        }
        if (message === 'EMPLOYEE_WORK_ENDED') {
          updateWorkStatus(response.id, false);
        }
      });

      var updateWorkStatus = function (personId, isPresent) {
        for (var i = 0; i < $scope.people.length; i++) {
          if ($scope.people[i].id === personId) {
            $scope.people[i].isPresent = isPresent;
            $scope.$apply();
            break;
          }
        }
      };

      $scope.showRow = function (isPersonActive) {
        if ($scope.showInactivePeopleSwitch == false) {
          return isPersonActive;
        } else {
          return true;
        }
      };

      $scope.editTimeSheet = function (person) {
        $location.url("/people/worktime/" + person.id);
      };

    }]);
