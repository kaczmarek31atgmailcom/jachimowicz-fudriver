'use strict';

angular.module('standRFIDApp')
    .controller('StandRFIDCtrl', ['$scope', '$location', '$timeout', 'StandRFIDService', function ($scope, $location, $timeout, StandRFIDService) {


        $scope.stats = '';
        $scope.focusInputText = function () {
            rfidForm.rfid.focus();
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
//380064fbb9

        $scope.parseRFID = function () {
            console.log($scope.rfid);
            if ($scope.rfid.length === 10) {
                var rfid = angular.copy($scope.rfid);
                StandRFIDService.getPersonMinutesRFID(rfid, function (minutes) {
                    $scope.totalMinutes = getTotalMinutes(minutes);
                    $scope.minutes = minutes;

                    StandRFIDService.getPersonStatsRFID(
                        rfid,
                        function (stats) {
                            $scope.stats = stats;
                            $scope.yesterday = stats.yesterday;
                            $scope.dayBeforeYestarday = stats.dayBeforeYestarday;
                            $scope.startPeriod = stats.startOfTheMonth;
                            $scope.endPeriod = stats.today;
                            var types = findTypes(stats);
                            $scope.types = setTypeValues(stats, types);
                            $scope.dayBeforeYesterdayTotalAmount = findDayBeforeYesterdayTotalAmount(stats);
                            $scope.dayBeforeYesterdayTotalWeight = findDayBeforeYesterdayTotalWeight(stats);
                            $scope.yesterdayTotalAmount = findYesterdayTotalAmount(stats);
                            $scope.yesterdayTotalWeight = findYesterdayTotalWeight(stats);
                            $scope.periodTotalAmount = findPeriodTotalAmount(stats);
                            $scope.periodTotalWeight = findPeriodTotalWeight(stats);
                        });
                    $scope.restartTimer();
                })
                $scope.rfid = '';
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
                type.dayBeforeYesterdayAmount = findDayBeforeYesterdayAmount(stats, type.id);
                type.dayBeforeYesterdayWeight = findDayBeforeYesterdayWeight(stats, type.id);
                type.yesterdayAmount = findYesterdayAmount(stats, type.id);
                type.yesterdayWeight = findYesterdayWeight(stats, type.id);
                type.periodAmount = findPeriodAmount(stats, type.id);
                type.periodWeight = findPeriodWeight(stats, type.id);
            });
            return types;
        }

        function findDayBeforeYesterdayAmount(stats, typeId) {
            for (var i = 0; i < stats.dayBeforeLastDayDetails.length; i++) {
                if (stats.dayBeforeLastDayDetails[i].typeId === typeId) {
                    return stats.dayBeforeLastDayDetails[i].amount;
                }
            }
            return 0;
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

        function findDayBeforeYesterdayWeight(stats, typeId) {
            for (var i = 0; i < stats.dayBeforeLastDayDetails.length; i++) {
                if (stats.dayBeforeLastDayDetails[i].typeId === typeId) {
                    return stats.dayBeforeLastDayDetails[i].weight;
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

        function findDayBeforeYesterdayTotalAmount(stats) {
            var result = 0;
            stats.dayBeforeLastDayDetails.forEach(function (item) {
                result += item.amount;
            });
            return result;
        }

        function findDayBeforeYesterdayTotalWeight(stats) {
            var result = 0;
            stats.dayBeforeLastDayDetails.forEach(function (item) {
                result += item.weight;
            });
            return result;
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

        $scope.minutesToHours = function (input) {
            if (input === undefined) {
                return 0
            } else {
                var hours = parseInt(input / 60);
                var minutes = input % 60;
                for (var i = hours.toString().length; i < 2; i++) {
                    hours = '0' + hours;
                }
                for (var j = minutes.toString().length; j < 2; j++) {
                    minutes = '0' + minutes;
                }
                return hours + ':' + minutes;
            }
        }

        function getTotalMinutes(minutes) {
            var result = 0;
            minutes.forEach(function (item) {
                result += item.minutes;
            })
            return result;
        }

    }]);
