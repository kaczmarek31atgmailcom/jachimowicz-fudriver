'use strict';

angular.module('frontendApp')
  .controller('wageCtrl', ['$scope','growl', '$modal', 'wageService', function ($scope,growl, $modal, wageService) {


    $scope.getHeaders = function () {
      wageService.getHeaders(function (headers) {
        if (headers.length > 0) {
          $scope.activeHeaderId = headers[0].id;
          $scope.wages = $scope.getWages($scope.activeHeaderId);
        }
        $scope.headers = headers;

      })
    };
    $scope.getHeaders();

    $scope.getWages = function (headerId) {
      if((headerId != undefined) && (headerId != null)){
        wageService.getWages(headerId, function (wages) {
          $scope.wages = wages;
          var types = extractTypes(wages);
          $scope.types = setWages(types, wages);
        })
      }
    };

    $scope.setActiveHeader = function (id) {
      $scope.activeHeaderId = id;
      $scope.wages = $scope.getWages($scope.activeHeaderId);
    };

    $scope.openAddHeaderModal = function () {
      var addUserModal = $modal({
        animation: $scope.animationsEnabled,
        templateUrl: 'views/wage/addWageHeaderModal.html',
        show: true
      })
    };

    function extractTypes(wages) {
      var tmp = [];
      wages.forEach(function (wage) {
        var type = {};
        type.wageId = wage.id;
        type.typeId = wage.typeId;
        type.typeName = wage.typeName;
        type.typeWeight = wage.typeWeight;
        type.regularValue = 0;
        type.sundayValue = 0;
        type.bonusValue = 0;
        tmp[type.typeId] = type;
      });
      var types = [];
      tmp.forEach(function (item) {
        types.push(item);
      });
      return types;
    }

    function setWages(types, wages) {
      types.forEach(function (type) {
        type.regularValue = findValue(wages, type.typeId, "REGULAR_DAY");
        type.sundayValue = findValue(wages, type.typeId, "SUNDAY");
        type.bonusValue = findValue(wages, type.typeId, "BONUS_DAY");
      });

      return types;
    }

    function findValue(wages, typeId, dayType) {
      for (var i = 0; i < wages.length; i++) {
        if ((wages[i].typeId === typeId) && (wages[i].dayType === dayType)) {
          return (wages[i].value / 100).toString().replace(".",",");
        }
      }
      return 0;
    }

    $scope.updateRegularWage = function (typeId, value) {
      var wageId = findWageId($scope.wages, typeId, "REGULAR_DAY");
      var command = {};
      command.wageId = wageId;
      command.value = Math.round(value.toString().replace(",",".") * 100);
      wageService.updateWage(command);
    };

    $scope.updateSundayWage = function (typeId, value) {
      var wageId = findWageId($scope.wages, typeId, "SUNDAY");
      var command = {};
      command.wageId = wageId;
      command.value = Math.round(value.toString().replace(",",".") * 100);
      wageService.updateWage(command);
    };

    $scope.updateBonusWage = function (typeId, value) {
      var wageId = findWageId($scope.wages, typeId, "BONUS_DAY");
      var command = {};
      command.wageId = wageId;
      command.value = Math.round(value.toString().replace(",",".") * 100);
      wageService.updateWage(command);
    };

    function findWageId(wages, typeId, dayType) {
      for (var i = 0; i < wages.length; i++) {
        if ((wages[i].typeId === typeId) && (wages[i].dayType === dayType)) {
          return wages[i].id;
        }
      }
    }

    $scope.$on("WageUpdated", function () {
      $scope.getWages($scope.activeHeaderId);
      growl.success('wages.message.wage-updated', {ttl:3000})
    });

    $scope.$on("WageHeaderCreated", function () {
      $scope.getHeaders();
      growl.success('wages.message.wage-header-created', {ttl:3000})
    });


    $scope.$on("error",function(){
      growl.error("error",{ttl:5000});
    });



  }]);
//////////////////////////////

angular.module('frontendApp')
  .controller('AddWageHeaderModalCtrl', ['$scope', 'wageService', function ($scope, wageService) {

    $scope.addWageHeader = function () {
      console.log($scope.name);
      var command = {};
      command.name = $scope.name;
      wageService.addWageHeader(command);
    }

  }]);
