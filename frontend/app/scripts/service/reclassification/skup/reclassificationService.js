'use strict';

angular.module('frontendApp').factory('reclassificationService', ['$rootScope', '$http',
    function ($rootScope, $http) {
        var reclassificationService = {};

        reclassificationService.getSkupTypes = function (successFn) {
            $http.get('/fudriver/rest/reclassification/rodzaj')
                .success(function (data) {
                    successFn(data);
                    $rootScope.$broadcast('Rodzaje ze skupu zaladowane');
                })
                .error(function (result) {
                    $rootScope.$broadcast('error', result.message);
                })
        };

        reclassificationService.getLocalTypes = function (successFn) {
            $http({
                url: '/fudriver/rest/reclassification/localRodzaj',
                method: 'GET',
                params: {active: false}
            })
                .success(function (data) {
                    successFn(data);
                    $rootScope.$broadcast('Lokalne Rodzaje zaladowane');
                })
                .error(function (result) {
                    $rootScope.$broadcast('error', result.message);
                })
        };


        reclassificationService.getActiveLocalTypes = function (successFn) {
            $http({
                url: '/fudriver/rest/reclassification/localRodzaj',
                params: {active: true},
                method: 'GET'
            })
                .success(function (data) {
                    params: {
                        active:true
                    }
                    successFn(data);
                    $rootScope.$broadcast('Lokalne Rodzaje zaladowane');
                })
                .error(function (result) {
                    $rootScope.$broadcast('error', result.message);
                })
        };


        reclassificationService.assignLocalRodzaj = function (changeRodzajCommand) {
            $http({
                url: '/fudriver/rest/reclassification/rodzaj/assign',
                method: 'PUT',
                data: changeRodzajCommand,
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            })
                .success(function (data) {
                    $rootScope.$broadcast('Rodzaj updated');
                })
                .error(function (result) {
                    $rootScope.$broadcast('error', result.message);
                })
        };

      reclassificationService.unassignLocalRodzaj = function (unassignRodzajCommand) {
        $http({
          url: '/fudriver/rest/reclassification/rodzaj/unassign',
          method: 'PUT',
          data: unassignRodzajCommand,
          headers: {
            'Content-Type': 'application/json; charset=UTF-8'
          }
        })
          .success(function (data) {
            $rootScope.$broadcast('Rodzaj updated');
          })
          .error(function (result) {
            $rootScope.$broadcast('error', result.message);
          })
      };


      reclassificationService.getProcessedHeaders = function (successFn) {
            $http.get('/fudriver/rest/reclassification/localHeader?processed=true')
                .success(function (data) {
                    successFn(data);
                    $rootScope.$broadcast("reclassification headers loaded");
                }).error(function (result) {
                $rootScope.$broadcast('error', result.message);
            })
        };

        reclassificationService.getNotProcessedHeaders = function (successFn) {
            $http.get('/fudriver/rest/reclassification/localHeader?processed=false')
                .success(function (data) {
                    successFn(data);
                    $rootScope.$broadcast("reclassification headers loaded");
                }).error(function (result) {
                $rootScope.$broadcast('error', result.message);
            })
        };


        reclassificationService.getHeader = function (headerId, successFn) {
            $http({
                url: '/fudriver/rest/reclassification/localHeader',
                params: {headerId: headerId},
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            })
                .success(function (data) {
                    successFn(data);
                    $rootScope.$broadcast("reclassification header loaded");
                }).error(function (result) {
                $rootScope.$broadcast('error', result.message);
            })
        };


        reclassificationService.getDetails = function (reclassificationId, successFn) {
            $http({
                url: '/fudriver/rest/reclassification/' + reclassificationId + '/details',
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            })
                .success(function (data) {
                    successFn(data);
                    $rootScope.$broadcast('details loaded');
                }).error(function (result) {
                $rootScope.$broadcast('error', result.message);
            })
        };

        reclassificationService.updateReclassificatoinRodzajRodzaj = function (changeRodzajCommand) {
            $http({
                url: '/fudriver/rest/reclassification/detail/rodzaj',
                method: 'PUT',
                data: changeRodzajCommand,
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            })
                .success(function (data) {
                    $rootScope.$broadcast('Reclassification rodzaj updated');
                })
                .error(function (result) {
                    $rootScope.$broadcast('error', result.message);
                })
        };

        reclassificationService.updateDetailStatus = function (command) {
            $http({
                url: '/fudriver/rest/reclassification/detail/rodzaj/activeStatus',
                method: 'PUT',
                data: command,
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            })
                .success(function (data) {
                    $rootScope.$broadcast('Detail status updated');
                })
                .error(function (result) {
                    $rootScope.$broadcast('error', result.message);
                })
        };

        reclassificationService.reclassify = function (command){
            $http({
                url: '/fudriver/rest/reclassification',
                method: 'POST',
                data: command,
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            })
                .success(function(data) {
                    $rootScope.$broadcast('reclassifictaion success', data);
                })
                .error(function(result){
                    $rootScope.$broadcast('error', result.message);
                })
        };

        return reclassificationService;
    }]);
