'use strict';
angular.module('frontendApp')
  .controller('trolleyDetailsCtrl', ['$scope', 'growl', '$location', '$route', 'scannerService','settingsPromiseService',
    function ($scope, growl, $location, $route, scannerService,settingsPromiseService) {

    $scope.showRemoveAllButton = false;

      settingsPromiseService.then(function (settingsService) {
        $scope.eastMode = settingsService.eastMushroomsMode;
      });

      $scope.wozek = [];
      var boxIdToBeRemoved = '';
      var trolleyId = $route.current.params.trolleyId;

      $scope.loadData = function () {
        $scope.people = scannerService.loadPeople();

          scannerService.getPeople(function (people) {
            $scope.people = people;
            $scope.showRemoveAllButton = true;
          });



        $scope.removeTrolley = function (id) {
          $scope.showRemoveAllButton = false;
          scannerService.removeTrolley(trolleyId);
        };

        $scope.supplierId = scannerService.loadSupplierId();
        if ($scope.supplierId == undefined) {
          scannerService.getSupplierId(function (supplierId) {
            $scope.supplierId = supplierId;
          })
        }


        scannerService.getWozek(trolleyId, function (wozek) {
          $scope.wozek = addShowTrashButtonParam(wozek);
        });
      };

      var addShowTrashButtonParam = function(wozek){
        wozek.forEach(function(item){
          item.showTrashButton = true;
        });
      return wozek;
      };

      $scope.$on('wozekLoaded', function () {
        loadNames();
      });

      $scope.$on('error', function () {
        growl.error("error", {ttl: 5000});
      });

      $scope.$on('TrolleyRemoved',function(){
        $scope.wozek = [];
        $location.url('/scanner');
      });

      $scope.$on('boxRemoved', function () {
        for (var i = 0; i <= $scope.wozek.length; i++) {
          if (($scope.wozek != undefined)) {
            if ($scope.wozek[i].id === boxIdToBeRemoved) {
              $scope.wozek.splice(i, 1);
            }
          }
        }
      });

      $scope.removeBox = function (boxId) {
        hideBox(boxId);
        boxIdToBeRemoved = boxId;
        var command = {};
        command.id = boxId;
        scannerService.removeBox(command);
      };

      var hideBox = function(boxId){
        for(var i = 0; i< $scope.wozek.length; i++){
          if(boxId === $scope.wozek[i].id){
            $scope.wozek[i].showTrashButton = false;
          }
        }
      };

      var loadNames = function () {
        if ($scope.people == undefined) {
          $location.url('/scanner');
        }
        $scope.wozek.forEach(function (item) {
          if($scope.eastMode === true){
            item.barcode = generateEastMushroomsBarcode($scope.supplierId, item.uniqId, item.pickerNr);
          }else {
            item.barcode = generateBarcode($scope.supplierId, item.uniqId, item.pickerId);
          }
          $scope.people.forEach(function (person) {
            if (person.id == item.pickerId) {
              item.pickerName = person.name;
              item.pickerSurname = person.surname;
            }
            if (person.id == item.trolleyManId) {
              item.trolleyManName = person.name;
              item.trolleyManSurname = person.surname;
            }
            if (person.id == item.leaderId) {
              item.leaderName = person.name;
              item.leaderSurname = person.surname;
            }
          })
        })
      };

      var generateBarcode = function (supplierId, uniqId, personId) {
        var supplierId = supplierId.toString();
        var uniqId = uniqId.toString();
        var personId = personId.toString();

        for (var i = supplierId.length; i < 3; i++) {
          supplierId = '0' + supplierId;
        }
        for (var j = uniqId.length; j < 5; j++) {
          uniqId = '0' + uniqId;
        }
        for (var z = personId.length; z < 5; z++) {
          personId = '0' + personId;
        }
        return supplierId + uniqId + personId;
      };


      var generateEastMushroomsBarcode = function (supplierId, uniqId, personNr) {
        var supplierId = supplierId.toString();
        var uniqId = uniqId.toString();
        var personNr = personNr.toString();

        for (var i = supplierId.length; i < 3; i++) {
          supplierId = '0' + supplierId;
        }
        for (var j = uniqId.length; j < 5; j++) {
          uniqId = '0' + uniqId;
        }
        for (var z = personNr.length; z < 2; z++) {
          personNr = '0' + personNr;
        }
        return '002' + uniqId + personNr + supplierId;
      };






    }]);
