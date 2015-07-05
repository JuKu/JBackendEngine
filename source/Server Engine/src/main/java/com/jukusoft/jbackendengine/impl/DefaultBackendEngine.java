package com.jukusoft.jbackendengine.impl;

import com.jukusoft.jbackendengine.AbstractBackendEngine;
import com.jukusoft.jbackendengine.IEditableBackendEngine;

import java.io.File;

/**
 * Created by Justin on 02.07.2015.
 */
public class DefaultBackendEngine extends AbstractBackendEngine {

    public DefaultBackendEngine () {
        super(new File("./config/"));
    }

    @Override
    public void startEngine(IEditableBackendEngine backendEngine) {
        //
    }

}
