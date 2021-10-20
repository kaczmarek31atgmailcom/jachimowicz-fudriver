'use strict';

angular.module('frontendApp')
  .controller('reclassifyReasonCtrl', ['$scope', '$modal', 'reclassifyReasonService', function ($scope, $modal, reclassifyReasonService) {

    $scope.init = function () {
      reclassifyReasonService.getReasons(function (reasons) {
        $scope.reasons = toList(reasons);
      })
    };


    var toList = function (map) {
      var output = []
      for (var key in map) {
        var reason = {"id": key, "description": map[key]};
        output.push(reason);
      }
      return output;
    };


    $scope.addReason = function () {
      var addReason = $modal({
        scope: $scope,
        animation: $scope.animationsEnabled,
        templateUrl: 'views/settings/modals/add-reclassify-reason-modal.html',
        show: true
      })
    };


    $scope.saveReason = function () {
      var command = {};
      command.description = $scope.description;
      reclassifyReasonService.saveReason(command)
    };

    $scope.$on('ReasonCreated', function () {
      reclassifyReasonService.getReasons(function (reasons) {
        var reasons = toList(reasons);
        $scope.refreshReasonList(reasons);
      })
    });

    $scope.refreshReasonList = function (reasons) {
      var found = false;
      for (var i = 0; i < reasons.length; i++) {
        for (var j = 0; j < $scope.reasons.length; j++)
          if ($scope.reasons[j].id === reasons[i].id) {
            found = true;
            break;
          }
        if (!found) {
          $scope.reasons.push(reasons[i]);
        }
        found = false;
      }

      var found = false;
      for (var i = 0; i < $scope.reasons.length; i++) {
        for (var j = 0; j < reasons.length; j++) {
          if ($scope.reasons[i].id === reasons[j].id) {
            found = true;
            break;
          }
        }
        if (!found) {
          $scope.reasons.splice(i, 1);
        }
        found = false;
      }

      return $scope.reasons;
    };

    $scope.removeReason = function (reasonId) {
      var command = {};
      command.id = reasonId;
      reclassifyReasonService.removeReason(command);
    };

    $scope.$on('ReasonRemoved',function(response,data){
      for(var i = 0; i< $scope.reasons.length; i++){
        if(parseInt($scope.reasons[i].id) === parseInt(data.data.entityId)){
          $scope.reasons.splice(i,1);
        }
      }
    });

  }]);
