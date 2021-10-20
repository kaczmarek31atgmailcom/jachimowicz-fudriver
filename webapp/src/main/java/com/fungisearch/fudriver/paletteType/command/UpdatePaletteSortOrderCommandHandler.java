package com.fungisearch.fudriver.paletteType.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.paletteType.command.model.PaletteTypeFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdatePaletteSortOrderCommandHandler {
    private final PaletteTypeFactory paletteTypeFactory;

    public UpdatePaletteSortOrderCommandHandler(PaletteTypeFactory paletteTypeFactory) {
        this.paletteTypeFactory = paletteTypeFactory;
    }

    public CommandResult handle(UpdatePaletteSortOrderCommand command){
        command.palettes.forEach(p -> paletteTypeFactory.find(p.paletteId).setSortOrder(p.sortOrder));
        return new CommandResult(CommandResult.Status.OK);
    }
}
