'use strict';

angular.module('frontendApp').controller('reclassifyDetailCtrl', ['$scope', '$route', '$location', 'reclassifyService', 'notify','growl','settingsPromiseService','TrolleyManService',
  function ($scope, $route, $location, reclassifyService, notify, growl, settingsPromiseService, TrolleyManService) {


    $scope.paletaNr = $route.current.params.nr;

    settingsPromiseService.then(function (settingsService) {
      $scope.eastMode = settingsService.eastMushroomsMode;
      $scope.requiredTrolleyMan = settingsService.requiredTrolleyMan;
    });


    var getTypes = function () {
      reclassifyService.getTypes(function (types) {
        $scope.types = types;
      })
    };

    var getCycles = function () {
      reclassifyService.getCycles(function (cycles) {
        $scope.cycles = cycles;
      })
    };


    var getDetails = function () {
      reclassifyService.getDetails($scope.paletaNr, function (details) {
        $scope.details = details.sort(sortDetails);
        if (!details.length > 0) {
          $location.url("/reclassify");
        }
      })
    };

    function sortDetails(o1,o2){
      if(o1.pickerId > o2.pickerId){
        return 1;
      }
      if(o1.pickerId < o2.pickerId){
        return -1;
      }
      if(o1.halaName > o2.halaName){
        return 1;
      }
      if(o1.halaName < o2.halaName){
        return -1;
      }
      if(o1.uniqId > o2.uniqId){
        return 1;
      }
      if(o1.uniqId < o2.uniqId){
        return -1;
      }

      return 0
    }

    function filterActiveTrolleyMen(trolleyMen){
      var result = [];
      trolleyMen.forEach(function(item){
        if(item.active === true){
          result.push(item);
        }
      })
      return result;
    }

    var getHeader = function () {
      reclassifyService.getHeader($scope.paletaNr, function (header) {
        TrolleyManService.getAllTrolleyMan(function(trolleyMen){
          $scope.trolleyMen = filterActiveTrolleyMen(trolleyMen);
          $scope.header = header;
          initSelectedType();
          initSelectedCycle();
        });
      })
    };




    $scope.init = function () {
      reclassifyService.getSupplier(function (supplier) {
        $scope.supplierId = supplier;
        getTypes();
        getCycles();
        getDetails();
        getHeader();
      })

    };


    $scope.selectedType = {};
    $scope.selectedCycle = {};


    $scope.findBarcode = function (uniqId, pickerId) {
      var supplierId = $scope.supplierId.toString();
      var uniqId = uniqId.toString();
      var pickerId = pickerId.toString();
      for (var i = supplierId.length; i < 3; i++) {
        supplierId = '0' + supplierId;
      }
      for (var i = uniqId.length; i < 5; i++) {
        uniqId = '0' + uniqId;
      }

      for (var i = pickerId.length; i < 5; i++) {
        pickerId = '0' + pickerId;
      }
      return supplierId + uniqId + pickerId;
    };

    $scope.findBarcodeEastMushrooms = function (uniqId, pickerNr) {
      var supplierId = $scope.supplierId.toString();
      var uniqId = uniqId.toString();
      var pickerNr = pickerNr.toString();
      for (var i = supplierId.length; i < 3; i++) {
        supplierId = '0' + supplierId;
      }
      for (var i = uniqId.length; i < 5; i++) {
        uniqId = '0' + uniqId;
      }

      for (var i = pickerNr.length; i < 2; i++) {
        pickerNr = '0' + pickerNr;
      }
      return '002'+ uniqId + pickerNr + supplierId ;
    };


    $scope.performUpdate = function () {
      var command = {};
      command.supplierId = $scope.supplierId;
      command.paletaNr = $scope.paletaNr;
      command.rodzajId = $scope.selectedType.id;
      command.cycleId = $scope.selectedCycle.id;
      command.trolleyManId = $scope.trolleyManId;
      reclassifyService.reclassify(command);
    };


    $scope.$on('reclassificationDone', function () {
      $scope.init();
      notify({message: 'Paleta przeklasyfikowana', duration: 1500});
    });


    var initSelectedType = function () {
      $scope.types.forEach(function (item) {
        if (item.id == $scope.header.rodzajId) {
          $scope.selectedType = item;
        }
      })
    };

    var initSelectedCycle = function () {

      $scope.cycles.forEach(function (item) {
        if (item.id == $scope.header.cycleId) {
          $scope.selectedCycle = item;
        }
      })
    };

    $scope.remove = function (id) {
      var command = {};
      command.id = id;
      reclassifyService.delete(command);
    };


    $scope.$on('EntryDeleted', function () {
      reclassifyService.getDetails($scope.paletaNr, function (details) {
        if (!details.length > 0) {
          $location.url("/reclassify");
        }
        var found = false;
        for (var i = $scope.details.length - 1; i >= 0; i--) {
          details.forEach(function (newItem) {
            if (newItem.id == $scope.details[i].id) {
              found = true;
            }
          });
          if (!found) {
            $scope.details.splice(i, 1);
          }
          found = false;
        }
      });
    });

/*
    $scope.deleteAll = function () {
      var command = {};
      command.paletteId = $scope.paletaNr;
      reclassifyService.deleteAll(command);
    };
*/
    $scope.$on('PaletteDeleted', function () {
      $location.url("/reclassify");
    });

    $scope.$on('error',function(){
      console.log('error');
      growl.error('error');
    })

  }]);
