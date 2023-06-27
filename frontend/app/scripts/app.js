'use strict';

/**
 * @ngdoc overview
 * @name frontendApp
 * @description
 * # frontendApp
 *
 * Main module of the application.
 */
angular
  .module('frontendApp', [
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ui.select',
    'ngTouch',
    'toggle-switch',
    'mgcrea.ngStrap',
    'myTranslator',
    'xeditable',
    'chart.js',
    'myFilters',
    'cgNotify',
    'ngStomp',
    'angular-clockpicker',
    'ui.bootstrap',
    'ui.bootstrap.datetimepicker',
    'angular-growl',
    'ui-rangeSlider',
    'barcode',
    'rzModule',
    'dndLists',
    'CustomDirectives',
    'io-barcode'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html'
      })
      .when('/leaderReport', {
        templateUrl: 'views/leaderReport.html',
        controller: 'LeaderReportCtrl'
      })
      .when('/worktimeReport', {
        templateUrl: 'views/people/worktimeReport/report.html'
      })
      .when('/scalesStatus', {
        templateUrl: 'views/scalesStatus.html'
      })
      .when('/collectedByChamber', {
        templateUrl: 'views/collectedByChamber.html'
      })
      .when('/scannerManReport', {
        templateUrl: 'views/report/scannerManReport/scannerManReport.html'
      })
      .when('/report/trolleyManReport', {
        templateUrl: 'views/report/trolleyManReport/trolleyManReport.html'
      })
      .when('/salesReport', {
        templateUrl: 'views/report/sales/salesReport.html'
      })
      .when('/sizeByCycle', {
        templateUrl: 'views/report/sizeByCycle/size-by-cycle.html'
      })
      .when('/report/typesByPickers', {
        templateUrl: 'views/report/typesByPickers/types-by-pickers.html'
      })
      .when('/report/typesByPicker/:pickerId', {
        templateUrl: 'views/report/typesByPickers/types-by-picker.html'
      })
      .when('/cycles', {
        templateUrl: 'views/cycles.html'
      })
      .when('/scanner', {
        templateUrl: 'views/scanner/scanner.html'
      })
      .when('/scannerSummary', {
        templateUrl: 'views/scanner/scannerSummary.html'
      })
      .when('/trolleyDetails/:trolleyId', {
        templateUrl: 'views/scanner/trolleyDetails.html'
      })
      .when('/trolleysSummary', {
        templateUrl: 'views/scanner/trolleysSummary.html'
      })
      .when('/people', {
        templateUrl: 'views/people/people.html'
      })
      .when('/foreignerAlertConfig', {
        templateUrl: 'views/people/foreignerAlertConfig.html'
      })
      .when('/editEmployee/:employeeId', {
        templateUrl: 'views/people/editEmployee.html'
      })
      .when('/addEmployee', {
        templateUrl: 'views/people/addEmployee.html'
      })
      .when('/badgeRegistration/:employeeId', {
        templateUrl: 'views/people/badgeRegistration.html'
      })
      .when('/people/barcodes', {
        templateUrl: 'views/people/barcodes/people-list.html'
      })
      .when('/people/worktime/timeSheet/:employeeId/:theDay', {
        templateUrl: 'views/people/worktime/timeSheetEdit.html'
      })
      .when('/people/worktime/:employeeId', {
        templateUrl: 'views/people/worktime/timeSheet.html'
      })
      .when('/people/worktime', {
        templateUrl: 'views/people/worktime/worktime-list.html'
      })
      .when('/reclassificationTypes', {
        templateUrl: 'views/reclassification/skup/types.html'
      })
      .when('/reclassificationHeaders', {
        templateUrl: 'views/reclassification/skup/headers.html'
      })
      .when('/reclassificationHistory', {
        templateUrl: 'views/reclassification/skup/historyHeaders.html'
      })
      .when('/reclassification/:reclassifcationId', {
        templateUrl: 'views/reclassification/skup/details.html'
      })
      .when('/reclassificationHistoryDetails/:reclassifcationId', {
        templateUrl: 'views/reclassification/skup/historyDetails.html'
      })
      .when('/reclassifyByBarcode', {
        templateUrl: 'views/administration/reclassify-by-barcode/reclassify-by-barcode.html'
      })
      .when('/users', {
        templateUrl: 'views/administration/users/users.html'
      })
