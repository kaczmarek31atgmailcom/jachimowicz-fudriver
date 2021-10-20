'use strict';

angular.module('frontendApp')
.controller('SalesReportCtrl',['$scope','growl','SalesReportService',function($scope,growl,SalesReportService){
  $scope.endDate = moment();
  $scope.startDate = moment().subtract(1,'months');

  $scope.init = function(){
    SalesReportService.getSalesReport(moment($scope.startDate).format("YYYY-MM-DD"),moment($scope.endDate).format("YYYY-MM-DD"),function(report){
      var customers = getCustomers(report);
      var types = getTypes(report);
      var customers = findCustomerSales(customers,types,report);
      $scope.types = findTypeTotals(types,report);
      $scope.customers = findCustomerTotals(customers,report);
      $scope.total = findTotal(report);
    })
  };
  $scope.init();

  function findCustomerSales(customers,types,report){
    types.forEach(function(type){
      customers.forEach(function(customer){
        customer.sales.push(findTotalWeight(customer.id,type.id,report));
      })
    });
  return customers;
  }

  function findTotalWeight(customerId,typeId,report){
    for(var i=0; i< report.length; i++){
      if((report[i].typeId === typeId) && (report[i].customerId === customerId)){
        return report[i].totalWeight;
      }
    }
  return 0;
  }

  function getCustomers(report){
    var tmp = [];
    var result = [];
    report.forEach(function(item){
      var customer = {};
      customer.id = item.customerId;
      customer.name = item.customerName;
      customer.sales = [];
      customer.total = 0;
      tmp[customer.id] = customer;
    });
    tmp.forEach(function(customer){
      result.push(customer);
    });
  return result;
  }

  function getTypes(report){
    var tmp = [];
    var result = [];
    report.forEach(function(item){
      var type = {};
      type.id = item.typeId;
      type.name = item.typeName;
      type.weight = item.typeWeight;
      type.total = 0;
      tmp[type.id] = type;
    });

    tmp.forEach(function(type){
      result.push(type);
    });
    return result;
  }

  function findCustomerTotals(customers,report){
    report.forEach(function(item){
      customers.forEach(function(customer){
        if(item.customerId === customer.id){
          customer.total += item.totalWeight;
        }
      })
    });
  return customers;
  }

  function findTypeTotals(types,report){
    report.forEach(function(item){
      types.forEach(function(type){
        if(type.id === item.typeId){
          type.total += item.totalWeight;
        }
      })
    });
  return types;
  }

  function findTotal(report){
    var result = 0;
    report.forEach(function(item){
      result += item.totalWeight;
    });
  return result;
  }

  $scope.exportAction = function () {
    switch ($scope.export_action) {
      case 'pdf':
        $scope.$broadcast('export-pdf', {});
        break;
      case 'excel':
        $scope.$broadcast('export-excel', {});
        break;
      case 'doc':
        $scope.$broadcast('export-doc', {});
        break;
      default:
        console.log('no event caught');
    }
  };

  $scope.export = function () {
    $scope.export_action = 'excel';
    $scope.exportAction();
  };


  $scope.$on('error', function (event, data) {
    growl.error('error',{ttl:5000});
  });


}]);
