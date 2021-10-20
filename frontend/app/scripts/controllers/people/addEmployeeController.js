'use strict';

angular.module('frontendApp')
  .controller('addEmployeeCtrl', ['$scope', '$rootScope', 'peopleService', function ($scope, $rootScope, peopleService) {

    $scope.groups = [];
    $scope.employee = {};
    $scope.employee.payrollStatus = 0;
    $scope.employee.isForeigner = false;
    $scope.createdEmployee = {};
    $scope.payrollStatuses = [{id: 0, name: 'pickerEdit.status.zero'}, {id: 1, name: 'pickerEdit.status.one'}, {
      id: 2,
      name: 'pickerEdit.status.two'
    }];
    $scope.freeNumbers = [];

    var init = function () {
      peopleService.getGroups(true, function (groups) {
        $scope.groups = groups;
        $scope.employee.groupId = $scope.groups[0].id;
      });
      peopleService.getReservedNumbers(function (numbers) {
        getFreeNumbers(numbers);
        $scope.employee.nr = $scope.freeNumbers[0];
      });
    };
    init();

    var getFreeNumbers = function (reservedNumbers) {
      var fullArray = [];
      for (var n = 0; n < reservedNumbers.length + 50; n++) {
        fullArray.push(n+1)
      }
      for (var i = 0; i < fullArray.length; i++) {
        for (var j = 0; j < reservedNumbers.length; j++) {
          if ((reservedNumbers[j] != undefined) && (reservedNumbers[j] === fullArray[i])) {
            fullArray.splice(i, 1);
          }
        }
      }
        fullArray.forEach(function(item){
          $scope.freeNumbers.push(item);
      });
    };


    $scope.addEmployee = function () {
      var command = {};
      command.nr = $scope.employee.nr;
      command.imie = $scope.employee.imie;
      command.nazwisko = $scope.employee.nazwisko;
      command.adres = $scope.employee.adres;
      command.mobile = $scope.employee.mobile;
      command.groupId = $scope.employee.groupId;
      command.birthDate = $scope.employee.birthDate;
      command.city = $scope.employee.city;
      command.rejon = $scope.employee.rejon;
      command.indeks = $scope.employee.indeks;
      command.imionaRodzicow = $scope.employee.imionaRodzicow;
      command.nrWizy = $scope.employee.nrWizy;
      command.koniecWaznosciWizy = $scope.employee.koniecWaznosciWizy;
      command.nrOswiadczenia = $scope.employee.nrOswiadczenia;
      command.nrZezwolenia = $scope.employee.nrZezwolenia;
      command.koniecWaznosciZezwolenia = $scope.employee.koniecWaznosciZezwolenia;
      command.poczatekWaznosciZezwolenia = $scope.employee.poczatekWaznosciZezwolenia;
      command.dataZameldowania = $scope.employee.dataZameldowania;
      command.dataWymeldowania = $scope.employee.dataWymeldowania;
      command.pesel = $scope.employee.pesel;
      command.nrPaszportu = $scope.employee.nrPaszportu;
      command.koniecWaznosciPaszportu = $scope.employee.koniecWaznosciPaszportu;
      command.rfid = $scope.employee.rfid;
      command.active = $scope.employee.active;
      command.isForeigner = $scope.employee.isForeigner;

      peopleService.createEmployee(command);
    };

    $scope.$on('employeeCreated', function (event, data) {
      peopleService.getEmployee(data.entityId, function (employee) {
        $scope.createdEmployee = employee;
        $scope.employee = {};
        $scope.employee.groupId = $scope.groups[0].id;
        $scope.employee.isForeigner = false;
      });
    });
  }
  ]);
