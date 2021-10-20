package com.fungisearch.fudriver.common.command;

import javax.persistence.MappedSuperclass;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by marcin on 02.09.16.
 */
@MappedSuperclass
public class BaseEntity {

    private String uuid = UUID.randomUUID().toString();

    public int hashCode() {
        return Objects.hashCode(uuid);
    }

    public boolean equals(Object that) {
        return this == that || that instanceof BaseEntity
                && Objects.equals(uuid, ((BaseEntity) that).uuid);
    }
}
