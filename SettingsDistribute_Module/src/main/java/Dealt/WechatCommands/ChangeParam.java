package Dealt.WechatCommands;

import Dealt.Wechat;
import Dealt.WechatReceiveDealt;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by zlsong on 16-4-20.
 */
public class ChangeParam implements Command{
    public String contentDealt(String[] params,String userID){
        if(params[1]!=null&&params[2]!=null&&params[3]!=null&&params[4]!=null) {
            String hostName = params[1];
            String metricName = params[2];
            String paramName = params[3];
            String paramValue = params[4];
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
                if(jsonObject.getString("Action").equals("ChangeParam")&&jsonObject.getString("MetricName").equals(metricName)){
                    host.remove(index);
                }
            }
            JSONObject changeParam = new JSONObject();
            changeParam.put("Action","ChangeParam");
            changeParam.put("MetricName",metricName);
            changeParam.put("ParamName",paramName);
            changeParam.put("ParamValue",paramValue);
            changeParam.put("FromUser",userID);
            host.put(changeParam);
            return "This command has been listed in the server,waiting for the client to get it";
        }
        return "ParamsError";
    }
    public void messageToUserAsCommandGetByHost(JSONObject commandRecord,String hostName){
        String userID = commandRecord.getString("FromUser");
        String metricName = commandRecord.getString("MetricName");
        String paramName = commandRecord.getString("ParamName");
        String paramValue = commandRecord.getString("ParamValue");
        Wechat wechat = new Wechat();
        wechat.SendMessage("Command ChangeParam "+hostName+" "+metricName+" "+paramName+" "+paramValue+" get by host",userID);
    }
}
