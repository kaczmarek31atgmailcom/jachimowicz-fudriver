'use strict';

angular.module('frontendApp')
  .controller('salaryCtrl', ['$scope', 'growl', '$filter', '$location', 'salaryService', 'EventService',
    function ($scope, growl, $filter, $location, salaryService, EventService) {

      $scope.bonuses = [];
      $scope.payedBonuses = [];
      $scope.assignedBonuses = [];
      $scope.salary = [];

      $scope.init = function () {
        $scope.showSpinner = true;

        salaryService.getMonths(function (months) {
          $scope.months = months;
          if ((salaryService.currentMonth !== null) && (salaryService.currentMonth !== undefined) && (salaryService.currentMonth.monthId !== undefined)) {
            $scope.currentMonth = salaryService.currentMonth;
          } else {
            $scope.currentMonth = $scope.months[0];
          }
          $scope.getMonthHeaders();
        });
      };


      $scope.cantCloseMonth = function () {
        var result = false;
        if ($scope.currentMonth === undefined || $scope.currentMonth.month === undefined) {
          return true;
        }
        result = moment($scope.currentMonth.month).format('YYYY-MM') === moment().format('YYYY-MM');
        if ($scope.salary === undefined) {
          return true;
        }
        $scope.salary.forEach(function (person) {
          if (person.payrollType === 'ACCORD') {
            if (!(person.wageHeaderId > 0)) {
              result = true;
            }
          }
          if (person.payrollType === 'HOURLY') {
            if ((person.regularHoursWage === 0) || (person.sundayHoursWage === 0) || (person.bonusHoursWage === 0) || person.isValid === false) {
              result = true;
            }
          }
        });
        return result;
      };

      function getBonusAssignment() {
        salaryService.getBonusAssignment($scope.currentMonth.monthId, function (assignments) {
          var assignments = assignments;
          if ($scope.salary !== undefined) {
            $scope.salary.forEach(function (person) {
              person.bonuses = [];
              $scope.bonuses.forEach(function (bonus) {
                person.bonuses[bonus.id] = isBonusAssigned(person.id, bonus.id, assignments);
              });
              person = countNotPayedTotals(person);
            });
          }
        });

      }

      function isBonusAssigned(personId, bonusId, assignments) {
        for (var i = 0; i < assignments.length; i++) {
          if ((assignments[i].personId === personId) && (assignments[i].bonusId === bonusId)) {
            return true;
          }
        }
        return false;
      }

      $scope.getMonthHeaders = function () {
        $scope.showSpinner = true;
        if (!($scope.currentMonth.isClosed)) {
          salaryService.getHeaders($scope.currentMonth.month, function (salary) {
            $scope.salary = countTotalWorkTime(salary);
            salaryService.getBonuses(function (bonuses) {
              $scope.bonuses = bonuses;
              getBonusAssignment();
              countNotPayedTotals();
              $scope.salary.sort(salaryCompare);
            });

            $scope.showSpinner = false;
          });
          getBonusAssignment();
        } else {
          salaryService.getPayedHeaders($scope.currentMonth.monthId, function (salary) {
            $scope.payedBonuses = getPayedBonusHeaders(salary);
            var payedSalary = countTotalWorkTime(salary);
            salary.forEach(function (item) {
            });
            $scope.payedSalary = setPayedTotals(payedSalary);
            $scope.payedSalary.sort(comparePayedSalary);
            $scope.showSpinner = false;
          });
        }
      };

      function salaryCompare(a, b) {
        if (a.nr < b.nr)
          return -1;
        if (a.nr > b.nr)
          return 1;
        return 0;
      }

      function comparePayedSalary(a, b) {
        if (a.personNr < b.personNr)
          return -1;
        if (a.personNr > b.personNr)
          return 1;
        return 0;
      }

      function setPayedTotals(salary) {

        salary.forEach(function (item) {
          item.total = 0;
          if (item.payrollType === 'ACCORD') {
            item.total += item.regularHarvestSalary;
            item.total += item.sundayHarvestSalary;
            item.total += item.bonusHarvestSalary;
          }
          if (item.payrollType === 'HOURLY') {
            item.total += item.regularWorkTimeSalary;
            item.total += item.sundayWorkTimeSalary;
            item.total += item.bonusWorkTimeSalary;
          }
          item.bonuses.forEach(function (bonus) {
            item.total += bonus.moneyAmount;
          })
        });
        return salary;
      }

      function getPayedBonusHeaders(salary) {
        var tmp = [];
        salary.forEach(function (personSalary) {
          personSalary.bonuses.forEach(function (bonus) {
            tmp[bonus.bonusId] = bonus.bonusName;
          })
        });
        var bonusHeaders = [];
        tmp.forEach(function (item) {
          var bonus = [];
          bonus.id = tmp.indexOf(item);
          bonus.name = item;
          bonusHeaders.push(bonus);
        });
        return bonusHeaders;
      }

      $scope.getPayedBonusValue = function (person, bonusId) {
        var value = 0;
        for (var i = 0; i < person.bonuses.length; i++) {
          if (person.bonuses[i].bonusId === bonusId) {
            value = person.bonuses[i].moneyAmount;
            break;
          }
        }
        return value;
      };

      function countTotalWorkTime(salary) {
        salary.forEach(function (item) {
          item.totalMinutes = item.regularMinutes + item.sundayMinutes + item.bonusMinutes;
        });
        return salary;
      }

      $scope.minutesToHours = function (minutes) {
        return parseInt(minutes / 60) + ":" + minutes % 60;
      };

      $scope.totalSalary = function (person) {
        var result = 0;
        if (person.payrollType === 'ACCORD') {
          result += person.regularHarvestSalary;
          result += person.sundayHarvestSalary;
          result += person.bonusHarvestSalary;
        }
        if (person.payrollType === 'HOURLY') {
          result += person.regularHoursSalary;
          result += person.sundayHoursSalary;
          result += person.bonusHoursSalary;
        }
        return result;
      };

      $scope.updateBonusAssignment = function (personId, bonusId) {
        var command = {};
        command.personId = personId;
        command.bonusId = bonusId;
        command.monthId = $scope.currentMonth.monthId;
        salaryService.updateBonusAssignment(command);
      };

      var countNotPayedTotals = function (person) {
        if (person === undefined) {
          return;
        }
        person.total = 0;
        var totalBonus = 0;
        person.totalFixedBonus = 0;
        person.totalPercentageBonus = 0;
        person.totalSalaryPlusPercentageBonus = 0;
        if (person.payrollType === 'ACCORD') {
          person.total += person.regularHarvestSalary;
          person.total += person.sundayHarvestSalary;
          person.total += person.bonusHarvestSalary;
        }
        if (person.payrollType === 'HOURLY') {
          person.total += person.regularHoursSalary;
          person.total += person.sundayHoursSalary;
          person.total += person.bonusHoursSalary;
        }

        person.totalWithoutBonuses = person.total;

        if (person.bonuses !== undefined) {

          for (var i = 0; i < person.bonuses.length; i++) {
            if (person.bonuses[i]) {
              var bonus = findBonus(i);
              if (bonus.type === 'payroll_percentage_bonus') {
                person.totalPercentageBonus += Math.round((person.total * (bonus.param / 100)));
                totalBonus += Math.round((person.total * (bonus.param / 100)));
              }
              if (bonus.type === 'payroll_fixed_bonus') {
                person.totalFixedBonus += bonus.param;
                totalBonus += bonus.param;
              }
            }
          }
          person.total += totalBonus;
        }
        person.totalSalaryPlusPercentageBonus = person.totalWithoutBonuses + person.totalPercentageBonus;
        return person;
      };

      var findBonus = function (bonusId) {
        for (var i = 0; i < $scope.bonuses.length; i++) {
          if ($scope.bonuses[i].id === bonusId) {
            return $scope.bonuses[i];
            break;
          }
        }
      };

      $scope.closeMonth = function (monthId) {
        var command = {};
        command.monthId = monthId;
        $scope.showSpinner = true;
        salaryService.closeMonth(command);
      };

      $scope.showDetails = function (person) {
        salaryService.currentMonth = $scope.currentMonth;
        if (person.payrollType === 'ACCORD') {
          $location.url("/salary/harvestDetails/" + person.personId + '/' + person.payoffDetailId);
        }
        if (person.payrollType === 'HOURLY') {
          $location.url("/salary/workTimeDetails/" + person.personId + '/' + person.payoffDetailId);
        }
      };

      $scope.getTotalHarvest = function () {
        var total = 0;
        if ($scope.salary !== undefined) {
          $scope.salary.forEach(function (person) {
            total += person.totalHarvest;
          });
        }
        return $filter('longToWeight')(total);
      };

      $scope.getTotalWorkTime = function () {
        var total = 0;
        if ($scope.salary !== undefined) {
          $scope.salary.forEach(function (person) {
            total += person.totalMinutes;
          });
          return $scope.minutesToHours(total);
        }
      };

      $scope.getTotalRegularSalary = function () {
        var total = 0;
        if ($scope.salary !== undefined) {
          $scope.salary.forEach(function (person) {

            if (person.payrollType === 'ACCORD') {
              total += person.regularHarvestSalary;
            }
            if (person.payrollType === 'HOURLY') {
              total += person.regularHoursSalary;
            }

          })
        }
        return $filter('longToCurrency')(total);
      };

      $scope.getTotalSundaySalary = function () {
        var total = 0;
        if ($scope.salary !== undefined) {
          $scope.salary.forEach(function (person) {
            if (person.payrollType === 'ACCORD') {
              total += person.sundayHarvestSalary;
            }
            if (person.payrollType === 'HOURLY') {
              total += person.sundayHoursSalary;
            }
          })
        }

        return $filter('longToCurrency')(total);
      };

      $scope.getTotalBonusSalary = function () {
        var total = 0;
        if ($scope.salary !== undefined) {
          $scope.salary.forEach(function (person) {

            if (person.payrollType === 'ACCORD') {
              total += person.bonusHarvestSalary;
            }
            if (person.payrollType === 'HOURLY') {
              total += person.bonusHoursSalary;
            }

          })
        }
        return $filter('longToCurrency')(total);
      };

      $scope.getTotalSalary = function () {
        var total = 0;
        if ($scope.salary !== undefined) {
          $scope.salary.forEach(function (person) {

            if (person.payrollType === 'ACCORD') {
              total += person.regularHarvestSalary;
              total += person.sundayHarvestSalary;
              total += person.bonusHarvestSalary;
            }
            if (person.payrollType === 'HOURLY') {
              total += person.regularHoursSalary;
              total += person.sundayHoursSalary;
              total += person.bonusHoursSalary;
            }
          })
        }
        return $filter('longToCurrency')(total);
      };

      $scope.getTotalSalaryWithBonus = function () {
        var total = 0;
        if ($scope.salary !== undefined) {
          $scope.salary.forEach(function (person) {
            total += person.total;
          })
        }
        return $filter('longToCurrency')(total);
      };


      $scope.$on("bonusAssignmentUpdated", function (response, data) {
        getBonusAssignment();
      });

      $scope.$on("MonthClosed", function () {
        salaryService.getMonths(function (months) {
          $scope.months = months;
          var currentMonthId = $scope.currentMonth.monthId;
          $scope.months.forEach(function (month) {
            if (month.monthId === currentMonthId) {
              $scope.currentMonth = month;
            }
          });
          salaryService.getPayedHeaders($scope.currentMonth.monthId, function (salary) {
            $scope.payedBonuses = getPayedBonusHeaders(salary);
            var payedSalary = countTotalWorkTime(salary);
            $scope.payedSalary = setPayedTotals(payedSalary);
          })

        });
        $scope.showSpinner = false;
      });

      $scope.event = EventService.getEvents(function (response) {
        var response = JSON.parse(response);
        var message = response.source;

        if (message === 'NULL_PAYROLL_TYPE') {
          growl.error('payroll.salary.message.payroll-type-not-set', {ttl: '10000'});
          growl.error(response.id, {ttl: '10000'});
          $scope.showSpinner = false;
        }

        if (message === 'NULL_WORK_DATE') {
          growl.error('payroll.salary.message.payroll-null-work-date', {ttl: '10000'});
          growl.error(response.id, {ttl: '10000'});
          $scope.showSpinner = false;
        }


      });


    }]);

