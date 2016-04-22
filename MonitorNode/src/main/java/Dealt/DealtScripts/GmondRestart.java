package Dealt.DealtScripts;

import org.json.JSONArray;

/**
 * Created by zhoulisu on 16-4-22.
 */
public class GmondRestart {
    //conf.d中配置为Enable，conf.a中配置为Disable
    static final String confdPath = "/etc/ganglia/conf.d/";
    static final String confaPath = "/etc/ganglia/conf.a/";

    //功能脚本存储路径
    static final String shPath = "/etc/ganglia/GangliaController/sh/";

    public static void Restart(){
        try {
            String[] cmd = new String[]{"sh", shPath+"GmondRestart.sh"};
            Process proc = Runtime.getRuntime().exec(cmd);
            /*
            String s;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while((s=bufferedReader.readLine()) != null)
            System.out.println(s);
             */
        }
        catch (Exception e){

        }
    }
}
