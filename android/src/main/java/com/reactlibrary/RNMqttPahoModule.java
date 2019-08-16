//adb logcat -s MQTT
package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.*;

import android.util.Log;


import javax.annotation.Nullable;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;


import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;




import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;


import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;



import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.*;
import java.security.SecureRandom;



// import org.eclipse.paho.android.service.MqttAndroidClient;

public class RNMqttPahoModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  
  private Callback onSuccessConnection;
  private Callback onFailureConneciton;
  
  private Callback onMessageArrived;
  private Callback onConnectionLost;




  MqttAsyncClient client;
  MemoryPersistence memPer;
  MqttConnectOptions mqttoptions;

  String user = "iotix_web";
  String pass = "iotix_web_118833";


  public RNMqttPahoModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }


  @Override
  public String getName() {
    return "RNMqttPaho";
  }


  @ReactMethod
  public void init(ReadableMap _options){ 
        WritableMap options = setOptions(_options);


        Log.d("MQTT","inicializando");
        memPer = new MemoryPersistence();
        mqttoptions = new MqttConnectOptions();

        mqttoptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        
        mqttoptions.setKeepAliveInterval(options.getInt("keepalive"));


        if(options.getBoolean("auth")) {
            String user = options.getString("user");
            String pass = options.getString("pass");
            if(user.length() > 0) 
              mqttoptions.setUserName(user);
            if(pass.length() > 0)
              mqttoptions.setPassword(pass.toCharArray());
        }


        String uri = "tcp://"+options.getString("uri");
        if(options.getBoolean("tls")){
            uri = "ssl://"+options.getString("uri");
            try {
                /* 
                http://stackoverflow.com/questions/3761737/https-get-ssl-with-android-and-self-signed-server-certificate      
                WARNING: for anybody else arriving at this answer, this is a dirty, 
                horrible hack and you must not use it for anything that matters. 
                SSL/TLS without authentication is worse than no encryption at all - 
                reading and modifying your "encrypted" data is trivial for an attacker and you wouldn't even know it was happening
                */
                
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new X509TrustManager[]{new X509TrustManager(){
                    public void checkClientTrusted(X509Certificate[] chain,
                            String authType) throws CertificateException {}
                    public void checkServerTrusted(X509Certificate[] chain,
                            String authType) throws CertificateException {}
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }}}, new SecureRandom());
    
                mqttoptions.setSocketFactory(sslContext.getSocketFactory());
            }catch(Exception e) {
    
            }
        }


 
            

    
        try {
            this.client = new MqttAsyncClient(uri, options.getString("clientId"), this.memPer);
            Log.d("MQTT","Client Criado");
        }catch(MqttException e) {

        }

        client.setCallback(new MqttCallback() {

   
                public void connectionLost(Throwable cause) {
                    //addToHistory("The Connection was lost.");
                    sendEvent(reactContext, "connectionLost",null);
                    
                }

                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //log("Incoming message: " + new String(message.getPayload()));
                    String _message = message.toString();
                    log("Incoming message: " + _message);


                    WritableMap data = Arguments.createMap();
                    data.putString("topic", topic);
                    data.putString("data", new String(message.getPayload()));
                    data.putInt("qos", message.getQos());
                    data.putBoolean("retain", message.isRetained());
                    sendEvent(reactContext, "message", data);


                    //callbackArrived(topic,_message);
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }

            }
        );

    }



    public WritableMap setOptions(ReadableMap _options){
        WritableMap defaultOptions = (WritableMap) new WritableNativeMap();

        defaultOptions.putString("uri", "localhost:8000");
		defaultOptions.putBoolean("tls", false);
		defaultOptions.putInt("keepalive", 60);
		defaultOptions.putString("clientId", "react-native-mqtt");
		defaultOptions.putInt("protocolLevel", 4);
		defaultOptions.putBoolean("clean", true);
		defaultOptions.putBoolean("auth", false);
		defaultOptions.putString("user", "");
		defaultOptions.putString("pass", "");
		defaultOptions.putBoolean("will", false);
		defaultOptions.putInt("protocolLevel", 4);
		defaultOptions.putBoolean("will", false);
		defaultOptions.putString("willMsg", "");
		defaultOptions.putString("willtopic", "");
		defaultOptions.putInt("willQos", 0);
		defaultOptions.putBoolean("willRetainFlag", false);




        if (_options.hasKey("uri")) 
            defaultOptions.putString("uri", _options.getString("uri"));
        else
            log("uri: not found");
        if(_options.hasKey("tls"))
            defaultOptions.putBoolean("tls",_options.getBoolean("tls"));
        if(_options.hasKey("keepalive"))
            defaultOptions.putInt("keepalive",_options.getInt("keepalive"));
        if(_options.hasKey("clientId"))
            defaultOptions.putString("clientId",_options.getString("clientId"));
        if(_options.hasKey("protocolLevel"))
            defaultOptions.putInt("protocolLevel",_options.getInt("protocolLevel"));
        if(_options.hasKey("clean"))
            defaultOptions.putBoolean("clean",_options.getBoolean("clean"));
        if(_options.hasKey("auth"))
            defaultOptions.putBoolean("auth",_options.getBoolean("auth"));
        if(_options.hasKey("user"))
            defaultOptions.putString("user",_options.getString("user"));
        if(_options.hasKey("pass"))
            defaultOptions.putString("pass",_options.getString("pass"));
        if(_options.hasKey("protocolLevel"))
            defaultOptions.putInt("protocolLevel",_options.getInt("protocolLevel"));
        if(_options.hasKey("will"))
            defaultOptions.putBoolean("will",_options.getBoolean("will"));
        if(_options.hasKey("willMsg"))
            defaultOptions.putString("willMsg",_options.getString("willMsg"));
        if(_options.hasKey("willTopic"))
            defaultOptions.putString("willTopic",_options.getString("willTopic"));
        if(_options.hasKey("willQos"))
            defaultOptions.putInt("willQos",_options.getInt("willQos"));
        if(_options.hasKey("willRetainFlag"))
            defaultOptions.putBoolean("willRetainFlag", _options.getBoolean("willRetainFlag"));

            return defaultOptions;
    }



    private void sendEvent(ReactContext reactContext,String eventName,@Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }



    public void callbackArrived(String topic,String message){
        log("CALLBACK ARRIVED");
        //onMessageArrived.invoke(topic,message);
    }


    public void callbackLost(){
        log("CALLBACK LOST");
        //onConnectionLost.invoke();
    }

    @ReactMethod
    public void connect(Callback onSuccess,Callback onFailure) {
        onSuccessConnection = onSuccess;
        onFailureConneciton = onFailure;
        
        try {
	         // Connect using a non-blocking connect
	        client.connect(this.mqttoptions, this.reactContext, new IMqttActionListener() {
	          public void onSuccess(IMqttToken asyncActionToken) {                    
                    Log.d("MQTT","Connected");  
                    onSuccessConnection.invoke();
	          }
	          public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("MQTT","Failure Connect"+ exception.toString());  
                    onFailureConneciton.invoke();
	          }
	        });
	      } catch (MqttException e) {
         	
	     }
    }
    


    @ReactMethod
    public void subscribe(final String topic, final int qos) {
        try {
          IMqttToken subToken = client.subscribe(topic, qos);

          subToken.setActionCallback(new IMqttActionListener() {
              @Override
              public void onSuccess(IMqttToken asyncActionToken) {
                  // The message was published
                log("Subscribe success");

              }
       
              @Override
              public void onFailure(IMqttToken asyncActionToken,
                                    Throwable exception) {
                  // The subscription could not be performed, maybe the user was not
                  // authorized to subscribe on the specified topic e.g. using wildcards
                log("Subscribe failed");     
              }
          });
        } catch (MqttException e) {
            log("Can't subscribe");
            log(e.toString());
          e.printStackTrace();
        }
      }


      void log(String message) {
        Log.d("MQTT", message);
      }

}