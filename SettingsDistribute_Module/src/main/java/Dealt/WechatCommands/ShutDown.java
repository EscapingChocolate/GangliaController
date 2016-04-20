package Dealt.WechatCommands;

import Dealt.Wechat;
import Dealt.WechatReceiveDealt;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by zlsong on 16-4-20.
 */
public class ShutDown implements Command{
    public String contentDealt(String[] params,String userID){
        if(params[1]!=null) {
            String hostName = params[1];
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
                if(jsonObject.getString("Action").equals("ShutDown")){
                    host.remove(index);
                }
            }
            JSONObject shutDown = new JSONObject();
            shutDown.put("Action","ShutDown");
            shutDown.put("FromUser",userID);
            host.put(shutDown);
            return "This command has been listed in the server,waiting for the client to get it";
        }
        return "ParamsError";
    }
    public void messageToUserAsCommandGetByHost(JSONObject commandRecord,String hostName){
        String userID = commandRecord.getString("FromUser");
        Wechat wechat = new Wechat();
        wechat.SendMessage("Command ShutDown "+hostName+" get by host",userID);
    }
}
