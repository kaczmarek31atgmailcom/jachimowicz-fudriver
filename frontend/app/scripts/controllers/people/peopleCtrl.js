'use strict';
angular.module('frontendApp')
  .controller('PeopleCtrl', ['$scope', '$rootScope', '$modal', '$http', '$location', 'EventService', 'peopleService',
    function ($scope, $rootScope, $modal, $http, $location, EventService, peopleService) {

      $scope.pickerModal = {};
      $scope.people = [];
      $scope.showInactivePeopleSwitch = false;
      $scope.orderByField = 'id';
      $scope.showOnlyForeigners = false;

      $scope.focusInputText = function () {
        peopleForm.searchPeople.focus();
      };


      $scope.showRow = function (isPersonActive) {
        if ($scope.showInactivePeopleSwitch == false) {
          return isPersonActive;
        } else {
          return true;
        }
      };

      $scope.getPeople = function () {
        peopleService.getPeople(true, function (people) {
          $scope.allPeople = people;
          $scope.people = people;
        })
      };

      $scope.showAlertsConfig = function(){
        $location.url("/foreignerAlertConfig");
      };

      $scope.editPicker = function (person) {
        $location.url("/editEmployee/" + person.id);
      };

      $scope.addPerson = function () {
        $location.url("/addEmployee/");
      };

      $scope.registerBadge = function (person) {
        $location.url("/badgeRegistration/" + person.id);
      };

      $scope.onSwitchChange = function () {
        peopleService.getPeople(!$scope.showInactivePeopleSwitch, function (people) {
          $scope.people = people;
        })
      };

      $scope.onPeopleOriginSwitchChange = function(){
        $scope.showOnlyForeigners = !($scope.showOnlyForeigners);
        $scope.people = [];
        if($scope.showOnlyForeigners){
          $scope.allPeople.forEach(function(person){
            if(person.isForeigner === true){
              $scope.people.push(person);
            }
          })
        } else {
          $scope.people = $scope.allPeople;
        }
      };

      $scope.changePassword = function (person) {
        $scope.person = person;
        $scope.person.password = '';
        var changePersonPassword = $modal({
          scope: $scope,
          animation: $scope.animationsEnabled,
          templateUrl: 'views/people/changePasswordModal.html',
          show: true
        })
      };

      $scope.submitNewPassword = function (personId, password) {
        var command = {};
        command.personId = personId;
        command.password = password;
        peopleService.changePassword(command);
      };

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


      $scope.setOrderByField = function (value) {
        $scope.orderByField = value;
      };

    }]);
