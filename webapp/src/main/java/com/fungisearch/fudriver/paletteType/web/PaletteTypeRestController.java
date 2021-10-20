package com.fungisearch.fudriver.paletteType.web;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.paletteType.command.UpdatePaletteSortOrderCommand;
import com.fungisearch.fudriver.paletteType.command.UpdatePaletteSortOrderCommandHandler;
import com.fungisearch.fudriver.paletteType.query.dao.PaletteTypeDao;
import com.fungisearch.fudriver.paletteType.query.dto.PaletteTypeDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rest/palette-type", produces = "application/json; charset=UTF-8")
public class PaletteTypeRestController {

    private final PaletteTypeDao paletteTypeDao;
    private final UpdatePaletteSortOrderCommandHandler updatePaletteSortOrderCommandHandler;

    public PaletteTypeRestController(PaletteTypeDao paletteTypeDao, UpdatePaletteSortOrderCommandHandler updatePaletteSortOrderCommandHandler) {
        this.paletteTypeDao = paletteTypeDao;
        this.updatePaletteSortOrderCommandHandler = updatePaletteSortOrderCommandHandler;
    }

    @GetMapping("/active")
    public List<PaletteTypeDto> getActivePalettes(){
        return paletteTypeDao.getActivePaletteTypes();
    }

    @PutMapping("/sort-order")
    public CommandResult setSortOrder(@RequestBody UpdatePaletteSortOrderCommand command){
        return updatePaletteSortOrderCommandHandler.handle(command);
    }
}
