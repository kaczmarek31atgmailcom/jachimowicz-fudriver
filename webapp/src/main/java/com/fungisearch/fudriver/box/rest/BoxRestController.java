package com.fungisearch.fudriver.box.rest;

import com.fungisearch.fudriver.box.command.*;
import com.fungisearch.fudriver.box.query.dao.BoxDao;
import com.fungisearch.fudriver.box.query.dto.BoxDto;
import com.fungisearch.fudriver.common.command.CommandResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rest/box", produces = "application/json; charset=UTF-8")
public class BoxRestController {
    private final CreateBoxCommandHandler createTypeGroupCommandHandler;
    private final UpdateBoxNameCommandHandler updateBoxNameCommandHandler;
    private final DeleteBoxCommandHandler deleteBoxCommandHandler;
    private final BoxDao boxDao;
    private final String JSON = "application/json; charset=UTF-8";

    @Autowired
    public BoxRestController(CreateBoxCommandHandler createTypeGroupCommandHandler, UpdateBoxNameCommandHandler updateBoxNameCommandHandler, DeleteBoxCommandHandler deleteBoxCommandHandler, BoxDao boxDao) {
        this.createTypeGroupCommandHandler = createTypeGroupCommandHandler;
        this.updateBoxNameCommandHandler = updateBoxNameCommandHandler;
        this.deleteBoxCommandHandler = deleteBoxCommandHandler;
        this.boxDao = boxDao;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = JSON)
    public CommandResult createBox(@RequestBody CreateBoxCommand command){
        return createTypeGroupCommandHandler.handle(command);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = JSON)
    public CommandResult updateName(@RequestBody ChangeBoxNameCommand command){
        return updateBoxNameCommandHandler.handle(command);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public CommandResult delete(@PathVariable(name = "id") long id){
        return deleteBoxCommandHandler.handle(id);
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    List<BoxDto> getActiveBoxes(){
        return boxDao.findActive();
    }
}
