'use strict';

angular.module('timerApp').factory('timerService', ['$rootScope','$http',
    function($rootScope,$http) {
        var service = {};

        service.getPerson = function (personId, successFn) {
            $http({
                method: 'GET',
                url: '/fudriver/rest/workTimeRecorder/person/' + personId
            }).then(function successCallback(response,data){
                successFn(response.data);
                $rootScope.$broadcast('peopleHeadersLoaded');
            }, function errorCallback(response){
                $rootScope.$broadcast('error', response);
            });
        };

        service.getCurrentTime = function (successFn) {
            $http({
                method: 'GET',
                url: '/fudriver/rest/currtime'
            }).then(function successCallback(response,data){
                successFn(response.data);
            }, function errorCallback(response){
                $rootScope.$broadcast('error', response);
            });
        };


        service.registerBadgeEvent = function(command,successFn){
            $http({
                method: 'POST',
                url: '/fudriver/rest/workTimeRecorder/badge-event',
                data: command,
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).then(function successCallback(data){
                successFn(data);
            }, function errorCallback(data){
                $rootScope.$broadcast('error', data);
            });
        };

        service.registerInputBadgeEvent = function(command,successFn){
            $http({
                method: 'POST',
                url: '/fudriver/rest/workTimeRecorder/open-only-badge-event',
                data: command,
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).then(function successCallback(data){
                successFn(data);
            }, function errorCallback(data){
                $rootScope.$broadcast('error', data);
            });
        };

        service.registerOutputBadgeEvent = function(command,successFn){
            $http({
                method: 'POST',
                url: '/fudriver/rest/workTimeRecorder/close-only-badge-event',
                data: command,
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).then(function successCallback(data){
                successFn(data);
            }, function errorCallback(data){
                $rootScope.$broadcast('error', data);
            });
        };


        return service;
}]);

/////////


angular.module('timerApp')
    .factory('EventService',['$rootScope',function($rootScope){
        var service ={};
        var source = new EventSource('/fudriver/events/connect');

        service.getEvents = function(callback){
            source.addEventListener("message",function(e){
                callback(e.data);
            });
        };
        return service;
    }]);
