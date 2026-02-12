'use strict';

angular.module('frontendApp')
.controller('ResetBarcodeCtrl', ['$rootScope','$scope','barcodeService',function($rootScope,$scope,barcodeService){
$scope.person = $scope.params.person;


$scope.reset = function(personId){
  $scope.loading = true;
  var command = {};
  command.personId = personId;
  barcodeService.resetBarcodes(command);
}

$scope.hideLoading = function(){
  $rootScope.$broadcast('hideLoading');
}

}])
