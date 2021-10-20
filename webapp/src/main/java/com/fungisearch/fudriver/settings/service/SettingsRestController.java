package com.fungisearch.fudriver.settings.service;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.chamberCommands.*;
import com.fungisearch.fudriver.settings.command.companyCommands.CreateCompanyCommand;
import com.fungisearch.fudriver.settings.command.companyCommands.CreateCompanyCommandHandler;
import com.fungisearch.fudriver.settings.command.companyCommands.EditCompanyCommand;
import com.fungisearch.fudriver.settings.command.companyCommands.EditCompanyCommandHandler;
import com.fungisearch.fudriver.settings.command.depotCommands.*;
import com.fungisearch.fudriver.settings.command.myceliumCommands.*;
import com.fungisearch.fudriver.settings.command.reclassifyReasonCommands.AddLocalReclassifyReasonCommand;
import com.fungisearch.fudriver.settings.command.reclassifyReasonCommands.AddLocalReclassifyReasonCommandHandler;
import com.fungisearch.fudriver.settings.command.reclassifyReasonCommands.RemoveLocalReclassifyReasonCommand;
import com.fungisearch.fudriver.settings.command.reclassifyReasonCommands.RemoveLocalReclassifyReasonCommandHandler;
import com.fungisearch.fudriver.settings.command.subsoilCommands.*;
import com.fungisearch.fudriver.settings.query.dao.SettingsDao;
import com.fungisearch.fudriver.settings.query.dto.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/rest/settings", produces = "application/json; charset=UTF8")
public class SettingsRestController {

    private final String JSON = "application/json; charset=UTF-8";

    private final SettingsDao settingsDao;
    private final AddLocalReclassifyReasonCommandHandler addLocalReclassifyReasonCommandHandler;
    private final RemoveLocalReclassifyReasonCommandHandler removeLocalReclassifyReasonCommandHandler;
    private final EditCompanyCommandHandler editCompanyCommandHandler;
    private final CreateCompanyCommandHandler createCompanyCommandHandler;
    private final AddDepotCommandHandler addDepotCommandHandler;
    private final UpdateDepotNameCommandHandler updateDepotNameCommandHandler;
    private final DeleteDepotCommandHandler deleteDepotCommandHandler;
    private final CreateChamberCommandHandler createChamberCommandHandler;
    private final UpdateChamberAreaCommandHandler updateChamberAreaCommandHandler;
    private final UpdateChamberDepotCommandHandler updateChamberDepotCommandHandler;
    private final UpdateChamberCompanyCommandHandler updateChamberCompanyCommandHandler;
    private final UpdateChamberNameCommandHandler updateChamberNameCommandHandler;
    private final DeleteChamberCommandHandler deleteChamberCommandHandler;
    private final CreateSubsoilCommandHandler createSubsoilCommandHandler;
    private final ChangeSubsoilNameCommandHandler changeSubsoilNameCommandHandler;
    private final DeleteSubsoilCommandHandler deleteSubsoilCommandHandler;
    private final CreateMyceliumCommandHandler createMyceliumCommandHandler;
    private final ChangeMyceliumNameCommandHandler changeMyceliumNameCommandHandler;
    private final DeleteMyceliumCommandHandler deleteMyceliumCommandHandler;

