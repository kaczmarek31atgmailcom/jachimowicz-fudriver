'use strict';

angular.module('frontendApp').factory('leaderReportService', ['$rootScope','$http',
  function($rootScope,$http) {
    var leaderReportService = {};
/*
    leaderReportService.getReport = function(startDay,endDay,successFn) {
      $http.get( '/fudriver/rest/reportForLeader',{params: {startDate: toYYYYMMDD(startDay), endDate: toYYYYMMDD(endDay)}})
        .success(function (data) {
        successFn(data);
        $rootScope.$broadcast('leaderReportLoaded');
      }).error(function(result) {
          $rootScope.$broadcast('error', result.error);
      });
    };
*/

    leaderReportService.getReport = function(startDay,endDay,successFn) {
      $http.get( '/fudriver/rest/zarobki/reportForLeader/' +startDay +'/' + endDay)
        .success(function (data) {
          successFn(data);
          $rootScope.$broadcast('leaderReportLoaded');
        }).error(function(result) {
        $rootScope.$broadcast('error', result.error);
      });
    };


    function toYYYYMMDD(inputDate) {
      var date = new Date(inputDate);
      var yyyy = date.getFullYear().toString();
      var mm = (date.getMonth() + 1).toString(); // getMonth() is zero-based
      var dd = date.getDate().toString();
      return yyyy + (mm[1] ? mm : '0' + mm[0]) + (dd[1] ? dd : '0' + dd[0]);
    }


    return leaderReportService;
  }]);
