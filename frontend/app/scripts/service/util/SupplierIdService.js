'use strict';
angular.module('frontendApp').factory('SupplierIdService', ['$rootScope', '$http', '$q',
  function ($rootScope, $http, $q) {
    var service = $q.defer();

    $q.all([
      $http.get('/fudriver/rest/config/supplierId')
    ]).then(function (data) {
      var supplierId = data[0].data;
      service.resolve({
        'supplierId': supplierId
      });
    });

    return service.promise;
  }]);
