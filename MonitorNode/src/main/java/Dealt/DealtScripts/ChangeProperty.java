package Dealt.DealtScripts;

import Dealt.Wechat;
import Main.MainProc;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by zhoulisu on 16-4-22.
 */
public class ChangeProperty implements DealtScript {
    public boolean Dealt(JSONObject params) {
        try {

            String metricName = params.getString("MetricName");
            String propertyName = params.getString("ParamName");
            String value = params.getString("ParamValue");

            File theChangedFile = new File(confdPath + metricName+".conf");
            BufferedReader reader = new BufferedReader(new FileReader(theChangedFile));
            String lineString,wholeString="";
            while((lineString=reader.readLine())!=null){
                if(lineString.contains(propertyName))
                {
                    lineString=propertyName+" = "+value;
                }
                wholeString+=lineString+"\n";
            }
            //System.out.println(wholeString);
            reader.close();
            FileChannel fileChannel = new FileOutputStream(confdPath+metricName+".conf").getChannel();
            fileChannel.write(ByteBuffer.wrap(wholeString.getBytes()));
            fileChannel.close();
            GmondRestart.Restart();
            //wechat dealt
            String commandOutput = params.toString().replace("{"," ").replace("}"," ").replace("\""," ");
            if(params.has("FromUser")){
                Wechat wechat = new Wechat();
                String message = "host "+ MainProc.hostName+" has dealt Command "+commandOutput;
                wechat.SendMessage(message,params.getString("FromUser"));
            }
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
