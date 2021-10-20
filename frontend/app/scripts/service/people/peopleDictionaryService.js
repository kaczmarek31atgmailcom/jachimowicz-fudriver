'use strict';
angular.module('frontendApp').factory('peopleDictionaryServicePromise', ['$rootScope', '$http', '$q',
  function ($rootScope, $http, $q) {
    var peopleDictionaryService = $q.defer();
    $q.all([
      $http.get('/fudriver/rest/config/supplierId')
    ]).then(function (data) {
      var supplierId = data[0].data;
      peopleDictionaryService.resolve({
        'supplierId': supplierId
      });
    });

    return peopleDictionaryService.promise;
  }]);
