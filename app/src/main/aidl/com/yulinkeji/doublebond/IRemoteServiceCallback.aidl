// IRemoteServiceCallback.aidl
package com.yulinkeji.doublebond;

// Declare any non-default types here with import statements

interface IRemoteServiceCallback {
    /**
         * Called when the service has a new value for you.
         */
        void valueChanged(String value);
}
