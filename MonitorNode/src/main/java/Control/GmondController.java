package Control;

import Dealt.MetricDealt;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by leo on 16-3-24.
 */
public class GmondController {
    private JSONArray formerSettings = new JSONArray("[]");
    //检查与上次获取settings是否一致
    private boolean CheckSame(JSONArray realTimeSettings){
        return realTimeSettings.toString().equals(formerSettings.toString());
    }
    //处理单次setting
    public void SingleSettingsDealt(JSONArray realTimeSettings){
        String act=null,metricName=null;
        double param;
        try {
            //如果与上次setting不同则处理
            if (!CheckSame(realTimeSettings)) {
                for (Object settingObject : realTimeSettings) {
                    JSONObject setting = (JSONObject) settingObject;
                    act = setting.getString("ACT");
                    metricName = setting.getString("METRIC_NAME");
                    param = setting.getDouble("PARAM");

                    //使metric生效
                    if (act.equals("ENABLE")) {
                        MetricDealt.Enable(metricName);
                    }
                    //使metric失效
                    else if (act.equals("DISABLE")) {
                        MetricDealt.Disable(metricName);
                    }
                    //修改指定metric的collect_every参数
                    else if (act.equals("COLLECT_EVERY")) {
                        MetricDealt.ChangeProperty(metricName, "collect_every", (int) param);
                    }
                    //修改制定metric的value_threshold参数
                    else if (act.equals("VALUE_THRESHOLD")) {
                        MetricDealt.ChangeProperty(metricName, "value_threshold", (int) param);
                    }

                }
                //存储此次setting
                this.formerSettings = realTimeSettings;
            }
        }
        catch (JSONException e){
            System.out.println(e.getMessage());
        }
    }
}
