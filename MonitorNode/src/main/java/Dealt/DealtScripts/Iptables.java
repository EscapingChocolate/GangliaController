package Dealt.DealtScripts;

import Dealt.Wechat;
import Main.MainProc;
import org.json.JSONObject;

/**
 * Created by zlsong on 16-5-9.
 */
public class Iptables implements DealtScript {
    public boolean Dealt(JSONObject params) {
        try {
            String ipstr = params.getString("IP");
            String cmd[] = new String[]{"sh", shPath + "Iptables.sh", ipstr};
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
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
