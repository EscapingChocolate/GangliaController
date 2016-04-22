package Control;

import Dealt.DealtScripts.DealtScript;
import Dealt.MetricDealt;
import org.aopalliance.reflect.Class;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by leo on 16-3-24.
 */
public class GmondController {

    public static void SingleSettingsDealt(JSONArray realTimeSettings){
        try {
            for (Object settingObject : realTimeSettings) {
                JSONObject setting = (JSONObject) settingObject;
                String act = setting.getString("ACT");
                java.lang.Class ownclass = java.lang.Class.forName("Dealt.DealtScripts."+act);
                DealtScript dealtScript = (DealtScript)ownclass.newInstance();
                if(dealtScript.Dealt(setting.getJSONObject("PARAMS"))){

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
