package com.fungisearch.fudriver.type.rest;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.type.command.*;
import com.fungisearch.fudriver.type.query.dao.TypeDao;
import com.fungisearch.fudriver.type.query.dto.TypeDto;
import com.fungisearch.fudriver.type.query.dto.TypeGroupDto;
import com.fungisearch.fudriver.type.query.dto.TypeSizeDto;
import com.fungisearch.fudriver.type.query.dto.TypeSizeInCyclesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
public class TypeRestController {

    private final TypeDao typeDao;
    private final CreateTypeGroupCommandHandler createTypeGroupCommandHandler;
    private final ChangeTypeGroupNameCommandHandler changeTypeGroupNameCommandHandler;
    private final DeleteTypeGroupCommandHandler deleteTypeGroupCommandHandler;
    private final CreateTypeCommandHandler createTypeCommandHandler;
    private final ChangeTypeBoxCommandHandler changeTypeBoxCommandHandler;
    private final ChangeTypeTypeGroupCommandHandler changeTypeTypeGroupCommandHandler;
    private final ChangeTypeTypeSizeCommandHandler changeTypeTypeSizeCommandHandler;
    private final ChangeTypeNameCommandHandler changeTypeNameCommandHandler;
    private final DeleteTypeCommandHandler deleteTypeCommandHandler;
    private final CreateTypeSizeCommandHandler createTypeSizeCommandHandler;
    private final ChangeTypeSizeNameCommandHandler changeTypeSizeNameCommandHandler;
    private final DeleteTypeSizeCommandHandler deleteTypeSizeCommandHandler;

    private final String JSON="application/json; charset=UTF-8";

    @Autowired
    public TypeRestController(TypeDao typeDao, CreateTypeGroupCommandHandler createTypeGroupCommandHandler, ChangeTypeGroupNameCommandHandler changeTypeGroupNameCommandHandler, DeleteTypeGroupCommandHandler deleteTypeGroupCommandHandler, CreateTypeCommandHandler createTypeCommandHandler, ChangeTypeBoxCommandHandler changeTypeBoxCommandHandler, ChangeTypeTypeGroupCommandHandler changeTypeTypeGroupCommandHandler, ChangeTypeTypeSizeCommandHandler changeTypeTypeSizeCommandHandler, ChangeTypeNameCommandHandler changeTypeNameCommandHandler, DeleteTypeCommandHandler deleteTypeCommandHandler, CreateTypeSizeCommandHandler createTypeSizeCommandHandler, ChangeTypeSizeNameCommandHandler changeTypeSizeNameCommandHandler, DeleteTypeSizeCommandHandler deleteTypeSizeCommandHandler) {
        this.typeDao = typeDao;
        this.createTypeGroupCommandHandler = createTypeGroupCommandHandler;
        this.changeTypeGroupNameCommandHandler = changeTypeGroupNameCommandHandler;
        this.deleteTypeGroupCommandHandler = deleteTypeGroupCommandHandler;
        this.createTypeCommandHandler = createTypeCommandHandler;
        this.changeTypeBoxCommandHandler = changeTypeBoxCommandHandler;
        this.changeTypeTypeGroupCommandHandler = changeTypeTypeGroupCommandHandler;
        this.changeTypeTypeSizeCommandHandler = changeTypeTypeSizeCommandHandler;
        this.changeTypeNameCommandHandler = changeTypeNameCommandHandler;
        this.deleteTypeCommandHandler = deleteTypeCommandHandler;
        this.createTypeSizeCommandHandler = createTypeSizeCommandHandler;
        this.changeTypeSizeNameCommandHandler = changeTypeSizeNameCommandHandler;
        this.deleteTypeSizeCommandHandler = deleteTypeSizeCommandHandler;
    }

    @RequestMapping(value = "/rest/type/active", method = RequestMethod.GET, produces = JSON)
    public List<TypeDto> getActiveTypes(){
        return typeDao.getActiveTypes();
    }

    @RequestMapping(value = "/rest/type", method = RequestMethod.POST, produces = JSON , consumes = JSON)
    public CommandResult createType(@RequestBody CreateTypeCommand command){
        return createTypeCommandHandler.handle(command);
    }