    public SettingsRestController(SettingsDao settingsDao, AddLocalReclassifyReasonCommandHandler addLocalReclassifyReasonCommandHandler, RemoveLocalReclassifyReasonCommandHandler removeLocalReclassifyReasonCommandHandler, EditCompanyCommandHandler editCompanyCommandHandler, CreateCompanyCommandHandler createCompanyCommandHandler, AddDepotCommandHandler addDepotCommandHandler, UpdateDepotNameCommandHandler updateDepotNameCommandHandler, DeleteDepotCommandHandler deleteDepotCommandHandler, CreateChamberCommandHandler createChamberCommandHandler, UpdateChamberAreaCommandHandler updateChamberAreaCommandHandler, UpdateChamberDepotCommandHandler updateChamberDepotCommandHandler, UpdateChamberCompanyCommandHandler updateChamberCompanyCommandHandler, UpdateChamberNameCommandHandler updateChamberNameCommandHandler, DeleteChamberCommandHandler deleteChamberCommandHandler, CreateSubsoilCommandHandler createSubsoilCommandHandler, ChangeSubsoilNameCommandHandler changeSubsoilNameCommandHandler, DeleteSubsoilCommandHandler deleteSubsoilCommandHandler, CreateMyceliumCommandHandler createMyceliumCommandHandler, ChangeMyceliumNameCommandHandler changeMyceliumNameCommandHandler, DeleteMyceliumCommandHandler deleteMyceliumCommandHandler) {
        this.settingsDao = settingsDao;
        this.addLocalReclassifyReasonCommandHandler = addLocalReclassifyReasonCommandHandler;
        this.removeLocalReclassifyReasonCommandHandler = removeLocalReclassifyReasonCommandHandler;
        this.editCompanyCommandHandler = editCompanyCommandHandler;
        this.createCompanyCommandHandler = createCompanyCommandHandler;
        this.addDepotCommandHandler = addDepotCommandHandler;
        this.updateDepotNameCommandHandler = updateDepotNameCommandHandler;
        this.deleteDepotCommandHandler = deleteDepotCommandHandler;
        this.createChamberCommandHandler = createChamberCommandHandler;
        this.updateChamberAreaCommandHandler = updateChamberAreaCommandHandler;
        this.updateChamberDepotCommandHandler = updateChamberDepotCommandHandler;
        this.updateChamberCompanyCommandHandler = updateChamberCompanyCommandHandler;
        this.updateChamberNameCommandHandler = updateChamberNameCommandHandler;
        this.deleteChamberCommandHandler = deleteChamberCommandHandler;
        this.createSubsoilCommandHandler = createSubsoilCommandHandler;
        this.changeSubsoilNameCommandHandler = changeSubsoilNameCommandHandler;
        this.deleteSubsoilCommandHandler = deleteSubsoilCommandHandler;
        this.createMyceliumCommandHandler = createMyceliumCommandHandler;
        this.changeMyceliumNameCommandHandler = changeMyceliumNameCommandHandler;
        this.deleteMyceliumCommandHandler = deleteMyceliumCommandHandler;
    }

    @RequestMapping(value = "/massHarvest", method = RequestMethod.GET)
    public Boolean isMassHarvestActive() {
        Boolean result = false;
        if (settingsDao.getValue(22) == 1) {
            result = true;
        }
        return result;
    }

    @RequestMapping(value = "/requiredLeader", method = RequestMethod.GET)
    public Boolean requiredLeader() {
        Boolean result = false;
        if (settingsDao.getValue(23) == 1) {
            result = true;
        }
        return result;
    }

    @RequestMapping(value = "/requiredTrolleyMan", method = RequestMethod.GET)
    public Boolean requiredTrolleyMan() {
        Boolean result = false;
        if (settingsDao.getValue(24) == 1) {
            result = true;
        }
        return result;
    }

    @RequestMapping(value = "/activeBrc", method = RequestMethod.GET)
    public Boolean activeBrc() {
        Boolean result = false;
        if (settingsDao.getValue(25) == 1) {
            result = true;
        }
        return result;
    }

    @RequestMapping(value = "/activeEastMushrooms", method = RequestMethod.GET)
    public Boolean activeEastMushrooms() {
        Boolean result = false;
        if (settingsDao.getValue(27) == 1) {
            result = true;
        }
        return result;
    }


    @RequestMapping(value = "/east-mushrooms-mode", method = RequestMethod.GET)
    public Boolean eastMode() {
        Boolean result = false;
        if (settingsDao.getValue(26) == 1) {
            result = true;
        }
        return result;
    }

    @RequestMapping(value = "/reclassifyReason", method = RequestMethod.GET)
    public Map<Long, String> getReclassifyReasons() {
        return settingsDao.getReclassifyReasons();
    }

