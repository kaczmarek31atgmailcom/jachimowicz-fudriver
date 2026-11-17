/* global describe, beforeEach, afterEach, module, inject, it, expect, jasmine, moment, spyOn, pending */
'use strict';

describe('Employee work time card', function () {
  var scope, backend, root, location, callback, unsubscribe, compile, templateCache;
  function stamp(value) { return moment(value).valueOf(); }
  function url(from, to) {
    return '/fudriver/rest/person/workPeriods?endDay=' + to + '&personId=2888&startDay=' + from;
  }
  function records() {
    return [{id: 2, startDate: stamp('2026-11-15T13:00:00'), endDate: stamp('2026-11-15T16:00:00')},
      {id: 1, startDate: stamp('2026-11-15T08:00:23'), endDate: stamp('2026-11-15T10:00:45')},
      {id: 3, startDate: stamp('2026-11-17T09:00:00'), endDate: null}];
  }
  beforeEach(module('frontendApp', function ($provide) {
    unsubscribe = jasmine.createSpy('unsubscribe');
    location = {path: jasmine.createSpy('path')};
    $provide.value('$route', {current: {params: {employeeId: '2888'}}});
    $provide.value('$location', location);
    $provide.value('EventService', {getEvents: function (fn) { callback = fn; return unsubscribe; }});
  }));
  beforeEach(inject(function ($controller, $httpBackend, $rootScope, $compile, $templateCache) {
    spyOn(moment, 'now').and.returnValue(stamp('2026-11-17T12:00:00'));
    root = $rootScope; backend = $httpBackend; scope = root.$new(); compile = $compile; templateCache = $templateCache;
    backend.expectGET('/fudriver/rest/person/2888').respond({imie: 'Jan', nazwisko: 'Testowy', nr: 12});
    backend.expectGET(url('2026-11-11', '2026-11-17')).respond(records());
    $controller('timeSheetCtrl', {$scope: scope});
    backend.flush();
  }));
  afterEach(function () {
    scope.$destroy();
    backend.verifyNoOutstandingExpectation();
    backend.verifyNoOutstandingRequest();
  });
  it('shows exactly seven inclusive days, latest first, with work and break totals', function () {
    expect(scope.days.length).toBe(7);
    expect(scope.days[0].key).toBe('2026-11-17');
    expect(scope.days[6].key).toBe('2026-11-11');
    expect(scope.total.workMinutes).toBe(300);
    expect(scope.total.pauseMinutes).toBe(179);
    expect(scope.total.recordedDays).toBe(2);
    expect(scope.total.periodCount).toBe(3);
    expect(scope.days[0].today).toBe(true);
  });
  it('marks open periods without adding their elapsed time to closed work', function () {
    expect(scope.total.openCount).toBe(1);
    expect(scope.days[0].workMinutes).toBe(0);
    expect(scope.days[0].timeline[0].type).toBe('open');
    expect(scope.days[0].timeline[0].width).toBeCloseTo(12.5, 3);
  });
  it('shows precise break intervals and does not remove two seconds per break', function () {
    var day = scope.days.filter(function (item) { return item.key === '2026-11-15'; })[0];
    var pause = day.timeline.filter(function (bar) { return bar.type === 'pause'; })[0];
    expect(pause.start).toBe(stamp('2026-11-15T10:00:45'));
    expect(pause.end).toBe(stamp('2026-11-15T13:00:00'));
    expect(day.firstTime).toBe('08:00');
    expect(day.lastTime).toBe('16:00');
  });
  it('does not spread a missed historical open period over later calendar days', function () {
    backend.expectGET(url('2026-11-11', '2026-11-17')).respond([
      {id: 4, startDate: stamp('2026-11-14T08:00:00'), endDate: null}
    ]);
    scope.refresh(); backend.flush();
    expect(scope.total.openCount).toBe(1);
    expect(scope.total.recordedDays).toBe(1);
    expect(scope.total.workMinutes).toBe(0);
    expect(scope.days[0].periodCount).toBe(0);
  });
  it('sums seconds before truncating daily minutes and flags overlapping data', function () {
    backend.expectGET(url('2026-11-11', '2026-11-17')).respond([
      {id: 4, startDate: stamp('2026-11-15T08:00:00'), endDate: stamp('2026-11-15T08:00:59')},
      {id: 5, startDate: stamp('2026-11-15T08:00:30'), endDate: stamp('2026-11-15T08:01:29')}
    ]);
    scope.refresh(); backend.flush();
    expect(scope.total.workMinutes).toBe(1);
    expect(scope.hasOverlaps).toBe(true);
  });
  it('filters empty days without changing the range summary', function () {
    scope.filters.showEmpty = false;
    expect(scope.days.filter(scope.visibleDay).length).toBe(2);
    expect(scope.total.workMinutes).toBe(300);
  });
  it('validates missing, reversed and excessively long ranges without sending requests', function () {
    scope.dateFrom = null; expect(scope.rangeError()).toBe('people.timesheet.card.invalid-dates');
    scope.applyRange();
    scope.dateFrom = moment('2026-11-18').toDate();
    expect(scope.rangeError()).toBe('people.timesheet.card.reversed-range'); scope.applyRange();
    scope.dateFrom = moment('2025-01-01').toDate();
    expect(scope.rangeError()).toBe('people.timesheet.card.range-too-long'); scope.applyRange();
  });
  it('loads all days of the selected month including its last day', function () {
    backend.expectGET(url('2026-11-01', '2026-11-30')).respond(records());
    scope.preset('month'); backend.flush();
    expect(scope.days.length).toBe(30);
    expect(scope.days[0].key).toBe('2026-11-30');
    expect(scope.days[29].key).toBe('2026-11-01');
  });
  it('moves to the preceding range without gaps or duplicate days', function () {
    backend.expectGET(url('2026-11-04', '2026-11-10')).respond([]);
    scope.shiftRange(-1); backend.flush();
    expect(scope.days[0].key).toBe('2026-11-10');
    expect(scope.days[6].key).toBe('2026-11-04');
    expect(scope.total.recordedDays).toBe(0);
  });
  it('opens the day editor using the existing route, including an empty day', function () {
    scope.edit(scope.days[6]);
    expect(location.path).toHaveBeenCalledWith('/people/worktime/timeSheet/2888/2026-11-11');
  });
  it('ignores stale responses when a newer date range is already loading', function () {
    scope.dateFrom = moment('2026-11-01').toDate(); scope.dateTo = moment('2026-11-02').toDate();
    backend.expectGET(url('2026-11-01', '2026-11-02')).respond([]); scope.applyRange();
    scope.dateFrom = moment('2026-11-03').toDate(); scope.dateTo = moment('2026-11-04').toDate();
    backend.expectGET(url('2026-11-03', '2026-11-04')).respond([]); scope.applyRange();
    backend.flush(1);
    expect(scope.loading).toBe(true);
    expect(scope.days[0].key).toBe('2026-11-17');
    backend.flush();
    expect(scope.days[0].key).toBe('2026-11-04');
    expect(scope.loading).toBe(false);
  });
  it('shows a translated error key on failure and can retry', function () {
    backend.expectGET(url('2026-11-11', '2026-11-17')).respond(500, {});
    scope.refresh(); backend.flush();
    expect(scope.error).toBe('people.timesheet.card.load-failed');
    expect(scope.loading).toBe(false);
    backend.expectGET(url('2026-11-11', '2026-11-17')).respond(records());
    scope.refresh(); backend.flush();
    expect(scope.error).toBe('');
  });
  it('refreshes for this employee RFID events and unsubscribes on exit', function () {
    backend.expectGET(url('2026-11-11', '2026-11-17')).respond(records());
    callback(JSON.stringify({source: 'EMPLOYEE_WORK_ENDED', id: 2888}));
    root.$digest(); backend.flush();
    scope.$destroy(); expect(unsubscribe).toHaveBeenCalled();
  });
  it('renders the actual translated card with accessible day editing', function () {
    var template = templateCache.get('time-sheet-card-test-template');
    if (!template) { pending('Run the isolated time sheet tests to compile the actual template.'); return; }
    backend.expectGET('/fudriver/rest/person/2888').respond({imie: 'Jan', nazwisko: 'Testowy'});
    backend.expectGET(url('2026-11-11', '2026-11-17')).respond(records());
    var element = compile(template)(scope); backend.flush(); root.$digest();
    expect(element.text()).toContain('Podsumowanie okresu');
    expect(element.text()).toContain('5 godz. 0 min');
    expect(element.text()).not.toContain('people.timesheet.');
    expect(element[0].querySelectorAll('.tc-day').length).toBe(7);
    expect(element[0].querySelector('.tc-edit-day').getAttribute('aria-label')).toBe('Edytuj czas pracy z dnia 17.11.2026');
    element.scope().filters.showEmpty = false; root.$digest();
    expect(element[0].querySelectorAll('.tc-day').length).toBe(2);
    element.scope().$destroy();
  });
});
