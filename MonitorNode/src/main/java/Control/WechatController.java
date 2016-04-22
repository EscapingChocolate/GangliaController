package Control;

import Dealt.DealtScripts.DealtScript;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by zhoulisu on 16-4-22.
 */
public class WechatController {
    public static void SingleCommandsDealt(JSONArray wechatCommands){
        try {
            for (Object settingObject : wechatCommands) {
                JSONObject command = (JSONObject) settingObject;
                String act = command.getString("Action");
                java.lang.Class ownclass = java.lang.Class.forName("Dealt.DealtScripts."+act);
                DealtScript dealtScript = (DealtScript)ownclass.newInstance();
                if(dealtScript.Dealt(command)){

                }
                else {

                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
