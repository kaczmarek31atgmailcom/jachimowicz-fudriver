package com.fungisearch.fudriver.person.barcode.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.person.barcode.query.dao.PersonBarcodeDao;
import com.fungisearch.fudriver.zarobki.query.dao.ZarobkiDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResetBarcodesCommandHandler {

private final ZarobkiDao zarobkiDao;
private final PersonBarcodeDao personBarcodeDao;

    public ResetBarcodesCommandHandler(ZarobkiDao zarobkiDao, PersonBarcodeDao personBarcodeDao) {
        this.zarobkiDao = zarobkiDao;
        this.personBarcodeDao = personBarcodeDao;
    }


    public CommandResult handle(ResetBarcodesCommand command){
        zarobkiDao.resetUniq(command.personId);
        personBarcodeDao.deleteUniqs(command.personId);

        return new CommandResult(CommandResult.Status.OK,"Barcodes removed");
    }
}
