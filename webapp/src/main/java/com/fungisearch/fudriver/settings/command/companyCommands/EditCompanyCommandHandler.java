package com.fungisearch.fudriver.settings.command.companyCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.Company;
import com.fungisearch.fudriver.settings.command.model.CompanyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by marcin on 14.03.17.
 */
@Service
@Transactional
public class EditCompanyCommandHandler {

    private final CompanyFactory companyFactory;

    @Autowired
    public EditCompanyCommandHandler(CompanyFactory companyFactory){
        this.companyFactory = companyFactory;
    }

    public CommandResult handle(long companyId, EditCompanyCommand command){
        companyFactory.getCompany(companyId)
                .edit(new Company.Edit()
                .name(command.name)
                .street(command.street)
                .city(command.city)
                .email(command.email)
                .nip(command.nip)
                .phoneNo(command.phoneNo)
                .regon(command.regon)
                .ggn(command.ggn));
        return new CommandResult(CommandResult.Status.OK,"Company Data Updated");
    }
}
