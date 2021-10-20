package com.fungisearch.fudriver.paletteType.command.model;

import com.fungisearch.fudriver.paletteType.command.repository.PaletteTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class PaletteTypeFactory {
    private final PaletteTypeRepository paletteTypeRepository;

    public PaletteTypeFactory(PaletteTypeRepository paletteTypeRepository) {
        this.paletteTypeRepository = paletteTypeRepository;
    }

    public PaletteType.PaletteTypeBuilder getBuilder(){
        return new PaletteType.PaletteTypeBuilder(paletteTypeRepository);
    }

    public PaletteType find(int paletteId) {
        PaletteType paletteType = paletteTypeRepository.find(paletteId);
        if(paletteType != null){
            paletteType.paletteTypeRepository = paletteTypeRepository;
        }
        return paletteType;
    }
}
