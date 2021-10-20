package com.fungisearch.fudriver.settings.command.chamberCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.settings.command.model.ChamberFactory;
import com.fungisearch.fudriver.settings.command.model.Company;
import com.fungisearch.fudriver.settings.command.model.CompanyFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateChamberCompanyCommandHandler {
    private final CompanyFactory companyFactory;
    private final ChamberFactory chamberFactory;

    public UpdateChamberCompanyCommandHandler(CompanyFactory companyFactory, ChamberFactory chamberFactory) {
        this.companyFactory = companyFactory;
        this.chamberFactory = chamberFactory;
    }

    public CommandResult handle(UpdateChamberCompanyCommand command){
        Company company = companyFactory.getCompany(command.companyId);
        chamberFactory.find(command.id).edit(new Chamber.Edit().company(company));
        return new CommandResult(CommandResult.Status.OK,"ChamberCompanyUpdated");
    }
}
