
package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class RNMqttPahoModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;


  public MqttAndroidClient mqttAndroidClient;
  final String serverUri = "tcp://m12.cloudmqtt.com:11111";
  final String clientId = "ExampleAndroidClient";
  final String subscriptionTopic = "sensor/+";
  final String username = "xxxxxxx";
  final String password = "yyyyyyyyyy";


  public RNMqttPahoModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }


  @Override
  public String getName() {
    return "RNMqttPaho";
  }


  @ReactMethod
  public void client(String message) {

  }


  public void Init(Context context){
      mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
      mqttAndroidClient.setCallback(new MqttCallbackExtended() {
          @Override
          public void connectComplete(boolean b, String s) {
              Log.w("mqtt", s);
          }

          @Override
          public void connectionLost(Throwable throwable) {

          }

          @Override
          public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
              Log.w("Mqtt", mqttMessage.toString());
          }

          @Override
          public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

          }
      });
      connect();
  }

  public void setCallback(MqttCallbackExtended callback) {
      mqttAndroidClient.setCallback(callback);
  }

  private void connect(){
      MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
      mqttConnectOptions.setAutomaticReconnect(true);
      mqttConnectOptions.setCleanSession(false);
      mqttConnectOptions.setUserName(username);
      mqttConnectOptions.setPassword(password.toCharArray());

      try {

          mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
              @Override
              public void onSuccess(IMqttToken asyncActionToken) {

                  DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                  disconnectedBufferOptions.setBufferEnabled(true);
                  disconnectedBufferOptions.setBufferSize(100);
                  disconnectedBufferOptions.setPersistBuffer(false);
                  disconnectedBufferOptions.setDeleteOldestMessages(false);
                  mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                  subscribeToTopic();
              }

              @Override
              public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                  Log.w("Mqtt", "Failed to connect to: " + serverUri + exception.toString());
              }
          });


      } catch (MqttException ex){
          ex.printStackTrace();
      }
  }


  private void subscribeToTopic() {
      try {
          mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
              @Override
              public void onSuccess(IMqttToken asyncActionToken) {
                  Log.w("Mqtt","Subscribed!");
              }

              @Override
              public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                  Log.w("Mqtt", "Subscribed fail!");
              }
          });

      } catch (MqttException ex) {
          System.err.println("Exceptionst subscribing");
          ex.printStackTrace();
      }
  }







}