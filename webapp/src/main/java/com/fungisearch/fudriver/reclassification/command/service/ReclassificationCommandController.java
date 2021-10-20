package com.fungisearch.fudriver.reclassification.command.service;


import com.fungisearch.fudriver.common.command.CommandHandler;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.reclassification.command.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by marcin on 04.02.16.
 */
@RestController
public class ReclassificationCommandController {

    private final ChangeAssignedRodzajCommandHandler changeAssignedRodzajHandler;
    private final UnassignRodzajCommandHandler unassignRodzajCommandHandler;
    private final CommandHandler<ChangeAssignedReclassificationRodzajCommand> changeAssignedReclassificationRodzajCommandCommandHandler;
    private final CommandHandler<TurnDetailOnOffCommand> turnDetailOnOffCommandHandler;
    private final CommandHandler<LocalRemoveZarobkiEntryCommand> deleteHander;
    private final CommandHandler<LocalRemovePaletteCommand> removePaletteHandler;
    private final LocalReclassifyCommandHandler localReclassifyCommandHandler;

    public ReclassificationCommandController(ChangeAssignedRodzajCommandHandler changeAssignedRodzajHandler, UnassignRodzajCommandHandler unassignRodzajCommandHandler, CommandHandler<ChangeAssignedReclassificationRodzajCommand> changeAssignedReclassificationRodzajCommandCommandHandler, CommandHandler<TurnDetailOnOffCommand> turnDetailOnOffCommandHandler, CommandHandler<LocalRemoveZarobkiEntryCommand> deleteHander, CommandHandler<LocalRemovePaletteCommand> removePaletteHandler, LocalReclassifyCommandHandler localReclassifyCommandHandler) {
        this.changeAssignedRodzajHandler = changeAssignedRodzajHandler;
        this.unassignRodzajCommandHandler = unassignRodzajCommandHandler;
        this.changeAssignedReclassificationRodzajCommandCommandHandler = changeAssignedReclassificationRodzajCommandCommandHandler;
        this.turnDetailOnOffCommandHandler = turnDetailOnOffCommandHandler;
        this.deleteHander = deleteHander;
        this.removePaletteHandler = removePaletteHandler;
        this.localReclassifyCommandHandler = localReclassifyCommandHandler;
    }

    @RequestMapping(value = "/rest/reclassification/rodzaj/assign", method = RequestMethod.PUT, consumes = "application/json; charset=UTF-8")
    public CommandResult assignLocalRodzaj(@RequestBody ChangeAssignedRodzajCommand command) {
        CommandResult result = changeAssignedRodzajHandler.handle(command);
        return result;
    }

    @RequestMapping(value = "/rest/reclassification/rodzaj/unassign", method = RequestMethod.PUT, consumes = "application/json; charset=UTF-8")
    public CommandResult unassignLocalRodzaj(@RequestBody UnassignRodzajCommand command) {
        return unassignRodzajCommandHandler.handle(command);
    }

    @RequestMapping(value = "/rest/reclassification/detail/rodzaj", method = RequestMethod.PUT, consumes = "application/json; charset=UTF-8")
    public CommandResult updateAssignedRodzaj(@RequestBody ChangeAssignedReclassificationRodzajCommand command) {
        CommandResult result = changeAssignedReclassificationRodzajCommandCommandHandler.handle(command);
        return result;
    }

    @RequestMapping(value = "/rest/reclassification/detail/rodzaj/activeStatus", method = RequestMethod.PUT, consumes = "application/json;charset=UTF-8")
    public CommandResult updateDetailStatus(@RequestBody TurnDetailOnOffCommand command) {
        CommandResult result = turnDetailOnOffCommandHandler.handle(command);
        return result;
    }


    @RequestMapping(value = "/rest/localReclassify", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8")
    public CommandResult reclassifyLocal(@RequestBody LocalReclassifyCommand command) {
        CommandResult result = localReclassifyCommandHandler.handle(command);
        return result;
    }


    @RequestMapping(value = "/rest/localReclassify/zarobkiEntry", method = RequestMethod.DELETE, consumes = "application/json; charset=UTF-8")
    @Transactional
    public CommandResult deleteLocal(@RequestBody LocalRemoveZarobkiEntryCommand command) {
        CommandResult result = deleteHander.handle(command);
        return result;
    }



}