/*
      .when('/massHarvest', {
        templateUrl: 'views/massHarvest/massHarvestRecorder.html'
      })

      .when('/test', {
        templateUrl: 'views/people/test.html'
      })
      .when('/waitingBoard', {
        templateUrl: 'views/warehouse/waitingBoard.html'
      })
      .when('/warehouse', {
        templateUrl: 'views/warehouse/status.html'
      })
      .when('/warehouse-age', {
        templateUrl: 'views/warehouse/warehouse-age.html'
      })
      .when('/outgo', {
        templateUrl: 'views/warehouse/outgo.html'
      })
      .when('/outgo-release/:orderId', {
        templateUrl: 'views/warehouse/outgo-release.html'
      })
      .when('/outgo-release/wz/:wzId', {
        templateUrl: 'views/warehouse/outgo-release-wz.html'
      })
      .when('/outgoSummary', {
        templateUrl: 'views/warehouse/outgoSummary.html'
      })
      .when('/outgo/delivery-letter/:orderId', {
        templateUrl: 'views/warehouse/deliveryLetter/delivery-letter.html'
      })
      .when('/outgo-release/delivery-letter/:orderId', {
        templateUrl: 'views/warehouse/deliveryLetter/release-delivery-letter.html'
      })
      .when('/order-report', {
        templateUrl: 'views/order/report/OrderHeaders.html'
      })
      .when('/order-report/:orderId', {
        templateUrl: 'views/order/report/OrderPalettes.html'
      })
      .when('/wzList', {
        templateUrl: 'views/warehouse/wzList.html'
      })
      .when('/wzDetail/:wzId', {
        templateUrl: 'views/warehouse/wzDetail.html'
      })
      .when('/brcByDate', {
        templateUrl: 'views/warehouse/brcByDate.html'
      })
      .when('/brcByBarcode', {
        templateUrl: 'views/warehouse/brcByBarcode.html'
      })
      .when('/brcByDateStockDetails', {
        templateUrl: 'views/warehouse/brcByDateStockDetails.html'
      })
      .when('/brcByWzDetails', {
        templateUrl: 'views/warehouse/brcByWzDetails.html'
      })
      .when('/brcByBarcodeStockDetails', {
        templateUrl: 'views/warehouse/brcByBarcodeStockDetails.html'
      })
      .when('/brcByBarcodeWzDetails', {
        templateUrl: 'views/warehouse/brcByBarcodeWzDetails.html'
      })
      .when('/findBarcode', {
        templateUrl: 'views/warehouse/findByBarcode.html'
      })
      .when('/findBarcodeByDate', {
        templateUrl: 'views/warehouse/findBarcodeByDate.html'
      })
      .when('/order-prepare', {
        templateUrl: 'views/warehouse/order-prepare/order-prepare.html'
      })
      .when('/order-prepare/remove-reservation', {
        templateUrl: 'views/warehouse/order-prepare/remove-reservation.html'
      })
  */
      .when('/reclassifyByCode', {
        templateUrl: 'views/warehouse/reclassify.html'
      })
      .when('/reclassify', {
        templateUrl: 'views/reclassification/local/reclassify.html'
      })
      .when('/reclassifyDetails/:nr', {
        templateUrl: 'views/reclassification/local/details.html'
      })
      .when('/reclassifyReport', {
        templateUrl: 'views/audit/reclassify-report.html'
      })
      .when('/calendar', {
        templateUrl: 'views/calendar/calendar.html'
      })
      .when('/wage', {
        templateUrl: 'views/wage/wage.html'
      })
      .when('/people-wages', {
        templateUrl: 'views/wage/people-wages.html'
      })
      .when('/bonus-types', {
        templateUrl: 'views/wage/bonus/bonus-types.html'
      })
      .when('/salary', {
        templateUrl: 'views/salary/salary.html'
      })
      .when('/salary/harvestDetails/:personId/:payoffDetailId', {
        templateUrl: 'views/salary/PayedHarvestSalaryDetails.html'
      })
      .when('/salary/workTimeDetails/:personId/:payoffDetailId', {
        templateUrl: 'views/salary/PayedWorkTimeSalaryDetails.html'
      })
      .when('/salary/accounts', {
        templateUrl: 'views/salary/accounts.html'
      })
      .when('/salary/account/history/:personId', {
        templateUrl: 'views/salary/account-history.html'
      })
      .when('/wageDetails', {
        templateUrl: 'views/wage/wageDetails.html'
      })
      .when('/order-admin', {
        templateUrl: 'views/order/order-admin.html'
      })
      .when('/create-order', {
        templateUrl: 'views/order/create-order.html'
      })
      .when('/edit-order/:orderId', {
        templateUrl: 'views/order/edit-order.html'
      })
      .when('/delivery-letter/edit/:orderId', {
        templateUrl: 'views/deliveryLetter/delivery-letter-edit.html'
      })
      .when('/reclassifyReason', {
        templateUrl: 'views/settings/reclassifyReason.html'
      })
      .when('/truck', {
        templateUrl: 'views/settings/truck/truckList.html'
      })
      .when('/truck/create', {
        templateUrl: 'views/settings/truck/truckCreate.html'
      })
      .when('/customer', {
        templateUrl: 'views/settings/customer/customerList.html'
      })
      .when('/currency', {
        templateUrl: 'views/settings/currency/currency.html'
      })
      .when('/company/:companyId', {
        templateUrl: 'views/settings/company/edit-company.html'
      })
      .when('/company-create', {
        templateUrl: 'views/settings/company/create-company.html'
      })
      .when('/company', {
        templateUrl: 'views/settings/company/company.html'
      })
      .when('/depot', {
        templateUrl: 'views/settings/depot/depot.html'
      })
      .when('/chamber', {
        templateUrl: 'views/settings/chamber/chamber.html'
      })
      .when('/palette-type', {
        templateUrl: 'views/palette-type/palette-type.html'
      })
      .when('/subsoil', {
        templateUrl: 'views/settings/subsoil/subsoil.html'
      })
      .when('/mycelium', {
        templateUrl: 'views/settings/mycelium/mycelium.html'
      })
      .when('/cycle-dates', {
        templateUrl: 'views/cycles/cycle-dates.html'
      })
      .when('/cycle-headers', {
        templateUrl: 'views/cycles/cycle-headers.html'
      })
      .when('/cycle-details/:cycleId', {
        templateUrl: 'views/cycles/cycle-details.html'
      })
      .when('/cycle-by-technologists', {
        templateUrl: 'views/cycles/technologists-by-cycles.html'
      })
      .when('/report/cycles-by-brigades', {
        templateUrl: 'views/report/cycles_by_brigades/main.html'
      })
      .when('/typeGroup', {
        templateUrl: 'views/administration/type/typeGroup.html'
      })
      .when('/typeSize', {
        templateUrl: 'views/administration/type/typeSize.html'
      })
      .when('/box', {
        templateUrl: 'views/administration/box/box.html'
      })
      .when('/type', {
        templateUrl: 'views/administration/type/type.html'
      })
      .when('/hotel/settings/hotel', {
        templateUrl: 'views/hotel/settings/hotel.html'
      })
      .when('/hotel/booking', {
        templateUrl: 'views/hotel/booking.html'
      })
      .when('/warehouse-east/reception', {
        templateUrl: 'views/eastMushroomsWarehouse/reception.html'
      })
      .when('/warehouse-east/release', {
        templateUrl: 'views/eastMushroomsWarehouse/release.html'
      })
      .when('/warehouse-east/warehouse', {
        templateUrl: 'views/eastMushroomsWarehouse/warehouse-status.html'
      })
      .when('/warehouse-east/wz', {
        templateUrl: 'views/eastMushroomsWarehouse/wz-headers.html'
      })
      .when('/warehouse-east/wz/:wzId', {
        templateUrl: 'views/eastMushroomsWarehouse/wz.html'
      })
      .when('/warehouse-east/shipment', {
        templateUrl: 'views/eastMushroomsWarehouse/shipment-headers.html'
      })
      .when('/warehouse-east/shipment/:shipmentId', {
        templateUrl: 'views/eastMushroomsWarehouse/shipment.html'
      })

      .when('/warehouse-east/reclassification', {
        templateUrl: 'views/eastMushroomsWarehouse/reclassification.html'
      })
      .when('/warehouse-east/change-palette', {
        templateUrl: 'views/eastMushroomsWarehouse/change-palette.html'
      })
      .when('/warehouse-east/change-palette-by-box', {
        templateUrl: 'views/eastMushroomsWarehouse/change-palette-by-box.html'
      })
      .when('/production-orders', {
        templateUrl: 'views/production-orders/orders.html'
      })
      .when('/production-order-local', {
        templateUrl: 'views/production-order-local/production-order-local.html'
      })
      .when('/trolleyMan', {
        templateUrl: 'views/administration/trolleyMan/trolleyMan.html'
      })


      .otherwise({
        redirectTo: '/'
      });
  })
  // xeditable settings
  .run(function (editableOptions) {
    editableOptions.theme = 'bs3';
  });


angular.module('frontendApp').config(['$httpProvider', function ($httpProvider) {
  //initialize get if not there
  if (!$httpProvider.defaults.headers.get) {
    $httpProvider.defaults.headers.get = {};
  }

  // Answer edited to include suggestions from comments
  // because previous version of code introduced browser-related errors

  //disable IE ajax request caching
  $httpProvider.defaults.headers.get['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GMT';
  // extra
  $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
  $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
}]);


angular.module('frontendApp')
  .config(['$locationProvider', function ($locationProvider) {
    $locationProvider.hashPrefix('');
  }]);

