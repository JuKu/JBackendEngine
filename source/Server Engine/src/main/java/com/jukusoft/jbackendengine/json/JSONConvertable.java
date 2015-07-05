package com.jukusoft.jbackendengine.json;

import org.json.JSONObject;

/**
 * Created by Justin on 19.06.2015.
 */
public interface JSONConvertable {

    public String toJSON ();
    public JSONObject getJSONObject ();

}
