'use strict';
angular.module('frontendApp').factory('cycleDictionaryServicePromise', ['$rootScope', '$http', '$q',
  function ($rootScope, $http, $q) {
    var cycleDictionaryService = $q.defer();
    $q.all([
      $http.get('/fudriver/rest/dictionary/kompostownie'),
      $http.get('/fudriver/rest/dictionary/grzybnie'),
      $http.get('/fudriver/rest/dictionary/torfy'),
      $http.get('/fudriver/rest/dictionary/dokarmiacz-podloza'),
      $http.get('/fudriver/rest/dictionary/dokarmiacz-okrywy'),
      $http.get('/fudriver/rest/dictionary/stopien-ubicia'),
      $http.get('/fudriver/rest/dictionary/kaking')
    ]).then(function (data) {
      var kompostownie = data[0].data,
        grzybnie = data[1].data,
        torfy = data[2].data,
        dok_pod = data[3].data,
        dok_okr = data[4].data,
        stopien_ubicia = data[5].data,
        kaking = data[6].data;

      cycleDictionaryService.resolve({
        'kompostownie': kompostownie,
        'grzybnie': grzybnie,
        'torfy': torfy,
        'dok_pod': dok_pod,
        'dok_okr': dok_okr,
        'stopien_ubicia': stopien_ubicia,
        'kaking': kaking
      });
    });

    return cycleDictionaryService.promise;
  }]);
