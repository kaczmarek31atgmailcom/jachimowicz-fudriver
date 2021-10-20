'use strict';

angular.module('frontendApp').factory('massHarvestService', ['$rootScope', '$http',
  function ($rootScope, $http) {
    var massHarvestService = {};

    massHarvestService.send = function (paleta) {
      $http({
        method: 'POST',
        url: '/fudriver/rest/mass-harvest',
        data: paleta,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8'
        }
      }).success(function (data) {
        $rootScope.$broadcast('paletaCreated', paleta.id);
      }).error(function (result) {
        $rootScope.$broadcast('error', result.message);
      });
    };
    return massHarvestService;
  }]);


angular.module('frontendApp').factory('massHarvestPromiseService', ['$rootScope', '$http', '$q',
  function ($rootScope, $http, $q) {
    var massHarvestDictionaryService = $q.defer();
    $q.all([
      $http.get('/fudriver/rest/mass-harvest/pickers'),
      $http.get('/fudriver/rest/mass-harvest/hale'),
      $http.get('/fudriver/rest/mass-harvest/types')
    ]).then(function (data) {
      var pickers = data[0].data;
      var hale = data[1].data;
      var types = data[2].data;

      massHarvestDictionaryService.resolve({
        'pickers': pickers,
        'hale': hale,
        'types': types
      });
    });

    return massHarvestDictionaryService.promise;
  }]);

