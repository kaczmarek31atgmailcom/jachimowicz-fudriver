// Karma configuration
// http://karma-runner.github.io/0.12/config/configuration-file.html
// Generated on 2015-07-14 using
// generator-karma 1.0.0

module.exports = function (config) {
  'use strict';

  config.set({
    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,

    // base path, that will be used to resolve files and exclude
    basePath: '../',

    // testing framework to use (jasmine/mocha/qunit/...)
    // as well as any additional frameworks (requirejs/chai/sinon/...)
    frameworks: [
      "jasmine"
    ],

    // list of files / patterns to load in the browser
    files: [
      // bower:js
      'bower_components/jquery/dist/jquery.js',
      'bower_components/angular/angular.js',
      'bower_components/bootstrap/dist/js/bootstrap.js',
      'bower_components/angular-animate/angular-animate.js',
      'bower_components/angular-cookies/angular-cookies.js',
      'bower_components/angular-resource/angular-resource.js',
      'bower_components/angular-route/angular-route.js',
      'bower_components/angular-sanitize/angular-sanitize.js',
      'bower_components/angular-touch/angular-touch.js',
      'bower_components/bootstrap-css/js/bootstrap.min.js',
      'bower_components/angular-translate/angular-translate.js',
      'bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
      'bower_components/angular-strap/dist/angular-strap.js',
      'bower_components/angular-strap/dist/angular-strap.tpl.js',
      'bower_components/angular-tooltip/dist/tooltip.js',
      'bower_components/sockjs-client/dist/sockjs.js',
      'bower_components/sockjs/sockjs.js',
      'bower_components/stomp-websocket/lib/stomp.min.js',
      'bower_components/angular-xeditable/dist/js/xeditable.js',
      'bower_components/angular-bootstrap-toggle-switch/angular-toggle-switch.js',
      'bower_components/Chart.js/Chart.js',
      'bower_components/angular-chart.js/dist/angular-chart.js',
      'bower_components/ngstorage/ngStorage.js',
      'bower_components/angular-notify/dist/angular-notify.js',
      'bower_components/ng-stomp/src/ng-stomp.js',
      'bower_components/moment/moment.js',
      'bower_components/lng-clockpicker/dist/jquery-clockpicker.js',
      'bower_components/angular-moment/angular-moment.js',
      'bower_components/re-tree/re-tree.js',
      'bower_components/ng-device-detector/ng-device-detector.js',
      'bower_components/angular-clockpicker/dist/angular-clockpicker.min.js',
      'bower_components/angular-ui-bootstrap-datetimepicker/datetimepicker.js',
      'bower_components/angular-growl-v2/build/angular-growl.js',
      'bower_components/angular-rangeslider/angular.rangeSlider.js',
      'bower_components/angular-barcode/dist/angular-barcode.min.js',
      'bower_components/angularjs-slider/dist/rzslider.js',
      'bower_components/angular-drag-and-drop-lists/angular-drag-and-drop-lists.js',
      'bower_components/angular-io-barcode/build/angular-io-barcode.js',
      'bower_components/angular-ui-select/dist/select.js',
      'bower_components/angular-mocks/angular-mocks.js',
      // endbower
      "app/scripts/**/*.js",
      "test/mock/**/*.js",
      "test/spec/**/*.js"
    ],

    // list of files / patterns to exclude
    exclude: [],

    // web server port
    port: 8081,

    // Start these browsers, currently available:
    // - Chrome
    // - ChromeCanary
    // - Firefox
    // - Opera
    // - Safari (only Mac)
    // - PhantomJS
    // - IE (only Windows)
    browsers: [
      "PhantomJS"

    ],

    reporters: ['progress', 'html'],

    htmlReporter: {
      outputFile: 'units.html'
    },

    // Which plugins to enable
    plugins: [
      "karma-phantomjs-launcher",
      "karma-jasmine",
      'karma-htmlfile-reporter'
    ],

    // Continuous Integration mode
    // if true, it capture browsers, run tests and exit
    singleRun: false,

    colors: true,

    // level of logging
    // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
    logLevel: config.LOG_INFO,

    // Uncomment the following lines if you are using grunt's server to run the tests
    // proxies: {
    //   '/': 'http://localhost:9000/'
    // },
    // URL root prevent conflicts with the site root
    // urlRoot: '_karma_'
  });
};
