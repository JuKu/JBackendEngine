package com.jukusoft.jbackendengine.authentification.impl;

import com.hazelcast.cache.ICache;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.jukusoft.jbackendengine.IBackendEngine;
import com.jukusoft.jbackendengine.authentification.*;
import com.jukusoft.jbackendengine.authentification.exception.PhoneNumberNotFoundException;
import com.jukusoft.jbackendengine.authentification.exception.UserNotFoundException;
import com.jukusoft.jbackendengine.authentification.exception.WrongPasswordException;
import com.jukusoft.jbackendengine.encryption.HashUtils;
import com.jukusoft.jbackendengine.phonenumber.PhoneNumberUtils;
import com.jukusoft.jbackendengine.phonenumber.exception.InvalidPhoneNumberException;
import org.json.JSONObject;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Justin on 02.07.2015.
 */
public class HazelcastUserManager implements IUserManager {

    private HazelcastInstance hazelcastInstance = null;
    private IBackendEngine backendEngine = null;
    private IMap<Long,String> userlist = null;
    private ICache<Long,Boolean> temporaryUserLockCache = null;
    private IMap<String,Long> telephoneNumberToUserIDMap = null;
    private IMap<String,String> telephoneNumberHashToTelephoneNumberMap = null;

    public HazelcastUserManager (HazelcastInstance hazelcastInstance, IBackendEngine backendEngine, String mapName) {
        this.hazelcastInstance = hazelcastInstance;
        this.backendEngine = backendEngine;
        this.userlist = this.hazelcastInstance.getMap(mapName);
        this.telephoneNumberToUserIDMap = this.hazelcastInstance.getMap("telephoneNumber-to-userID");
        this.telephoneNumberHashToTelephoneNumberMap = this.hazelcastInstance.getMap("telephoneNumberMD5Hash-to-telephoneNumber");

        CacheManager manager = Caching.getCachingProvider().getCacheManager();
        MutableConfiguration<String, String> configuration = new MutableConfiguration<String, String>();

        //entries will be removed automatically 1 day after creation
        configuration.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 60 * 60 * 24)));

        Cache<String,String> cache = manager.createCache("tempUserLockCache", configuration);
        this.temporaryUserLockCache = cache.unwrap(ICache.class);
    }

    public HazelcastUserManager (HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        this.userlist = this.hazelcastInstance.getMap("userlist");
    }

    @Override
    public void addUser(AddUserRequest addUserRequest) {
        JSONObject jsonObject = new JSONObject();
    }

    @Override
    public void addUser(AddUserWithTelephoneNumberRequest addUserWithTelephoneNumberRequest) throws InvalidPhoneNumberException {
        String telephoneNumber = addUserWithTelephoneNumberRequest.getTelephoneNumber();
        String password = addUserWithTelephoneNumberRequest.getPasswordHash();

        if (!PhoneNumberUtils.isValidePhoneNumber(telephoneNumber, addUserWithTelephoneNumberRequest.getCountryCode())) {
            throw new InvalidPhoneNumberException("invalid phoneNumber" + telephoneNumber + ".");
        }

        JSONObject newJSONObj = new JSONObject();
        newJSONObj.put("telephoneNumber", telephoneNumber);
        newJSONObj.put("passwordHash", password);
        newJSONObj.put("locked", false);

        //add additional version information
        newJSONObj.put("buildNumber", addUserWithTelephoneNumberRequest.getBuildNumer());

        //add date
        Calendar calendar = new GregorianCalendar();
        newJSONObj.put("dbInserted", calendar.getTimeInMillis());

        //generate new userID
        long newUserID = this.backendEngine.getUniqueUserIDGenerator().generate();

        userlist.put(newUserID, newJSONObj.toString());
        userlist.flush();

        telephoneNumberToUserIDMap.put(telephoneNumber, newUserID);
        telephoneNumberToUserIDMap.flush();

        telephoneNumberHashToTelephoneNumberMap.put(HashUtils.computeMD5Hash(telephoneNumber), telephoneNumber);
        telephoneNumberHashToTelephoneNumberMap.flush();

        this.backendEngine.getNotificationManager().getService("newRegistrations").publish(newUserID);
    }

    @Override
    public boolean containsUser(Long userID) {
        return this.userlist.containsKey(userID);
    }

    @Override
    public void updateUser(Long userID, IUser user) throws UserNotFoundException {
        if (this.containsUser(userID)) {
            this.userlist.putAsync(userID, user.toJSON().toString());
            this.userlist.flush();
        } else {
            throw new UserNotFoundException("user with userID " + userID + " not found.");
        }
    }

    @Override
    public void removeUser(Long userID) {
        this.userlist.removeAsync(userID);
    }

    @Override
    public IUser authWithTelephoneNumber(String telephoneNumber, String passwordHash) throws WrongPasswordException{
        return null;
    }

    @Override
    public void lockUser(Long userID) throws UserNotFoundException {
        if (this.userlist.containsKey(userID)) {
            JSONObject jsonObject = new JSONObject(this.userlist.get(userID));
            jsonObject.put("locked", true);

            this.userlist.putAsync(userID, jsonObject.toString());
            this.userlist.flush();
        } else {
            throw new UserNotFoundException("user with userID " + userID + " not found.");
        }
    }

    @Override
    public void unlockUser(Long userID) throws UserNotFoundException {
        if (this.userlist.containsKey(userID)) {
            JSONObject jsonObject = new JSONObject(this.userlist.get(userID));
            jsonObject.put("locked", false);

            this.userlist.putAsync(userID, jsonObject.toString());
            this.userlist.flush();
        } else {
            throw new UserNotFoundException("user with userID " + userID + " not found.");
        }
    }

    @Override
    public boolean isLocked(Long userID) throws UserNotFoundException {
        if (this.userlist.containsKey(userID)) {
            JSONObject jsonObject = new JSONObject(this.userlist.get(userID));
            return jsonObject.has("locked") && jsonObject.getBoolean("locked");
        } else {
            throw new UserNotFoundException("user with userID " + userID + " not found.");
        }
    }

    @Override
    public void lockTemporaryUser(Long userID, Long lockTime) {
        this.temporaryUserLockCache.put(userID, true, CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, lockTime)).create());
    }

    @Override
    public void unlockTemporaryUser(Long userID) {
        this.temporaryUserLockCache.removeAsync(userID);
    }

    @Override
    public boolean isTemporaryLocked(Long userID) {
        if (this.temporaryUserLockCache.containsKey(userID) && this.temporaryUserLockCache.get(userID)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Permission> getPermissionsByUserID(Long userID) {
        return null;
    }

    @Override
    public Long getUserIDByTelephoneNumber(String telephoneNumber) throws PhoneNumberNotFoundException {
        if (this.telephoneNumberToUserIDMap.containsKey(telephoneNumber)) {
            return this.telephoneNumberToUserIDMap.get(telephoneNumber);
        } else {
            throw new PhoneNumberNotFoundException("phoneNumber " + telephoneNumber + " not found.");
        }
    }

    @Override
    public Long getUserIDByTelephoneNumberHash(String telephoneNumberHash) throws PhoneNumberNotFoundException {
        if (this.telephoneNumberHashToTelephoneNumberMap.containsKey(telephoneNumberHash)) {
            String telephoneNumber = this.telephoneNumberHashToTelephoneNumberMap.get(telephoneNumberHash);
            return this.getUserIDByTelephoneNumber(telephoneNumber);
        } else {
            throw new PhoneNumberNotFoundException("phoneNumberHash " + telephoneNumberHash + " not found.");
        }
    }

    @Override
    public IUser getUser(Long userID) throws UserNotFoundException {
        if (this.userlist.containsKey(userID)) {
            String value = this.userlist.get(userID);
            JSONObject json = new JSONObject(value);
            HazelcastDBUser hazelcastDBUser = new HazelcastDBUser(this);
            hazelcastDBUser.loadFromJSON(json);

            return hazelcastDBUser;
        } else {
            this.backendEngine.getLoggerManager().debug("user with userID " + userID + " not found.");
            throw new UserNotFoundException("user with userID " + userID + " not found.");
        }
    }
}
