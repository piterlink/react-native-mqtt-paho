
# react-native-mqtt-paho

## Getting started

`$ npm install react-native-mqtt-paho --save`

### Mostly automatic installation

`$ react-native link react-native-mqtt-paho`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-mqtt-paho` and add `RNMqttPaho.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNMqttPaho.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNMqttPahoPackage;` to the imports at the top of the file
  - Add `new RNMqttPahoPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-mqtt-paho'
  	project(':react-native-mqtt-paho').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-mqtt-paho/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-mqtt-paho')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNMqttPaho.sln` in `node_modules/react-native-mqtt-paho/windows/RNMqttPaho.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Mqtt.Paho.RNMqttPaho;` to the usings at the top of the file
  - Add `new RNMqttPahoPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNMqttPaho from 'react-native-mqtt-paho';

// TODO: What to do with the module?
RNMqttPaho;
```
  