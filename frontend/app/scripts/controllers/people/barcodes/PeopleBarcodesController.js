'use strict';

angular.module('frontendApp')
  .controller('PeopleBarcodesCtrl', ['$rootScope', '$scope', 'growl', '$uibModal', 'barcodeService', 'peopleDictionaryServicePromise','settingsPromiseService',
    function ($rootScope, $scope, growl, $uibModal, barcodeService, peopleDictionaryServicePromise,settingsPromiseService) {

      $scope.loading = true;
      $scope.eastMushroomsError = false;
      $scope.orderByField = 'nr';

      settingsPromiseService.then(function (settingsService) {
        $scope.eastMode = settingsService.eastMushroomsMode;
      });


      peopleDictionaryServicePromise.then(function (peopleDictionaryService) {
        var supplierId = peopleDictionaryService.supplierId.toString();
        while (supplierId.length < 3) {
          supplierId = '0' + supplierId;
        }
        $scope.supplierId = supplierId;
      });

      $scope.getFiveCharsEmployeeId = function (id) {
        var result = id.toString();
        while (result.length < 5) {
          result = '0' + result;
        }
        return result;
      };

      $scope.getTwoCharsEmployeeNr = function (nr) {
        var result = nr.toString();
        if(result.length > 2){
          if(!($scope.eastMushroomsError) && ($scope.eastMode === true)){
          growl.error('people.barcodes.header.error.to-long-person-nr',{ttl:5000});
          $scope.eastMushroomsError = true;
          }
          return "!!!!!";
        }
        while (result.length < 2) {
          result = '0' + result;
        }
        return result;
      };


      var init = function () {
        $scope.loading = true;
        barcodeService.getBarcodeHeaders(function (headers) {
          $scope.headers = headers;
          $scope.loading = false;
        })
      };
      init();

      $scope.reserve = function (personId, amount) {
        if (amount > 0) {
          var command = {};
          command.pickerId = personId;
          command.numberOfUniqsToBeCreated = amount;
          barcodeService.createBarcodes(command);
        }
      };

      $scope.openListModal = function (personId) {
        barcodeService.getNotUsedBarcodes(personId, function (barcodes) {
          $scope.unusedBarcodes = barcodes;

          var modalInstance = $uibModal.open({
            templateUrl: 'views/people/barcodes/list-barcodes-modal.html',
            controller: ['$scope','$uibModalInstance','barcodes',function ($scope, $uibModalInstance, barcodes) {
              $scope.barcodes = barcodes;

              $scope.cancel = function () {
                $scope.barcodes = [];
                $uibModalInstance.dismiss();
              };

            }],
            resolve: {
              barcodes: function () {
                return $scope.unusedBarcodes;
              }
            }
          });
        });
      };


      $scope.openRemoveModal = function (personId) {

        $scope.barcodes = [];
        $scope.slider = {};
        $scope.slider.personId = personId;
        $scope.slider.range = {};
        $scope.slider.range.floor = 0;
        $scope.slider.range.ceil = 0;


        barcodeService.getNotUsedBarcodes(personId, function (barcodes) {
          $scope.barcodes = barcodes;
          $scope.slider.range.floor = $scope.barcodes[0];
          $scope.slider.range.ceil = $scope.barcodes[$scope.barcodes.length - 1];

          var modalInstance = $uibModal.open({
            templateUrl: 'views/people/barcodes/remove-barcodes-modal.html',
            controller: ['$rootScope', '$scope', '$uibModalInstance', 'values', function ($rootScope, $scope, $uibModalInstance, values) {
              $scope.slider = JSON.parse(JSON.stringify(values)); //Copy of the object in order to keep original values in $scope.percentages in parent controller.


              var buildStepsArray = function(start,end) {
                var stepsArray = [];
                stepsArray.push(start);
                stepsArray.push(end);
                for(var i = parseInt(start - 1); i< parseInt(end); i++) {
                  if ((i % 1000) === 0) {
                    stepsArray.push(i);
                  }
                }
                var result = stepsArray.filter(onlyUnique);
                result.sort(compare);
                  return result;
                };

              function onlyUnique(value, index, self) {
                return self.indexOf(value) === index;
              }

              function compare(o1,o2){
                var a = parseInt(o1);
                var b = parseInt(o2);
                if(a < b){
                  return -1
                }
                if(a>b){
                  return 1;
                }
                return 0;
              }

              $scope.slider.range.options = {
                floor: parseInt($scope.slider.range.floor -1),
                ceil: $scope.slider.range.ceil
                //stepsArray: buildStepsArray($scope.slider.range.floor,$scope.slider.range.ceil)
              };

              $scope.cancel = function () {
                $uibModalInstance.dismiss();
              };



              $scope.deleteBarcodes = function () {
                var command = {};
                command.personId = $scope.slider.personId;
                command.startNumber = $scope.slider.range.floor;
                command.endNumber = $scope.slider.range.ceil;
                barcodeService.deleteBarcodes(command);
              };

              $scope.$on('barcodesDeleted', function () {
                $uibModalInstance.close($scope.slider);
                $rootScope.$broadcast('BarcodesDeletedSuccessfully');
              });

              $scope.$on('error', function () {
                $rootScope.$broadcast('ModalError');
              });
            }],

            resolve: {
              values: function () {
                return $scope.slider;
              }
            }
          });

          modalInstance.result.then(function (slider) {
            $scope.slider = slider;
          });
          modalInstance.rendered.then(function () {
            $rootScope.$broadcast('rzSliderForceRender'); //Force refresh sliders on render. Otherwise bullets are aligned at left side.
          });

        });
      };

      $scope.setOrderByField = function (value) {
        $scope.orderByField = value;
      };

      $scope.$on('BarcodesDeletedSuccessfully', function () {
        growl.success("people.barcodes.message.barcodes.removed", {ttl: 2000});
        barcodeService.getBarcodeHeaders(function (headers) {
          $scope.headers = headers;
        });
      });

      $scope.$on('barcodesCreated', function () {
        growl.success("people.barcodes.message.barcodes.created", {ttl: 2000});
        barcodeService.getBarcodeHeaders(function (headers) {
          $scope.headers = headers;
        });
      });

      $scope.$on('error', function () {
        growl.error("error", {ttl: 5000});
      });

      $scope.$on('ModalError', function () {
        growl.error("error", {ttl: 5000});
      });


      $scope.$on('BarcodeHeadersLoaded', function () {
        $scope.loading = false;
      });


    }]);
