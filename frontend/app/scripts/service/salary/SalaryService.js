'use strict';

angular.module('frontendApp').
  factory('salaryService',['$rootScope','$http',function($rootScope,$http){
  var service = {};

  service.currentPerson = {};
  service.currentMonth = {};

  service.getHeaders = function (day,successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/salary/' + day,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.getPayedHeaders = function (dayId,successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/salary/payed/' + dayId,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.getPayedSeconds = function (personId,startDay,endDay,successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/person/workSeconds/personId/' + personId + '/startDay/' + startDay + '/endDay/' + endDay,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
    });
  };



  service.getMonths = function (successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/salary/payroll-month',
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.getBonuses = function (successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/salary/bonus/active',
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.getBonusAssignment = function (monthId,successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/salary/bonus/assignment/' + monthId,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.getSalaryAccountStatus = function (successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/salary/account/status',
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.getSalaryAccountHistory = function (personId,successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/salary/account/history/' + personId,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.salaryAccountPayoff = function(command){
    $http({
      url: '/fudriver/rest/salary/payment/payoff',
      method: 'POST',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    })
      .success(function(response,data) {
        $rootScope.$broadcast('PayoffCreated', response, data);
      })
      .error(function(result){
        $rootScope.$broadcast('error', result);
      })
  };

  service.salaryAccountPayment = function(command){
    $http({
      url: '/fudriver/rest/salary/payment/payment',
      method: 'POST',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    })
      .success(function(response,data) {
        $rootScope.$broadcast('PaymentCreated', response, data);
      })
      .error(function(result){
        $rootScope.$broadcast('error', result);
      })
  };

  service.updateBonusAssignment = function(command){
    $http({
      url: '/fudriver/rest/salary/bonus/assignment',
      method: 'POST',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    })
      .success(function(response,data) {
        $rootScope.$broadcast('bonusAssignmentUpdated', response, data);
      })
      .error(function(result){
        $rootScope.$broadcast('error', result);
      })
  };

  service.closeMonth = function(command){
    $http({
      url: '/fudriver/rest/salary/payroll-month',
      method: 'POST',
      data: command,
      timeout: 600000,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    })
      .success(function(response,data) {
        $rootScope.$broadcast('MonthClosed', response, data);
      })
      .error(function(result){
        $rootScope.$broadcast('error', result);
      })
  };

  service.getPayedHarvestDetails = function (personId,payoffDetailId,successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/salary/payed/details/harvest/' + personId + '/' + payoffDetailId,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.getPayedBonusDetails = function (personId,payoffDetailId,successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/salary/payed/details/bonus/' + personId + '/' + payoffDetailId,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
    });
  };

  service.getPayedTimeDetails = function (personId,payoffDetailId,successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/salary/payed/details/time/' + personId + '/' + payoffDetailId,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
    });
  };


  service.getSalaryMonthName = function (payrollDetailId,successFn) {
    $http({
      method: 'GET',
      url: '/fudriver/rest/salary/payroll-month/name/' + payrollDetailId,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
    });
  };


  return service;
}]);
