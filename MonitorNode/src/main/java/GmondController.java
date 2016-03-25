import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by leo on 16-3-24.
 */
public class GmondController {
    private JSONArray former = new JSONArray("[]");

    private boolean CheckSame(JSONArray realTimeSettings){
        return realTimeSettings.toString().equals(former.toString());
    }

    public void SingleSettingsDealt(JSONArray realTimeSettings){
        if(!CheckSame(realTimeSettings)) {
            for (Object settingObject : realTimeSettings) {
                JSONObject setting = (JSONObject) settingObject;

                String act=setting.getString("ACT");
                String metricName=setting.getString("METRIC_NAME");
                double param=setting.getDouble("PARAM");

                if(act.equals("ENABLE")){
                    MetricDealt.Enable(metricName);
                }

                else if(act.equals("DISABLE")){
                    MetricDealt.Disable(metricName);
                }

                else if(act.equals("COLLECT_EVERY")){
                    MetricDealt.CollectEvery(metricName,param);
                }

                else if(act.equals("VALUE_THRESHOLD")){
                    MetricDealt.ValueThreshold(metricName,param);
                }

            }
            this.former=realTimeSettings;
        }
    }
}
