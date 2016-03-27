import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by leo on 16-3-25.
 */
public class MetricDealt {

    //conf.d中配置为Enable，conf.a中配置为Disable
    private static String confdPath = "/etc/ganglia/conf.d/";
    private static String confaPath = "/etc/ganglia/conf.a/";

    //功能脚本存储路径
    private static String shPath = "/etc/ganglia/sh/";

    private static void GmondRestart(){
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

    public static void Enable(String metricName){
        /*
        File availableFile=new File(confaPath+metricName);
        File directFile = new File(confdPath+metricName);
        availableFile.renameTo(directFile);
         */
        try {
            String cmd[]=new String[]{"sh",shPath+"Move.sh",confaPath+metricName+".conf",confdPath+metricName+".conf"};
            Process process=Runtime.getRuntime().exec(cmd);
            process.waitFor();
            GmondRestart();
        }
        catch (Exception e){

        }
    }
    public static void Disable(String metricName){
        try {
            String cmd[]=new String[]{"sh",shPath+"Move.sh",confdPath+metricName+".conf",confaPath+metricName+".conf"};
            Process process=Runtime.getRuntime().exec(cmd);
            GmondRestart();
        }
        catch (Exception e){

        }
    }


    public static void ChangeProperty(String metricName,String propertyName,int param){
        try {
            File theChangedFile = new File(confdPath + metricName+".conf");
            BufferedReader reader = new BufferedReader(new FileReader(theChangedFile));
            String lineString,wholeString="";
            while((lineString=reader.readLine())!=null){
                if(lineString.contains(propertyName))
                {
                    lineString=propertyName+" = "+param;
                }
                wholeString+=lineString+"\n";
            }
            System.out.println(wholeString);
            reader.close();
            FileChannel fileChannel = new FileOutputStream(confdPath+metricName+".conf").getChannel();
            fileChannel.write(ByteBuffer.wrap(wholeString.getBytes()));
            fileChannel.close();
            GmondRestart();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
