package com.fungisearch.fudriver.productionOrderLocal.command.model;

import com.fungisearch.fudriver.productionOrderLocal.command.repository.ProductionOrderLocalRepository;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "production_order_local")
public class ProductionOrderLocal {

    transient ProductionOrderLocalRepository orderRepository;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;

    @Column(name = "type_id")
    private int typeId;

    @Column(name = "amount")
    private int amount;

    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dueDate;


    public ProductionOrderLocal(ProductionOrderLocalRepository repository) {
        this.orderRepository = repository;
    }

    private ProductionOrderLocal(){}

    private void save(){
        orderRepository.save(this);
    }

    public void setAmount(int amount){
        this.amount = amount;
    }

    public static class ProductionOrderLocalBuilder{
        private final ProductionOrderLocalRepository repository;
        private int typeId;
        private int amount;
        private Date dueDate;

        public ProductionOrderLocalBuilder(ProductionOrderLocalRepository repository) {
            this.repository = repository;
        }

        public ProductionOrderLocalBuilder typeId(int typeId){
            this.typeId = typeId;
            return this;
        }

        public ProductionOrderLocalBuilder amount(int amount){
            this.amount = amount;
            return this;
        }

        public ProductionOrderLocalBuilder dueDate(Date dueDate){
            this.dueDate = dueDate;
            return this;
        }

        public ProductionOrderLocal build(){
            ProductionOrderLocal order = repository.findByTypeIdAndDay(typeId,dueDate);
            if(order != null){
                order.orderRepository = repository;
            } else {
                order = new ProductionOrderLocal(repository);
                order.typeId = typeId;
                order.dueDate = dueDate;
                order.save();
            }
            order.amount = amount;
            return order;
        }
    }

}
