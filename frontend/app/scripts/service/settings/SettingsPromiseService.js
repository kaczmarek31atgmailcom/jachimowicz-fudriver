'use strict';
angular.module('frontendApp').factory('settingsPromiseService', ['$rootScope', '$http', '$q', function ($rootScope, $http, $q) {
  var service = $q.defer();
  $q.all([
    $http.get('/fudriver/rest/settings/massHarvest'),
    $http.get('/fudriver/rest/settings/requiredLeader'),
    $http.get('/fudriver/rest/settings/requiredTrolleyMan'),
    $http.get('/fudriver/rest/settings/east-mushrooms-mode')
  ]).then(function (data) {
    var massHarvest = data[0].data,
      requiredLeader = data[1].data,
      requiredTrolleyMan = data[2].data,
      eastMushroomsMode = data[3].data;
    service.resolve({
      'massHarvest': massHarvest,
      'requiredLeader': requiredLeader,
      'requiredTrolleyMan': requiredTrolleyMan,
      'eastMushroomsMode': eastMushroomsMode
    });
  });
  return service.promise;

}]);
