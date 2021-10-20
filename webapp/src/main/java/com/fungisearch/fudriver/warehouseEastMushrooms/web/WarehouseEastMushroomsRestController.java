package com.fungisearch.fudriver.warehouseEastMushrooms.web;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.fileGenerator.eastMushrooms.EastMushroomsPaletteLabelService;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.warehouseCommand.CreateAcceptedWarehousePaletteCommand;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.reclassificationCommand.ReclassifyWarehouseUnitsCommandHandler;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.reclassificationCommand.ReclassifyWarehouseUnitsCommand;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.warehouseCommand.*;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.wzCommand.CreateShipmentCommand;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.wzCommand.CreateShipmentCommandHandler;
import com.fungisearch.fudriver.warehouseEastMushrooms.query.dao.EastWarehouseDao;
import com.fungisearch.fudriver.warehouseEastMushrooms.query.dto.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/east-warehouse", produces = "application/json; charset=UTF-8")
public class WarehouseEastMushroomsRestController {
    private final EastWarehouseDao eastWarehouseDao;
    private final CreateWarehousePaletteCommandHandler createWarehousePaletteCommandHandler;
    private final AssignHarvestPaletteToWarehousePaletteCommandHandler assignHarvestPaletteToWarehousePaletteCommandHandler;
    private final UnAssignHarvestPaletteCommandHandler unAssignHarvestPaletteCommandHandler;
    private final MoveHarvestPaletteBetweenWarehousePalettesCommandHandler moveHarvestPaletteBetweenWarehousePalettesCommandHandler;
    private final AcceptWarehousePaletteToWarehouseCommandHandler acceptWarehousePaletteToWarehouseCommandHandler;
    private final EastMushroomsPaletteLabelService eastMushroomsPaletteLabelService;
    private final CreateShipmentCommandHandler createShipmentCommandHandler;
    private final SendHarvestPalettesToWarehouseCommandHandler sendHarvestPalettesToWarehouseCommandHandler;
    private final ReclassifyWarehouseUnitsCommandHandler reclassifyWarehouseUnitsCommandHandler;
    private final CreateAcceptedWarehousePaletteCommandHandler addEmptyWarehousePaletteCommandHandler;
    private final MoveBoxesBetweenHarvestPalettesCommandHandler moveBoxesBetweenHarvestPalettesCommandHandler;
    private final JoinWarehousePalettesCommandHandler joinWarehousePalettesCommandHandler;
    private final ChangeWarehousePaletteTypeCommandHandler changeWarehousePaletteTypeCommandHandler;
    private final ProxyService proxyService;


    public WarehouseEastMushroomsRestController(EastWarehouseDao eastWarehouseDao, CreateWarehousePaletteCommandHandler createWarehousePaletteCommandHandler, AssignHarvestPaletteToWarehousePaletteCommandHandler assignHarvestPaletteToWarehousePaletteCommandHandler, UnAssignHarvestPaletteCommandHandler unAssignHarvestPaletteCommandHandler, MoveHarvestPaletteBetweenWarehousePalettesCommandHandler moveHarvestPaletteBetweenWarehousePalettesCommandHandler, AcceptWarehousePaletteToWarehouseCommandHandler acceptWarehousePaletteToWarehouseCommandHandler, EastMushroomsPaletteLabelService eastMushroomsPaletteLabelService, CreateShipmentCommandHandler createShipmentCommandHandler, SendHarvestPalettesToWarehouseCommandHandler sendHarvestPalettesToWarehouseCommandHandler, ReclassifyWarehouseUnitsCommandHandler reclassifyWarehouseUnitsCommandHandler, CreateAcceptedWarehousePaletteCommandHandler addEmptyWarehousePaletteCommandHandler, MoveBoxesBetweenHarvestPalettesCommandHandler moveBoxesBetweenHarvestPalettesCommandHandler, JoinWarehousePalettesCommandHandler joinWarehousePalettesCommandHandler, ChangeWarehousePaletteTypeCommandHandler changeWarehousePaletteTypeCommandHandler, ProxyService proxyService) {
        this.eastWarehouseDao = eastWarehouseDao;
        this.createWarehousePaletteCommandHandler = createWarehousePaletteCommandHandler;
        this.assignHarvestPaletteToWarehousePaletteCommandHandler = assignHarvestPaletteToWarehousePaletteCommandHandler;
        this.unAssignHarvestPaletteCommandHandler = unAssignHarvestPaletteCommandHandler;
        this.moveHarvestPaletteBetweenWarehousePalettesCommandHandler = moveHarvestPaletteBetweenWarehousePalettesCommandHandler;
        this.acceptWarehousePaletteToWarehouseCommandHandler = acceptWarehousePaletteToWarehouseCommandHandler;
        this.eastMushroomsPaletteLabelService = eastMushroomsPaletteLabelService;
        this.createShipmentCommandHandler = createShipmentCommandHandler;
        this.sendHarvestPalettesToWarehouseCommandHandler = sendHarvestPalettesToWarehouseCommandHandler;
        this.reclassifyWarehouseUnitsCommandHandler = reclassifyWarehouseUnitsCommandHandler;
        this.addEmptyWarehousePaletteCommandHandler = addEmptyWarehousePaletteCommandHandler;
        this.moveBoxesBetweenHarvestPalettesCommandHandler = moveBoxesBetweenHarvestPalettesCommandHandler;
        this.joinWarehousePalettesCommandHandler = joinWarehousePalettesCommandHandler;
        this.changeWarehousePaletteTypeCommandHandler = changeWarehousePaletteTypeCommandHandler;
        this.proxyService = proxyService;
    }

