package com.fungisearch.fudriver.wozek.command.service;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.wozek.command.CreateTrolleyManCommand;
import com.fungisearch.fudriver.wozek.command.CreateTrolleyManCommandHandler;
import com.fungisearch.fudriver.wozek.command.EditTrolleyManCommand;
import com.fungisearch.fudriver.wozek.command.EditTrolleyManCommandHandler;
import com.fungisearch.fudriver.wozek.query.dao.TrolleyManDao;
import com.fungisearch.fudriver.wozek.query.dto.TrolleyManDto;
import com.fungisearch.fudriver.wozek.query.dto.TrolleyManReportDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rest/trolley-man")
public class TrolleyManRestController {
    private final CreateTrolleyManCommandHandler createTrolleyManCommandHandler;
    private final EditTrolleyManCommandHandler editTrolleyManCommandHandler;
    private final TrolleyManDao trolleyManDao;

    public TrolleyManRestController(CreateTrolleyManCommandHandler createTrolleyManCommandHandler, EditTrolleyManCommandHandler editTrolleyManCommandHandler, TrolleyManDao trolleyManDao) {
        this.createTrolleyManCommandHandler = createTrolleyManCommandHandler;
        this.editTrolleyManCommandHandler = editTrolleyManCommandHandler;
        this.trolleyManDao = trolleyManDao;
    }

    @PostMapping
    public CommandResult createTrolleyMan(@RequestBody CreateTrolleyManCommand command){
        return createTrolleyManCommandHandler.handle(command);
    }

    @PutMapping
    public CommandResult editTrolleyMan(@RequestBody EditTrolleyManCommand command){
        return editTrolleyManCommandHandler.handle(command);
    }

    @GetMapping
    public List<TrolleyManDto> getAllTrolleyMan(){
        return trolleyManDao.getAllTrolleyMan();
    }

    @GetMapping("/report/startDate/{startDate}/endDate/{endDate}")
    public List<TrolleyManReportDto> getTrolleyManReport(@PathVariable(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                         @PathVariable(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        return trolleyManDao.getTrolleyManReport(startDate,endDate);
    }

}
