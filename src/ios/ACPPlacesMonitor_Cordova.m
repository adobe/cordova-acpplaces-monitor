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

/********* cordova-acpprofilemonitor.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import <ACPPlacesMonitor/ACPPlacesMonitor.h>
#import <Cordova/CDVPluginResult.h>

@interface ACPPlacesMonitor_Cordova : CDVPlugin

- (void)extensionVersion:(CDVInvokedUrlCommand*)command;
- (void)start:(CDVInvokedUrlCommand*)command;
- (void)stop:(CDVInvokedUrlCommand*)command;
- (void)updateLocation:(CDVInvokedUrlCommand*)command;
- (void)setRequestLocationPermission:(CDVInvokedUrlCommand*)command;
- (void)setPlacesMonitorMode:(CDVInvokedUrlCommand*)command;

@end

@implementation ACPPlacesMonitor_Cordova

- (void)extensionVersion:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;
        NSString* extensionVersion = [ACPPlacesMonitor extensionVersion];

        if (extensionVersion != nil && [extensionVersion length] > 0) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:extensionVersion];
        } else {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        }

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)start:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        [ACPPlacesMonitor start];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)stop:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        BOOL shouldClearPlacesData = [self getCommandArg:command.arguments[0]];
        [ACPPlacesMonitor stop:shouldClearPlacesData];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)updateLocation:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        [ACPPlacesMonitor updateLocationNow];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)setRequestLocationPermission:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        ACPPlacesMonitorRequestAuthorizationLevel authorizationLevel = [self convertToAuthorizationLevel:[self getCommandArg:command.arguments[0]]];
        [ACPPlacesMonitor setRequestAuthorizationLevel:authorizationLevel];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)setPlacesMonitorMode:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        ACPPlacesMonitorMode mode = [self convertToMonitorMode:[self getCommandArg:command.arguments[0]]];
        [ACPPlacesMonitor setPlacesMonitorMode:mode];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

/*
 * Helper functions
 */

- (id) getCommandArg:(id) argument {
    return argument == (id)[NSNull null] ? nil : argument;
}

- (ACPPlacesMonitorRequestAuthorizationLevel) convertToAuthorizationLevel:(NSNumber*) authorization {
    if(authorization.integerValue == 1){
        return ACPPlacesRequestMonitorAuthorizationLevelAlways;
    } else {
        return ACPPlacesMonitorRequestAuthorizationLevelWhenInUse;
    }
}

- (ACPPlacesMonitorMode) convertToMonitorMode:(NSNumber*) monitorMode {
    if(monitorMode.integerValue == 1){
        return ACPPlacesMonitorModeSignificantChanges;
    } else {
        return ACPPlacesMonitorModeContinuous;
    }
}

@end
