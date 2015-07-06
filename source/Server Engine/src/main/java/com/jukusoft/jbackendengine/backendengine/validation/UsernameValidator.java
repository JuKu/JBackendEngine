package com.jukusoft.jbackendengine.backendengine.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Justin on 02.07.2015.
 */
public class UsernameValidator {

    public static boolean isValide (String name) {
        /*
         * http://www.mkyong.com/regular-expressions/how-to-validate-username-with-regular-expression/
         *  ^                   # Start of the line
         *  [a-z0-9_-]	        # Match characters and symbols in the list, a-z, 0-9, underscore, hyphen
         *  {3,15}              # Length at least 3 characters and maximum length of 15
         *  $                   # End of the line
         */
        String usernamePattern = "^[a-z0-9_-]{3,20}$";

        Pattern pattern = Pattern.compile(usernamePattern);
        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }

}
