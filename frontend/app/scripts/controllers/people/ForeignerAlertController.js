"use strict";

angular.module('frontendApp')
  .controller('foreignerAlertCtrl', ['$scope', 'growl','peopleService', function ($scope,growl, peopleService) {

    var init = function () {
      peopleService.getForeignerAlert(function (alerts) {
        $scope.alerts = alerts;
        $scope.backupAlert = angular.copy(alerts);
      });
    };
    init();

    $scope.save = function () {
      if (($scope.alerts.visaDays !== $scope.backupAlert.visaDays) ||
        ($scope.alerts.stayDays !== $scope.backupAlert.stayDays) ||
        ($scope.alerts.statementDays !== $scope.backupAlert.statementDays) ||
        ($scope.alerts.passportDays !== $scope.backupAlert.passportDays)) {
        console.log($scope.alerts.visaDays + ' !== ' + $scope.backupAlert.visaDays);
        var command = {};
        command.visaDays = $scope.alerts.visaDays;
        command.stayDays = $scope.alerts.stayDays;
        command.statementDays = $scope.alerts.statementDays;
        command.passportDays = $scope.alerts.passportDays;
        peopleService.updateForeignerAlert(command);
      }
    };

    $scope.$on('error', function () {
      growl.error('error', {ttl: 5000})
    });

    $scope.$on('foreignerAlertUpdated', function () {
      growl.success('people.alert.message.success', {ttl: 5000})
    });

  }]);
