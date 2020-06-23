/*
 Copyright 2020 Adobe. All rights reserved.
 This file is licensed to you under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License. You may obtain a copy
 of the License at http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software distributed under
 the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
 OF ANY KIND, either express or implied. See the License for the specific language
 governing permissions and limitations under the License.
 */

package com.adobe.marketing.mobile.cordova;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import com.adobe.marketing.mobile.PlacesMonitor;
import com.adobe.marketing.mobile.PlacesMonitorLocationPermission;

/**
 * This class echoes a string called from JavaScript.
 */
public class ACPPlacesMonitor_Cordova extends CordovaPlugin {

    final static String METHOD_PLACESMONITOR_EXTENSION_VERSION_PLACESMONITOR = "extensionVersion";
    final static String METHOD_PLACESMONITOR_START = "start";
    final static String METHOD_PLACESMONITOR_STOP = "stop";
    final static String METHOD_PLACESMONITOR_UPDATE_LOCATION = "updateLocation";
    final static String METHOD_PLACESMONITOR_SET_LOCATION_PERMISSION = "setRequestLocationPermission";
    final static String METHOD_PLACESMONITOR_SET_PLACES_MONITOR_MODE = "setPlacesMonitorMode";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        if (METHOD_PLACESMONITOR_EXTENSION_VERSION_PLACESMONITOR.equals(action)) {
            extensionVersion(callbackContext);
            return true;
        } else if (METHOD_PLACESMONITOR_START.equals(action)) {
            start(callbackContext);
            return true;
        } else if (METHOD_PLACESMONITOR_STOP.equals(action)) {
            stop(args, callbackContext);
            return true;
        }else if (METHOD_PLACESMONITOR_UPDATE_LOCATION.equals(action)) {
            updateLocation(callbackContext);
            return true;
        } else if (METHOD_PLACESMONITOR_SET_LOCATION_PERMISSION.equals(action)) {
            setRequestLocationPermission(args, callbackContext);
            return true;
        } else if (METHOD_PLACESMONITOR_SET_PLACES_MONITOR_MODE.equals(action)) {
            setPlacesMonitorMode(callbackContext);
            return true;
        }

        return false;
    }

    private void extensionVersion(final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String extensionVersion = PlacesMonitor.extensionVersion();
                if (extensionVersion.length() > 0) {
                    callbackContext.success(extensionVersion);
                } else {
                    callbackContext.error("Extension version is null or empty");
                }
            }
        });
    }

    private void start(final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                PlacesMonitor.start();
                callbackContext.success();
            }
        });
    }

    private void stop(final JSONArray args, final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                boolean shouldClearPlacesData;
                try {
                    shouldClearPlacesData = args.getBoolean(0);
                } catch (JSONException e) {
                    callbackContext.error("Error while parsing arguments, Error " + e.getLocalizedMessage());
                    return;
                }
                PlacesMonitor.stop(shouldClearPlacesData);
                callbackContext.success();
            }
        });
    }

    private void updateLocation(final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                PlacesMonitor.updateLocation();
                callbackContext.success();
            }
        });
    }

    private void setRequestLocationPermission(final JSONArray args, final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                PlacesMonitorLocationPermission locationPermissionValue;
                try {
                    locationPermissionValue = getLocationPermissionValue(args.getInt(0));
                } catch (JSONException e) {
                    callbackContext.error("Error while parsing arguments, Error " + e.getLocalizedMessage());
                    return;
                }
                PlacesMonitor.setRequestLocationPermission(locationPermissionValue);
                callbackContext.success();
            }
        });
    }

    private void setPlacesMonitorMode(final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                // TODO: this method is not implemented in Android
                callbackContext.success();
            }
        });
    }

    // ===============================================================
    // Helpers
    // ===============================================================
    private PlacesMonitorLocationPermission getLocationPermissionValue(final int locationPermission){
        if(locationPermission == 1) {
            return PlacesMonitorLocationPermission.ALWAYS_ALLOW;
        } else if(locationPermission  == 2) {
            return PlacesMonitorLocationPermission.NONE;
        } else {
            return  PlacesMonitorLocationPermission.WHILE_USING_APP;
        }
    }
}
