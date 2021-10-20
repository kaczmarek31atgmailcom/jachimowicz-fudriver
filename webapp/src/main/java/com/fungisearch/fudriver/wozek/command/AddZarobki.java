package com.fungisearch.fudriver.wozek.command;

import com.fungisearch.fudriver.chamber.query.dao.ChamberDao;
import com.fungisearch.fudriver.settings.query.dao.SettingsDao;
import com.fungisearch.fudriver.type.query.dao.TypeDao;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehouseUnit;
import com.fungisearch.fudriver.wozek.command.model.WozekEntry;
import com.fungisearch.fudriver.wozek.command.model.WozekEntryFactory;
import com.fungisearch.fudriver.wozek.command.repository.WozekRepository;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiEntry;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by marcin on 21.04.16.
 */
@Service
public class AddZarobki {

    @Autowired
    private WozekRepository wozekRepository;

    @Autowired
    private TypeDao typeDao;

    @Autowired
    private ChamberDao chamberDao;

    @Autowired
    private ZarobkiFactory zarobkiFactory;

    @Autowired
    private WozekEntryFactory wozekEntryFactory;

    @Autowired
    private SettingsDao settingsDao;

    public void addZarobkiWarehouseUnit(WarehouseUnit warehouseUnit){
        Long exportId = typeDao.findExportId(warehouseUnit.getLocalTypeId());
        Double typeWeight = typeDao.findWeight(warehouseUnit.getLocalTypeId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Long timeshort = Long.parseLong(sdf.format(new Date()));
        Double export = 0.0;
        Double kraj = 0.0;
        Double inne = 0.0;
        if (exportId == 1) {
            kraj = typeWeight;
        }
        if (exportId == 2) {
            export = typeWeight;
        }
        if (exportId == 3) {
            inne = typeWeight;
        }
        ZarobkiEntry zarobkiEntry = zarobkiFactory.builder().
                pickerId(warehouseUnit.getPickerId()).
                export(export).
                kraj(kraj).
                inne(inne).
                cycleId(warehouseUnit.getCycleId()).
                harvestTime(warehouseUnit.getHarvestTime()).
                rodzajId(warehouseUnit.getLocalTypeId()).
                payed(false).
                trolleyId(warehouseUnit.getHarvestPaletteId()).
                trollyeManId(warehouseUnit.getTrolleyManId()).
                timeshort(timeshort).
                userId(warehouseUnit.getScannerManId()).
                uniqId(warehouseUnit.getUniqId()).
                ilosc(typeWeight)
                .workingMinutes(0L)
                .additionalMinutes(0L)

                .build();
        zarobkiEntry.create();


    }

    public void addZarobki(Long wozekId) {
        List<WozekEntry> wozekEntries = wozekRepository.getEntriesForWozekId(wozekId);
        if (wozekEntries.size() > 0) {
            Long exportId = typeDao.findExportId(wozekEntries.get(0).getRodzajId());
            Double typeWeight = typeDao.findWeight(wozekEntries.get(0).getRodzajId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            Long timeshort = Long.parseLong(sdf.format(new Date()));
            Map<Long, Long> cycleToDepot = chamberDao.getCycleDepotMapping();
            for (WozekEntry wozek : wozekEntries) {
                double export = 0;
                double kraj = 0;
                double inne = 0;
                if (exportId == 1) {
                    kraj = typeWeight;
                }
                if (exportId == 2) {
                    export = typeWeight;
                }
                if (exportId == 3) {
                    inne = typeWeight;
                }

                ZarobkiEntry zarobkiEntry = zarobkiFactory.builder().
                        pickerId(wozek.getPickerId()).
                        export(export).
                        kraj(kraj).
                        inne(inne).
                        cycleId(wozek.getCykleId()).
                        harvestTime(new Date()).
                        rodzajId(wozek.getRodzajId()).
                        payed(false).
                        trolleyId(wozek.getWozekNr()).
                        timeshort(timeshort).
                        userId(wozek.getWagowyId()).
                        uniqId(wozek.getUniqId()).
                        ilosc(typeWeight).
                        leaderId(wozek.getBrygadzistaId()).
                        trollyeManId(wozek.getWozkowyId())
                        .qualityManId(wozek.getJakoscowiecId())
                        .qualityCheckStatus(wozek.getQualityStatus().getIndex())
                        .build();
                zarobkiEntry.create();


                wozekEntryFactory.get(wozek.getId()).delete();
            }
        }
    }
}
