package com.jukusoft.jbackendengine.backendengine.uniqueID;

/**
 * Created by Justin on 02.07.2015.
 */
public interface IUniqueUserIDGenerator {

    public Long generate (Long serverID);
    public Long generate ();

}
