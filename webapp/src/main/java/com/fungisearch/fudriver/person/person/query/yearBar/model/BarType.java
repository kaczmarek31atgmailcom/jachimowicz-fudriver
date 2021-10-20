package com.fungisearch.fudriver.person.person.query.yearBar.model;

/**
 * Created by marcin on 09.01.16.
 */
public enum BarType {
    ACTIVE,
    INACTIVE,
    PAUSE;

    @Override
    public String toString() {
        switch (this) {
            case ACTIVE:
                return "active";
            case INACTIVE:
                return "inactive";
            case PAUSE:
                return "pause";
        }
        return null;
    }


    }
