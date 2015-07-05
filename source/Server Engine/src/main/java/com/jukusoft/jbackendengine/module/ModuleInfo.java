package com.jukusoft.jbackendengine.module;

import java.lang.annotation.*;

/**
 * Created by Justin on 03.07.2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented

public @interface ModuleInfo {

    public enum Priority {
        LOW, MEDIUM, HIGH, DB, LOW_LEVEL
    }

    public enum START_ORDER {
        FIRST, LAST, DB, LOW_LEVEL
    }

    Priority priority() default Priority.MEDIUM;

    String moduleName ();
    String version ();
    String description () default "";
    boolean permissionToOverrideEngineModules () default false;
    String startAfter () default "";
    String author ();
    String[] dependencies ();

}