    @RequestMapping(value = "/rest/type/{id}", method = RequestMethod.DELETE, produces = JSON)
    public CommandResult deleteType(@PathVariable(value = "id") long id){
        return deleteTypeCommandHandler.handle(id);
    }

    @RequestMapping(value = "/rest/type/name", method = RequestMethod.PUT,produces = JSON, consumes = JSON)
    public CommandResult changeTypename(@RequestBody ChangeTypeNameCommand command){
        return changeTypeNameCommandHandler.handle(command);
    }

    @RequestMapping(value = "/rest/type/box", method = RequestMethod.PUT, produces = JSON, consumes = JSON)
    public CommandResult updateTypeBox(@RequestBody ChangeTypeBoxCommand command){
        return changeTypeBoxCommandHandler.handle(command);
    }

    @RequestMapping(value = "/rest/type/typeGroup", method = RequestMethod.PUT, consumes = JSON, produces = JSON)
    public CommandResult updateTypeGroup(@RequestBody ChangeTypeTypeGroupCommand command){
        return changeTypeTypeGroupCommandHandler.handle(command);
    }

    @RequestMapping(value = "/rest/type/typeSize", method = RequestMethod.PUT, consumes = JSON, produces = JSON)
    public CommandResult updateTypeSize(@RequestBody ChangeTypeTypeSizeCommand command){
        return changeTypeTypeSizeCommandHandler.handle(command);
    }

    @RequestMapping(value = "/rest/typeGroup", method = RequestMethod.POST, consumes = JSON, produces = JSON)
    public CommandResult createTypeGroup(@RequestBody CreateTypeGroupCommand command){
        return createTypeGroupCommandHandler.handle(command);
    }

    @RequestMapping(value = "/rest/typeGroup", method = RequestMethod.PUT, consumes = JSON, produces = JSON)
    public CommandResult changeTypeGroupName(@RequestBody ChangeTypeGroupNameCommand command){
        return changeTypeGroupNameCommandHandler.handle(command);
    }

    @RequestMapping(value = "/rest/typeGroup/{id}", method = RequestMethod.DELETE, produces = JSON)
    public CommandResult deleteTypeGroup(@PathVariable(value = "id") long id){
        return deleteTypeGroupCommandHandler.handle(id);
    }

    @RequestMapping(value = "/rest/typeGroup/active", method = RequestMethod.GET, produces = JSON)
    public List<TypeGroupDto> getActiveTypeGroups(){
        return typeDao.getActiveTypeGroups();
    }

    @RequestMapping(value = "/rest/typeSize", method = RequestMethod.POST, consumes = JSON, produces = JSON)
    public CommandResult createTypeSize(@RequestBody CreateTypeSizeCommand command){
        return createTypeSizeCommandHandler.handle(command);
    }

    @RequestMapping(value = "/rest/typeSize/active", method = RequestMethod.GET, produces = JSON)
    public List<TypeSizeDto> getTypeSizes(){
        return typeDao.getActiveTypeSizes();
    }

    @RequestMapping(value = "/rest/typeSize", method = RequestMethod.PUT, consumes = JSON, produces = JSON)
    public CommandResult changeTypeSizeName(@RequestBody ChangeTypeSizeNameCommand command){
        return changeTypeSizeNameCommandHandler.handle(command);
    }

    @RequestMapping(value = "/rest/typeSize/{id}", method = RequestMethod.DELETE, produces = JSON)
    public CommandResult deleteTypeSize(@PathVariable(value = "id") long id){
        return deleteTypeSizeCommandHandler.handle(id);
    }

    @RequestMapping(value = "rest/typeSize/cycles/{startDate}/{endDate}", method = RequestMethod.GET, produces = JSON)
    public List<TypeSizeInCyclesDto> getTypeSizesInCycles(@PathVariable(value = "startDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
                                                          @PathVariable(value = "endDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate){
        return typeDao.getTypeSizesInCycles(startDate,endDate);
    }

}
