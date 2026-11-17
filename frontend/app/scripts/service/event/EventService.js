'use strict';

angular.module('frontendApp')
  .factory('EventService',['$rootScope',function($rootScope){
    var service ={};
    var source = new EventSource('/fudriver/events/connect');

    service.getEvents = function(callback){
      var listener = function (e) { callback(e.data); };
      source.addEventListener("message", listener);
      return function () { source.removeEventListener("message", listener); };
    };
    return service;
  }]);
