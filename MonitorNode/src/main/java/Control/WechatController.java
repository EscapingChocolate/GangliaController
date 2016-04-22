package Control;

import Dealt.DealtScripts.DealtScript;
import Dealt.Wechat;
import Main.MainProc;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by zhoulisu on 16-4-22.
 */
public class WechatController {
    public static void SingleCommandsDealt(JSONArray wechatCommands){

            for (Object settingObject : wechatCommands) {
                JSONObject command = (JSONObject) settingObject;
                try {
                    String act = command.getString("Action");
                    java.lang.Class ownclass = java.lang.Class.forName("Dealt.DealtScripts."+act);
                    DealtScript dealtScript = (DealtScript)ownclass.newInstance();
                    if(dealtScript.Dealt(command)){

                    }
                    else {

                    }
                }
                catch (Exception e){
                    Wechat wechat = new Wechat();
                    String commandOutput = command.toString().replace("{"," ").replace("}"," ").replace("\""," ");
                    String message = "host "+ MainProc.hostName+" can't dealt Command "+commandOutput;
                    wechat.SendMessage(message,command.getString("FromUser"));
                    e.printStackTrace();
                }
        }

    }
}
