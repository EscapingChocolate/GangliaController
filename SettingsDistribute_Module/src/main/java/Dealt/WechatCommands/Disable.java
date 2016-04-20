package Dealt.WechatCommands;

import Dealt.Wechat;
import Dealt.WechatReceiveDealt;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by zlsong on 16-4-20.
 */
public class Disable implements Command{
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
                if(jsonObject.getString("Action").equals("Disable")&&jsonObject.getString("MetricName").equals(metricName)){
                    host.remove(index);
                }
            }
            JSONObject disable = new JSONObject();
            disable.put("Action","Disable");
            disable.put("MetricName",metricName);
            disable.put("FromUser",userID);
            host.put(disable);
            return "This command has been listed in the server,waiting for the client to get it";
        }
        return "ParamsError";
    }

    public void messageToUserAsCommandGetByHost(JSONObject commandRecord,String hostName){
        String userID = commandRecord.getString("FromUser");
        String metricName = commandRecord.getString("MetricName");
        Wechat wechat = new Wechat();
        wechat.SendMessage("Command Disable "+hostName+" "+metricName+" get by host",userID);
    }
}
