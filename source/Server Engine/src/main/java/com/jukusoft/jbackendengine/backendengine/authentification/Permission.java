package com.jukusoft.jbackendengine.backendengine.authentification;

import com.jukusoft.jbackendengine.backendengine.json.JSONConvertable;
import org.json.JSONObject;

/**
 * Created by Justin on 21.06.2015.
 */
public class Permission implements JSONConvertable {

    private String permName = "";
    private String description = "";
    private boolean hasPermission = false;

    public Permission (String permName, String description, boolean hasPermission) {
        this.permName = permName;
        this.description = description;
        this.hasPermission = hasPermission;
    }

    public Permission (String permName, String description) {
        this.permName = permName;
        this.description = description;
    }

    public String getName () {
        return this.permName;
    }

    public String getDescription () {
        return this.description;
    }

    public boolean hasPermission () {
        return this.hasPermission;
    }

    @Override
    public String toJSON() {
        return this.getJSONObject().toString();
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", this.permName);
        jsonObject.put("description", this.description);
        jsonObject.put("hasPermission", this.hasPermission);

        return jsonObject;
    }
}
