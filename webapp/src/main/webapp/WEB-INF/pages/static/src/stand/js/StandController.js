'use strict';

angular.module('standApp')
    .controller('StandCtrl', ['$scope', '$location', '$timeout', 'StandService', function ($scope, $location, $timeout, StandService) {

        $scope.yesterday = moment().subtract(1, 'days').format('YYYY-MM-DD');
        $scope.startPeriod = moment().startOf('month').format('YYYY-MM-DD');
        $scope.endPeriod = moment().format('YYYY-MM-DD');
        $scope.stats = '';
        $scope.focusInputText = function () {
            barForm.barcode.focus();
        };

        var customTimeout = $timeout(function () {
            $scope.stats = '';
        }, 20000);


        $scope.restartTimer = function () {
            $timeout.cancel(customTimeout);
            customTimeout = $timeout(function () {
                $scope.stats = '';
            }, 20000);
        };


        $scope.parseBarcode = function () {
            if ($scope.barcode.length == 13) {
                StandService.getPersonStats(
                    $scope.barcode.substr(8, 5),
                    $scope.yesterday,
                    $scope.startPeriod,
                    $scope.endPeriod,
                    function (stats) {
                        $scope.stats = stats;
                        var types = findTypes(stats);
                        $scope.types = setTypeValues(stats, types);
                        $scope.yesterdayTotalAmount = findYesterdayTotalAmount(stats);
                        $scope.yesterdayTotalWeight = findYesterdayTotalWeight(stats);
                        $scope.periodTotalAmount = findPeriodTotalAmount(stats);
                        $scope.periodTotalWeight = findPeriodTotalWeight(stats);
                        $scope.restartTimer();
                    });
                $scope.barcode = '';
            }
        };

        function findTypes(stats) {
            var tmp = [];
            stats.lastDayDetails.forEach(function (lastDayDetail) {
                var type = {};
                type.id = lastDayDetail.typeId;
                type.name = lastDayDetail.typeName;
                type.weight = lastDayDetail.typeWeight;
                tmp[type.id] = type;
            });
            stats.periodDetails.forEach(function (periodDetail) {
                var type = {};
                type.id = periodDetail.typeId;
                type.name = periodDetail.typeName;
                type.weight = periodDetail.typeWeight;
                tmp[type.id] = type;
            });
            var types = [];
            tmp.forEach(function (type) {
                types.push(type);
            });
            return types;
        }

        function setTypeValues(stats, types) {
            types.forEach(function (type) {
                type.yesterdayAmount = findYesterdayAmount(stats, type.id);
                type.yesterdayWeight = findYesterdayWeight(stats, type.id);
                type.periodAmount = findPeriodAmount(stats, type.id);
                type.periodWeight = findPeriodWeight(stats, type.id);
            });
            return types;
        }

        function findYesterdayAmount(stats, typeId) {
            for (var i = 0; i < stats.lastDayDetails.length; i++) {
                if (stats.lastDayDetails[i].typeId === typeId) {
                    return stats.lastDayDetails[i].amount;
                }
            }
            return 0;
        }

        function findPeriodAmount(stats, typeId) {
            for (var i = 0; i < stats.periodDetails.length; i++) {
                if (stats.periodDetails[i].typeId === typeId) {
                    return stats.periodDetails[i].amount;
                }
            }
            return 0;
        }

        function findYesterdayWeight(stats, typeId) {
            for (var i = 0; i < stats.lastDayDetails.length; i++) {
                if (stats.lastDayDetails[i].typeId === typeId) {
                    return stats.lastDayDetails[i].weight;
                }
            }
            return 0;
        }

        function findPeriodWeight(stats, typeId) {
            for (var i = 0; i < stats.periodDetails.length; i++) {
                if (stats.periodDetails[i].typeId === typeId) {
                    return stats.periodDetails[i].weight;
                }
            }
            return 0;
        }

        function findYesterdayTotalAmount(stats) {
            var result = 0;
            stats.lastDayDetails.forEach(function (item) {
                result += item.amount;
            });
            return result;
        }

        function findYesterdayTotalWeight(stats) {
            var result = 0;
            stats.lastDayDetails.forEach(function (item) {
                result += item.weight;
            });
            return result;
        }

        function findPeriodTotalAmount(stats) {
            var result = 0;
            stats.periodDetails.forEach(function (item) {
                result += item.amount;
            });
            return result;
        }

        function findPeriodTotalWeight(stats) {
            var result = 0;
            stats.periodDetails.forEach(function (item) {
                result += item.weight;
            });
            return result;
        }

    }]);
