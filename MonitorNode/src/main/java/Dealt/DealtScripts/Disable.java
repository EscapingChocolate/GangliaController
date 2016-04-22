package Dealt.DealtScripts;

import Dealt.Wechat;
import Main.MainProc;
import org.json.JSONObject;

/**
 * Created by zhoulisu on 16-4-22.
 */
public class Disable implements DealtScript {
    public boolean Dealt(JSONObject params) {
        try {
            String metricName = params.getString("MetricName");
            String cmd[]=new String[]{"sh",shPath+"Move.sh",confdPath+metricName+".conf",confaPath+metricName+".conf"};
            Process process=Runtime.getRuntime().exec(cmd);
            process.waitFor();
            GmondRestart.Restart();
            System.out.println("Disable!"+metricName);

            //wechat dealt
            String commandOutput = params.toString().replace("{"," ").replace("}"," ").replace("\""," ");
            if(params.has("FromUser")){
                Wechat wechat = new Wechat();
                String message = "host "+ MainProc.hostName+" has dealt Command "+commandOutput;
                wechat.SendMessage(message,params.getString("FromUser"));
            }
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
