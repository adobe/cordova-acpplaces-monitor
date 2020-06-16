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
#import <ACPPlaces/ACPPlace.h>
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
        dispatch_semaphore_t semaphore = dispatch_semaphore_create(0);
        __block CDVPluginResult* pluginResult = nil;
        NSArray<NSString*>* userAttributesToRetrieve = [self getCommandArg:command.arguments[0]];
        [ACPUserProfile getUserAttributes:userAttributesToRetrieve withCompletionHandler:^(NSDictionary* _Nullable userAttributes, NSError* error) {
            if(userAttributes != nil && userAttributes.count != 0) {
                NSData* jsonData = [NSJSONSerialization dataWithJSONObject:userAttributes options:0 error:nil];
                pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:[[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding]];
            }
            if(error){
                [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:[NSString stringWithFormat:@"User profile request error code: %@", error]] callbackId:command.callbackId];
            }
            dispatch_semaphore_signal(semaphore);
        }];
        dispatch_semaphore_wait(semaphore, dispatch_time(DISPATCH_TIME_NOW, ((int64_t)1 * NSEC_PER_SEC)));
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)stop:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        NSString* attribute = [self getCommandArg:command.arguments[0]];
        [ACPUserProfile removeUserAttribute:attribute];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)updateLocation:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        NSArray<NSString*>* userAttributes = [self getCommandArg:command.arguments[0]];
        [ACPUserProfile removeUserAttributes:userAttributes];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)setRequestLocationPermission:(CDVInvokedUrlCommand*)command
{
    // maps to setRequestAuthorizationLevel on iOS
    [self.commandDelegate runInBackground:^{
        NSString* attributeName = [self getCommandArg:command.arguments[0]];
        NSObject* attributeValue = [self getCommandArg:command.arguments[1]];
        NSString *attributeValueString = [self convertObjectToString:attributeValue];
        [ACPUserProfile updateUserAttribute:attributeName withValue:attributeValueString];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)setPlacesMonitorMode:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        NSDictionary* attributes = [self getCommandArg:command.arguments[0]];
        [ACPUserProfile updateUserAttributes:attributes];
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

- (NSString*) convertObjectToString:(NSObject*) attributeValue {
    NSString* stringValue = @"";
    if ([attributeValue isKindOfClass:[NSString class]]) {
        stringValue = (NSString*)attributeValue;
    } else if([attributeValue isKindOfClass:[NSArray class]]){
        NSArray* tempArray = (NSArray*)attributeValue;
        stringValue = [[tempArray valueForKey:@"description"] componentsJoinedByString:@", "];
    }
    return stringValue;
}

@end
