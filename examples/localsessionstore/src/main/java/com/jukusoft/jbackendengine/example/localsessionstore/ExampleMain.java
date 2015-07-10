package com.jukusoft.jbackendengine.example.localsessionstore;

import com.jukusoft.jbackendengine.backendengine.IEditableBackendEngine;
import com.jukusoft.jbackendengine.backendengine.factory.BackendEngineFactory;
import com.jukusoft.jbackendengine.backendengine.sessionstore.ISession;
import com.jukusoft.jbackendengine.backendengine.sessionstore.ISessionStore;
import com.jukusoft.jbackendengine.backendengine.sessionstore.exception.SessionNotFoundException;

/**
 * Created by Justin on 10.07.2015.
 */
public class ExampleMain {

    public static void main (String[] args) {
        //create a new backend engine instance
        IEditableBackendEngine backendEngine = BackendEngineFactory.createNewDefaultBackendEngine();

        //get session store
        ISessionStore sessionStore = backendEngine.getSessionStore();

        //create new session
        ISession session = sessionStore.createNewSession();

        //you can add data to session
        session.putData("userID", 1l + "");

        //you have to commit data to write changes
        session.commitData();

        //get userID
        Long userID = Long.parseLong(session.getData("userID"));

        //inserted date in ms
        Long insertedDate = session.getInsertedDate();

        //get session key
        String sessionKey = session.getSessionKey();

        /*
        * You can also get a session by session key
        */
        try {
            ISession session1 = sessionStore.getSession("session-key");
        } catch (SessionNotFoundException e) {
            backendEngine.getLoggerManager().warn("Couldnt found session with session key " + sessionKey + ".");
        }

        //remove session
        sessionStore.removeSession("session-key");
    }

}
