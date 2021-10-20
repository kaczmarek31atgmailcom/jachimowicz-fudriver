package com.fungisearch.fudriver.settings.command.companyCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.CompanyFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CreateCompanyCommandHandler {

    private final CompanyFactory companyFactory;

    public CreateCompanyCommandHandler(CompanyFactory companyFactory) {
        this.companyFactory = companyFactory;
    }

    public CommandResult handle(CreateCompanyCommand command){
        companyFactory
                .getBuilder()
                .city(command.city)
                .email(command.email)
                .ggn(command.ggn)
                .name(command.name)
                .nip(command.nip)
                .phoneNo(command.phoneNo)
                .regon(command.regon)
                .street(command.street)
                .build();
        return new CommandResult(CommandResult.Status.OK,"CompanyCreated");
    }
}
