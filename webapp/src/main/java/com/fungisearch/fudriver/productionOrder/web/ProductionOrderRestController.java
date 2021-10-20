package com.fungisearch.fudriver.productionOrder.web;

import com.fungisearch.fudriver.productionOrder.query.dao.ProductionOrderDao;
import com.fungisearch.fudriver.productionOrder.query.dto.OrderProgressBarDto;
import com.fungisearch.fudriver.productionOrder.query.dto.ProductionOrderDto;
import com.fungisearch.fudriver.productionOrder.query.dto.ProductionOrderScanDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rest/productionOrder")
public class ProductionOrderRestController {
    private final ProductionOrderDao productionOrderDao;

    public ProductionOrderRestController(ProductionOrderDao productionOrderDao) {
        this.productionOrderDao = productionOrderDao;
    }

    @GetMapping("/{startDate}/{endDate}")
    public List<ProductionOrderDto> getOrders(@PathVariable(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                              @PathVariable(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return productionOrderDao.getOrders(startDate, endDate);
    }

    @GetMapping("/order-bars")
    public List<OrderProgressBarDto> getOrderBars(){
        return productionOrderDao.getProductionOrderStatus();
    }

    @GetMapping("scanner-order")
    public List<ProductionOrderScanDto> getScannerOrders(){
        return productionOrderDao.getProductionOrdersForScanner();
    }
}
