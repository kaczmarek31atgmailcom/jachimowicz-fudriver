package com.fungisearch.fudriver.reclassification.command.service;

import com.fungisearch.fudriver.reclassification.command.model.RodzajSkup;
import com.fungisearch.fudriver.reclassification.command.model.RodzajSkupFactory;
import com.fungisearch.fudriver.reclassification.command.repository.RodzajSkupRepository;
import com.fungisearch.fudriver.reclassification.query.dto.SkupRodzajDto;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by marcin on 03.02.16.
 */
@Service
public class ReclassificationCommandService {

    private final BeanValidator beanValidator;
    private final RodzajSkupRepository rodzajSkupRepository;
    private final RodzajSkupFactory rodzajSkupFactory;

    public ReclassificationCommandService(BeanValidator beanValidator, RodzajSkupRepository rodzajSkupRepository, RodzajSkupFactory rodzajSkupFactory) {
        this.beanValidator = beanValidator;
        this.rodzajSkupRepository = rodzajSkupRepository;
        this.rodzajSkupFactory = rodzajSkupFactory;
    }


    public void saveRodzajeSkup(List<SkupRodzajDto> rodzajeDto) {
        for (SkupRodzajDto dto : rodzajeDto) {
            rodzajSkupFactory.getBuilder()
                    .description(dto.description)
                    .groupId(dto.typeGroupId)
                    .groupName(dto.typeGroupName)
                    .name(dto.name)
                    .remoteId(dto.remoteId)
                    .weight(dto.weight)
                    .active(dto.active)
                    .build();
        }
    }

    public void updateRodzajeSkup(List<SkupRodzajDto> rodzajeDto) {
        for (SkupRodzajDto dto : rodzajeDto) {
            RodzajSkup rodzaj = rodzajSkupFactory.findById(dto.id);
            rodzaj.setName(dto.name);
            rodzaj.setRemoteId(dto.remoteId);
            rodzaj.setWeight(dto.weight);
            rodzaj.setDescription(dto.description);
            rodzaj.setGroupId(dto.typeGroupId);
            rodzaj.setGroupName(dto.typeGroupName);
            rodzaj.setActive(dto.active);
        }
    }

    public void removeNonExistingTypes(List<SkupRodzajDto> typesOnSkup) {
        List<RodzajSkup> rodzajeLokalne = rodzajSkupFactory.findAll();
        List<RodzajSkup> typesToBeRemoved = rodzajeLokalne
                .stream()
                .filter(rl -> !isTypeOnSkup(typesOnSkup, rl.getRemoteId()))
                .collect(Collectors.toList());
        typesToBeRemoved.forEach(RodzajSkup::remove);
    }

    private boolean isTypeOnSkup(List<SkupRodzajDto> typesOnSkup, long remoteId) {
        return typesOnSkup
                .stream()
                .anyMatch(t -> t.id.equals(remoteId));
    }
}
