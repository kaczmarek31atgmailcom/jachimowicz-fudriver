'use strict';
angular.module('frontendApp').service('reclassifyService', ['$rootScope', '$http', function ($rootScope, $http) {
  var service = {};

  service.getHeaders = function (dateFrom, dateTo, successFn) {
    $rootScope.$broadcast('loading');
    $http({
      method: 'GET',
      url: '/fudriver/rest/reclassify/paletaHeader',
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      },
      params: {'dateFrom': toYYYYMMDD(dateFrom), 'dateTo': toYYYYMMDD(dateTo)}
    }).then(function successCallback(response) {
      successFn(response.data);
      $rootScope.$broadcast('PaletaHeadersLoaded');
      $rootScope.$broadcast('loaded');
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
      $rootScope.$broadcast('loaded');
    });
  };

  function toYYYYMMDD(inputDate) {
    var date = new Date(inputDate);
    var yyyy = date.getFullYear().toString();
    var mm = (date.getMonth() + 1).toString(); // getMonth() is zero-based
    var dd = date.getDate().toString();
    return yyyy + (mm[1] ? mm : '0' + mm[0]) + (dd[1] ? dd : '0' + dd[0]);
  }

  service.getHeader = function (nr, successFn) {
    $rootScope.$broadcast('loading');
    $http({
      method: 'GET',
      url: '/fudriver/rest/reclassify/paletaHeader/' + nr,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
      $rootScope.$broadcast('PaletaHeaderLoaded');
      $rootScope.$broadcast('loaded');
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
      $rootScope.$broadcast('loaded');
    });
  };


  service.getDetails = function (nr, successFn) {
    $rootScope.$broadcast('loading');
    $http({
      method: 'GET',
      url: '/fudriver/rest/reclassify/paletaDetails/' + nr,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
      $rootScope.$broadcast('PaletaDetailsLoaded');
      $rootScope.$broadcast('loaded');
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
      $rootScope.$broadcast('loaded');
    });
  };

  service.getSupplier = function (successFn) {
    $rootScope.$broadcast('loading');
    $http({
      method: 'GET',
      url: '/fudriver/rest/config/supplierId',
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
      $rootScope.$broadcast('SupplierLoaded');
      $rootScope.$broadcast('loaded');
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
      $rootScope.$broadcast('loaded');
    });
  };

  service.getCycles = function (successFn) {
    $rootScope.$broadcast('loading');
    $http({
      method: 'GET',
      url: '/fudriver/rest/reclassify/cycles',
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
      $rootScope.$broadcast('CyclesLoaded');
      $rootScope.$broadcast('loaded');
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
      $rootScope.$broadcast('loaded');
    });
  };

  service.getTypes = function (successFn) {
    $rootScope.$broadcast('loading');
    $http({
      method: 'GET',
      url: '/fudriver/rest/reclassify/types',
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).then(function successCallback(response) {
      successFn(response.data);
      $rootScope.$broadcast('TypesLoaded');
      $rootScope.$broadcast('loaded');
    }, function errorCallBack(response) {
      $rootScope.$broadcast('error', response);
      $rootScope.$broadcast('loaded');
    });
  };



  service.reclassify = function (command) {
    $http({
      method: 'POST',
      url: '/fudriver/rest/localReclassify',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).success(function (data) {
      $rootScope.$broadcast('reclassificationDone',data);
    }).error(function (result) {
      $rootScope.$broadcast('error', result);
    });
  };


  service.delete = function (command) {
    $http({
      method: 'DELETE',
      url: '/fudriver/rest/localReclassify/zarobkiEntry',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).success(function (data) {
      $rootScope.$broadcast('EntryDeleted',data);
    }).error(function (result) {
      $rootScope.$broadcast('error', result);
    });
  };

/*
  rest.deleteAll = function (command) {
    $http({
      method: 'DELETE',
      url: '/fudriver/rest/localReclassify/palette',
      data: command,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8'
      }
    }).success(function (data) {
      $rootScope.$broadcast('PaletteDeleted',data);
    }).error(function (result) {
      $rootScope.$broadcast('error', result);
    });
  };
*/


  service.getReasons = function (successFn) {
    $http({
      url: '/fudriver/rest/reclassificaton/reclassifyReason',
      method: 'GET'
    })
      .success(function (data) {
        successFn(data);
        $rootScope.$broadcast('reasonsLoaded');
      })
      .error(function (result) {
        $rootScope.$broadcast('error', result.message);
      })
  };

  return service;

}]);
