package com.fungisearch.fudriver.person.service;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.person.barcode.command.CreateUniqCommand;
import com.fungisearch.fudriver.person.barcode.command.CreateUniqCommandHandler;
import com.fungisearch.fudriver.person.barcode.command.ResetBarcodesCommand;
import com.fungisearch.fudriver.person.barcode.command.ResetBarcodesCommandHandler;
import com.fungisearch.fudriver.person.barcode.query.dao.PersonBarcodeDao;
import com.fungisearch.fudriver.person.barcode.query.dto.PersonBarcodeHeaderDto;
import com.fungisearch.fudriver.person.person.command.*;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by idea on 29.02.16.
 */
@RestController
public class PersonRestService {

    private final EditPersonCommandHandler editPersonHandler;
    private final AddPersonCommandHandler addPersonHandler;
    private final ActivatePersonCommandHandler activateHandler;
    private final InactivatePersonCommandHandler inactivateHandler;
    private final ChangeTimeSheetsListCommandHandler changeTimeSheetsHandler;
    private final CreateUniqCommandHandler createUniqHandler;
    private final RegisterBadgeCommandHandler registerBadgeHandler;
    private final DeleteRfidCommandHandler deleteRfidHandler;
    private final ChangePersonPasswordCommandHandler changePasswordHandler;
    private final DeleteUniqCommandHandler deleteUniqCommandHandler;
    private final PersonBarcodeDao personBarcodeDao;
    private final EditForeignerAlertCommandHandler editForeignerAlertCommandHandler;
    private final ResetBarcodesCommandHandler resetBarcodesCommandHandler;

    public PersonRestService(EditPersonCommandHandler editPersonHandler, AddPersonCommandHandler addPersonHandler, ActivatePersonCommandHandler activateHandler, InactivatePersonCommandHandler inactivateHandler, ChangeTimeSheetsListCommandHandler changeTimeSheetsHandler, CreateUniqCommandHandler createUniqHandler, RegisterBadgeCommandHandler registerBadgeHandler, DeleteRfidCommandHandler deleteRfidHandler, ChangePersonPasswordCommandHandler changePasswordHandler, DeleteUniqCommandHandler deleteUniqCommandHandler, PersonBarcodeDao personBarcodeDao, EditForeignerAlertCommandHandler editForeignerAlertCommandHandler, ResetBarcodesCommandHandler resetBarcodesCommandHandler) {
        this.editPersonHandler = editPersonHandler;
        this.addPersonHandler = addPersonHandler;
        this.activateHandler = activateHandler;
        this.inactivateHandler = inactivateHandler;
        this.changeTimeSheetsHandler = changeTimeSheetsHandler;
        this.createUniqHandler = createUniqHandler;
        this.registerBadgeHandler = registerBadgeHandler;
        this.deleteRfidHandler = deleteRfidHandler;
        this.changePasswordHandler = changePasswordHandler;
        this.deleteUniqCommandHandler = deleteUniqCommandHandler;
        this.personBarcodeDao = personBarcodeDao;
        this.editForeignerAlertCommandHandler = editForeignerAlertCommandHandler;
        this.resetBarcodesCommandHandler = resetBarcodesCommandHandler;
    }

    @RequestMapping(value = "/rest/person", method = RequestMethod.PUT, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseStatus(value = HttpStatus.OK)
    public CommandResult updatePerson(@RequestBody EditPersonCommand command) {
        CommandResult result = editPersonHandler.handle(command);
        return result;
    }

    @RequestMapping(value = "/rest/person", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public CommandResult addPerson(@RequestBody AddPersonCommand command) {
        return addPersonHandler.handle(command);

    }

    @RequestMapping(value = "/rest/person/activate", method = RequestMethod.PUT, consumes = "application/json; charset=UTF-8")
    @ResponseStatus(value = HttpStatus.OK)
    public CommandResult activatePerson(@RequestBody ActivatePersonCommand command) {
        CommandResult result = activateHandler.handle(command);
        return result;
    }

    @RequestMapping(value = "/rest/person/inactivate", method = RequestMethod.PUT, consumes = "application/json; charset=UTF-8")
    @ResponseStatus(value = HttpStatus.OK)
    public CommandResult inactivatePerson(@RequestBody InactivatePersonCommand command) {
        CommandResult result = inactivateHandler.handle(command);
        return result;
    }

    @RequestMapping(value = "/rest/person/timeSheet", method = RequestMethod.PUT, consumes = "application/json; charset=UTF-8")
    public CommandResult changeTimeSheets(@RequestBody List<ChangeTimeSheetCommand> commands) {
        ChangeTimeSheetsListCommand list = new ChangeTimeSheetsListCommand();
        list.commands = commands;
        CommandResult result = changeTimeSheetsHandler.handle(list);
        return result;
    }

    @RequestMapping(value = "/rest/person/uniq", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8")
    public CommandResult createUniq(@RequestBody CreateUniqCommand command) {
        CommandResult result = createUniqHandler.handle(command);
        return result;
    }

    @RequestMapping(value = "/rest/person/uniq", method = RequestMethod.DELETE, consumes = "application/json; charset=UTF-8")
    public CommandResult deleteUniq(@RequestBody DeleteUniqCommand command) {
        CommandResult result = deleteUniqCommandHandler.handle(command);
        return result;
    }


    @RequestMapping(value = "/rest/person/badge", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8")
    public CommandResult registerBadge(@RequestBody RegisterBadgeCommand command) {
        CommandResult result = registerBadgeHandler.handle(command);
        return result;
    }

    @RequestMapping(value = "/rest/person/badge", method = RequestMethod.DELETE, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @Transactional
    public CommandResult deleteBadge(@RequestBody DeleteRfidCommand command) {
        CommandResult result = deleteRfidHandler.handle(command);
        return result;
    }

    @RequestMapping(value = "/rest/person/password", method = RequestMethod.PUT, consumes = "application/json; charset=UTF-8")
    public CommandResult registerBadge(@RequestBody ChangePersonPasswordCommand command) {
        CommandResult result = changePasswordHandler.handle(command);
        return result;
    }

    @RequestMapping(value = "/rest/person/barcode", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public List<PersonBarcodeHeaderDto> getBarcodeHeaders(){
        return personBarcodeDao.findHeaders();
    }

    @RequestMapping(value = "/rest/person/foreignerAlert", method = RequestMethod.PUT, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public CommandResult updateForeignerAlert(@RequestBody EditForeignerAlertCommand command){
        return editForeignerAlertCommandHandler.handle(command);
    }

    @RequestMapping(value = "/rest/person/barcode/reset", method = RequestMethod.PUT, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public CommandResult resetBarcodes(@RequestBody ResetBarcodesCommand command){
        return resetBarcodesCommandHandler.handle(command);
    }


}
