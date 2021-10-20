'use strict';

angular.module('frontendApp')
    .controller('reclassificationHistoryDetailsCtrl', ['$scope', '$route', 'reclassificationService', function ($scope, $route, reclassificationService) {

        $scope.getDetails = function () {
            var reclassificationId = $route.current.params.reclassifcationId;
            reclassificationService.getDetails(reclassificationId, function (details) {
                $scope.details = details;
            });
            $scope.header = reclassificationService.getHeader(reclassificationId, function (header) {
                $scope.header = header;
            });
        };


    }]);