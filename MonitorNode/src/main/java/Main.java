import org.json.JSONArray;
import org.json.JSONObject;

import javax.management.monitor.Monitor;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.Properties;

/**
 * Created by leo on 16-3-22.
 */
public class Main {


    public static void main(String[] args){

        //获取MonitorNode配置
        FileChannel fileChannel=null;
        String jsonString = "";
        try {
            fileChannel = new FileInputStream("etc/ganglia/MonitorNodeConfig").getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (fileChannel.read(buffer) != -1) {
                buffer.flip();
                jsonString += new String(buffer.array(), 0, buffer.limit());
                buffer.clear();
            }
        }
        catch (IOException e){

        }
        finally {
            try {
                fileChannel.close();
            }
            catch (IOException e){

            }
            catch (NullPointerException e){

            }
        }

        JSONObject monitorNodeConfig=new JSONObject(jsonString);
        String summaryNodeURI = monitorNodeConfig.getString("SUMMARY_NODE_URI");//分析节点uri
        String hostName = monitorNodeConfig.getString("HOST_NAME");//本机hostname，须与summarynode配置文件一致
        int period = monitorNodeConfig.getInt("PERIOD");//轮询summarynode的周期
        //
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
