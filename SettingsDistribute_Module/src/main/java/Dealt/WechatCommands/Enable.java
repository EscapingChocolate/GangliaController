package Dealt.WechatCommands;

import Dealt.Wechat;
import Dealt.WechatReceiveDealt;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by zlsong on 16-4-20.
 */
public class Enable implements Command{
    public String contentDealt(String[] params,String userID){
        if(params[1]!=null&&params[2]!=null) {
            String hostName = params[1];
            String metricName = params[2];
            JSONArray host;
            if(WechatReceiveDealt.wechatCommandStorage.has(hostName)){
                host = WechatReceiveDealt.wechatCommandStorage.getJSONArray(hostName);
            }
            else{
                host = new JSONArray();
                WechatReceiveDealt.wechatCommandStorage.put(hostName,host);
            }
            for(int index = 0;index<host.length();index++){
                JSONObject jsonObject = (JSONObject)host.get(index);
                if(jsonObject.getString("Action").equals("Enable")&&jsonObject.getString("MetricName").equals(metricName)){
                    host.remove(index);
                }
            }
            JSONObject enable = new JSONObject();
            enable.put("Action","Enable");
            enable.put("MetricName",metricName);
            enable.put("FromUser",userID);
            host.put(enable);
            return "This command has been listed in the server,waiting for the client to get it";
        }
        return "ParamsError";
    }

    public void messageToUserAsCommandGetByHost(JSONObject commandRecord,String hostName){
        String userID = commandRecord.getString("FromUser");
        String metricName = commandRecord.getString("MetricName");
        Wechat wechat = new Wechat();
        wechat.SendMessage("Command Enable "+hostName+" "+metricName+" get by host",userID);
    }
}
