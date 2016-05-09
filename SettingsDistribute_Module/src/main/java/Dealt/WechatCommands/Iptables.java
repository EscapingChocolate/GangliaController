package Dealt.WechatCommands;

import Dealt.Wechat;
import Dealt.WechatReceiveDealt;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by zlsong on 16-5-9.
 */
public class Iptables implements Command {
    public String contentDealt(String[] params,String userID){
        if(params[1]!=null&&params[2]!=null) {
            String hostName = params[1];
            String IP = params[2];
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
                if(jsonObject.getString("Action").equals("Iptables")&&jsonObject.getString("IP").equals(IP)){
                    host.remove(index);
                }
            }
            JSONObject iptables = new JSONObject();
            iptables.put("Action","Iptables");
            iptables.put("IP",IP);
            iptables.put("FromUser",userID);
            host.put(iptables);
            return "This command has been listed in the server,waiting for the client to get it";
        }
        return "ParamsError";
    }

    public void messageToUserAsCommandGetByHost(JSONObject commandRecord,String hostName){
        String userID = commandRecord.getString("FromUser");
        String IP = commandRecord.getString("IP");
        Wechat wechat = new Wechat();
        wechat.SendMessage("Command Iptables "+hostName+" "+IP+" get by host",userID);
    }
}
