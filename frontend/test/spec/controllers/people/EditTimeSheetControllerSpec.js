/* global describe, beforeEach, afterEach, module, inject, it, expect, jasmine, moment, pending */
'use strict';

describe('Daily work time editor', function () {
  var scope, backend, controller, root, events, callback, unsubscribe, confirm, location, translate, compile, templateCache;
  var periodsUrl = '/fudriver/rest/person/workPeriods?endDay=2026-11-15&personId=2832&startDay=2026-11-15';
  function timestamp(time) { return moment('2026-11-15 ' + time, 'YYYY-MM-DD HH:mm:ss').valueOf(); }
  function records() {
    return [{id: 2, startDate: timestamp('13:00:00'), endDate: timestamp('16:00:00')},
      {id: 1, startDate: timestamp('08:00:23'), endDate: timestamp('10:00:45')}];
  }
  beforeEach(module('frontendApp', function ($provide, $translateProvider) {
    $translateProvider.translations('en', {
      'people.timesheet.edit.duration': '{{hours}} h {{minutes}} min',
      'people.timesheet.edit.confirm-delete': 'Delete period {{start}} – {{end}}?',
      'people.timesheet.edit.in-progress': 'In progress',
      'people.timesheet.edit.period-range': '{{start}} – {{end}}',
      'people.timesheet.edit.message.invalid-time': 'Enter a time in HH:mm format.'
    });
    unsubscribe = jasmine.createSpy('unsubscribe');
    events = {getEvents: function (fn) { callback = fn; return unsubscribe; }};
    confirm = jasmine.createSpy('confirm').and.returnValue(true);
    location = {path: jasmine.createSpy('path')};
    $provide.value('EventService', events);
    $provide.value('$route', {current: {params: {employeeId: '2832', theDay: '2026-11-15'}}});
    $provide.value('$location', location);
  }));
  beforeEach(inject(function ($controller, $httpBackend, $rootScope, $translate, $compile, $templateCache) {
    translate = $translate; compile = $compile; templateCache = $templateCache;
    backend = $httpBackend; controller = $controller; root = $rootScope; scope = root.$new();
    backend.expectGET('/fudriver/rest/person/2832').respond({imie: 'Jan', nazwisko: 'Testowy'});
    backend.expectGET(periodsUrl).respond(records());
    controller('editTimeSheetCtrl', {$scope: scope, $window: {confirm: confirm}});
    backend.flush();
  }));
  afterEach(function () {
    scope.$destroy();
    backend.verifyNoOutstandingExpectation();
    backend.verifyNoOutstandingRequest();
  });
  it('formats duration and delete confirmations with translation parameters', function () {
    expect(scope.durationLabel(517)).toBe('8 godz. 37 min');
    confirm.and.returnValue(false);
    scope.remove(scope.workPeriods[0]);
    expect(confirm).toHaveBeenCalledWith('Usunąć okres 08:00 – 10:00?');
  });
  it('uses the active language in dynamic labels and confirmations', function () {
    translate.use('en'); root.$digest();
    expect(scope.durationLabel(517)).toBe('8 h 37 min');
    expect(scope.periodLabel({start: '08:00', end: ''})).toBe('08:00 – In progress');
    confirm.and.returnValue(false);
    scope.workPeriods[0].originalEnd = '';
    scope.remove(scope.workPeriods[0]);
    expect(confirm).toHaveBeenCalledWith('Delete period 08:00 – In progress?');
  });
  it('translates validation messages and updates them after a language change', function () {
    scope.invalid = {start: '25:00', end: '26:00'};
    var element = compile('<span>{{validation(invalid, true) | translate}}</span>')(scope);
    root.$digest();
    expect(element.text()).toBe('Wpisz godziny w formacie HH:mm.');
    translate.use('en'); root.$digest();
    expect(element.text()).toBe('Enter a time in HH:mm format.');
  });
  it('sorts periods and summarizes saved work and breaks', function () {
    expect(scope.workPeriods[0].id).toBe(1);
    expect(scope.workMinutes).toBe(300);
    expect(scope.pauseMinutes).toBe(179);
    expect(scope.person.imie).toBe('Jan');
  });
  it('rejects enclosing overlaps and allows touching boundaries', function () {
    expect(scope.validation({start: '07:00', end: '17:00'}, true)).not.toBe('');
    expect(scope.validation({start: '10:01', end: '13:00'}, true)).toBe('');
    expect(scope.validation({start: '16:00', end: '17:00'}, true)).toBe('');
  });
  it('rejects invalid, reversed and overnight clock values', function () {
    expect(scope.validation({start: '25:00', end: '26:00'}, true)).not.toBe('');
    expect(scope.validation({start: '18:00', end: '17:00'}, true)).not.toBe('');
    expect(scope.validation({start: '23:00', end: '01:00'}, true)).not.toBe('');
  });
  it('does not save while typing and preserves untouched seconds on explicit save', function () {
    var period = scope.workPeriods[0];
    period.end = '11:00';
    root.$digest();
    expect(scope.isDirty(period)).toBe(true);
    backend.expectPUT('/fudriver/rest/person/workPeriod', {
      id: 1, startDate: timestamp('08:00:23'), endDate: timestamp('11:00:00')
    }).respond({});
    backend.expectGET(periodsUrl).respond(records());
    scope.save(period);
    expect(scope.busy).toBe(true);
    backend.flush();
    expect(scope.busy).toBe(false);
    expect(scope.hasChanges()).toBe(false);
    expect(scope.notice).not.toBe('');
  });
  it('retains the draft after a server conflict', function () {
    var period = scope.workPeriods[0]; period.end = '11:00';
    backend.expectPUT('/fudriver/rest/person/workPeriod').respond(409, {body: 'conflict'});
    scope.save(period); backend.flush();
    expect(period.end).toBe('11:00');
    expect(scope.hasChanges()).toBe(true);
    expect(scope.busy).toBe(false);
    expect(scope.error).toBe('people.timesheet.edit.message.overlap');
  });
  it('does not permit clearing the end of a closed period', function () {
    var period = scope.workPeriods[0]; period.end = '';
    expect(scope.validation(period, false)).not.toBe('');
  });
  it('cancels drafts and protects edits in other rows', function () {
    var first = scope.workPeriods[0]; first.end = '11:00';
    expect(scope.hasOtherChanges(scope.workPeriods[1])).toBe(true);
    scope.cancel(first);
    expect(first.end).toBe('10:00');
    expect(scope.hasChanges()).toBe(false);
  });
  it('requires confirmation before deleting a period', function () {
    confirm.and.returnValue(false); scope.remove(scope.workPeriods[0]);
    expect(confirm).toHaveBeenCalled();
    expect(scope.workPeriods.length).toBe(2);
  });
  it('deletes a period with the JSON content type required by Spring', function () {
    backend.expect('DELETE', '/fudriver/rest/person/workPeriod', {workPeriodId: 1}, function (headers) {
      return headers['Content-Type'] === 'application/json; charset=UTF-8';
    }).respond({});
    backend.expectGET(periodsUrl).respond([records()[0]]);
    scope.remove(scope.workPeriods[0]);
    backend.flush();
    expect(scope.workPeriods.length).toBe(1);
    expect(scope.notice).toBe('people.timesheet.edit.message.deleted');
    expect(scope.busy).toBe(false);
  });
  it('closes a period with the JSON content type even for a numeric body', function () {
    backend.expectPUT('/fudriver/rest/person/workPeriod/close', '1', function (headers) {
      return headers['Content-Type'] === 'application/json; charset=UTF-8';
    }).respond({});
    backend.expectGET(periodsUrl).respond(records());
    scope.close(scope.workPeriods[0]);
    backend.flush();
    expect(scope.notice).toBe('people.timesheet.edit.message.closed');
  });
  it('adds a period using timestamps on the selected calendar day', function () {
    scope.newPeriod = {start: '16:00', end: '17:00'};
    backend.expectPOST('/fudriver/rest/workTimeRecorder/closed', {
      personId: 2832, startTime: timestamp('16:00:00'), endTime: timestamp('17:00:00')
    }).respond({});
    backend.expectGET(periodsUrl).respond(records());
    scope.addPeriod(); backend.flush();
    expect(scope.notice).toBe('people.timesheet.edit.message.created');
  });
  it('does not overwrite drafts when an RFID event arrives', function () {
    scope.workPeriods[0].end = '11:00';
    callback(JSON.stringify({source: 'EMPLOYEE_WORK_ENDED', id: 2832}));
    root.$digest();
    expect(scope.externalChange).toBe(true);
    expect(scope.workPeriods[0].end).toBe('11:00');
  });
  it('unsubscribes from events when leaving the page', function () {
    scope.$destroy(); expect(unsubscribe).toHaveBeenCalled();
  });
  it('prevents navigation when discarding changes is declined', function () {
    scope.workPeriods[0].end = '11:00'; confirm.and.returnValue(false);
    var event = root.$broadcast('$locationChangeStart');
    expect(event.defaultPrevented).toBe(true);
  });
  it('renders translated text and accessible labels in the actual editor template', function () {
    var template = templateCache.get('time-sheet-editor-test-template');
    if (!template) { pending('Run node test/run-time-sheet-editor-tests.js to compile the actual template.'); return; }
    backend.expectGET('/fudriver/rest/person/2832').respond({imie: 'Jan', nazwisko: 'Testowy'});
    backend.expectGET(periodsUrl).respond(records());
    var element = compile(template)(scope);
    backend.flush(); root.$digest();
    expect(element.text()).toContain('EWIDENCJA CZASU PRACY');
    expect(element.text()).toContain('Czas pracy bez przerw');
    expect(element[0].querySelector('nav').getAttribute('aria-label')).toBe('Ścieżka nawigacji');
    expect(element[0].querySelector('.ts-timeline-period').getAttribute('aria-label')).toContain('Okres pracy 08:00');
    expect(element.text()).not.toContain('people.timesheet.edit.');
    element.scope().$destroy();
  });
  it('offers retry instead of showing a stale form when reload fails', function () {
    backend.expectGET(periodsUrl).respond(500, {});
    scope.refresh(); backend.flush();
    expect(scope.loadFailed).toBe(true);
    expect(scope.loading).toBe(false);
  });
});
