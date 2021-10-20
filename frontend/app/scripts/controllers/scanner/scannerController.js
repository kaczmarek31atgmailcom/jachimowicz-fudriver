'use strict';
angular.module('frontendApp')
  .controller('scannerCtrl', ['$scope', 'growl', '$location', '$interval', 'settingsPromiseService', 'scannerService','TrolleyManService',
    function ($scope, growl, $location, $interval, settingsPromiseService, scannerService, TrolleyManService) {


      $scope.people = [];
      $scope.types = [];
      $scope.cycles = [];
      $scope.supplierId = "";
      $scope.errorMessage = '';
      $scope.currentType = '';
      $scope.currentCycle = '';
      $scope.currentTrolleyMan = '';
      $scope.currentLeader = '';
      $scope.currentQualityCheck = 0;
      $scope.currentPicker = '';
      $scope.requiredLeader = false;
      $scope.requiredTrolleyMan = false;
      $scope.today = moment().format("YYYY-MM-DD");
      $scope.tomorrow = moment().add(1,'days').format("YYYY-MM-DD");
      $scope.orders = [];

      var cyclePrefix = '99900001';
      var typePrefix = '99900002';
      var trolleyManPrefix = '99900003';
      var leaderPrefix = '99900004';
      var qualityCheckPrefix = '99900005';


      settingsPromiseService.then(function (settingsService) {
        $scope.requiredTrolleyMan = settingsService.requiredTrolleyMan;
        $scope.requiredLeader = settingsService.requiredLeader;
        $scope.eastMode = settingsService.eastMushroomsMode;
      });


      var intervalPromise = $interval(function(){
        scannerService.getOrdersForTable(function(orders){
          $scope.todayOrders = $scope.getTodayOrders(orders);
          $scope.tomorrowOrders = $scope.getTomorrowOrders(orders);
        })
      },2000);



      $scope.$on('$destroy', function () {
        $interval.cancel(intervalPromise);
      });

      $scope.$watch('currentType', function(newValue,oldValue){
        if(newValue !== undefined) {
          scannerService.setCurrentType(newValue);
        }
      })

      $scope.$watch('currentCycle', function(newValue,oldValue){
        if(newValue !== undefined) {
          scannerService.setCurrentCycle(newValue);
        }
      })



      $scope.getTodayOrders = function(orders){
        var result = [];
        orders.forEach(function(order){
          if(moment(order.dueDate).isSame(moment(),'day')){
            result.push(order);
          }
        })
        return result.sort(compareOrders);
      }

      $scope.getTomorrowOrders = function(orders){
        var result = [];
        orders.forEach(function(order){
          if(moment(order.dueDate).isSame(moment().add(1,'days'),'day')){
            result.push(order);
          }
        })
        return result.sort(compareOrders);
      }


      function compareOrders(a,b){
        if(a.localTypeName > b.localTypeName){
          return 1;
        }
        if(a.localTypeName < b.localTypeName){
          return -1;
        }
        return 0;
      }

      $scope.focusInputText = function () {
        document.getElementById("barcode").focus();
      };

      $scope.loadDictionaries = function () {

        TrolleyManService.getAllTrolleyMan(function(trolleyMen){
          $scope.trolleyMen = trolleyMen;
        });

        scannerService.getPeople(function (people) {
          $scope.people = people;
          scannerService.setPeople($scope.people);
        });
        scannerService.getTypes(function (types) {
          $scope.types = types;
        });
        scannerService.getCycles(function (cycles) {
          $scope.cycles = cycles.sort(compareCycle);
        });
        scannerService.getSupplierId(function (supplierId) {
          var id = supplierId.toString();
          while (id.length < 3) {
            id = '0' + id;
          }
          $scope.supplierId = id;
        });

        function compareCycle(a, b) {
          if (a.name > b.name) {
            return 1;
          }
          if (a.name < b.name) {
            return -1;
          }
          return 0;
        }

        $scope.currentCycle = scannerService.getCurrentCycle();
        $scope.currentType = scannerService.getCurrentType();
        $scope.currentLeader = scannerService.getCurrentLeader();
        $scope.currentTrolleyMan = scannerService.getCurrentTrolleyMan();
      };

      $scope.parseBarcode = function () {
        if ($scope.eastMode === true) {
          parseEastBarcode();
        } else {
          parseNonEastBarcode();
        }
      };

      function parseEastBarcode() {
        $scope.notADigit = false;
        $scope.errorMessage = '';
        if ((!$scope.barcode.match(/^\d+$/)) && $scope.barcode.length > 0) {
          $scope.notADigit = true;
          $scope.barcode = '';
          errorSound();
        }
        if (($scope.barcode.length === 3) && (($scope.barcode !== '002') && ($scope.barcode !== '999'))) {
          $scope.errorMessage = 'invalidSupplierId';
          $scope.barcode = '';
          errorSound();
        }


        if ($scope.barcode.length === 13) {
          var supplierId = $scope.barcode.substr(10, 3);
          var prefix = $scope.barcode.substr(0, 8);
          if (prefix === cyclePrefix) {
            setCurrentCycle($scope.barcode.substr(8, 5));
          } else if (prefix === typePrefix) {
            setCurrentType($scope.barcode.substr(8, 5));
          } else if (prefix === qualityCheckPrefix) {
            $scope.currentQualityCheck = parseInt($scope.barcode.substr(8, 5));
          } else if (prefix === trolleyManPrefix) {
            $scope.currentTrolleyMan = findPersonByNr($scope.barcode.substr(8, 2));
          } else if (prefix === leaderPrefix) {
            $scope.currentLeader = findPersonByNr($scope.barcode.substr(8, 2));
          } else if (findPersonByNr($scope.barcode.substr(8, 2)) === undefined) {
            $scope.errorMessage = "noSuchPicker";
            errorSound();
            return;
          } else if (checkRequiredFields()) {
            if ($scope.barcode.substr(10, 3) !== $scope.supplierId) {
              $scope.errorMessage = 'invalidSupplierId';
              $scope.barcode = '';
              errorSound();
              return;
            }
            commitBoxEastMushrooms();
          }
          $scope.barcode = '';
        }
      }


      function parseNonEastBarcode() {
        $scope.notADigit = false;
        $scope.errorMessage = '';
        if ((!$scope.barcode.match(/^\d+$/)) && $scope.barcode.length > 0) {
          $scope.notADigit = true;
          $scope.barcode = '';
          errorSound();
        }
        if (($scope.barcode.length == 3) && (($scope.barcode != $scope.supplierId) && ($scope.barcode != '999'))) {
          $scope.errorMessage = 'invalidSupplierId';
          $scope.barcode = '';
          errorSound();
        }

        if ($scope.barcode.length == 13) {
          var prefix = $scope.barcode.substr(0, 8);
          if (prefix == cyclePrefix) {
            setCurrentCycle($scope.barcode.substr(8, 5));
          } else if (prefix == typePrefix) {
            setCurrentType($scope.barcode.substr(8, 5));
          } else if (prefix == qualityCheckPrefix) {
            $scope.currentQualityCheck = parseInt($scope.barcode.substr(8, 5));
          } else if (prefix == trolleyManPrefix) {
            $scope.currentTrolleyMan = findTrolleyMan($scope.barcode.substr(8, 5));
          } else if (prefix == leaderPrefix) {
            $scope.currentLeader = findPerson($scope.barcode.substr(8, 5));
          } else if (findPerson($scope.barcode.substr(8, 5)) == undefined) {
            $scope.errorMessage = "noSuchPicker";
            errorSound();
          } else if (checkRequiredFields()) {
            commitBox();
          }
          $scope.barcode = '';
        }
      }


      var commitBox = function () {
        var addWozekEntry = {};
        addWozekEntry.pickerId = parseInt($scope.barcode.substr(8, 5));
        addWozekEntry.rodzajId = $scope.currentType.id;
        addWozekEntry.cycleId = $scope.currentCycle.id;
        addWozekEntry.uniqId = parseInt($scope.barcode.substr(3, 5));
        if ($scope.requiredLeader) {
          addWozekEntry.brygadzistaId = $scope.currentLeader.id;
        }
        if ($scope.requiredTrolleyMan) {
          addWozekEntry.wozkowyId = $scope.currentTrolleyMan.id;
        }
        addWozekEntry.qualityStatus = $scope.currentQualityCheck;
        $scope.test = addWozekEntry;
        scannerService.commitBox(addWozekEntry);
      };

      var commitBoxEastMushrooms = function () {
        var addWozekEntry = {};
        addWozekEntry.pickerId = findPersonByNr($scope.barcode.substr(8, 2)).id;
        addWozekEntry.rodzajId = $scope.currentType.id;
        addWozekEntry.cycleId = $scope.currentCycle.id;
        addWozekEntry.uniqId = parseInt($scope.barcode.substr(3, 5));
        if ($scope.requiredLeader) {
          addWozekEntry.brygadzistaId = $scope.currentLeader.id;
        }
        if ($scope.requiredTrolleyMan) {
          addWozekEntry.wozkowyId = $scope.currentTrolleyMan.id;
        }
        addWozekEntry.qualityStatus = $scope.currentQualityCheck;
        $scope.test = addWozekEntry;
        scannerService.commitBox(addWozekEntry);
      };


      $scope.$on('error', function (event, data) {
        $scope.currentPicker = '';
        $scope.errorMessage = data.body;
        errorSound();
      });

      $scope.$on('boxCommitSuccess', function (event, data) {
        $scope.currentPicker = findPerson(data.entityId);
      });

      var checkRequiredFields = function () {
        var result = true;
        if ($scope.currentCycle == undefined) {
          $scope.errorMessage = "noCycleSet";
          result = false;
          errorSound();
        }

        if ($scope.currentType == undefined) {
          $scope.errorMessage = "noTypeSet";
          result = false;
          errorSound();
        }

        if (($scope.requiredLeader) && ($scope.currentLeader == undefined)) {
          $scope.errorMessage = "noLeaderSet";
          result = false;
          errorSound();
        }

        if (($scope.requiredTrolleyMan) && ($scope.currentTrolleyMan == undefined)) {
          $scope.errorMessage = "noTrolleyManSet";
          result = false;
          errorSound();
        }

        return result;
      };


      var setCurrentCycle = function (id) {
        id = parseInt(id);
        $scope.cycles.forEach(function (item) {
          if (item.chamberId == id) {
            $scope.currentCycle = item;
          }
        })
      };

      var errorSound = function () {
        var audio = new Audio('/fudriver/sounds/blackhole.wav');
        audio.play();
      };


      $scope.goToSummary = function () {
        scannerService.setCurrentCycle($scope.currentCycle);
        scannerService.setCurrentType($scope.currentType);
        scannerService.setCurrentLeader($scope.currentLeader);
        scannerService.setCurrentTrolleyMan($scope.currentTrolleyMan);
        $location.url('/scannerSummary');
      };

      var setCurrentType = function (id) {
        id = parseInt(id);
        $scope.types.forEach(function (item) {
          if (item.id == id) {
            $scope.currentType = item;
          }
        })
      };


      var findPerson = function (id) {
        id = parseInt(id);
        var person;
        $scope.people.forEach(function (item) {
          if (item.id == id) {
            person = item;
          }
        });
        return person;
      };

      var findTrolleyMan = function (id) {
        id = parseInt(id);
        var trolleyMan;
        $scope.trolleyMen.forEach(function (item) {
          if (item.id == id) {
            trolleyMan = item;
          }
        });
        return trolleyMan;
      };


      var findPersonByNr = function (nr) {
        nr = parseInt(nr);
        var person;
        $scope.people.forEach(function (item) {
          if (item.nr === nr) {
            person = item;
          }
        });
        return person;
      };


      $scope.$on('error', function () {
        growl.error('error', {ttl: 5000});
      });





    }]);
