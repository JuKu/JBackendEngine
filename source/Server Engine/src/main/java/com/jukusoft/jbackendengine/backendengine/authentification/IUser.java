package com.jukusoft.jbackendengine.backendengine.authentification;

import com.jukusoft.jbackendengine.backendengine.authentification.exception.UserLockException;
import com.jukusoft.jbackendengine.backendengine.authentification.exception.UserNotFoundException;
import com.jukusoft.jbackendengine.backendengine.json.JSONConvertable;
import com.jukusoft.jbackendengine.backendengine.phonenumber.exception.InvalidPhoneNumberException;
import com.jukusoft.jbackendengine.backendengine.validation.exception.InvalidUsernameException;

import java.util.List;
import java.util.Map;

/**
 * Created by Justin on 02.07.2015.
 */
public interface IUser extends JSONConvertable {

    public Long getUserID ();
    public String getName ();
    public void setName (String name) throws InvalidUsernameException, UserNotFoundException;
    public void setNameWithoutValidation (String name) throws UserNotFoundException;
    public String getTelephoneNumber ();
    public void setTelephoneNumber (String telephoneNumber, String countryCode) throws InvalidPhoneNumberException, UserNotFoundException;
    public String getProfileField (String profileField);
    public void setProfileField (String profileField, String value);
    public List<Map.Entry<String,String>> getAllProfileFields ();
    public void commitProfileFields () throws UserNotFoundException;
    public boolean isLocked ();
    public void lock () throws UserLockException, UserNotFoundException;
    public void unlock () throws UserLockException, UserNotFoundException;
    public boolean isTemporaryLocked ();
    public void lockTemporary (Long lockTime) throws UserLockException;
    public void unlockTemporary () throws UserLockException;
    public void commit () throws UserNotFoundException;
    public void access ();

}
