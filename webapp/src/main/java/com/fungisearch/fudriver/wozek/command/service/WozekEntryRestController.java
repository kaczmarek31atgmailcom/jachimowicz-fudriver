package com.fungisearch.fudriver.wozek.command.service;

import com.fungisearch.fudriver.common.command.CommandHandler;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.wozek.command.*;
import com.fungisearch.fudriver.wozek.command.model.QualityStatus;
import com.fungisearch.fudriver.wozek.query.service.WozekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by marcin on 23.02.16.
 */
@RestController
public class WozekEntryRestController {


    @Autowired
    private CommandHandler<AddWozekEntryCommand> addWozekEntryCommandHandler;

    @Autowired
    private CommandHandler<RemoveWozekEntryCommand> removeWozekHandler;

    @Autowired
    private CommitWozekCommandHandler commitWozekHandler;

    @Autowired
    private CommandHandler<CommitWaitingWozekCommand> commitWaitingWozekHandler;

    @Autowired
    private CommandHandler<RejectWozekCommand> rejectWozekHandler;

    @Autowired
    UserService userService;

    @Autowired
    WozekService wozekService;

    @RequestMapping(value="/rest/wozekEntry", method= RequestMethod.POST, consumes="application/json; charset=UTF-8")
    @Transactional
    public CommandResult addWozekEntry(@RequestBody AddWozekEntryCommand command) {
        CommandResult result = addWozekEntryCommandHandler.handle(command);
        return result;
    }

    @RequestMapping(value="/rest/wozek/qualityStatus", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public CommandResult qualityCheck(){
        String body = QualityStatus.values()[0].toString();
        return new CommandResult(1L, CommandResult.Status.OK, body);
    }

    @RequestMapping(value = "/rest/wozekEntry", method = RequestMethod.DELETE, consumes="application/json; charset=UTF-8")
    @Transactional
    public CommandResult deleteWozekEntry(@RequestBody RemoveWozekEntryCommand command){
        CommandResult result = removeWozekHandler.handle(command);
        return result;
    }

    @RequestMapping(value = "/rest/wozek/commit", method = RequestMethod.PUT)
    public CommandResult commitWozek(@RequestBody CommitWozekCommand command){
        CommandResult result = commitWozekHandler.handle(command);
        return result;
    }

    @RequestMapping(value = "/rest/wozek/onHold", method = RequestMethod.PUT)
    public CommandResult onHold(@RequestBody Long wozekId){
        wozekService.onHold(wozekId);
        return CommandResult.OK;
    }

    @RequestMapping(value = "/rest/wozek/activate", method = RequestMethod.PUT)
    public CommandResult activate(@RequestBody Long wozekId){
        wozekService.activate(wozekId);
        return CommandResult.OK;
    }


    @RequestMapping(value = "/rest/waiting/wozek/commit", method = RequestMethod.POST)
    @Transactional
    public CommandResult commitWaitingWozek(@RequestBody CommitWaitingWozekCommand command){
        CommandResult result = commitWaitingWozekHandler.handle(command);
        return result;
    }

    @RequestMapping(value = "/rest/waiting/wozek/reject", method = RequestMethod.POST)
    @Transactional
    public CommandResult rejectWozek(@RequestBody RejectWozekCommand command){
        CommandResult result = rejectWozekHandler.handle(command);
        return result;
    }
}
