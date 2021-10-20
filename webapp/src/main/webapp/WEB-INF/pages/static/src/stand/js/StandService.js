'use strict';

angular.module('standApp').factory('StandService', ['$rootScope','$http',
    function($rootScope,$http) {
        var service = {};

        service.getPersonStats = function (personId, yesterday, startPeriod, endPeriod,successFn) {
            $http({
                method: 'GET',
                url: '/fudriver/rest/stand/pickerStats/' + personId + '/' + yesterday + '/' + startPeriod +'/'+  endPeriod
            }).then(function successCallback(response,data){
                successFn(response.data);
            }, function errorCallback(response){
                $rootScope.$broadcast('error', response);
            });
        };

        return service;
    }]);

