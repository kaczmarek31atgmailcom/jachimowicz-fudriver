package com.fungisearch.fudriver.paletteType.command.model;

import com.fungisearch.fudriver.paletteType.command.repository.PaletteTypeRepository;

import javax.persistence.*;

@Entity
@Table(name = "palette_type")
public class PaletteType {

    transient PaletteTypeRepository paletteTypeRepository;

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name ="remote_palette_id")
    public int remotePaletteId;

    @Column(name = "name")
    public String name;

    @Column(name = "active")
    public boolean active;

    @Column(name = "sort_order")
    public int sortOrder;


    public PaletteType(PaletteTypeRepository paletteTypeRepository){
        this.paletteTypeRepository = paletteTypeRepository;
    }
    private PaletteType(){}

    public Integer getId() {
        return id;
    }

    public int getRemotePaletteId() {
        return remotePaletteId;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    private void save(){
        paletteTypeRepository.save(this);
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public static class PaletteTypeBuilder{
        private final PaletteTypeRepository paletteTypeRepository;
        private int remotePaletteId;
        private String name;
        private boolean active;

        public PaletteTypeBuilder(PaletteTypeRepository paletteTypeRepository) {
            this.paletteTypeRepository = paletteTypeRepository;
        }

        public PaletteTypeBuilder remotePaletteId(int remotePaletteId){
            this.remotePaletteId = remotePaletteId;
            return this;
        }

        public PaletteTypeBuilder name(String name){
            this.name = name;
            return this;
        }
        public PaletteTypeBuilder active(boolean active){
            this.active = active;
            return this;
        }

        public void build(){
            PaletteType paletteType = paletteTypeRepository.findByRemotePaletteId(remotePaletteId);
            if(paletteType == null){
                paletteType = new PaletteType(paletteTypeRepository);
                paletteType.remotePaletteId = remotePaletteId;
                paletteType.name = name;
                paletteType.active = active;
                paletteType.sortOrder = 0;
                paletteType.save();
            } else {
                if(paletteType.active != active){
                    paletteType.active = active;
                }
                if(!paletteType.active){
                    paletteType.sortOrder = 0;
                }
                if(!paletteType.name.equals(name)) {
                    paletteType.name = name;
                }
            }
        }
    }
}
