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

var ACPPlacesMonitor = (function() {
    var exec = require('cordova/exec');
	var ACPPlacesMonitor = (typeof exports !== 'undefined') && exports || {};
	var PLUGIN_NAME = "ACPPlacesMonitor_Cordova";

    // ===========================================================================
    // public enums
    // ===========================================================================
    ACPPlacesMonitor.LocationPermissionWhileUsingTheApp= 0;
    ACPPlacesMonitor.LocationPermissionAlwaysAllow = 1;
    ACPPlacesMonitor.LocationPermissionNone = 2;

    ACPPlacesMonitor.MonitorModeContinuous = 0;
    ACPPlacesMonitor.MonitorModeSignificantChanges = 1;

	// ===========================================================================
	// public APIs
	// ===========================================================================
    // Get the current PlacesMonitor extension version.
    ACPPlacesMonitor.extensionVersion = function (success, error) {
        var FUNCTION_NAME = "extensionVersion";

        if (success && !isFunction(success)) {
            printNotAFunction("success", FUNCTION_NAME);
            return;
        }

        if (error && !isFunction(error)) {
            printNotAFunction("error", FUNCTION_NAME);
            return;
        }

        return exec(success, error, PLUGIN_NAME, FUNCTION_NAME, []);
    };

    // Begin tracking the device's location and monitoring nearby Places.
    ACPPlacesMonitor.start = function (success, error) {
        var FUNCTION_NAME = "start";

        if (success && !isFunction(success)) {
            printNotAFunction("success", FUNCTION_NAME);
            return;
        }

        if (error && !isFunction(error)) {
            printNotAFunction("error", FUNCTION_NAME);
            return;
        }

        return exec(success, error, PLUGIN_NAME, FUNCTION_NAME, []);
    };

    // Stop tracking the device's location.
    ACPPlacesMonitor.stop = function (shouldClearData, success, error) {
        var FUNCTION_NAME = "stop";

        if(shouldClearData && !acpIsBoolean(shouldClearData)) {
            acpPrintNotABoolean("shouldClearData", FUNCTION_NAME);
            return;
        }

        if (success && !isFunction(success)) {
            printNotAFunction("success", FUNCTION_NAME);
            return;
        }

        if (error && !isFunction(error)) {
            printNotAFunction("error", FUNCTION_NAME);
            return;
        }
        return exec(success, error, PLUGIN_NAME, FUNCTION_NAME, [shouldClearData]);
    };

    // Immediately update the device's location and refresh the nearby POIs that are monitored by the extension.
    ACPPlacesMonitor.updateLocation = function (success, error) {
        var FUNCTION_NAME = "updateLocation";

        if (success && !isFunction(success)) {
            printNotAFunction("success", FUNCTION_NAME);
            return;
        }

        if (error && !isFunction(error)) {
            printNotAFunction("error", FUNCTION_NAME);
            return;
        }
        return exec(success, error, PLUGIN_NAME, FUNCTION_NAME, []);
    };

    // Sets the type of location permission request for which the user is prompted to select.
    ACPPlacesMonitor.setRequestLocationPermission = function (locationPermission, success, error) {
        var FUNCTION_NAME = "setRequestLocationPermission";

        if (!acpIsNumber(locationPermission)) {
            acpIsNumber("locationPermission", FUNCTION_NAME);
            return;
        }

        if (success && !isFunction(success)) {
            printNotAFunction("success", FUNCTION_NAME);
            return;
        }

        if (error && !isFunction(error)) {
            printNotAFunction("error", FUNCTION_NAME);
            return;
        }

        return exec(success, error, PLUGIN_NAME, FUNCTION_NAME, [locationPermission]);
    };

    // Set the Places Monitor monitoring mode (iOS only).
    ACPPlacesMonitor.setPlacesMonitorMode = function (monitorMode, success, error) {
        var FUNCTION_NAME = "setPlacesMonitorMode";

        if (!acpIsNumber(monitorMode)) {
            acpPrintNotANumber("monitorMode", FUNCTION_NAME);
            return;
        }

        if (success && !isFunction(success)) {
            printNotAFunction("success", FUNCTION_NAME);
            return;
        }

        if (error && !isFunction(error)) {
            printNotAFunction("error", FUNCTION_NAME);
            return;
        }

        exec(success, error, PLUGIN_NAME, FUNCTION_NAME, [monitorMode]);
    };

	return ACPPlacesMonitor;
}());

// ===========================================================================
// helper functions
// ===========================================================================
function acpIsNumber (value) {
    return typeof value === 'number' && isFinite(value);
};

function acpPrintNotANumber (paramName, functionName) {
    console.log("Ignoring call to '" + functionName + "'. The '" + paramName + "' parameter is required to be a Number.");
};

function isFunction (value) {
    return typeof value === 'function';
};

function printNotAFunction (paramName, functionName) {
    console.log("Ignoring call to '" + functionName + "'. The '" + paramName + "' parameter is required to be a function.");
};

function acpIsBoolean (value) {
    return typeof value === 'boolean';
};

function acpPrintNotABoolean (paramName, functionName) {
    console.log("Ignoring call to '" + functionName + "'. The '" + paramName + "' parameter is required to be a boolean.");
};

module.exports = ACPPlacesMonitor;
