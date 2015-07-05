package com.jukusoft.jbackendengine.authentification;

import com.jukusoft.jbackendengine.authentification.exception.PhoneNumberNotFoundException;
import com.jukusoft.jbackendengine.authentification.exception.UserNotFoundException;
import com.jukusoft.jbackendengine.authentification.exception.WrongPasswordException;
import com.jukusoft.jbackendengine.manager.IManager;
import com.jukusoft.jbackendengine.phonenumber.exception.InvalidPhoneNumberException;

import java.util.List;

/**
 * Created by Justin on 02.07.2015.
 */
public interface IUserManager extends IManager {

    public void addUser (AddUserRequest addUserRequest);
    public void addUser (AddUserWithTelephoneNumberRequest addUserWithTelephoneNumberRequest) throws InvalidPhoneNumberException;
    public boolean containsUser (Long userID);
    public void updateUser (Long userID, IUser user) throws UserNotFoundException;
    public void removeUser (Long userID);
    public IUser authWithTelephoneNumber (String telephoneNumber, String passwordHash) throws WrongPasswordException;
    public void lockUser (Long userID) throws UserNotFoundException;
    public void unlockUser (Long userID) throws UserNotFoundException;
    public boolean isLocked (Long userID) throws UserNotFoundException;
    public void lockTemporaryUser (Long userID, Long lockTime);
    public void unlockTemporaryUser (Long userID);
    public boolean isTemporaryLocked (Long userID);
    public List<Permission> getPermissionsByUserID (Long userID);
    public Long getUserIDByTelephoneNumber (String telephoneNumber) throws PhoneNumberNotFoundException;
    public Long getUserIDByTelephoneNumberHash (String telephoneNumberHash) throws PhoneNumberNotFoundException;
    public IUser getUser (Long userID) throws UserNotFoundException;

}
