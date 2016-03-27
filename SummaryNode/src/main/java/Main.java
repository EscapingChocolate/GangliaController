import Control_Module.Hosts;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by leo on 16-3-17.
 */
public class Main {
    public static void main(String[] args) throws Exception{
        FileChannel fileChannel=new FileInputStream("/etc/ganglia/GangliaController/SummaryNodeConfig").getChannel();
        ByteBuffer buffer=ByteBuffer.allocate(1024);
        String jsonString="";
        while(fileChannel.read(buffer)!=-1)
        {
            buffer.flip();
            jsonString+=new String(buffer.array(),0,buffer.limit());
            buffer.clear();
        }
        JSONObject summaryNodeConfig=new JSONObject(jsonString);
        String hostsConfigPath,gmetadURI,settingsUpdateFilePath;

        hostsConfigPath=summaryNodeConfig.getString("HOST_CONFIG_PATH");
        gmetadURI=summaryNodeConfig.getString("GMETAD_URI");
        settingsUpdateFilePath=summaryNodeConfig.getString("SETTINGS_UPDATE_FILE_PATH");


        Hosts theMainHosts=new Hosts(hostsConfigPath);
        while (true) {
            theMainHosts.SettingsUpdate(gmetadURI,settingsUpdateFilePath);
        }

    }
}
