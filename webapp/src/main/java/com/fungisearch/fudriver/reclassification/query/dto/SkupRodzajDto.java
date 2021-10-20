package com.fungisearch.fudriver.reclassification.query.dto;

/**
 * Created by marcin on 02.02.16.
 */
public class SkupRodzajDto {
    public Long id;
    public Long remoteId;
    public String name;
    public String description;
    public Double weight;
    public Long typeGroupId;
    public String typeGroupName;
    public boolean active;


    @Override
    public String toString() {
        return "SkupRodzajDto{" +
                "id=" + id +
                ", remoteId=" + remoteId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", weight=" + weight +
                ", typeGroupId=" + typeGroupId +
                ", typeGroupName='" + typeGroupName + '\'' +
                ", active=" + active +
                '}';
    }
}
