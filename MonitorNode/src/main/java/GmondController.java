import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by leo on 16-3-24.
 */
public class GmondController {
    private JSONArray former = new JSONArray("[]");
    GmondController(){
    }
    private boolean CheckSame(JSONArray realTimeSettings){
        return realTimeSettings.toString().equals(former.toString());
    }
    private void MetricEnable(String metricName,String param){
        System.out.println("Enable "+metricName+" "+param);
    }

    private void MetricDisable(String metricName,String param){
        System.out.println(metricName+" "+param);
    }

    public void SingleSettingsDealt(JSONArray realTimeSettings){
        if(!CheckSame(realTimeSettings)) {
            for (Object settingObject : realTimeSettings) {
                JSONObject setting = (JSONObject) settingObject;
                if (setting.getString("ACT").equals("ENABLE")) {
                    MetricEnable(setting.getString("METRIC_NAME"),setting.getString("PARAM"));
                } else if (setting.getString("ACT").equals("DISABLE")) {
                    MetricDisable(setting.getString("METRIC_NAME"),setting.getString("PARAM"));
                }
            }
            this.former=realTimeSettings;
        }
    }
}
