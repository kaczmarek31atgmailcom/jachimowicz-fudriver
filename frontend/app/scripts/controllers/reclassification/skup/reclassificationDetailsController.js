'use strict';

angular.module('frontendApp')
.controller('reclassificationDetailsCtrl', ['$scope', '$route', 'reclassificationService', function ($scope, $route, reclassificationService) {

    $scope.showError = false;
    $scope.showMessage = false;
    $scope.disabledKeys = false;
    $scope.message = "";
    $scope.details = [];
    $scope.types = [];
    $scope.header = {};


    $scope.getDetails = function () {
      var reclassificationId = $route.current.params.reclassifcationId;
        reclassificationService.getDetails(reclassificationId, function (details) {
            $scope.details = details;
        });
        reclassificationService.getActiveLocalTypes(function (types) {
            $scope.types = types;
        });
        $scope.header = reclassificationService.getHeader(reclassificationId, function (header) {
            $scope.header = header;
        });
    };

    $scope.valid = function () {
        var result = true;
        $scope.details.forEach(function (item) {
            if ((item.reclassificationTypeName == null) && item.active) {
                result = false;
            }
        });
        return result;
    };

    $scope.updateReclassificationType = function (reclassificationDetailId, data) {
        var changeAssignedReclassificationRodzaj = {};
        changeAssignedReclassificationRodzaj.reclassificationId = reclassificationDetailId;
        changeAssignedReclassificationRodzaj.typeId = data.id;
        reclassificationService.updateReclassificatoinRodzajRodzaj(changeAssignedReclassificationRodzaj);
        for (var i = 0; i < $scope.details.length; i++) {
            if ($scope.details[i].id == reclassificationDetailId) {
                $scope.details[i].reclassificationTypeName = data.name;
                $scope.details[i].reclassificationTypeWeight = data.weight;
            }
        }
    };


    $scope.turnOff = function (detail) {
        var command = {};
        command.id = detail.id;
        command.status = detail.active;
        reclassificationService.updateDetailStatus(command);
    };

    $scope.reclassify = function () {
        var command = {};
        command.id = $scope.header.id;
        reclassificationService.reclassify(command);
    };

    $scope.$on('reclassifictaion success',function(event,args){
        $scope.event = event;
        $scope.args = args;
        if(args.status == 'ERROR'){
            $scope.showMessage = false;
            $scope.showError = true;
            $scope.disabledKeys = true;
            $scope.message = 'reclassification.details.error.reclassification-already-processed';
        }
        if(args.status == 'OK'){
            $scope.showError=false;
            $scope.showMessage = true;
            $scope.disabledKeys = true;
            $scope.message = 'reclassification.details.message.reclassification-processed'
        }
    });

}]);

