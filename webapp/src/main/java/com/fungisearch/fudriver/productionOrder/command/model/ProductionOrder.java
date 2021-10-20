package com.fungisearch.fudriver.productionOrder.command.model;

import com.fungisearch.fudriver.productionOrder.command.repository.ProductionOrderRepository;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "production_order")
public class ProductionOrder {

    transient ProductionOrderRepository productionOrderRepository;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;

    @Column(name = "warehouse_order_id")
    public int warehouseOrderId;

    @Column(name = "creation_date")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date creationDate;

    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dueDate;

    @Column(name = "warehouse_type_id")
    private int warehouseTypeId;

    @Column(name = "due_amount")
    private int dueAmount;

    @Column(name = "delivered_amount")
    private int deliveredAmount;

    @Column(name = "status")
    private int status;

    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private int version;

    private ProductionOrder() {
    }

    public ProductionOrder(ProductionOrderRepository productionOrderRepository) {
        this.productionOrderRepository = productionOrderRepository;
    }


    private void save() {
        productionOrderRepository.save(this);
    }

    public static class ProductionOrderBuilder {
        private final ProductionOrderRepository productionOrderRepository;
        private int warehouseOrderId;
        private Date creationDate;
        private Date dueDate;
        private int warehouseTypeId;
        private int dueAmount;
        private int deliveredAmount;
        private int status;
        private String description;
        private int version;


        public ProductionOrderBuilder(ProductionOrderRepository productionOrderRepository) {
            this.productionOrderRepository = productionOrderRepository;
        }

        public ProductionOrderBuilder warehouseOrderId(int warehouseOrderId) {
            this.warehouseOrderId = warehouseOrderId;
            return this;
        }

        public ProductionOrderBuilder creationDate(Date creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public ProductionOrderBuilder dueDate(Date dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public ProductionOrderBuilder warehouseTypeId(int warehouseTypeId) {
            this.warehouseTypeId = warehouseTypeId;
            return this;
        }


        public ProductionOrderBuilder dueAmount(int dueAmount) {
            this.dueAmount = dueAmount;
            return this;
        }

        public ProductionOrderBuilder deliveredAmount(int deliveredAmount) {
            this.deliveredAmount = deliveredAmount;
            return this;
        }

        public ProductionOrderBuilder status(int status) {
            this.status = status;
            return this;
        }

        public ProductionOrderBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductionOrderBuilder version(int version) {
            this.version = version;
            return this;
        }

        public void build() {
            ProductionOrder productionOrder = productionOrderRepository.findByWarehouseId(warehouseOrderId);
            if (productionOrder == null) {
                productionOrder = new ProductionOrder(productionOrderRepository);
                productionOrder.warehouseOrderId = warehouseOrderId;
                productionOrder.creationDate = creationDate;
                productionOrder.dueDate = dueDate;
                productionOrder.warehouseTypeId = warehouseTypeId;
                productionOrder.dueAmount = dueAmount;
                productionOrder.deliveredAmount = deliveredAmount;
                productionOrder.status = status;
                productionOrder.description = description;
                productionOrder.version = version;
                productionOrder.save();
            } else {
                productionOrder.productionOrderRepository = productionOrderRepository;
                if (productionOrder.version != version) {
                    productionOrder.version = version;
                    productionOrder.dueAmount = dueAmount;
                    productionOrder.deliveredAmount = deliveredAmount;
                    productionOrder.status = status;
                }
            }
        }
    }
}
