package com.fungisearch.fudriver.productionOrderLocal.web;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.productionOrderLocal.command.UpdateProductionOrderLocalCommand;
import com.fungisearch.fudriver.productionOrderLocal.command.UpdateProductionOrderLocalCommandHandler;
import com.fungisearch.fudriver.productionOrderLocal.query.dao.ProductionOrderLocalDao;
import com.fungisearch.fudriver.productionOrderLocal.query.dto.ProductionOrderLocalDto;
import com.fungisearch.fudriver.productionOrderLocal.query.dto.ProductionOrderLocalScanDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rest/productionOrderLocal")
public class ProductionOrderLocalRestService {
    private final ProductionOrderLocalDao productionOrderLocalDao;
    private final UpdateProductionOrderLocalCommandHandler updateProductionOrderLocalCommandHandler;

    public ProductionOrderLocalRestService(ProductionOrderLocalDao productionOrderLocalDao, UpdateProductionOrderLocalCommandHandler updateProductionOrderLocalCommandHandler) {
        this.productionOrderLocalDao = productionOrderLocalDao;
        this.updateProductionOrderLocalCommandHandler = updateProductionOrderLocalCommandHandler;
    }


    @GetMapping("/startDate/{startDate}/endDate/{endDate}")
    public List<ProductionOrderLocalDto> getOrders(@PathVariable(name ="startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                            @PathVariable(name ="endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        return productionOrderLocalDao.getOrders(startDate,endDate);
    }

    @GetMapping("/day/{day}/typeId/{typeId}")
    public ProductionOrderLocalDto getOrder(@PathVariable(name ="day") @DateTimeFormat(pattern = "yyyy-MM-dd") Date day,
                                            @PathVariable(name="typeId") int typeId){
        return productionOrderLocalDao.getOrder(day, typeId);
    }

    @PutMapping
    public CommandResult updateOrder(@RequestBody UpdateProductionOrderLocalCommand command){
        return updateProductionOrderLocalCommandHandler.handle(command);
    }

    @GetMapping("/scanner-order")
    public List<ProductionOrderLocalScanDto> getScannerOrders(){
        return productionOrderLocalDao.getProductionOrdersForScanner();
    }

}
