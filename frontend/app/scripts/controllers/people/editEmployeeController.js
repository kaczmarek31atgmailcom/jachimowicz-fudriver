'use strict';

angular.module('frontendApp')
  .controller('editEmployeeCtrl', ['$scope', '$rootScope', 'growl', '$route', '$http', 'peopleService', 'peopleDictionaryServicePromise', function ($scope, $rootScope, growl, $route, $http, peopleService, peopleDictionaryServicePromise) {
    $scope.employee = {};
    $scope.groups = [];
    $scope.yearBars = [];
    $scope.editWorkPeriodsFlag = false;
    $scope.workPeriods = [];
    $scope.payrollStatuses = [{id: 0, name: 'pickerEdit.status.zero'}, {id: 1, name: 'pickerEdit.status.one'}, {id: 2, name: 'pickerEdit.status.two'}];
    $scope.freeNumbers = [];
    $scope.alerts = [];

    $scope.getEmployee = function () {
      var employeeId = $route.current.params.employeeId;
      peopleService.getEmployee(employeeId, function (employeeData) {
        $scope.employee = employeeData;
        $scope.employeeName = employeeData.imie;
        $scope.employeeSurname = employeeData.nazwisko;
        peopleService.getReservedNumbers(function (numbers) {
          getFreeNumbers(numbers);
          if ($scope.employee.nr > 0) {
            $scope.freeNumbers.push($scope.employee.nr);
            $scope.freeNumbers.sort(function (a, b) {
              return parseInt(a) - parseInt(b);
            });
          }
        });


      });
      peopleService.getForeignerAlert(function (alerts) {
        $scope.alerts = alerts;
      });

      peopleService.getYearBars(employeeId, function (yearBars) {
        $scope.yearBars = yearBars;
      });
      peopleService.getGroups(true, function (groups) {
        $scope.groups = groups;
      });


    };

    var getFreeNumbers = function (reservedNumbers) {
      var fullArray = [];
      for (var n = 0; n < reservedNumbers.length + 10; n++) {
        fullArray.push(n + 1)
      }
      for (var i = 0; i < fullArray.length; i++) {
        for (var j = 0; j < reservedNumbers.length; j++) {
          if ((reservedNumbers[j] != undefined) && (reservedNumbers[j] === fullArray[i])) {
            fullArray.splice(i, 1);
          }
        }
      }
      $scope.freeNumbers = [];
      fullArray.forEach(function (item) {
        $scope.freeNumbers.push(item);
      });
    };


    $scope.$on('employeeUpdated', function () {
      $scope.getEmployee();
      growl.success("pickerEdit.message.picker-data-updated", {ttl: 3000})
    });

    $scope.$on('error', function (event, data) {
      $scope.errorMessage = data.body;
    });

    $scope.activate = function (employee) {
      var activeEmpl = {};
      activeEmpl.personId = employee.id;
      activeEmpl.version = employee.version;
      if (employee.active) {
        peopleService.activate(activeEmpl);
      } else {
        peopleService.inactivate(activeEmpl);
      }
    };

    $scope.getNumberOfDaysBetween = function(endDate,startDate){
      var diff = 0;
      if((startDate !== null ) && (endDate !== null)){
        var end = moment(endDate);
        var start = moment(startDate);
        if (start.isSameOrBefore(end)) {
          diff = end.diff(start, 'days') + 1;
        }
      }
      return diff;
    };


    $scope.$on('employeeInactivated', function () {
      var employeeId = $route.current.params.employeeId;
      $scope.employee.nr = 0;
      peopleService.getWorkPeriods(employeeId, function (workPeriods) {
        $scope.workPeriods = workPeriods;
      });

      peopleService.getYearBars(employeeId, function (yearBars) {
        $scope.yearBars = yearBars;
      });

    });

    $scope.hasError = function (fieldName, date) {
      switch (fieldName) {
        case 'visaDate':
          if ($scope.alerts.visaDays === 0) {
            return false;
          } else {
            if (($scope.alerts.visaDays !== null) && (typeof date !== 'undefined')  && (date !== null)) {
              return moment().add($scope.alerts.visaDays, 'days').isSameOrAfter(date, 'day');
            }
          }
          if ((typeof date === 'undefined') || (date === null)) {
            return true;
          }
          return false;
          break;
        case 'statementDate':
          if ($scope.alerts.statementDays === 0) {
            return false;
          } else {
            if (($scope.alerts.statementDays !== 'null') && (date !== undefined) && (date !== null)) {
              return moment().add($scope.alerts.statementDays, 'days').isSameOrAfter(date, 'day');
            }
          }
          if ((date === undefined) || (date === null)) {
            return true;
          }
          return false;
          break;
        case 'passportDate':
          if ($scope.alerts.passportDays === 0) {
            return false;
          } else {
            if (($scope.alerts.passportDays !== null) && (date !== undefined) && (date !== null)) {
              return moment().add($scope.alerts.passportDays, 'days').isSameOrAfter(date, 'day');
            }
          }
          if ((date === undefined) || (date === null)) {
            return true;
          }
          return false;
          break;
        case 'stayDate':
          if ($scope.alerts.stayDays === 0) {
            return false;
          } else {
            if ((date !== undefined) && ($scope.alerts.stayDays != null) && (date !== null)) {
              return moment().add($scope.alerts.stayDays, 'days').isSameOrAfter(date, 'day');
            }
          }
          if ((date === undefined) || (date === null)) {
            return true;
          }
          return false;
          break;

        default:
          return false;
      }
    };


    $scope.$on('employeeActivated', function () {
      var employeeId = $route.current.params.employeeId;
      peopleService.getEmployee($scope.employee.id, function (employeeData) {
        $scope.employee.nr = employeeData.nr;
        $scope.employee.version = employeeData.version;
      });
      peopleService.getWorkPeriods(employeeId, function (workPeriods) {
        $scope.workPeriods = workPeriods;
      });

      peopleService.getYearBars(employeeId, function (yearBars) {
        $scope.yearBars = yearBars;
      });
    });


    $scope.updateEmployee = function (employee) {
      var updateEmployee = {};
      updateEmployee.id = employee.id;
      updateEmployee.nr = employee.nr;
      updateEmployee.imie = employee.imie;
      updateEmployee.nazwisko = employee.nazwisko;
      updateEmployee.adres = employee.adres;
      updateEmployee.mobile = employee.mobile;
      updateEmployee.groupId = employee.groupId;
      updateEmployee.birthDate = employee.birthDate;
      updateEmployee.city = employee.city;
      updateEmployee.rejon = employee.rejon;
      updateEmployee.indeks = employee.indeks;
      updateEmployee.imionaRodzicow = employee.imionaRodzicow;
      updateEmployee.nrWizy = employee.nrWizy;
      updateEmployee.koniecWaznosciWizy = employee.koniecWaznosciWizy;
      updateEmployee.nrOswiadczenia = employee.nrOswiadczenia;
      updateEmployee.nrZezwolenia = employee.nrZezwolenia;
      updateEmployee.koniecWaznosciZezwolenia = employee.koniecWaznosciZezwolenia;
      updateEmployee.poczatekWaznosciZezwolenia = employee.poczatekWaznosciZezwolenia;
      updateEmployee.dataZameldowania = employee.dataZameldowania;
      updateEmployee.dataWymeldowania = employee.dataWymeldowania;
      updateEmployee.pesel = employee.pesel;
      updateEmployee.nrPaszportu = employee.nrPaszportu;
      updateEmployee.koniecWaznosciPaszportu = employee.koniecWaznosciPaszportu;
      updateEmployee.rfid = employee.rfid;
      updateEmployee.active = employee.active;
      updateEmployee.isForeigner = employee.isForeigner;
      updateEmployee.version = employee.version;
      peopleService.updateEmployee(updateEmployee);
    };

    $scope.editWorkPeriods = function () {
      var employeeId = $route.current.params.employeeId;
      peopleService.getWorkPeriods(employeeId, function (workPeriods) {
        $scope.workPeriods = workPeriods;
      });
      $scope.editWorkPeriodsFlag = true;
    };

    $scope.saveWorkPeriods = function () {
      peopleService.updateTimeSheet($scope.workPeriods);
      $scope.editWorkPeriodsFlag = false;
    };

    $scope.cancelWorkPeriodsEdit = function () {
      $scope.editWorkPeriodsFlag = false;
    };

    $scope.$on('timeSheetUpdated', function () {
      var employeeId = $route.current.params.employeeId;

      peopleService.getWorkPeriods(employeeId, function (workPeriods) {
        $scope.workPeriods = workPeriods;
      });

      peopleService.getYearBars(employeeId, function (yearBars) {
        $scope.yearBars = yearBars;
      })

    });

    $scope.updateStartDate = function (workPeriodId, startDate) {
      $scope.workPeriods.forEach(function (item) {
        if (item.id == workPeriodId) {
          item.startDate = startDate;
        }
      });
    };

    $scope.updateEndDate = function (workPeriodId, endDate) {
      $scope.workPeriods.forEach(function (item) {
        if (item.id == workPeriodId) {
          item.endDate = endDate;
        }
      });
    };

    $scope.getMaxDate = function (index) {
      if ((index + 1) < $scope.workPeriods.length) {
        return $scope.workPeriods[index + 1].startDate;
      }
      return null;
    };

    $scope.getMinDate = function (index) {
      if (index > 0) {
        return $scope.workPeriods[index - 1].endDate;
      }
      return null;
    };


  }]);
