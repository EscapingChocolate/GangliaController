import org.json.JSONArray;
import org.json.JSONObject;

import javax.management.monitor.Monitor;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.Properties;

/**
 * Created by leo on 16-3-22.
 */
public class Main {
    public static void main(String[] args) throws Exception{
        FileChannel fileChannel=new FileInputStream("/home/leo/Documents/GangliaController/MonitorNodeConfig").getChannel();
        ByteBuffer buffer=ByteBuffer.allocate(1024);
        String jsonString="";
        while(fileChannel.read(buffer)!=-1)
        {
            buffer.flip();
            jsonString+=new String(buffer.array(),0,buffer.limit());
            buffer.clear();
        }
        JSONObject monitorNodeConfig=new JSONObject(jsonString);
        String summaryNodeURI = monitorNodeConfig.getString("SUMMARY_NODE_URI");
        String hostName = monitorNodeConfig.getString("HOST_NAME");
        GmondController gmondController=new GmondController();
        while (true){
            SettingsGetter settingsGetter=new SettingsGetter(summaryNodeURI,hostName);
            JSONArray realTimeSettings=settingsGetter.getRoot();
            if(realTimeSettings!=null) {
                gmondController.SingleSettingsDealt(realTimeSettings);
            }
        }
    }
}