    @RequestMapping(value = "/reclassifyReason", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8")
    public CommandResult addReason(@RequestBody AddLocalReclassifyReasonCommand command) {
        return addLocalReclassifyReasonCommandHandler.handle(command);
    }

    @RequestMapping(value = "/reclassifyReason", method = RequestMethod.DELETE, consumes = "application/json; charset=UTF-8")
    public CommandResult addReason(@RequestBody RemoveLocalReclassifyReasonCommand command) {
        return removeLocalReclassifyReasonCommandHandler.handle(command);
    }


    @RequestMapping(value = "/company/{id}", method = RequestMethod.GET)
    public CompanyDto getCompany(@PathVariable(name = "id") Long id) {
        return settingsDao.getCompany(id);
    }

    @RequestMapping(value = "/company", method = RequestMethod.GET)
    public List<CompanyDto> getCompanies() {
        return settingsDao.getCompanies();
    }

    @RequestMapping(value = "/company/{id}", method = RequestMethod.PUT)
    public CommandResult updateCompanyDetails(@PathVariable(name = "id") long id, @RequestBody EditCompanyCommand command) {
        return editCompanyCommandHandler.handle(id, command);
    }

    @PostMapping(value = "/company")
    public CommandResult createCompany(@RequestBody CreateCompanyCommand command){
        return createCompanyCommandHandler.handle(command);
    }

    @RequestMapping(value = "/depot", method = RequestMethod.POST, consumes = JSON)
    public CommandResult createDepot(@RequestBody AddDepotCommand command) {
        return addDepotCommandHandler.handle(command);
    }

    @RequestMapping(value = "/depot/active", method = RequestMethod.GET)
    public Set<DepotDto> getActiveDepots() {
        return settingsDao.getActiveDepots();
    }

    @RequestMapping(value = "/depot", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult updateDepotName(@RequestBody UpdateDepotNameCommand command) {
        return updateDepotNameCommandHandler.handle(command);
    }

    @RequestMapping(value = "/depot/{depotId}", method = RequestMethod.DELETE)
    public CommandResult deleteDepot(@PathVariable(name = "depotId") long depotId) {
        return deleteDepotCommandHandler.handle(depotId);
    }

    @RequestMapping(value = "/chamber/active", method = RequestMethod.GET)
    public Set<ChamberDto> getActiveChambers() {
        return settingsDao.getActiveChambers();
    }

    @RequestMapping(value = "/chamber", method = RequestMethod.POST, consumes = JSON)
    public CommandResult createChamber(@RequestBody CreateChamberCommand command) {
        return createChamberCommandHandler.handle(command);
    }

    @RequestMapping(value = "/chamber/area", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult updateChamberArea(@RequestBody UpdateChamberAreaCommand command) {
        return updateChamberAreaCommandHandler.handle(command);
    }

    @RequestMapping(value = "/chamber/name", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult updateChamberName(@RequestBody UpdateChamberNameCommand command) {
        return updateChamberNameCommandHandler.handle(command);
    }

    @RequestMapping(value = "/chamber/depot", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult updateChamberDepot(@RequestBody UpdateChamberDepotCommand command) {
        return updateChamberDepotCommandHandler.handle(command);
    }

    @RequestMapping(value = "/chamber/company", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult updateChamberCompany(@RequestBody UpdateChamberCompanyCommand command) {
        return updateChamberCompanyCommandHandler.handle(command);
    }

    @RequestMapping(value = "/chamber/{id}", method = RequestMethod.DELETE)
    public CommandResult deleteChamber(@PathVariable(name = "id") long id) {
        return deleteChamberCommandHandler.handle(id);
    }

    @RequestMapping(value = "/subsoil", method = RequestMethod.POST, consumes = JSON)
    public CommandResult createSubsoil(@RequestBody CreateSubsoilCommand command) {
        return createSubsoilCommandHandler.handle(command);
    }

    @RequestMapping(value = "/subsoil", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult changeSubsoilName(@RequestBody ChangeSubsoilNameCommand command) {
        return changeSubsoilNameCommandHandler.handle(command);
    }

    @RequestMapping(value = "/subsoil/{id}", method = RequestMethod.DELETE)
    public CommandResult deleteSubsoil(@PathVariable(name = "id") long id) {
        return deleteSubsoilCommandHandler.handle(id);
    }

    @RequestMapping(value = "/subsoil/active", method = RequestMethod.GET)
    public Set<SubsoilDto> getActiveSubsoils() {
        return settingsDao.getActiveSubsoils();
    }

    @RequestMapping(value = "/mycelium", method = RequestMethod.POST, consumes = JSON)
    public CommandResult createMycelium(@RequestBody CreateMyceliumCommand command) {
        return createMyceliumCommandHandler.handle(command);
    }

    @RequestMapping(value = "/mycelium/name", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult changeMyceliumName(@RequestBody ChangeMyceliumNameCommand command) {
        return changeMyceliumNameCommandHandler.handle(command);
    }

    @RequestMapping(value = "/mycelium/{id}", method = RequestMethod.DELETE)
    public CommandResult deleteMycelium(@PathVariable(name = "id") long id) {
        return deleteMyceliumCommandHandler.handle(id);
    }

    @RequestMapping(value = "/mycelium/active", method = RequestMethod.GET)
    public Set<MyceliumDto> getActiveMycliums() {
        return settingsDao.getActiveMyceliums();
    }
}
