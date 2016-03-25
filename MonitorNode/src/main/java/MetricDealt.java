import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by leo on 16-3-25.
 */
public class MetricDealt {

    static String confdPath ="/etc/ganglia/conf.d/";
    static String confaPath ="/etc/ganglia/conf.a/";

    public static void Enable(String metricName){
        /*
        File availableFile=new File(confaPath+metricName);
        File directFile = new File(confdPath+metricName);
        availableFile.renameTo(directFile);
         */
        String cmd = new String("mv "+confaPath+metricName+" "+confdPath+metricName);
        try {
            Runtime.getRuntime().exec(cmd);
        }
        catch (Exception e){

        }
    }
    public static void Disable(String metricName){
        String cmd = new String("mv "+confdPath+metricName+" "+confaPath+metricName);
        try {
            Runtime.getRuntime().exec(cmd);
        }
        catch (Exception e){

        }
    }
    public static void CollectEvery(String metricName,double period){
        try {
            FileChannel fileChannel = new FileInputStream(confdPath+metricName).getChannel();
        }
        catch (Exception e){

        }
    }
    public static void ValueThreshold(String metricName,double threshold){

    }
}
