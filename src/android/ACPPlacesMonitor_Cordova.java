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

import org.apache.cordova.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.adobe.marketing.mobile.Places;
import com.adobe.marketing.mobile.PlacesMonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

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
    final static String LOG_TAG = "ACPPlacesMonitor_Cordova";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        if (METHOD_PLACESMONITOR_EXTENSION_VERSION_PLACESMONITOR.equals(action)) {
            extensionVersion(callbackContext);
            return true;
        } else if (METHOD_PLACESMONITOR_START.equals(action)) {
            start(callbackContext);
            return true;
        } else if (METHOD_PLACESMONITOR_STOP.equals(action)) {
            stop(callbackContext);
            return true;
        }else if (METHOD_PLACESMONITOR_UPDATE_LOCATION.equals(action)) {
            updateLocation(callbackContext);
            return true;
        } else if (METHOD_PLACESMONITOR_SET_LOCATION_PERMISSION.equals(action)) {
            updateUserAttribute(args, callbackContext);
            return true;
        } else if (METHOD_PLACESMONITOR_SET_PLACES_MONITOR_MODE.equals(action)) {
            setPlacesMonitorMode(args, callbackContext);
            return true;
        }

        return false;
    }

    private void extensionVersion(final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String extensionVersion = ACPPlacesMonitor.extensionVersion();
                if (extensionVersion.length() > 0) {
                    callbackContext.success(extensionVersion);
                } else {
                    callbackContext.error("Extension version is null or empty");
                }
            }
        });
    }

    private void start(final JSONArray args, final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if (args == null || args.length() != 1) {
                    callbackContext.error("Invalid argument count, expected 1 (attributeNames).");
                    return;
                }
                List<String> attributeNames;
                try {
                    attributeNames = getListFromJSONArray(args.getJSONArray(0));
                } catch (JSONException e) {
                    callbackContext.error("Error while parsing arguments, Error " + e.getLocalizedMessage());
                    return;
                }
                UserProfile.getUserAttributes(attributeNames, new AdobeCallback<Map<String, Object>>() {
                    @Override
                    public void call(Map<String, Object> retrievedAttributes) {
                        if(retrievedAttributes != null) {
                            JSONArray jsonArray = new JSONArray();
                            JSONObject json;
                            int index = 0;
                            try {
                                Iterator it = retrievedAttributes.entrySet().iterator();
                                while(it.hasNext()) 
                                {
                                    json = new JSONObject();
                                    Map.Entry<String,Object> entry = (Map.Entry<String,Object>)it.next();
                                    json.put(entry.getKey(), entry.getValue());
                                    jsonArray.put(index, json);
                                    index++;
                                } 
                            } catch (JSONException e){
                                LOG.d(LOG_TAG, "Error putting data into JSON: " + e.getLocalizedMessage());
                            }
                            callbackContext.success(jsonArray.toString());
                        } else {
                            callbackContext.error("Error retrieving user attributes.");
                        }
                    }
                });
            }
        });
    }

    private void stop(final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if (args == null || args.length() != 1) {
                    callbackContext.error("Invalid argument count, expected 1 (attributeName).");
                    return;
                }
                String attributeName;
                try {
                    attributeName = args.getString(0);
                } catch (JSONException e) {
                    callbackContext.error("Error while parsing arguments, Error " + e.getLocalizedMessage());
                    return;
                }
                UserProfile.removeUserAttribute(attributeName);
                callbackContext.success();
            }
        });
    }

    private void updateLocation(final JSONArray args, final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if (args == null || args.length() != 1) {
                    callbackContext.error("Invalid argument count, expected 1 (attributeName).");
                    return;
                }
                List<String> attributeNames;
                try {
                    attributeNames = getListFromJSONArray(args.getJSONArray(0));
                } catch (JSONException e) {
                    callbackContext.error("Error while parsing arguments, Error " + e.getLocalizedMessage());
                    return;
                }
                UserProfile.removeUserAttributes(attributeNames);
                callbackContext.success();
            }
        });
    }

    private void setRequestLocationPermission(final JSONArray args, final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if (args == null || args.length() != 2) {
                    callbackContext.error("Invalid argument count, expected 2 (attributeName and attributeValue).");
                    return;
                }
                String attributeName;
                Object attributeValue;
                try {
                    attributeName = args.getString(0);
                    attributeValue = args.get(1);
                } catch (JSONException e) {
                    callbackContext.error("Error while parsing arguments, Error " + e.getLocalizedMessage());
                    return;
                }
                UserProfile.updateUserAttribute(attributeName, attributeValue);
                callbackContext.success();
            }
        });
    }

    private void setPlacesMonitorMode(final JSONArray args, final CallbackContext callbackContext) {
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
    private HashMap<String, Object> getObjectMapFromJSON(JSONObject data) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        @SuppressWarnings("rawtypes")
        Iterator it = data.keys();
        while (it.hasNext()) {
            String n = (String) it.next();
            try {
                map.put(n, data.getString(n));
            } catch (JSONException e) {
                LOG.d(LOG_TAG, "JSON error: " + e.getLocalizedMessage());
            }
        }

        return map;
    }

    private List getListFromJSONArray(JSONArray array) throws JSONException {
        List list = new ArrayList();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.get(i));
        }
        return list;
    }
}
