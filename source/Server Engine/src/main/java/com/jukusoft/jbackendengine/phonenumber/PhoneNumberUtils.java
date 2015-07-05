package com.jukusoft.jbackendengine.phonenumber;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * Created by Justin on 02.07.2015.
 */
public class PhoneNumberUtils {

    public static boolean isValidePhoneNumber (String telephoneNumber, String countryCode) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber numberProto = null;

        try {
            numberProto = phoneUtil.parse(telephoneNumber, countryCode);
        } catch (NumberParseException e) {
            e.printStackTrace();
            return false;
        }

        return phoneUtil.isValidNumber(numberProto);
    }

}
