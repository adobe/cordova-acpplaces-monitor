
# Adobe Experience Platform - Places Monitor plugin for Cordova apps

[![CI](https://github.com/adobe/cordova-acpplaces-monitor/workflows/CI/badge.svg)](https://github.com/adobe/cordova-acpplaces-monitor/actions)
[![npm](https://img.shields.io/npm/v/@adobe/cordova-acpplacesmonitor)](https://www.npmjs.com/package/@adobe/cordova-acpplacesmonitor)
[![GitHub](https://img.shields.io/github/license/adobe/cordova-acpplaces-monitor)](https://github.com/adobe/cordova-acpplaces-monitor/blob/main/LICENSE)

- [Prerequisites](#prerequisites)  
- [Installation](#installation)
- [Usage](#usage)  
- [Running Tests](#running-tests)
- [Sample App](#sample-app)
- [Additional Cordova Plugins](#additional-cordova-plugins)
- [Contributing](#contributing)  
- [Licensing](#licensing)  

## Prerequisites  

Cordova is distributed via [Node Package Management](https://www.npmjs.com/) (aka - `npm`).  

In order to install and build Cordova applications you will need to have `Node.js` installed. [Install Node.js](https://nodejs.org/en/).  

Once Node.js is installed, you can install the Cordova framework from terminal:  

```  
sudo npm install -g cordova  
```

## Installation

To start using the Places Monitor plugin for Cordova, navigate to the directory of your Cordova app and install the plugin:
```
cordova plugin add https://github.com/adobe/cordova-acpplacesmonitor.git
```
Check out the documentation for help with APIs

## Usage

##### Getting the SDK version:
```js
ACPPlacesMonitor.extensionVersion(function(version){  
    console.log(version);
}, function(error){  
    console.log(error);  
});
```
##### Registering the extension with ACPCore and starting the Places Monitor:  

##### **iOS**
Within the App's application:didFinishLaunchingWithOptions, register the SDK extensions:
```objective-c
#import "ACPCore.h"
#import "ACPPlaces.h"
#import "ACPPlacesMonitor.h"

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary*)launchOptions {
    [ACPCore configureWithAppId:@"yourAppId"];
    [ACPPlaces registerExtension]; //Register Places with Core
    [ACPPlacesMonitor registerExtension]; //Register PlacesMonitor with Core
    [ACPCore start: nil];

    return YES;
}
```
The following updates are also neccessary for Places Monitor on iOS:

- [Enable location updates in the background](https://docs.adobe.com/content/help/en/places/using/places-ext-aep-sdks/places-monitor-extension/using-places-monitor-extension.html#enable-location-updates-background)
- [Add the keys NSLocationWhenInUseUsageDescription and NSLocationAlwaysAndWhenInUseUsageDescription to the app's plist file](https://docs.adobe.com/content/help/en/places/using/places-ext-aep-sdks/places-monitor-extension/using-places-monitor-extension.html#configuring-the-plist-keys)

##### **Android**

Within the App's OnCreate method, register the SDK extensions and start the Places Monitor:
```java
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.Places;
import com.adobe.marketing.mobile.PlacesMonitor;

public class MobileApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MobileCore.setApplication(this);
        MobileCore.ConfigureWithAppId("yourAppId");
        try {
            Places.registerExtension(); //Register Places with Core
            PlacesMonitor.registerExtension(); //Register PlacesMonitor with Core
            MobileCore.start(null);
        } catch (Exception e) {
            //Log the exception
        }
    }
}
```
The following update is also neccessary for Places Monitor on Android:

- [Add location permissions to the app manifest](https://docs.adobe.com/content/help/en/places/using/places-ext-aep-sdks/places-monitor-extension/using-places-monitor-extension.html#add-permissions-to-the-manifest)

##### Start the Places Monitor:

```js
// start the places monitor when the device ready event is heard
document.addEventListener("deviceready", function () {
  ACPPlacesMonitor.start(function(response) {  
    console.log("Successfully started the Places Monitor.");
  }, function(error){  
    console.log(error);  
  });
}, function (error) {
        console.log(error);
});
```
##### Stop the Places Monitor:
```js
var clearPlacesData = true;
ACPPlacesMonitor.stop(clearPlacesData, function(response) {  
    console.log("Successfully stopped the Places Monitor.");
}, function(error){  
    console.log(error);  
});
```
##### Update the device's location:
```js
ACPPlacesMonitor.updateLocation(function(response) {  
    console.log("Successfully updated the location.");
}, function(error){  
    console.log(error);  
});
```
##### Set or upgrade the location permission request (Android) / request authorization level (iOS):
```js
ACPPlacesMonitor.setRequestLocationPermission(ACPPlacesMonitor.LocationPermissionAlwaysAllow, function(response) {  
    console.log("Successfully set the location permission request.");
}, function(error){  
    console.log(error);  
}); 
```
##### Set the monitoring mode (iOS only):
```js
ACPPlacesMonitor.setPlacesMonitorMode(ACPPlacesMonitor.MonitorModeContinuous, function(response) {  
    console.log("Successfully set the places monitor mode.");
}, function(error){  
    console.log(error);  
}); 
```
## Running Tests
Install cordova-paramedic `https://github.com/apache/cordova-paramedic`
```bash
npm install -g cordova-paramedic
```

Run the tests
```
cordova-paramedic --platform ios --plugin . --verbose
```
```
cordova-paramedic --platform android --plugin . --verbose
```

## Sample App

A Cordova app for testing the Adobe SDK plugins is located at [https://github.com/adobe/cordova-acpsample](https://github.com/adobe/cordova-acpsample). The app is configured for both iOS and Android platforms.  

## Additional Cordova Plugins

Below is a list of additional Cordova plugins from the AEP SDK suite:

| Extension | GitHub | npm |
|-----------|--------|-----|
| Core SDK | https://github.com/adobe/cordova-acpcore | [![npm](https://img.shields.io/npm/v/@adobe/cordova-acpcore)](https://www.npmjs.com/package/@adobe/cordova-acpcore)
| Adobe Analytics | https://github.com/adobe/cordova-acpanalytics | [![npm](https://img.shields.io/npm/v/@adobe/cordova-acpanalytics)](https://www.npmjs.com/package/@adobe/cordova-acpanalytics)
| Places | https://github.com/adobe/cordova-acpplaces | [![npm](https://img.shields.io/npm/v/@adobe/cordova-acpplaces)](https://www.npmjs.com/package/@adobe/cordova-acpplaces)
| Project Griffon (Beta) | https://github.com/adobe/cordova-acpgriffon | [![npm](https://img.shields.io/npm/v/@adobe/cordova-acpgriffon)](https://www.npmjs.com/package/@adobe/cordova-acpgriffon)
| User Profile | https://github.com/adobe/cordova-acpuserprofile | [![npm](https://img.shields.io/npm/v/@adobe/cordova-acpuserprofile)](https://www.npmjs.com/package/@adobe/cordova-acpuserprofile)

## Contributing
Looking to contribute to this project? Please review our [Contributing guidelines](.github/CONTRIBUTING.md) prior to opening a pull request.

We look forward to working with you!

## Licensing  
This project is licensed under the Apache V2 License. See [LICENSE](LICENSE) for more information.
