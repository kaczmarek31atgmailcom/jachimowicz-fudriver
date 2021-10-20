"use strict";

angular.module("frontendApp")
  .controller("SalaryAccountsCtrl", ["$scope", "$rootScope", "growl", "$location", "$modal", "salaryService",
    function ($scope, $rootScope, growl, $location, $modal, salaryService) {

      function init() {
        salaryService.getSalaryAccountStatus(function (accounts) {
          $scope.accounts = hideInactiveWithZeroAmount(accounts);
        })
      }

      init();

      function hideInactiveWithZeroAmount(accounts) {
        var result = [];
        accounts.forEach(function (account) {
          if ((!(account.isActive)) && (account.moneyAmount !== 0)) {
            result.push(account);
          }
        });
        return result;
      }

      $scope.openPayoffModal = function (person) {
        var scope = $rootScope.$new();
        scope.params = {person: person};
        var payoffModal = $modal({
          controller: "SalaryAccountPayoffModalCtrl",
          scope: scope,
          animation: $scope.animationsEnabled,
          templateUrl: "views/salary/PayoffModal.html"
        });
      };

      $scope.openPaymentModal = function (person) {
        var scope = $rootScope.$new();
        scope.params = {person: person};
        var paymentModal = $modal({
          controller: "SalaryAccountPaymentModalCtrl",
          scope: scope,
          animation: $scope.animationsEnabled,
          templateUrl: "views/salary/PaymentModal.html"
        });
      };

      $scope.showAccountHistory = function (person) {
        salaryService.currentPerson = person;
        $location.url("/salary/account/history/" + person.personId);
      };

      $scope.$on("PayoffCreated", function () {
        growl.success("payroll.salary.account.message.payoff.success", {ttl: 3000});
        init();
      });

      $scope.$on("PaymentCreated", function () {
        growl.success("payroll.salary.account.message.payment.success", {ttl: 3000});
        init();
      });

      $scope.$on('error', function () {
        growl.error('error', {ttl: 5000})
      });
    }]);


//////////////////////////////
angular.module("frontendApp")
  .controller("SalaryAccountPayoffModalCtrl", ["$scope", "growl", "salaryService", function ($scope, growl, salaryService) {
    $scope.amount = ($scope.params.person.moneyAmount / 100).toString().replace(',', '').replace('.', ',');

    $scope.doPayoff = function () {
      var command = {};
      command.personId = $scope.params.person.personId;
      command.amount = Math.round($scope.amount.toString().replace(",", ".") * 100);
      salaryService.salaryAccountPayoff(command);
    }
  }]);
//////////////////////////////
angular.module("frontendApp")
  .controller("SalaryAccountPaymentModalCtrl", ["$scope", "growl", "salaryService", function ($scope, growl, salaryService) {
    $scope.amount = ($scope.params.person.moneyAmount / 100).toString().replace(',', '').replace('.', ',');

    $scope.doPayoff = function () {
      var command = {};
      command.personId = $scope.params.person.personId;
      command.amount = Math.round($scope.amount.toString().replace(",", ".") * 100);
      salaryService.salaryAccountPayment(command);
    }
  }]);
//////////////////////////////
angular.module("frontendApp")
  .controller("SalaryAccountHistoryCtrl", ["$scope", "growl", "$route", "$location", "salaryService", function ($scope, growl, $route, $location, salaryService) {
    var personId = $route.current.params.personId;
    $scope.person = salaryService.currentPerson;


    var init = function () {
      salaryService.getSalaryAccountHistory(personId, function (history) {
        $scope.history = history;
        if ((($scope.person.personId === null) || ($scope.person.personId === undefined)) && ($scope.history.length === 0)) {
          $location.url("/salary/accounts");
        }
        if (($scope.person.personId === null) || ($scope.person.personId === undefined)) {
          $scope.person.personId = $scope.history[0].id;
          $scope.person.personName = $scope.history[0].name;
          $scope.person.personSurname = $scope.history[0].surname;
        }
      })
    };
    init();

    $scope.getTotal = function (history) {
      var total = 0;
      history.forEach(function(item){
        total += item.amount;
      });
    return total;
    };


    $scope.$on('error', function () {
      growl.error('error', {ttl: 5000})
    });

  }]);
