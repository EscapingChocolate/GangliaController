package Control_Module;

import DataReader.ReadGmetad;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by leo on 16-3-14.
 */
public class Hosts {
    /*
    hostsSettingsAlterRecord结构：
    [
        (singleHostSettingAlter)
        {
            {"HOST_NAME":"<host_name>"},
            {
                "SETTINGS":
                    [
                        (setting)
                        {
                            "METRIC_NAME":"<metric_name>",
                            "ACT":"ENABLE|DISABLE",(extend...)
                            "PARAM":"<param>"
                        }
                        ......
                    ]
            }
        }
        ......
    ]
     */
    private class MyFilter implements FilenameFilter{
        private Pattern pattern=Pattern.compile("^.*?(?<!~)$");
        public boolean accept(File dir, String name) {
            return pattern.matcher(name).matches();
        }
    }

    private class HostSettingsAlterRecord{
        private Set<JSONObject> settingsRecord=new HashSet<JSONObject>();
        private String hostName;
        HostSettingsAlterRecord(String hostName){
            this.hostName=hostName;
        }
        public void putSettingsRecord(JSONObject singleRealTimeSettingAlter){
            this.settingsRecord.add(singleRealTimeSettingAlter);
        }
        public Set<JSONObject> getSettingsRecord(){
            return settingsRecord;
        }
        public String getHostName(){
            return hostName;
        }
    }

    private Set<HostSettingsAlterRecord> hostsSettingsAlterRecord=new HashSet<HostSettingsAlterRecord>();//记录每个Host的settings修改
    private Set<Host> hostSet=new HashSet<Host>();
    private ReadGmetad readGmetad;
    public Hosts(String path){
        File filePath=new File(path);
        String[] list=filePath.list(new MyFilter());
        for(String configFilePath:list){
            try {
                hostSet.add(new Host(path+"/"+configFilePath));
            }
            catch (Exception e){
                System.out.println("host create failed for "+e.getMessage());
            }
        }
    }
    public void SettingsUpdate(String gmetadURI,String settingsUpdateFilePath){
        this.readGmetad =new ReadGmetad(gmetadURI);
        if(readGmetad.getHostsArray()!=null) {
            for (Host host : hostSet) {
                for (Object object : readGmetad.getHostsArray()) {
                    JSONObject singleHostRealTimeInfo = (JSONObject) object;
                    if (host.GetHostName().equals(singleHostRealTimeInfo.getString("NAME"))) {
                        UpdateInfoFilter(host.updateSettingsInfo(singleHostRealTimeInfo));
                    }
                }

            }
            WriteSettingsUpdate(settingsUpdateFilePath);
        }
    }

    private void UpdateInfoFilter(Set<JSONObject> SingleHostRealTimeUpdateInfo){
        for(JSONObject singleRealTimeSettingAlter:SingleHostRealTimeUpdateInfo){
            boolean newHost=true;
            for(HostSettingsAlterRecord hostSettingsAlterRecord:hostsSettingsAlterRecord){
                if(singleRealTimeSettingAlter.getString("HOST_NAME").equals(hostSettingsAlterRecord.getHostName())){
                    hostSettingsAlterRecord.putSettingsRecord(singleRealTimeSettingAlter);
                    newHost=false;
                    break;
                }
            }
            if(newHost){
                HostSettingsAlterRecord hostSettingsAlterRecord=new HostSettingsAlterRecord(singleRealTimeSettingAlter.getString("HOST_NAME"));
                hostSettingsAlterRecord.putSettingsRecord(singleRealTimeSettingAlter);
                hostsSettingsAlterRecord.add(hostSettingsAlterRecord);
            }
        }
    }

    private void WriteSettingsUpdate(String folderPath){
        for(HostSettingsAlterRecord hostSettingsAlterRecord: hostsSettingsAlterRecord){
            try {
                File newfile =new File(folderPath  +"/"+ hostSettingsAlterRecord.getHostName());
                newfile.createNewFile();
                FileChannel fileChannel = new FileOutputStream(folderPath  +"/"+ hostSettingsAlterRecord.getHostName()).getChannel();
                fileChannel.write(ByteBuffer.wrap((new JSONArray(hostSettingsAlterRecord.getSettingsRecord())).toString().getBytes()));
                fileChannel.close();
                System.out.println("write settings:");
                hostsSettingsAlterRecord.clear();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("write finish");
    }
}
