'use strict';
angular.module('frontendApp')
  .factory('scannerService', ['$rootScope', '$http', function ($rootScope, $http) {
    var service = {};

    var currentCycle = '';
    var currentLeader = '';
    var currentType = '';
    var currentTrolleyMan = '';
    var people = [];
    var supplierId = '';

    service.getPeople = function (successFn) {
      $http({
        url: '/fudriver/rest/wozek/people',
        method: 'GET'
      })
        .success(function (data) {
          successFn(data);
          $rootScope.$broadcast('peopleLoaded');
        })
        .error(function (result) {
          $rootScope.$broadcast('error', result.message);
        })
    };

    service.getTypes = function (successFn) {
      $http({
        url: '/fudriver/rest/wozek/type',
        method: 'GET'
      })
        .success(function (data) {
          successFn(data);
          $rootScope.$broadcast('TypesLoaded');
        })
        .error(function (result) {
          $rootScope.$broadcast('error', result.message);
        })
    };

    service.getCycles = function (successFn) {
      $http({
        url: '/fudriver/rest/wozek/cycle',
        method: 'GET'
      })
        .success(function (data) {
          successFn(data);
          $rootScope.$broadcast('CyclesLoaded');
        })
        .error(function (result) {
          $rootScope.$broadcast('error', result.message);
        })
    };

    /*
    service.getOrders = function (successFn) {
      $http({
        url: '/fudriver/rest/productionOrder/order-bars',
        method: 'GET',
        timeout: '500'
      })
        .success(function (data) {
          successFn(data);
          $rootScope.$broadcast('OrdersLoaded');
        })
        .error(function (result) {
          $rootScope.$broadcast('error', result);
        })
    };
*/

    /*zamówienia starego typu: */
    service.getOrders = function(successFn){
      $http({
        url: '/fudriver/rest/wozek/order',
        method: 'GET',
        timeout: '500'
      })
        .success(function (data) {
          successFn(data);
          $rootScope.$broadcast('OrdersLoaded');
        })
        .error(function (result) {
          $rootScope.$broadcast('error', result);
        })
    };

/* wersja ze skupem
    service.getOrdersForTable = function (successFn) {
      $http({
        url: '/fudriver/rest/productionOrder/scanner-order',
        method: 'GET',
        timeout: '500'
      })
        .success(function (data) {
          successFn(data);
          $rootScope.$broadcast('OrdersLoaded');
        })
        .error(function (result) {
          $rootScope.$broadcast('error', result);
        })
    };
*/
    service.getOrdersForTable = function (successFn) {
      $http({
        url: '/fudriver/rest/productionOrderLocal/scanner-order',
        method: 'GET',
        timeout: '500'
      })
        .success(function (data) {
          successFn(data);
          $rootScope.$broadcast('OrdersLoaded');
        })
        .error(function (result) {
          $rootScope.$broadcast('error', result);
        })
    };


    service.getSupplierId = function (successFn) {
      $http({
        url: '/fudriver/rest/config/supplierId',
        method: 'GET'
      })
        .success(function (data) {
          successFn(data);
          service.setSupplierId(data);
          $rootScope.$broadcast('SupplierIdLoaded');
        })
        .error(function (result) {
          $rootScope.$broadcast('error', result.message);
        })
    };


    service.commitBox = function (addWozekEntry) {
      $http({
        url: '/fudriver/rest/wozekEntry',
        method: 'POST',
        data: addWozekEntry,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      })
        .success(function (data) {
          $rootScope.$broadcast('boxCommitSuccess', data);
        })
        .error(function (result) {
          $rootScope.$broadcast('error', result);
        })
    };

    service.commitTrolley = function (command) {
      $http({
        url: '/fudriver/rest/wozek/commit',
        method: 'PUT',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      })
        .success(function (data) {
          $rootScope.$broadcast('trolleyCommitSuccess', data);
        })
        .error(function (result) {
          $rootScope.$broadcast('trolleyCommitError', result);
        })
    };

    service.commitEastMushroomsTrolleyStrightToWarehouse = function (command) {
      $http({
        url: '/fudriver/rest/east-warehouse/warehouse-palette/create-and-accept',
        method: 'POST',
        data: command,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      })
        .success(function (data) {
          $rootScope.$broadcast('MassCommitSuccess', data);
        })
        .error(function (result) {
          $rootScope.$broadcast('MassCommitError', result);
        })
    };


    service.onHold = function (trolleyId) {
      $http({
        url: '/fudriver/rest/wozek/onHold',
        method: 'PUT',
        data: trolleyId,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      })
        .success(function (data) {
          $rootScope.$broadcast('onHoldSucceed', data);
        })
        .error(function (result) {
          $rootScope.$broadcast('onHoldFailed', result);
        })
    };

    service.activateTrolley = function (trolleyId) {
      $http({
        url: '/fudriver/rest/wozek/activate',
        method: 'PUT',
        data: trolleyId,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      })
        .success(function (data) {
          $rootScope.$broadcast('trolleyActivated', data);
        })
        .error(function (result) {
          $rootScope.$broadcast('trolleyActivationError', result);
        })
    };

    service.removeBox = function (id) {
      $http({
        url: '/fudriver/rest/wozekEntry',
        method: 'DELETE',
        data: id,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      })
        .success(function () {
          $rootScope.$broadcast('boxRemoved');
        })
        .error(function (result) {
          $rootScope.$broadcast('error', result.message);
        })
    };

    service.removeTrolley = function (id) {
      $http({
        url: '/fudriver/rest/wozek/' + id,
        method: 'DELETE',
        data: id,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      })
        .success(function () {
          $rootScope.$broadcast('TrolleyRemoved');
        })
        .error(function (result) {
          $rootScope.$broadcast('error', result.message);
        })
    };


    service.getWozek = function (trolleyId, successFn) {
      $http({
        url: '/fudriver/rest/wozek/' + trolleyId,
        method: 'GET'
      })
        .success(function (data) {
          successFn(data);
          $rootScope.$broadcast('wozekLoaded');
        })
        .error(function (result) {
          $rootScope.$broadcast('error', result);
        })

    };

    service.getWozekHeaders = function (successFn) {
      $http({
        url: '/fudriver/rest/wozek/header',
        method: 'GET'
      })
        .success(function (data) {
          successFn(data);
          $rootScope.$broadcast('wozekHeadersLoaded')
        })
        .error(function (result) {
          $rootScope.$broadcast('error', result.message);
        })
    };

    service.getTrolleysSummary = function (successFn) {
      $http({
        url: '/fudriver/rest/wozek/trolleysSummary',
        method: 'GET'
      })
        .success(function (data) {
          successFn(data);
        })
        .error(function (result) {
          $rootScope.$broadcast('error', result.message);
        })
    };

    service.setCurrentCycle = function (currentCycle) {
      this.currentCycle = currentCycle;
    };

    service.getCurrentCycle = function () {
      return this.currentCycle;
    };

    service.setCurrentType = function (currentType) {
      this.currentType = currentType;
    };

    service.getCurrentType = function () {
      return this.currentType;
    };

    service.setCurrentLeader = function (currentLeader) {
      this.currentLeader = currentLeader;
    };

    service.getCurrentLeader = function () {
      return this.currentLeader;
    };

    service.setCurrentTrolleyMan = function (currentTrolleyMan) {
      this.currentTrolleyMan = currentTrolleyMan;
    };

    service.getCurrentTrolleyMan = function () {
      return this.currentTrolleyMan;
    };

    service.setPeople = function (people) {
      this.people = people;
    };

    service.loadPeople = function () {
      return this.people;
    };

    service.setSupplierId = function (supplierId) {
      this.supplierId = supplierId;
    };

    service.loadSupplierId = function () {
      return this.supplierId;
    };

    return service;
  }]);
