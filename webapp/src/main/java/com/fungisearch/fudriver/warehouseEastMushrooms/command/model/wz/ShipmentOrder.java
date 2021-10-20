package com.fungisearch.fudriver.warehouseEastMushrooms.command.model.wz;

import com.fungisearch.fudriver.warehouseEastMushrooms.command.repository.EastMushroomsWarehouseRepository;
import com.fungisearch.fudriver.warehouseEastMushrooms.query.dao.EastWarehouseDao;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "east_mushrooms_shipment_order")
public class ShipmentOrder {
    transient EastMushroomsWarehouseRepository eastMushroomsWarehouseRepository;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @Column(name = "remote_type_id")
    private int remoteTypeId;

    @Column(name = "remote_type_name")
    private String remoteTypeName;

    @Column(name = "remote_type_weight")
    private int remoteTypeWeight;

    @Column(name = "local_type_id")
    private int localTypeId;

    @Column(name = "local_type_name")
    private String localTypeName;

    @Column(name = "local_type_weight")
    private int localTypeWeight;

    @Column(name = "amount")
    private int amount;

    @Column(name = "ordered_amount")
    private int orderedAmount;

    @Column(name = "deliverd_amount")
    private int deliveredAmount;

    private ShipmentOrder(){}

    public ShipmentOrder(EastMushroomsWarehouseRepository eastMushroomsWarehouseRepository){
        this.eastMushroomsWarehouseRepository = eastMushroomsWarehouseRepository;
    }

    private void save(){
        eastMushroomsWarehouseRepository.save(this);
    }

    public static class ShipmentOrderBuilder{
        private final EastMushroomsWarehouseRepository eastMushroomsWarehouseRepository;
        private final EastWarehouseDao eastWarehouseDao;

        private Shipment shipment;
        private int localTypeId;

        public ShipmentOrderBuilder(EastMushroomsWarehouseRepository eastMushroomsWarehouseRepository, EastWarehouseDao eastWarehouseDao) {
            this.eastMushroomsWarehouseRepository = eastMushroomsWarehouseRepository;
            this.eastWarehouseDao = eastWarehouseDao;
        }

        public ShipmentOrderBuilder shipment(Shipment shipment){
            this.shipment = shipment;
            return this;
        }

        public ShipmentOrderBuilder localTypeId(int localTypeId){
            this.localTypeId = localTypeId;
            return this;
        }

        public void build(){
            ShipmentOrder shipmentOrder = new ShipmentOrder(eastMushroomsWarehouseRepository);
            shipmentOrder.shipment = shipment;

        }

    }
}
