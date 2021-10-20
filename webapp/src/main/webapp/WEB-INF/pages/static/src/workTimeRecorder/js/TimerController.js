angular.module('timerApp')
    .controller('timerCtrl', ['$scope','growl','timerService', function ($scope,growl,timerService) {

        $scope.person = '';
        $scope.displayTime = {};
/*
        $scope.event = EventService.getEvents(function (response) {
            var response = JSON.parse(response);
            if(response.source === "TIME_MINUTE_CHANGED") {
                $scope.currentTime = response.message;
                $scope.$apply();
            }
        });
*/

        $scope.focusInputText = function () {
            setTimeout(function () {
                badgeForm.badge.focus();
            }, 10);
        };


        function init(){
            timerService.getCurrentTime(function(time){
                $scope.currentTime = moment(time);
                startClock();
            })
            $scope.focusInputText();
        }
        init();

        function startClock(){
            var startTimer = setInterval(function(){
                timer()
            },1000);

            var timeUpdater = setInterval(function(){

                    timerService.getCurrentTime(function(time){
                        if(time != null){
                            clearInterval(startTimer);
                            $scope.currentTime = moment(time);
                            startTimer = setInterval(function(){
                                timer()
                            },1000);
                        }
                    })

                },60000)
            }





        function timer(){
            $scope.currentTime.add(1,'second');
            $scope.displayTime = $scope.currentTime.format("HH:mm:ss");
            $scope.$apply();

        }


        $scope.parseBadge = function (keyEvent) {
            if (keyEvent.which === 13) {
                sendBadge($scope.badge);

                $scope.badge = '';
            }
        };


        var sendBadge = function(badge){
            var command = {};
            command.rfid = angular.copy(badge);
            timerService.registerBadgeEvent(command,function(person){
                setPerson(person.data)
            });
        };

        var setPerson = function (person) {
            console.log(person);
            if(person.personId === 0){
                growl.error("Identyfikator nie przypisany do żadnej osoby ", {ttl:5000});
            }
            if((person.personId > 0) && (person.active)){
                growl.success("Rozpoczęcie pracy " + person.name + " " + person.surname, {ttl:5000});
            }
            if((person.personId > 0) && (!(person.active))){
                growl.warning("Zakończenie pracy " + person.name + " " + person.surname, {ttl:5000});
            }
        };
    }]);
//////////////////////////////////////////
angular.module('timerApp')
    .controller('timerInputCtrl', ['$scope','growl','timerService','EventService', function ($scope,growl,timerService,EventService) {

        $scope.person = '';
        $scope.start = false;
        $scope.end = false;
        $scope.connectionError = true;
        $scope.noSuchPerson = false;
        $scope.currentTime = {};

        var lastBadge = '';

/*
        $scope.event = EventService.getEvents(function (response) {
            var response = JSON.parse(response);
            if(response.source === "TIME_MINUTE_CHANGED") {
                $scope.currentTime = response.message;
                $scope.$apply();
            }
        });
*/
        function init(){
            console.log("in init");
            $scope.currentTime = timerService.getCurrentTime(function(time){
                $scope.currentTime = time;
                startClock();
            })
            $scope.focusInputText();
        }
        init();

        function startClock(){
            console.log('startClock: ' + $scope.currentTime);
            setInterval(function(){
                $scope.currentTime = moment($scope.currentTime).add(1,'second');
            })
        }

        $scope.parseBadgeInput = function (keyEvent) {
            if (keyEvent.which === 13) {
                sendBadgeInput($scope.badge);
                lastBadge = $scope.badge;
                $scope.badge = '';
            }
        };

        $scope.focusInputText = function () {
            setTimeout(function () {
                badgeForm.badge.focus();
            }, 10);
        };

        var sendBadge = function(badge){
            var command = {};
            command.rfid = angular.copy(badge);
            timerService.registerBadgeEvent(command,function(person){
                setPerson(person.data)
            });
        };


        var sendBadgeInput = function(badge){
            var command = {};
            command.rfid = angular.copy(badge);
            timerService.registerInputBadgeEvent(command,function(person){
                setPerson(person.data)
            });
        };

        var sendBadgeOutput = function(badge){
            var command = {};
            command.rfid = angular.copy(badge);
            timerService.registerOutputBadgeEvent(command,function(person){
                setPerson(person.data)
            });
        };


        var setPerson = function (person) {

            if(person.personId === 0){
                growl.error("Identyfikator nie przypisany do żadnej osoby ", {ttl:5000});
            }
            console.log(person.isStateUpdated + " " + person.personId);
            if((person.personId > 0) && (person.isStateUpdated)){
                growl.success("Rozpoczęcie pracy " + person.name + " " + person.surname, {ttl:5000});
            }
            if((person.personId > 0) && (!(person.isStateUpdated))){
                growl.error("Powtórne skanowianie " + person.name + " " + person.surname, {ttl:5000});
            }
        };

        $scope.$on('error',function(){
            growl.error("Błąd łączenia z serwerem", {ttl:2000});
        })
    }]);





//////////////////////////////////////////
angular.module('timerApp')
    .controller('timerOutputCtrl', ['$scope','growl','timerService','EventService', function ($scope,growl,timerService,EventService) {

        $scope.person = '';
        $scope.start = false;
        $scope.end = false;
        $scope.connectionError = true;
        $scope.noSuchPerson = false;
        $scope.currentTime = {};

        var lastBadge = '';


        $scope.event = EventService.getEvents(function (response) {
            var response = JSON.parse(response);
            if(response.source === "TIME_MINUTE_CHANGED") {
                $scope.currentTime = response.message;
                $scope.$apply();
            }
        });


        $scope.parseBadgeOutput = function (keyEvent) {
            if (keyEvent.which === 13) {
                sendBadgeOutput($scope.badge);
                lastBadge = $scope.badge;
                $scope.badge = '';
            }
        };



        $scope.focusInputText = function () {
            setTimeout(function () {
                badgeForm.badge.focus();
            }, 10);
        };

        var sendBadge = function(badge){
            var command = {};
            command.rfid = angular.copy(badge);
            timerService.registerBadgeEvent(command,function(person){
                setPerson(person.data)
            });
        };


        var sendBadgeInput = function(badge){
            var command = {};
            command.rfid = angular.copy(badge);
            timerService.registerInputBadgeEvent(command,function(person){
                setPerson(person.data)
            });
        };

        var sendBadgeOutput = function(badge){
            var command = {};
            command.rfid = angular.copy(badge);
            timerService.registerOutputBadgeEvent(command,function(person){
                setPerson(person.data)
            });
        };


        var setPerson = function (person) {

            if(person.personId === 0){
                growl.error("Identyfikator nie przypisany do żadnej osoby ", {ttl:5000});
            }
            console.log(person.isStateUpdated + " " + person.personId);
            if((person.personId > 0) && (person.isStateUpdated)){
                growl.success("Zakończenie pracy " + person.name + " " + person.surname, {ttl:5000});
            }
            if((person.personId > 0) && (!(person.isStateUpdated))){
                growl.error("Powtórne skanowianie " + person.name + " " + person.surname, {ttl:5000});
            }
        };
    }]);
