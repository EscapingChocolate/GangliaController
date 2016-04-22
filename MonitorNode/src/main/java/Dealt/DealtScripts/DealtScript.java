package Dealt.DealtScripts;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by zhoulisu on 16-4-22.
 */
public interface DealtScript {
    //conf.d中配置为Enable，conf.a中配置为Disable
    final String confdPath = "/etc/ganglia/conf.d/";
    final String confaPath = "/etc/ganglia/conf.a/";

    //功能脚本存储路径
    final String shPath = "/etc/ganglia/GangliaController/sh/";

    boolean Dealt(JSONObject params);
}