    @GetMapping(value = "/waiting-harvest-palettes")
    public List<HarvestPaletteDto> getHarvestPalettesWaitingForeptance() {
        return eastWarehouseDao.getWaitingHarvestPalettes();
    }

    @PostMapping(value = "/warehouse-palette")
    public CommandResult createWarehousePalette(@RequestBody CreateWarehousePaletteCommand command) {
        return createWarehousePaletteCommandHandler.handle(command);
    }

    @PostMapping(value = "/warehouse-palette/accepted")
    public CommandResult createAcceptedWarehousePalette(@RequestBody CreateAcceptedWarehousePaletteCommand command) {
        return addEmptyWarehousePaletteCommandHandler.handle(command);
    }

    @PostMapping(value = "/warehouse-palette/create-and-accept")
    public CommandResult createAndAcceptWarehousePalette(@RequestBody SendHaverstPalettesToWarehouseCommand command) {
        return sendHarvestPalettesToWarehouseCommandHandler.handle(command);
    }

    @GetMapping(value = "/warehouse-palette/created")
    public List<CreatedWarehousePaletteDto> getCreatedWarehousePalettes() {
        return eastWarehouseDao.getCreatedWarehousePalettes();
    }

    @GetMapping(value = "/warehouse-palette/created/{paletteId}")
    public CreatedWarehousePaletteDto getCreatedWarehousePalettes(@PathVariable long paletteId) {
        return eastWarehouseDao.getCreatedWarehousePalette(paletteId);
    }

    @PutMapping(value = "/warehouse-palette/assign-harvest-palette")
    public CommandResult assignHarvestPalette(@RequestBody AssignHarvestPaletteToWarehousePaletteCommand command) {
        return assignHarvestPaletteToWarehousePaletteCommandHandler.handle(command);
    }

    @PutMapping(value = "/warehouse-palette/accept-warehouse-palette-to-warehouse")
    public CommandResult acceptWarehousePalette(@RequestBody AcceptWarehousePaletteToWarehouseCommand command) {
        return acceptWarehousePaletteToWarehouseCommandHandler.handle(command);
    }

    @PutMapping(value = "/warehouse-palette/unassign-harvest-palette")
    public CommandResult unassignHarvestPalette(@RequestBody UnAssignHarvestPaletteCommand command) {
        return unAssignHarvestPaletteCommandHandler.handle(command);
    }

    @PutMapping(value = "/warehouse-palette/move-harvest-palette")
    public CommandResult moveHarvestPalette(@RequestBody MoveHarvestPaletteBetweenWarehousePalettesCommand command) {
        return moveHarvestPaletteBetweenWarehousePalettesCommandHandler.handle(command);
    }

