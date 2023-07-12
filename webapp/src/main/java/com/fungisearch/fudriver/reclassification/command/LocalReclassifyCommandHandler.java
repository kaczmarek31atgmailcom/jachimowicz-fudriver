package com.fungisearch.fudriver.reclassification.command;

import com.fungisearch.fudriver.audit.command.model.LocalReclassificationLogFactory;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.query.dao.SettingsDao;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiEntry;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Zmiana rodzaju i chłodni dla całej palety
 */
@Service
@Transactional
public class LocalReclassifyCommandHandler{


    @Autowired
    private ZarobkiFactory zarobkiFactory;
    @Autowired
    private SettingsDao settingsDao;
    @Autowired
    private UserService userService;
    @Autowired
    private LocalReclassificationLogFactory localReclassificationLogFactory;
    @Autowired
    private TypeFactory typeFactory;


    public CommandResult handle(LocalReclassifyCommand command) {
        List<ZarobkiEntry> wozek = zarobkiFactory.getWozek(command.paletaNr);
        Type targetType = typeFactory.findById(command.rodzajId);
        for (ZarobkiEntry entry : wozek) {
            //createAuditLog(command,entry);
            entry.reclassify(command.rodzajId, command.cycleId, targetType.getWeight(), targetType.getExportType(),command.trolleyManId);
        }
        return CommandResult.OK;
    }

    private void createAuditLog(LocalReclassifyCommand command, ZarobkiEntry zarobkiEntry) {
        localReclassificationLogFactory.builder()
                .date(new Date())
                .userId(userService.getCurrentUserId())
                .pickerId(zarobkiEntry.getPickerId())
                .uniqId(zarobkiEntry.getUniqId())
                .supplierId(command.supplierId)
                .sourceTypeId(zarobkiEntry.getRodzajId())
                .sourceCycleId(zarobkiEntry.getCycleId())
                .targetCycleId(command.cycleId)
                .targetTypeId(command.rodzajId)
                .build();
    }
}

