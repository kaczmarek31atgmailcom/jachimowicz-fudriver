'use strict';

angular.module('frontendApp')
.factory('CommonService',['$rootScope','$http',function($rootScope,$http){
  var service ={};

  service.getProducerId = function (successFn) {
    $http({
      url: '/fudriver/rest/config/supplierId',
      method: 'GET'
    })
      .success(function (data) {
        successFn(data);
        $rootScope.$broadcast('SupplierIdLoaded');
      })
      .error(function (result) {
        $rootScope.$broadcast('error', result.message);
      })
  };

  return service;
}]);