    @PutMapping(value = "/warehouse-palette/move-boxes")
    public CommandResult moveBoxesToOtherWarehousePalette(@RequestBody MoveBoxesBetweenWarehousePalettesCommand command) {
        return moveBoxesBetweenHarvestPalettesCommandHandler.handle(command);
    }

    @PutMapping(value = "/warehouse-palette/join-palettes")
    public CommandResult joinWarehousePalettes(@RequestBody JoinWarehousePalettesCommand command){
        return joinWarehousePalettesCommandHandler.handle(command);
    }

    @GetMapping(value = "/warehouse-palette/ready-to-release")
    public List<WarehousePaletteDto> getWarehousePaletteReadyToRelease() {
        return eastWarehouseDao.getPalettesReadyToRelease();
    }

    @GetMapping(value = "/warehouse-palette/ready-to-release/{warehousePaletteId}")
    public WarehousePaletteDto getWarehousePaletteReadyToRelease(@PathVariable (name = "warehousePaletteId") long paletteId) {
        return eastWarehouseDao.getPaletteReadyToRelease(paletteId);
    }

    @PostMapping(value = "/warehouse-palette/print-label/{paletteId}")
    public CommandResult printLabel(@PathVariable(name = "paletteId") long paletteId) {
        eastMushroomsPaletteLabelService.createLabel(paletteId);
        return new CommandResult(CommandResult.Status.OK, "LabelPrinted");
    }

    @PutMapping(value="/warehouse-palette/palette-type")
    public CommandResult changePaletteType(@RequestBody ChangeWarehousePaletteTypeCommand command){
        return changeWarehousePaletteTypeCommandHandler.handle(command);
    }

    @PostMapping(value = "/shipment")
    public CommandResult createShipment(@RequestBody CreateShipmentCommand command) {
        return createShipmentCommandHandler.handle(command);
    }

    @GetMapping(value = "/shipment/header/period/{startDate}/{endDate}")
    public List<ShipmentHeaderDto> getShipmentHeaders(@PathVariable(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                          @PathVariable(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return eastWarehouseDao.getShipmentHeaders(DateUtils.getStartOfDay(startDate), DateUtils.getEndOfDay(endDate));
    }

    @GetMapping(value = "/shipment/header/{shipmentId}")
    public ShipmentHeaderDto getShipmentHeader(@PathVariable(name = "shipmentId") long shipmentId){
        proxyService.sendShipmentToProxy(shipmentId);
        return eastWarehouseDao.getShipmentHeader(shipmentId);
    }


    @GetMapping(value = "/shipment/{shipmentId}/palette")
    public List<ShipmentPaletteDto> getShipmentPalettes(@PathVariable(name = "shipmentId") long shipmentId){
        return eastWarehouseDao.getShipmentPalettes(shipmentId);
    }

    @GetMapping(value = "/wz/{wzId}")
    public WzDto getWz(@PathVariable(name = "wzId") long wzId){
        return eastWarehouseDao.getWz(wzId);
    }

    @GetMapping(value = "/status")
    public List<WarehouseStatusDto> getWarehouseStatus(){
        return eastWarehouseDao.getWarehouseStatus();
    }

    @GetMapping(value = "/warehouseUnit/{pickerId}/{uniqId}")
    public WarehouseUnitDto findWarehouseUnit(@PathVariable(name = "pickerId") int pickerId,
                                             @PathVariable(name = "uniqId") int uniqId){
        return eastWarehouseDao.findWarehouseUnit(uniqId,pickerId);
    }

    @PostMapping(value = "/reclassify")
    public CommandResult reclassify(@RequestBody ReclassifyWarehouseUnitsCommand command){
        return reclassifyWarehouseUnitsCommandHandler.handle(command);
    }

    @GetMapping(value = "/wz/{wzId}/send-to-proxy")
    public void sendToProxy(@PathVariable(name = "wzId") long wzId){
        proxyService.sendShipmentToProxy(wzId);
    }

    @GetMapping(value="/proxytest/{shipmentId}")
    public ProxyShipmentDto testProxy(@PathVariable(name="shipmentId") long shipmentId){
        return eastWarehouseDao.getShipmentForProxy(shipmentId);
    }

}
