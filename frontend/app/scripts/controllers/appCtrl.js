'use strict';


/**
 * @ngdoc function
 * @name frontendApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the frontendApp
 */

angular.module('frontendApp')
  .controller('AppCtrl', ['$scope', '$rootScope', '$interval', '$http', '$location', '$window','appService',
    function ($scope, $rootScope, $interval, $http, $location, $window, appService) {

      $scope.privileges = {};
      $scope.userDetails = {};
      $scope.showMassHarvest = false;
      $scope.activeBrc = false;
      $scope.activeEastMushrooms = false;
      $scope.isWarehouseOrdersActive = false;

      $interval(function () {
        $http({
          method: 'GET',
          url: '/fudriver/rest/current-user',
          timeout: '10000'
        }).then(function successCallback(data) {
          if (!data.data.name) {
            var url = "http://" + $window.location.host + "/fudriver/login";
            $window.location.href = url;
          }
        }, function errorCallback() {
          var url = "http://" + $window.location.host + "/fudriver/login";
          $window.location.href = url;
        });
      },50000);


      $scope.setPrivileges = function () {
        for (var key in $scope.userDetails) {
          if ($scope.userDetails.hasOwnProperty(key)) {
            if (key === 'authorities') {
              for (var authority in $scope.userDetails[key]) {
                $scope.privileges[$scope.userDetails[key][authority].authority] = true;
              }
            }
          }
        }
      };

      var authenticate = function (successFn) {
        $http({
          method: 'GET',
          url: '/fudriver/rest/current-user'
        }).then(function successCallback(response, data) {
          if (response.data.name) {
            $rootScope.authenticated = true;
          } else {
            $rootScope.authenticated = false;
          }
          $scope.userDetails = response.data;
          $scope.setPrivileges();
          successFn && successFn();
        }, function errorCallback() {
          $rootScope.authenticated = false;
          successFn && successFn();
        });
      };


      authenticate();
      $scope.credentials = {};
      $scope.login = function () {
        $http.post('fudriver/spring_security_login', $.param($scope.credentials), {
          headers: {
            'content-type': 'application/x-www-form-urlencoded'
          }
        }).success(function () {
          authenticate(function () {
            if ($rootScope.authenticated) {
              $location.path('index.html');
              $scope.error = false;
            } else {
              $location.path('index.html');
              $scope.error = true;
            }
          });
        }).error(function () {
          $location.path('index.html');
          $scope.error = true;
          $rootScope.authenticated = false;
        });
      };

      $scope.logout = function () {
        $http.post('logout', {}).success(function () {
          $rootScope.authenticated = false;
          $scope.privileges = {};
          $scope.userDetails = {};
          //$location.path('j_spring_security_logout');
          $location.path('/logout');
        }).error(function (x) {
          $rootScope.authenticated = false;
          $scope.privileges = {};
          $scope.userDetails = {};
        });
      };


      var getMassHarvestVisibility = function () {
        appService.getMassHarvest(function (response) {
          $scope.showMassHarvest = response;
        });
      };
      getMassHarvestVisibility();


      var isBrcActive = function () {
        appService.getActiveBrc(function (response) {
          $scope.activeBrc = response;
        });
      };
      isBrcActive();

      var isEastMushroomsActive = function () {
        appService.getActiveEastMushrooms(function (response) {
          $scope.activeEastMushrooms = response;
        });
      };
      isEastMushroomsActive();

      var isWarehouseOrdersActive = function () {
        appService.getActiveEastMushrooms(function (response) {
          $scope.isWarehouseOrdersActive = response;
        });
      };
      isEastMushroomsActive();


      var getVersion = function () {

        appService.getVersion(function (response) {
          var strResponse = JSON.stringify(response);
          var json = jQuery.parseJSON(strResponse);
          jQuery.each(json, function (i, val) {

            if (i == 'git.commit.id') {
              $scope.version = val;
            }
          });

        })
      };
      getVersion();

    }]);




