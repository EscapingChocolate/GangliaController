package Main;

import Control.GmondController;
import Control.WechatCommandAnalyse;
import Control.WechatController;
import Download.SettingsGetter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by leo on 16-3-22.
 */
public class MainProc {
    public static String hostName;
    public static void main(String[] args){

        //获取MonitorNode配置
        FileChannel fileChannel=null;
        String jsonString = "";
        try {
            fileChannel = new FileInputStream("/etc/ganglia/GangliaController/MonitorNodeConfig").getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (fileChannel.read(buffer) != -1) {
                buffer.flip();
                jsonString += new String(buffer.array(), 0, buffer.limit());
                buffer.clear();
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
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
        String wechatCommandURI = monitorNodeConfig.getString("WECHAT_COMMAND_URI");
        hostName = monitorNodeConfig.getString("HOST_NAME");//本机hostname，须与summarynode配置文件一致
        int period = monitorNodeConfig.getInt("PERIOD");//轮询summarynode的周期
        //

        while (true){
            SettingsGetter settingsGetter=new SettingsGetter(summaryNodeURI,hostName);
            SettingsGetter wecahtCommandGetter = new SettingsGetter(wechatCommandURI,hostName);
            JSONArray realTimeSettings=settingsGetter.getRoot();
            JSONArray wechatCommands = wecahtCommandGetter.getRoot();
            if(realTimeSettings!=null) {
                GmondController.SingleSettingsDealt(realTimeSettings);
            }
            if(wechatCommands!=null){
                WechatController.SingleCommandsDealt(wechatCommands);
            }
            try {
                Thread thread = Thread.currentThread();
                thread.sleep(period);
            }
            catch (InterruptedException e){

            }
        }
    }
}
