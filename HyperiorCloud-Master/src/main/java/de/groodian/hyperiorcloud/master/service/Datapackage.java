package de.groodian.hyperiorcloud.master.service;

import java.io.Serializable;
import java.util.ArrayList;

public class Datapackage extends ArrayList<Object> implements Serializable {

    private static final long serialVersionUID = 1884397299414251338L;

    public Datapackage(Object... data) {
        for (Object current : data) {
            this.add(current);
        }
    }

}
