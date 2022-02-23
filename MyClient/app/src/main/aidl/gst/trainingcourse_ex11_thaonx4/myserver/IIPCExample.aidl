// IIPCExample.aidl
package gst.trainingcourse_ex11_thaonx4.myserver;

import gst.trainingcourse_ex11_thaonx4.myserver.type.PackageInfo;

// Declare any non-default types here with import statements

interface IIPCExample {
    /** Request the process ID of this service */
       int getPid();

       /** Count of received connection requests from clients */
       int getConnectionCount();

       /** Set displayed value of screen */
       void setDisplayedValue(String packageName, int pid, String data);

       void setDisplayedValueWithObject(in PackageInfo packageInfo);
}