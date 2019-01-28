using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Mqtt.Paho.RNMqttPaho
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNMqttPahoModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNMqttPahoModule"/>.
        /// </summary>
        internal RNMqttPahoModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNMqttPaho";
            }
        }
    }
}
