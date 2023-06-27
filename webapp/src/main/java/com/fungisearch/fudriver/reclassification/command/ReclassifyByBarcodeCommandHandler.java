package com.fungisearch.fudriver.reclassification.command;

import com.fungisearch.fudriver.audit.command.model.LocalReclassificationLogFactory;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.user.query.dto.UserDto;
import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehouseUnit;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehouseUnitFactory;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiEntry;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ReclassifyByBarcodeCommandHandler {
    private final ZarobkiFactory zarobkiFactory;
    private final UserService userService;
    private final LocalReclassificationLogFactory localReclassificationLogFactory;
    private final TypeFactory typeFactory;
    private final WarehouseUnitFactory warehouseUnitFactory;


    public ReclassifyByBarcodeCommandHandler(ZarobkiFactory zarobkiFactory, UserService userService, LocalReclassificationLogFactory localReclassificationLogFactory, TypeFactory typeFactory, WarehouseUnitFactory warehouseUnitFactory) {
        this.zarobkiFactory = zarobkiFactory;
        this.userService = userService;
        this.localReclassificationLogFactory = localReclassificationLogFactory;
        this.typeFactory = typeFactory;
        this.warehouseUnitFactory = warehouseUnitFactory;
    }

    public CommandResult handle(List<ReclassifyByBarcodeCommand> commands) {
        for (ReclassifyByBarcodeCommand command : commands) {
            Type targetType = typeFactory.findById((long) command.targetTypeId);
            ZarobkiEntry zarobkiEntry = zarobkiFactory.getByPersonAndUniq(command.pickerId,command.uniqId);
            createAuditLog(targetType,zarobkiEntry,command.uniqId);
            zarobkiEntry.reclassify(targetType.getId(), targetType.getWeight(), targetType.getExportType());
            reclassifyAtWarehouse(zarobkiEntry.getUniqId(),zarobkiEntry.getPickerId(),targetType);
        }
        return new CommandResult(CommandResult.Status.OK);
    }

    public void reclassifyAtWarehouse(long uniqId,long pickerId,Type targetType){
        WarehouseUnit warehouseUnit = warehouseUnitFactory.findByUniqAndPicker(uniqId,pickerId);
        if(warehouseUnit != null){
            warehouseUnit.reclassify(targetType.getId());
        }

    }

        public void createAuditLog(Type targetType,ZarobkiEntry zarobkiEntry,long uniqId){
            Type sourceType = typeFactory.findById(zarobkiEntry.rodzajId);
            UserDto user = userService.getCurrentUser();
            localReclassificationLogFactory.builder()
                    .date(new Date())
                    .sourceTypeId(zarobkiEntry.rodzajId)
                    .sourceTypename(sourceType.getName())
                    .sourceTypeWeight(sourceType.getWeight())
                    .targetTypeId(targetType.getId())
                    .targetTypeName(targetType.getName())
                    .targetTypeWeight(targetType.getWeight())
                    .uniqId(uniqId)
                    .userId(zarobkiEntry.getUserId())
                    .login(user.login)
                    .name(user.name)
                    .surname(user.surname)
                    .pickerId((long)zarobkiEntry.getPickerId())
                    .build();
        }

}
