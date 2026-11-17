/* global describe, beforeEach, afterEach, module, inject, it, expect, jasmine, pending */
'use strict';

describe('Employee work time list', function () {
  var scope, backend, root, location, callback, unsubscribe, compile, cache;
  var activeUrl = '/fudriver/rest/person/headers?active=true';
  function people() {
    return [{id: 2888, nr: 20, name: 'Jan', surname: 'Żółć', groupName: 'Brygada 1', active: true, isPresent: true},
      {id: 2832, nr: 2, name: 'Anna', surname: 'Kowalska', groupName: 'Brygada 2', active: true, isPresent: false},
      {id: 2744, nr: 11, name: 'Piotr', surname: 'Nowak', groupName: null, active: true, isPresent: true}];
  }
  beforeEach(module('frontendApp', function ($provide) {
    location = {path: jasmine.createSpy('path')}; unsubscribe = jasmine.createSpy('unsubscribe');
    $provide.value('$location', location);
    $provide.value('EventService', {getEvents: function (fn) { callback = fn; return unsubscribe; }});
  }));
  beforeEach(inject(function ($controller, $httpBackend, $rootScope, $compile, $templateCache) {
    backend = $httpBackend; root = $rootScope; scope = root.$new(); compile = $compile; cache = $templateCache;
    backend.expectGET(activeUrl).respond(people());
    $controller('WorkTimeCtrl', {$scope: scope}); backend.flush();
  }));
  afterEach(function () {
    scope.$destroy(); backend.verifyNoOutstandingExpectation(); backend.verifyNoOutstandingRequest();
  });
  it('loads active employees, counts work status and sorts by surname', function () {
    expect(scope.total).toEqual({all: 3, present: 2, away: 1});
    expect(scope.pagePeople.map(function (p) { return p.id; })).toEqual([2832, 2744, 2888]);
    expect(scope.groups.length).toBe(3);
    expect(scope.loading).toBe(false);
  });
  it('searches names without Polish accents in any word order, as well as IDs and numbers', function () {
    scope.filters.query = 'zolc jan'; scope.applyFilters();
    expect(scope.filteredPeople.length).toBe(1); expect(scope.filteredPeople[0].id).toBe(2888);
    scope.filters.query = '2832'; scope.applyFilters();
    expect(scope.filteredPeople[0].name).toBe('Anna');
    scope.filters.query = '11'; scope.applyFilters();
    expect(scope.filteredPeople[0].id).toBe(2744);
  });
  it('combines group and work status filters without changing source totals', function () {
    scope.filters.group = 'group:Brygada 2'; scope.filters.status = 'present'; scope.applyFilters();
    expect(scope.filteredPeople.length).toBe(0); expect(scope.total.all).toBe(3);
    scope.filters.status = 'away'; scope.applyFilters(); expect(scope.filteredPeople.length).toBe(1);
    scope.clearFilters(); expect(scope.filteredPeople.length).toBe(3);
    scope.filters.group = 'ungrouped'; scope.applyFilters(); expect(scope.filteredPeople[0].id).toBe(2744);
  });
  it('sorts numeric fields numerically and reverses the selected column', function () {
    scope.sortBy('nr'); expect(scope.pagePeople.map(function (p) { return p.nr; })).toEqual([2, 11, 20]);
    expect(scope.sortDirection('nr')).toBe('ascending');
    scope.sortBy('nr'); expect(scope.pagePeople.map(function (p) { return p.nr; })).toEqual([20, 11, 2]);
    expect(scope.sortDirection('nr')).toBe('descending');
  });
  it('paginates large lists and resets the page after changing filters or page size', function () {
    var records = [];
    for (var i = 1; i <= 60; i++) { records.push({id: i, nr: i, name: 'Jan', surname: 'Pracownik', active: true, isPresent: false}); }
    backend.expectGET(activeUrl).respond(records); scope.load(); backend.flush();
    expect(scope.pagePeople.length).toBe(25); expect(scope.pageCount).toBe(3);
    scope.goToPage(3); expect(scope.pagePeople.length).toBe(10); expect(scope.firstResult).toBe(51);
    scope.settings.pageSize = 50; scope.changePageSize(); expect(scope.page).toBe(1); expect(scope.pagePeople.length).toBe(50);
    scope.goToPage(2); scope.filters.query = '60'; scope.applyFilters();
    expect(scope.page).toBe(1); expect(scope.pagePeople.length).toBe(1);
  });
  it('updates counters and filtered rows after RFID events even with string IDs', function () {
    scope.filters.status = 'present'; scope.applyFilters();
    callback(JSON.stringify({source: 'EMPLOYEE_WORK_STARTED', id: '2832'})); root.$digest();
    expect(scope.total.present).toBe(3); expect(scope.filteredPeople.length).toBe(3);
    callback(JSON.stringify({source: 'EMPLOYEE_WORK_ENDED', id: '2888'})); root.$digest();
    expect(scope.total.present).toBe(2); expect(scope.filteredPeople.length).toBe(2);
    callback('not JSON'); root.$digest(); expect(scope.total.present).toBe(2);
  });
  it('retains a newer RFID status when an older HTTP snapshot arrives', function () {
    backend.expectGET(activeUrl).respond(people()); scope.load();
    callback(JSON.stringify({source: 'EMPLOYEE_WORK_ENDED', id: 2888})); root.$digest(); backend.flush();
    expect(scope.people.filter(function (p) { return p.id === 2888; })[0].isPresent).toBe(false);
    expect(scope.total.present).toBe(1);
  });
  it('loads inactive employees only when explicitly requested', function () {
    var records = people(); records.push({id: 123, nr: 1, name: 'Były', surname: 'Pracownik', active: false, isPresent: false});
    scope.filters.includeInactive = true;
    backend.expectGET('/fudriver/rest/person/headers?active=false').respond(records); scope.load(); backend.flush();
    expect(scope.total.all).toBe(4); expect(scope.people[3].active).toBe(false);
  });
  it('shows a translated load error and retries without leaving a loading indicator', function () {
    backend.expectGET(activeUrl).respond(500, {}); scope.load(); backend.flush();
    expect(scope.error).toBe('people.timesheet.list.load-failed'); expect(scope.loading).toBe(false);
    backend.expectGET(activeUrl).respond(people()); scope.load(); backend.flush(); expect(scope.error).toBe('');
  });
  it('opens the existing employee card route and unsubscribes when leaving', function () {
    scope.editTimeSheet(scope.people[0]); expect(location.path).toHaveBeenCalledWith('/people/worktime/2888');
    scope.$destroy(); expect(unsubscribe).toHaveBeenCalled();
  });
  it('renders the actual translated list with accessible status and navigation', function () {
    var template = cache.get('time-sheet-list-test-template');
    if (!template) { pending('Run the isolated time sheet tests to compile the actual template.'); return; }
    backend.expectGET(activeUrl).respond(people());
    var element = compile(template)(scope); backend.flush(); root.$digest();
    expect(element.text()).toContain('Czas pracy zespołu');
    expect(element.text()).toContain('W trakcie pracy');
    expect(element.text()).not.toContain('people.timesheet.');
    expect(element[0].querySelectorAll('tbody tr').length).toBe(3);
    var pageSize = element[0].querySelector('#wl-page-size');
    expect(pageSize.options.length).toBe(3);
    expect(pageSize.options[pageSize.selectedIndex].text).toBe('25');
    expect(element[0].querySelector('#wl-sort').options.length).toBe(5);
    expect(element[0].querySelector('.wl-person-action button').getAttribute('aria-label')).toContain('Anna Kowalska');
    expect(element[0].querySelector('th[aria-sort="ascending"]')).not.toBe(null);
    element.scope().filters.query = 'brak wyników'; element.scope().applyFilters(); root.$digest();
    expect(element.text()).toContain('Brak pasujących pracowników');
    element.scope().$destroy();
  });
});
