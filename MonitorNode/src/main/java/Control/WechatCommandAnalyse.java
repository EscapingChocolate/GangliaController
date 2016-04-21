package Control;

import Dealt.MetricDealt;
import Dealt.Wechat;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by zlsong on 16-4-21.
 */
public class WechatCommandAnalyse {
    public static void Analyse(JSONArray wechatCommands,String hostName){
        for(Object object:wechatCommands){
            JSONObject command = (JSONObject)object;
            System.out.println(command.toString());
            String commandOutput = command.toString().replace("{"," ").replace("}"," ").replace("\""," ");
            String action=null;
            String userId=null;
            Wechat wechat = new Wechat();
            try{
                userId = command.getString("FromUser");
            }
            catch (Exception e){
                //no userid dealt

            }
            try{
                action = command.getString("Action");
            }
            catch (Exception e){
                //get Action error dealt
                String message = "host "+hostName+" can't get Action by Command "+commandOutput;
                wechat.SendMessage(message,userId);
            }
            if(action!=null) {

                if (action.equals("ShutDown")) {

                }
                else if (action.equals("Enable")) {
                    String metricName=null;
                    try{
                        metricName = command.getString("MetricName");
                    }
                    catch (Exception e){
                        String message = "host "+hostName+" can't get MetricName by Command "+commandOutput;
                        wechat.SendMessage(message,userId);
                    }
                    if(metricName!=null) {
                        MetricDealt.Enable(metricName);
                        String message = "host "+hostName+" has dealt Command"+commandOutput;
                        wechat.SendMessage(message,userId);
                    }
                }
                else if (action.equals("Disable")) {
                    String metricName=null;
                    try{
                        metricName = command.getString("MetricName");
                    }
                    catch (Exception e){
                        String message = "host "+hostName+" can't get MetricName by Command "+commandOutput;
                        wechat.SendMessage(message,userId);
                    }
                    if(metricName!=null) {
                        MetricDealt.Disable(metricName);
                        String message = "host "+hostName+" has dealt Command "+commandOutput;
                        wechat.SendMessage(message,userId);
                    }
                }
                else if (action.equals("ChangeParam")) {
                    String metricName=null;
                    String paramName=null;
                    String paramValue=null;
                    try{
                        metricName = command.getString("MetricName");
                        paramName = command.getString("ParamName");
                        paramValue = command.getString("ParamValue");
                    }
                    catch (Exception e){
                        String message = "host "+hostName+" can't get params of ChangeParam by Command "+commandOutput;
                        wechat.SendMessage(message,userId);
                    }
                    if(metricName!=null&&paramName!=null&&paramValue!=null) {
                        MetricDealt.ChangeProperty(metricName,paramName,paramValue);
                        String message = "host "+hostName+" has dealt Command "+commandOutput;
                        wechat.SendMessage(message,userId);
                    }
                }
                else {
                    String message = "host "+hostName+" can't verify acton by command "+commandOutput;
                    wechat.SendMessage(message,userId);
                }
            }
        }
    }
}
