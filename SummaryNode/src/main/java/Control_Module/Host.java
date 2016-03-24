package Control_Module;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by leo on 16-3-10.
 */
public class Host {

    private JSONObject hostConfig;

    //读入处理规则设置文件生成对象
    public Host(String path) throws Exception{
        FileChannel fileChannel=new FileInputStream(path).getChannel();
        ByteBuffer buffer=ByteBuffer.allocate(1024);
        String jsonString="";

        while(fileChannel.read(buffer)!=-1)
        {
            buffer.flip();
            jsonString+=new String(buffer.array(),0,buffer.limit());
            buffer.clear();
        }
        hostConfig=new JSONObject(jsonString);
        fileChannel.close();
        ValidityChecking();
    }

    public String GetHostName(){
        return hostConfig.getString("HOST_NAME");
    }

    //检查hostConfig合法性,非法则终止程序
    /*
    hostConfig有效结构：
    {
        “HOST_NAME":"<name>",
        "METRICS":ff
            [
                (metric)
                {
                    "METRIC_NAME":"<name>",
                    "SECTIONS":
                        [
                            (section)
                            {
                                "DOMAINS":
                                    [
                                        (domain)
                                        {"MAX":"<max>","MIN":"<min>"},
                                        ......
                                    ]
                                "ACTIONS":
                                [
                                    (action)
                                    {
                                        (if "ACTION_TYPE"=="ALARM")
                                        {
                                            "ACTION_TYPE":"ALARM",“ALARM_METHOD":"<alarm_method>"
                                        }
                                        (if "ACTION_TYPE"=="SETTINGS_ALTER")
                                        {
                                            "ACTION_TYPE"=="SETTINGS_ALTER",
                                            "SETTINGS":
                                                [
                                                    (setting)
                                                    {
                                                        "METRIC_NAME":"<metric_name>",
                                                        "ACT":"ENABLE|DISABLE",
                                                        "HOST_NAME":"<host_name>"
                                                        "PARAM":"<param>"
                                                    }
                                                    ......
                                                ]
                                        }
                                    },
                                    ......
                                ]
                            }
                            ......
                        ]
                }
                ,
                (metric)
                ......
            ]
    }
    (大括号域内为JSONObject，中括号域内为JSONArray；小括号内为Array的元素，无意义）
    */
    private void ValidityChecking(){
        try{
            hostConfig.get("HOST_NAME");
            JSONArray metricsConfig=hostConfig.getJSONArray("METRICS");
            for(Object metricObject:metricsConfig){
                JSONObject metric=(JSONObject)metricObject;
                metric.get("METRIC_NAME");
                JSONArray sections=metric.getJSONArray("SECTIONS");
                for(Object sectionObject:sections){
                    JSONObject section=(JSONObject)sectionObject;
                    JSONArray domains=section.getJSONArray("DOMAINS");
                    for(Object domainObject:domains){
                        JSONObject domain=(JSONObject)domainObject;
                        if((!domain.has("MIN"))&&(!domain.has("MAX"))){
                            throw new Exception("NO MAX OR MIN");
                        }
                        if((domain.has("MAX"))&&(domain.has("MIN"))&&(domain.getDouble("MAX")<domain.getDouble("MIN"))){
                            throw new Exception("MAX<MIN");
                        }
                    }
                    for(Object actionObject:section.getJSONArray("ACTIONS")) {
                        JSONObject action=(JSONObject)actionObject;
                        if (action.getString("ACTION_TYPE").equals("ALARM")) {
                            action.get("ALARM_METHOD");
                        } else if (action.getString("ACTION_TYPE").equals("SETTINGS_ALTER")) {
                            for (Object settingObject : action.getJSONArray("SETTINGS")) {
                                JSONObject setting = (JSONObject) settingObject;
                                setting.get("METRIC_NAME");
                                setting.get("HOST_NAME");
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
            System.exit(0);
        }
    }

    /*针对一个realTimeHostInfo中的一个realTimeMetricsInfo从hostConfig中
    选取对应的metricConfig进行分析返回包含配置修改信息的Set<JSONObject>*/
    private Set<JSONObject> FillSettingsInfo(JSONObject realTimeSingleMetricInfo){

        Set<JSONObject> settingsAlterInfo=new HashSet<JSONObject>();//存储修改信息
        JSONArray metricsConfig=hostConfig.getJSONArray("METRICS");

        for(Object metricConfigObject:metricsConfig){
            JSONObject metricConfig=(JSONObject)metricConfigObject;

            //找寻对应metricConfig
            if(metricConfig.getString("METRIC_NAME").equals(realTimeSingleMetricInfo.getString("NAME"))){
                JSONArray sections=metricConfig.getJSONArray("SECTIONS");
                //根据metricConfig中section信息对realTimeMetricsInfo进行分析处理
                for(Object sectionObject:sections){
                    boolean inDomains=false;
                    JSONObject section=(JSONObject)sectionObject;
                    //判断VAL是否在有效域内
                    for(Object domainObject:section.getJSONArray("DOMAINS")){
                        JSONObject domain=(JSONObject)domainObject;

                        if(domain.has("MAX")&&(!domain.has("MIN"))){
                            if(realTimeSingleMetricInfo.getDouble("VAL")<domain.getDouble("MAX")){
                                inDomains=true;
                                break;
                            }
                        }
                        else if(domain.has("MIN")&&(!domain.has("MAX"))){
                            if(realTimeSingleMetricInfo.getDouble("VAL")>domain.getDouble("MIN")){
                                inDomains=true;
                                break;
                            }
                        }
                        else if(domain.has("MAX")&&domain.has("MIN")){
                            if((realTimeSingleMetricInfo.getDouble("VAL")>domain.getDouble("MIN"))&&(realTimeSingleMetricInfo.getDouble("VAL")<domain.getDouble("MAX"))){
                                inDomains=true;
                                break;
                            }
                        }
                    }

                    if(inDomains){
                        for(Object actionObject:section.getJSONArray("ACTIONS")) {
                            JSONObject action=(JSONObject)actionObject;
                            if (action.getString("ACTION_TYPE").equals("ALARM")) {

                                //alarm module not complete yet

                            } else if (action.getString("ACTION_TYPE").equals("SETTINGS_ALTER")) {
                                //将settings全部写入settingsAlterInfo
                                for (Object settingObject : action.getJSONArray("SETTINGS")) {
                                    settingsAlterInfo.add((JSONObject)settingObject);
                                }
                            }
                        }
                    }
                }

            }

        }
        return settingsAlterInfo;
    }

    public Set<JSONObject> updateSettingsInfo(JSONObject realTimeHostInfo){
        Set<JSONObject> settingsAlterOutput=new HashSet<JSONObject>();
        try {
            JSONArray realTimeMetricsInfo = realTimeHostInfo.getJSONArray("METRIC");
            //对每个metric进行分析处理，分析后的配置更改信息汇总于settingsOutput
            for (Object object : realTimeMetricsInfo) {
                JSONObject realTimeSingleMetricInfo = (JSONObject) object;
                settingsAlterOutput.addAll(FillSettingsInfo(realTimeSingleMetricInfo));
            }
        }
        finally {
            return settingsAlterOutput;
        }
    }



}
