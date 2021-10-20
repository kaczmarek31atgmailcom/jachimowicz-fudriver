'use strict';

angular.module('frontendApp')
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
