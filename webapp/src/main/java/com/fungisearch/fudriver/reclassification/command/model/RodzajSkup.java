package com.fungisearch.fudriver.reclassification.command.model;

import com.fungisearch.fudriver.reclassification.command.repository.RodzajSkupRepository;
import com.fungisearch.fudriver.type.command.model.Type;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "skup_rodzaj")
public class RodzajSkup {

    transient RodzajSkupRepository rodzajSkupRepository;

    public void setRemoteId(Long remoteId) {
        this.remoteId = remoteId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setActive(Boolean active) {
        this.active = active;
        if(!active){
            this.remoteId = 0L;
            this.weight = 0.0;
            this.groupName = "";
            this.groupId = 0L;
            //this.assignedTypes = new HashSet<>();
        }
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="remote_id")
    @NotNull
    private Long remoteId;

    @Column(name="name")
    @NotEmpty
    private String name;

    @Column(name="weight")
    @NotNull
    private Double weight;

    @Column(name="description")
    private String description;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "active")
    private Boolean active;

    //@OneToMany(targetEntity = Type.class, fetch = FetchType.LAZY, mappedBy = "rodzajSkup")
    //private Set<Type> assignedTypes = new HashSet<>();

    private RodzajSkup(){}

    public RodzajSkup(RodzajSkupRepository rodzajSkupRepository){
        this.rodzajSkupRepository = rodzajSkupRepository;
    }

    private void save(){
        rodzajSkupRepository.save(this);
    }
    public Long getId() {
        return id;
    }
    public Long getRemoteId() {
        return remoteId;
    }
    public String getName() {
        if(name != null) {
            return name;
        }
        return new String();
    }
    public Double getWeight() {
        if(weight != null) {
           return weight;
        }
        return new Double(0);
    }
    public String getDescription() {
        return description;
    }
    public Long getGroupId() {
        return groupId;
    }
    public String getGroupName() {
        return groupName;
    }

    public RodzajSkupRepository getRodzajSkupRepository() {
        return rodzajSkupRepository;
    }

    public Boolean getActive() {
        return active;
    }

    //public Set<Type> getAssignedTypes() {
//        return assignedTypes;
//    }

    public void remove(){

        //rodzajSkupRepository.remove(this);
        this.active = false;
    }

    public static class RodzajSkupBuilder{
        private final RodzajSkupRepository rodzajSkupRepository;
        private Long remoteId;
        private String name;
        private Double weight;
        private String description;
        private Long groupId;
        private String groupName;
        private Boolean active;

        public RodzajSkupBuilder(RodzajSkupRepository rodzajSkupRepository) {
            this.rodzajSkupRepository = rodzajSkupRepository;
        }

        public RodzajSkupBuilder remoteId(long remoteId){
            this.remoteId = remoteId;
            return this;
        }

        public RodzajSkupBuilder name(String name){
            this.name = name;
            return this;
        }

        public RodzajSkupBuilder weight(Double weight){
            this.weight = weight;
            return this;
        }

        public RodzajSkupBuilder description(String description){
            this.description = description;
            return this;
        }

        public RodzajSkupBuilder groupId(Long groupId){
            this.groupId = groupId;
            return this;
        }

        public RodzajSkupBuilder groupName(String groupName){
            this.groupName = groupName;
            return this;
        }

        public RodzajSkupBuilder active(Boolean active){
            this.active = active;
            return this;
        }

        public RodzajSkup build(){
            RodzajSkup rodzajSkup = new RodzajSkup(rodzajSkupRepository);
            rodzajSkup.description = description;
            rodzajSkup.groupId = groupId;
            rodzajSkup.groupName = groupName;
            rodzajSkup.name = name;
            rodzajSkup.remoteId = remoteId;
            rodzajSkup.weight = weight;
            rodzajSkup.active = active;
            rodzajSkup.save();
            return rodzajSkup;
        }

        public RodzajSkup buildDummy(){
            RodzajSkup rodzajSkup = new RodzajSkup();
            rodzajSkup.description = "";
            rodzajSkup.groupId = 0L;
            rodzajSkup.groupName = "";
            rodzajSkup.name = "";
            rodzajSkup.remoteId = 0L;
            rodzajSkup.weight = 0.00;
            rodzajSkup.active = false;
            return rodzajSkup;
        }
    }
}
