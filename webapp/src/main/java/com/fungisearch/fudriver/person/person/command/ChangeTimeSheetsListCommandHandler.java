package com.fungisearch.fudriver.person.person.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.person.person.command.model.TimeSheet;
import com.fungisearch.fudriver.person.person.command.repository.TimeSheetRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by idea on 07.03.16.
 */
@Service
@Transactional
public class ChangeTimeSheetsListCommandHandler  {

    @Autowired
    private TimeSheetRepository timeSheetRepository;

    @Autowired
    private BeanValidator beanValidator;


    public CommandResult handle(ChangeTimeSheetsListCommand commands) {
        for(ChangeTimeSheetCommand command: commands.commands){
            TimeSheet timeSheet = timeSheetRepository.findOne(command.id);
            timeSheet.setTimeSheetRepository(timeSheetRepository);
            timeSheet.setBeanValidator(beanValidator);

            timeSheet.edit(new TimeSheet.Edit().
                    startDate(command.startDate).
                    endDate(command.endDate).
                    version(command.version));
        }
        return CommandResult.OK;
    }
}