//////////////////////////////////

angular.module("frontendApp")
  .controller("PayedSalaryHarvestDetailsCtrl", ["$scope", "$routeParams", "peopleService", "salaryService",
    function ($scope, $routeParams, peopleService, salaryService) {
      $scope.personId = $routeParams.personId;
      $scope.totals = {};
      $scope.payoffDetailId = $routeParams.payoffDetailId;
      $scope.salaryTotal = 0;

      var init = function () {
        salaryService.getSalaryMonthName($scope.payoffDetailId, function (monthName) {

          $scope.firstDay = moment(monthName.monthName).format("YYYY/MM/DD");
          $scope.lastDay = moment(monthName.monthName).endOf('month').format("YYYY/MM/DD");

          $scope.firstDayRequest = moment(monthName.monthName).format("YYYY-MM-DD");
          $scope.lastDayRequest = moment(monthName.monthName).endOf('month').format("YYYY-MM-DD");

        });

        salaryService.getPayedHarvestDetails($scope.personId, $scope.payoffDetailId, function (details) {
          $scope.raw = details;
          var types = getTypes(details);
          $scope.details = assigneValuesToTypes(types, details);
          $scope.totals = getTotals($scope.details);


          salaryService.getPayedBonusDetails($scope.personId, $scope.payoffDetailId, function (bonuses) {
            $scope.bonuses = bonuses;
            $scope.bonusTotal = getBonusTotal(bonuses);

            $scope.salaryTotal = $scope.bonusTotal + $scope.totals.totalSalary;

            salaryService.getPayedSeconds($scope.personId,$scope.firstDayRequest,$scope.lastDayRequest,function(workTime){
              $scope.workTime = workTime.sort(compareWorkTime);
              $scope.workSecondsTotal = getTotalWorkTime(workTime);
            })

          });
        });



        peopleService.getEmployee($scope.personId, function (person) {
          $scope.person = person;
        })
      };
      init();

      var getTypes = function (details) {
        var tmp = [];
        var types = [];
        details.forEach(function (detail) {
          var type = {};
          type.id = detail.typeId;
          type.name = detail.typeName;
          type.weight = detail.typeWeight;
          type.regularWage = "";
          type.regularKgAmount = 0;
          type.regularMoneyAmount = 0;
          type.sundayWage = "";
          type.sundayKgAmount = 0;
          type.sundayMoneyAmount = 0;
          type.bonusWage = "";
          type.bonusKgAmount = 0;
          type.bonusMoneyAmount = 0;
          type.totalKg = 0;
          type.totalMoney = 0;
          type.totalMoney = 0;
          tmp[type.id] = type;
        });
        tmp.forEach(function (type) {
          types.push(type);
        });
        return types;
      };

      function assigneValuesToTypes(types, details) {
        types.forEach(function (type) {
          type.regularWage = getWage(type.id, "REGULAR_DAY", details);
          type.regularKgAmount = getKgAmount(type.id, "REGULAR_DAY", details);
          type.regularMoneyAmount = getMoneyAmount(type.id, "REGULAR_DAY", details);
          type.sundayWage = getWage(type.id, "SUNDAY", details);
          type.sundayKgAmount = getKgAmount(type.id, "SUNDAY", details);
          type.sundayMoneyAmount = getMoneyAmount(type.id, "SUNDAY", details);
          type.bonusWage = getWage(type.id, "BONUS_DAY", details);
          type.bonusKgAmount = getKgAmount(type.id, "BONUS_DAY", details);
          type.bonusMoneyAmount = getMoneyAmount(type.id, "BONUS_DAY", details);
          type.totalMoneyAmount = getTypeTotalMoneyAmount(type.id, details);
        });
        return types;
      }

      function getKgAmount(typeId, dayType, details) {
        for (var i = 0; i < details.length; i++) {
          if ((details[i].typeId === typeId) && (details[i].dayType === dayType)) {
            return details[i].kgAmount;
          }
        }
        return 0;
      }


      function getMoneyAmount(typeId, dayType, details) {
        for (var i = 0; i < details.length; i++) {
          if ((details[i].typeId === typeId) && (details[i].dayType === dayType)) {
            return details[i].moneyAmount;
          }
        }
        return 0;
      }

      function getWage(typeId, dayType, details) {
        for (var i = 0; i < details.length; i++) {
          if ((details[i].typeId === typeId) && (details[i].dayType === dayType)) {
            return details[i].wageValue;
          }
        }
        return "";
      }

      function getTypeTotalMoneyAmount(typeId, details) {
        var moneyAmount = 0;
        details.forEach(function (detail) {
          if (typeId === detail.typeId) {
            moneyAmount += detail.moneyAmount;
          }
        });
        return moneyAmount;
      }

      function getTotals(details) {
        var totals = {};
        totals.regularKg = 0;
        totals.regularSalary = 0;
        totals.sundayKg = 0;
        totals.sundaySalary = 0;
        totals.bonusKg = 0;
        totals.bonusSalary = 0;
        totals.totalSalary = 0;

        details.forEach(function (detail) {
          totals.regularKg += detail.regularKgAmount;
          totals.regularSalary += detail.regularMoneyAmount;
          totals.sundayKg += detail.sundayKgAmount;
          totals.sundaySalary += detail.sundayMoneyAmount;
          totals.bonusKg += detail.bonusKgAmount;
          totals.bonusSalary += detail.bonusMoneyAmount
          totals.totalSalary += detail.totalMoneyAmount;
        });
        return totals;
      }

      function getBonusTotal(bonuses) {
        var total = 0;
        bonuses.forEach(function (bonus) {
          total += bonus.moneyAmount;
        });
        return total;
      }

      function getTotalWorkTime(workTime){
        var result = 0;
        workTime.forEach(function(day){
          console.log(day);
          result += day.seconds;
        })
        return result;
      }

      function compareWorkTime(a,b){
        if(a.date > b.date){
          return 1;
        }
        if(a.date < b.date){
          return -1;
        }
        return 0;
      }

    }]);

//////////////////////////////////

angular.module("frontendApp")
  .controller("PayedSalaryTimeDetailsCtrl", ["$scope", "growl", "$routeParams", "peopleService", "salaryService",
    function ($scope, growl, $routeParams, peopleService, salaryService) {

      $scope.personId = $routeParams.personId;
      $scope.payoffDetailId = $routeParams.payoffDetailId;

      var init = function () {
        salaryService.getPayedTimeDetails($scope.personId, $scope.payoffDetailId, function (salary) {
          $scope.salary = salary;
        });
        init;
      }

    }]);
