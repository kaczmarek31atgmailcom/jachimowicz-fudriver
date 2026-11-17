'use strict';

// Isolated Jasmine suite using the repository's existing browser dependencies.
// Run from frontend: node test/run-time-sheet-editor-tests.js
var fs = require('fs');
var os = require('os');
var path = require('path');
var childProcess = require('child_process');
var root = path.resolve(__dirname, '..');
var temporary = fs.mkdtempSync(path.join(os.tmpdir(), 'fudriver-time-editor-'));
function tag(file) { return '<script src="file://' + path.join(root, file) + '"></script>'; }
var html = '<!doctype html><html><head><meta charset="utf-8"></head><body>' +
  tag('node_modules/jasmine-core/lib/jasmine-core/jasmine.js') +
  '<script>var jasmine=jasmineRequire.core(jasmineRequire);var env=jasmine.getEnv();' +
  'var api=jasmineRequire.interface(jasmine,env);Object.keys(api).forEach(function(k){window[k]=api[k];});</script>' +
  tag('bower_components/angular/angular.js') + tag('bower_components/angular-mocks/angular-mocks.js') +
  tag('bower_components/moment/moment.js') + tag('bower_components/angular-translate/angular-translate.js') +
  tag('app/scripts/translator.js') + '<script>angular.module("frontendApp",["myTranslator"])' +
  '.run(function($templateCache){$templateCache.put("time-sheet-editor-test-template",' +
  JSON.stringify(fs.readFileSync(path.join(root, 'app/views/people/worktime/timeSheetEdit.html'), 'utf8')) +
  ');$templateCache.put("time-sheet-card-test-template",' +
  JSON.stringify(fs.readFileSync(path.join(root, 'app/views/people/worktime/timeSheet.html'), 'utf8')) +
  ');$templateCache.put("time-sheet-list-test-template",' +
  JSON.stringify(fs.readFileSync(path.join(root, 'app/views/people/worktime/worktime-list.html'), 'utf8')) +
  ');});</script>' +
  tag('app/scripts/controllers/people/editTimeSheetController.js') +
  tag('test/spec/controllers/people/EditTimeSheetControllerSpec.js') +
  tag('app/scripts/controllers/people/timeSheetController.js') +
  tag('test/spec/controllers/people/TimeSheetControllerSpec.js') +
  tag('app/scripts/controllers/people/workTime/WorkTimeController.js') +
  tag('test/spec/controllers/people/WorkTimeControllerSpec.js') +
  '<script>var failures=[],count=0;env.addReporter({specDone:function(r){count++;' +
  'if(r.status==="failed")failures.push({name:r.fullName,errors:r.failedExpectations});},' +
  'jasmineDone:function(){window.testResults={count:count,failures:failures};}});env.execute();</script></body></html>';
fs.writeFileSync(path.join(temporary, 'tests.html'), html);
var runner = 'var page=require("webpage").create();' +
  'page.onError=function(m){console.log(m);phantom.exit(2);};' +
  'page.open(' + JSON.stringify('file://' + path.join(temporary, 'tests.html')) + ',function(status){' +
  'if(status!=="success")phantom.exit(2);var tries=0;setInterval(function(){' +
  'var r=page.evaluate(function(){return window.testResults;});' +
  'if(r){console.log(JSON.stringify(r));phantom.exit(r.count>0&&r.failures.length===0?0:1);}' +
  'if(++tries>100){console.log("Test timeout");phantom.exit(2);}},100);});';
fs.writeFileSync(path.join(temporary, 'runner.js'), runner);
var result;
try {
  var environment = Object.assign({}, process.env, {OPENSSL_CONF: '/dev/null'});
  result = childProcess.spawnSync(path.join(root, 'node_modules/phantomjs-prebuilt/lib/phantom/bin/phantomjs'),
    [path.join(temporary, 'runner.js')], {env: environment, encoding: 'utf8', timeout: 15000});
  if (result.stdout) { process.stdout.write(result.stdout); }
  if (result.stderr) { process.stderr.write(result.stderr); }
  if (result.error) { console.error(result.error.message); }
} finally {
  fs.unlinkSync(path.join(temporary, 'tests.html'));
  fs.unlinkSync(path.join(temporary, 'runner.js'));
  fs.rmdirSync(temporary);
}
process.exit(result && result.status === 0 ? 0 : 1);
