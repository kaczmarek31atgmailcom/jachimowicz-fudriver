'use strict';

angular.module('frontendApp')
  .controller('CustomerCtrl', ['$scope', 'growl', '$modal', 'CustomerService', function ($scope, growl, $modal, CustomerService) {


    var init = function () {
      CustomerService.getActiveCustomers(function (customers) {
        $scope.customers = customers;
      })
    };
    init();

    $scope.openEditCustomerModal = function (customer) {
      $scope.editCustomer = angular.copy(customer);
      var editCustomerModal = $modal({
        scope: $scope,
        animation: $scope.animationsEnabled,
        templateUrl: 'views/settings/customer/editCustomerModal.html',
        show: true
      })
    };

    $scope.openCreateCustomerModal = function () {
      $scope.createCustomer = {};
      var createCustomerModal = $modal({
        scope: $scope,
        animation: $scope.animationsEnabled,
        templateUrl: 'views/settings/customer/createCustomerModal.html',
        show: true
      })
    };


    $scope.updateCustomer = function (customer) {
      var command = {};
      command.id = customer.id;
      command.name = customer.name;
      command.address = customer.address;
      CustomerService.updateCustomer(command);
    };

    $scope.$on('CustomerUpdated', function () {
      growl.success("settings.customer.message.customer.updated", {ttl: 3000});
      init();
    });

    $scope.createCustomer = function(name,address){
      var command = {};
      command.name = name;
      command.address = address;
      CustomerService.createCustomer(command);
    };

    $scope.$on('CustomerCreated', function () {
      growl.success("settings.customer.message.customer.created", {ttl: 3000});
      init();
    });

    $scope.openDeleteCustomerPrompt = function(customer){
      $scope.customerToDelete = angular.copy(customer);
      var deleteCustomerModal = $modal({
        scope: $scope,
        animation: $scope.animationsEnabled,
        templateUrl: 'views/settings/customer/confirmCustomerDeleteModal.html',
        show: true
      })
    };

    $scope.deleteCustomer = function(customerId){
      CustomerService.deleteCustomer(customerId);
    };

    $scope.$on('CustomerDeleted',function(response,data){
      for(var i = 0; i< $scope.customers.length;i++){
        if($scope.customers[i].id === parseInt(data.data.entityId)){
          $scope.customers.splice(i,1);
          break;
        }
      }
      growl.success("settings.customer.message.customer.deleted", {ttl: 3000})
    });

  }]);
