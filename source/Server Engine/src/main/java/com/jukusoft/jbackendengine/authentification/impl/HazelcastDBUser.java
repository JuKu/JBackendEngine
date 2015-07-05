package com.jukusoft.jbackendengine.authentification.impl;

import com.jukusoft.jbackendengine.authentification.IUser;
import com.jukusoft.jbackendengine.authentification.IUserManager;
import com.jukusoft.jbackendengine.authentification.exception.UserLockException;
import com.jukusoft.jbackendengine.authentification.exception.UserNotFoundException;
import com.jukusoft.jbackendengine.phonenumber.PhoneNumberUtils;
import com.jukusoft.jbackendengine.phonenumber.exception.InvalidPhoneNumberException;
import com.jukusoft.jbackendengine.validation.UsernameValidator;
import com.jukusoft.jbackendengine.validation.exception.InvalidUsernameException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Justin on 02.07.2015.
 */
public class HazelcastDBUser implements IUser {

    private IUserManager userManager = null;
    private Long userID = 0l;
    private String name = "";
    private String telephoneNumber = "";
    private String countryCode = "";
    private Map<String,String> profileFields = new HashMap<String,String>();
    private Long lastLogin = 0l;
    private boolean locked = false;
    private boolean temporaryLocked = false;
    private String passwordHash = "";
    private Long dbInserted = 0l;
    private int buildNumber = 2;

    public HazelcastDBUser (IUserManager userManager) {
        this.userManager = userManager;
    }

    public HazelcastDBUser () {
        //
    }

    @Override
    public Long getUserID() {
        return this.userID;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) throws InvalidUsernameException, UserNotFoundException {
        if (UsernameValidator.isValide(name)) {
            this.name = name;

            if (this.userManager != null) {
                this.userManager.updateUser(this.userID, this);
            }
        } else {
            throw new InvalidUsernameException("invalid username " + name + ".");
        }
    }

    @Override
    public void setNameWithoutValidation(String name) throws UserNotFoundException {
        this.name = name;

        if (this.userManager != null) {
            this.userManager.updateUser(this.userID, this);
        }
    }

    @Override
    public String getTelephoneNumber() {
        return this.telephoneNumber;
    }

    @Override
    public void setTelephoneNumber(String telephoneNumber, String countryCode) throws InvalidPhoneNumberException, UserNotFoundException {
        if (!PhoneNumberUtils.isValidePhoneNumber(telephoneNumber, countryCode)) {
            throw new InvalidPhoneNumberException("phone number " + telephoneNumber + " isnt valide for country code " + countryCode + ".");
        }

        this.telephoneNumber = telephoneNumber;

        if (this.userManager != null) {
            this.userManager.updateUser(this.userID, this);
        }
    }

    @Override
    public String getProfileField(String profileField) {
        return this.profileFields.get(profileField);
    }

    @Override
    public void setProfileField(String profileField, String value) {
        this.profileFields.put(profileField, value);
    }

    @Override
    public List<Map.Entry<String, String>> getAllProfileFields() {
        List<Map.Entry<String,String>> profileFields = new ArrayList<Map.Entry<String,String>>();

        for (Map.Entry<String,String> entry : this.profileFields.entrySet()) {
            profileFields.add(entry);
        }

        return profileFields;
    }

    @Override
    public void commitProfileFields() throws UserNotFoundException {
        if (this.userManager != null) {
            this.userManager.updateUser(this.userID, this);
        }
    }

    @Override
    public boolean isLocked() {
        return this.locked;
    }

    @Override
    public void lock() throws UserLockException, UserNotFoundException {
        this.locked = true;

        if (this.userManager != null) {
            this.userManager.lockUser(this.userID);
        } else {
            throw new UserLockException("user manager is null.");
        }
    }

    @Override
    public void unlock() throws UserLockException, UserNotFoundException {
        this.locked = false;

        if (this.userManager != null) {
            this.userManager.unlockUser(this.userID);
        } else {
            throw new UserLockException("user manager is null");
        }
    }

    @Override
    public boolean isTemporaryLocked() {
        if (this.userManager != null) {
            return this.userManager.isTemporaryLocked(this.userID);
        } else {
            return this.temporaryLocked;
        }
    }

    @Override
    public void lockTemporary(Long lockTime) throws UserLockException {
        if (this.userManager != null) {
            this.userManager.lockTemporaryUser(this.userID, lockTime);
        } else {
            throw new UserLockException("user manager is null.");
        }
    }

    @Override
    public void unlockTemporary() throws UserLockException {
        if (this.userManager != null) {
            this.userManager.unlockTemporaryUser(this.userID);
        } else {
            throw new UserLockException("user manager is null.");
        }
    }

    @Override
    public void commit() throws UserNotFoundException {
        if (this.userManager != null) {
            this.userManager.updateUser(this.userID, this);
        }
    }

    public void loadFromJSON (JSONObject jsonObject) {
        this.userID = jsonObject.getLong("userID");
        this.telephoneNumber = jsonObject.getString("telephoneNumber");
        this.name = jsonObject.getString("name");
        this.passwordHash = jsonObject.getString("passwordHash");
        this.dbInserted = jsonObject.getLong("dbInserted");

        if (jsonObject.has("locked") && jsonObject.getBoolean("locked")) {
            this.locked = true;
        } else {
            this.locked = false;
        }

        this.buildNumber = jsonObject.getInt("buildNumber");
        this.lastLogin = jsonObject.getLong("lastLogin");
        this.countryCode = jsonObject.getString("countryCode");
    }

    @Override
    public void access() {

    }

    @Override
    public String toJSON() {
        return this.getJSONObject().toString();
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID", this.userID);
        jsonObject.put("telephoneNumber", this.telephoneNumber);
        jsonObject.put("name", this.name);
        jsonObject.put("passwordHash", this.passwordHash);
        jsonObject.put("dbInserted", this.dbInserted);
        jsonObject.put("locked", this.locked);
        jsonObject.put("buildNumber", this.buildNumber);
        jsonObject.put("lastLogin", this.lastLogin);
        jsonObject.put("countryCode", this.countryCode);

        JSONArray jsonArray = new JSONArray();

        for (Map.Entry<String,String> entry : this.profileFields.entrySet()) {
            JSONObject jsonObject1 = new JSONObject();

            jsonObject1.put("key", entry.getKey());
            jsonObject1.put("value", entry.getValue());

            jsonArray.put(jsonObject1.toString());
        }

        jsonObject.put("profilefields", jsonArray.toString());

        return jsonObject;
    }
}
