'use strict';

angular.module('frontendApp')
  .controller('badgeCtrl', ['$scope', '$rootScope', '$route', 'peopleService', function ($scope, $rootScope, $route, peopleService) {

    $scope.successMessage = '';
    $scope.errorMessage = '';
    $scope.employee = null;
    var lastBadge = '';
    $scope.badgeOwner = null;
    $scope.badge = null;

    var employeeId = $route.current.params.employeeId;
    $scope.employee = peopleService.getEmployee(employeeId, function (employee) {
      $scope.employee = employee;
    });

    $scope.focusInputText = function () {
      setTimeout(function () {
        badgeForm.badge.focus();
      },1);
    };


    $scope.parseBadge = function (keyEvent) {
      if (keyEvent.which === 13) {
        $scope.errorMessage = null;
        $scope.successMessage = null;
        if ($scope.badge != $scope.employee.rfid) {
          var registerBadge = {};
          registerBadge.employeeId = $route.current.params.employeeId;
          registerBadge.badge = $scope.badge.toString();
          registerBadge.version = $scope.employee.version;
          peopleService.registerBadge(registerBadge);
          lastBadge = $scope.badge;
        } else {
          $scope.successMessage = "badgeNotChanged";
          $scope.errorMessage = '';

        }
        $scope.badge = '';
      }
    };

    $scope.$on('badgeRegistrationError', function (event, response) {
      if(response.body == 'RfidDuplication'){
        peopleService.getPersonByRfid(lastBadge,function(badgeOwner){
          $scope.badgeOwner = badgeOwner;
          $scope.errorMessage = response.body;
        })
      } else {
        $scope.errorMessage = response.body;
      }
    });


    $scope.$on('badgeRegistered', function (response) {
      $scope.successMessage = response.name;
      $scope.errorMessage = '';
      $scope.employee.rfid = lastBadge;
      $scope.employee.version++;

    });

    $scope.removeBadge = function () {
      var command = {};
      command.employeeId = $scope.employee.id;
      command.version = $scope.employee.version;
      peopleService.deleteBadge(command);
    };

    $scope.$on('badgeDeleted', function () {
      $scope.employee.rfid = null;
      $scope.employee.version++;
      $scope.errorMessage = '';
      $scope.successMessage = 'badgeRemoved';
    });

    $scope.$on('error', function (event, response) {
      $scope.successMessage = null;
      $scope.errorMessage = response.body;

    })
  }]);
