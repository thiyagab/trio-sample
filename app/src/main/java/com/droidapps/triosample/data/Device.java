package com.droidapps.triosample.data;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by bt on 5/8/16.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class Device {
    public String name;
    public String deviceType;
    public String model;

}
