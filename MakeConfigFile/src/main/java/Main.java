import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

/**
 * Created by zhoulisu on 16-4-21.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //System.out.println("enter the document path");
        String folderPath = "/etc/ganglia/GangliaController/HostsConfig";

        JSONObject hostConfig = new JSONObject();
        System.out.println("enter the host name please");
        String hostName = scanner.nextLine();
        hostConfig.put("HOST_NAME",hostName);
        hostConfig.put("METRICS",new JSONArray());
        JSONArray metrics = hostConfig.getJSONArray("METRICS");
        System.out.println("enter the num of metrics");
        int metricNum = scanner.nextInt();
        String nouse = scanner.nextLine();
        for(int i=0;i<metricNum;i++){
            JSONObject metric = new JSONObject();
            metrics.put(metric);///////
            System.out.println("    enter the name of metric"+i);
            System.out.print("    ");
            String metricName = scanner.nextLine();
            metric.put("METRIC_NAME",metricName);
            metric.put("SECTIONS",new JSONArray());
            JSONArray sections = metric.getJSONArray("SECTIONS");
            System.out.println("    enter the num of sections in metric "+metricName);
            System.out.print("    ");
            int sectionNum = scanner.nextInt();
            nouse = scanner.nextLine();
            for(Integer j=0;j<sectionNum;j++){
                JSONObject section = new JSONObject();
                sections.put(section);///////
                section.put("SECTION_ID",j.toString());
                section.put("DOMAINS",new JSONArray());
                JSONArray domains = section.getJSONArray("DOMAINS");
                System.out.println("        enter the num of domains in section"+j+" in metric "+metricName);
                System.out.print("        ");
                int domainNum = scanner.nextInt();
                nouse = scanner.nextLine();
                for(int k=0;k<domainNum;k++){
                    JSONObject domain = new JSONObject();
                    domains.put(domain);/////
                    System.out.println("            enter the mod of domain"+k+":1 for min only;2 for max only;3 for both");
                    System.out.print("            ");
                    int mod = scanner.nextInt();
                    nouse = scanner.nextLine();
                    while (true)
                    {
                        if (mod == 1) {
                            System.out.println("            enter the min value");
                            System.out.print("            ");
                            String min = scanner.nextLine();
                            domain.put("MIN",min);
                            break;
                        }
                        else if (mod == 2) {
                            System.out.println("            enter the max value");
                            System.out.print("            ");
                            String max = scanner.nextLine();
                            domain.put("MAX",max);
                            break;
                        }
                        else if (mod == 3) {
                            System.out.println("            enter the min value");
                            System.out.print("            ");
                            String min = scanner.nextLine();
                            System.out.println("            enter the max value");
                            System.out.print("            ");
                            String max = scanner.nextLine();
                            domain.put("MIN",min);
                            domain.put("MAX",max);
                            break;
                        }
                        else {
                            System.out.println("            mod error!");
                            //System.out.print("            ");
                        }
                    }

                }
                section.put("ACTIONS",new JSONArray());
                JSONArray actions = section.getJSONArray("ACTIONS");
                System.out.println("        enter the num of actions in section"+j+" in metric "+metricName);
                System.out.print("        ");
                int actionNum = scanner.nextInt();
                nouse = scanner.nextLine();
                for(Integer l=0;l<actionNum;l++){
                    JSONObject action = new JSONObject();
                    actions.put(action);///
                    action.put("ACTION_ID",l.toString());
                    while (true)
                    {
                        System.out.println("            enter ACTION_EVERY in action"+l+" in section"+j+" in metric "+metricName+",-1 for only once");
                        System.out.print("            ");
                        Integer action_every = scanner.nextInt();
                        nouse = scanner.nextLine();
                        if(action_every<0&&action_every!=-1){
                            System.out.println("            ACTION_EVERY error");
                            //System.out.print("            ");
                        }
                        else {
                            action.put("ACTION_EVERY",action_every.toString());
                            break;
                        }
                    }
                    while (true) {
                        System.out.println("            enter ACTION_TYPE in action" + l + " in section" + j + " in metric " + metricName + ",DO_SCRIPT or ALARM");
                        System.out.print("            ");
                        String action_type = scanner.nextLine();
                        if (action_type.equals("DO_SCRIPT")) {
                            action.put("ACTION_TYPE",action_type);
                            action.put("SETTINGS",new JSONArray());
                            JSONArray settings = action.getJSONArray("SETTINGS");
                            System.out.println("            enter the num of settings in action"+l+" in section"+j+" in metric "+metricName);
                            System.out.print("            ");
                            int settingNum = scanner.nextInt();
                            nouse = scanner.nextLine();
                            for(int m=0;m<settingNum;m++){
                                JSONObject setting = new JSONObject();
                                settings.put(setting);
                                //System.out.println("                enter metric_name in setting"+m+" in action"+l+" in section"+j+" in metric "+metricName);
                                //System.out.print("                ");
                                //String metricNameInSetting = scanner.nextLine();
                                System.out.println("                enter act in setting"+m+" in action"+l+" in section"+j+" in metric "+metricName);
                                System.out.print("                ");
                                String act = scanner.nextLine();
                                System.out.println("                enter host_name in setting"+m+" in action"+l+" in section"+j+" in metric "+metricName);
                                System.out.print("                ");
                                String hostNameInSetting = scanner.nextLine();
                                int numParam;
                                while (true) {
                                    System.out.println("                enter num of param in setting" + m + " in action" + l + " in section" + j + " in metric " + metricName);
                                    System.out.print("                ");
                                    numParam = scanner.nextInt();
                                    nouse = scanner.nextLine();
                                    if(numParam<1){
                                        System.out.println("                param num error");
                                    }
                                    else {
                                        break;
                                    }
                                }

                                setting.put("PARAMS",new JSONObject());
                                JSONObject params = setting.getJSONObject("PARAMS");
                                for(int n=0;n<numParam;n++){
                                    System.out.println("                    enter name of param"+n);
                                    System.out.print("                    ");
                                    String paramName = scanner.nextLine();
                                    System.out.println("                    enter name of param"+n);
                                    System.out.print("                    ");
                                    String paramValue = scanner.nextLine();
                                    params.put(paramName,paramValue);
                                }

                                setting.put("ACT",act);
                                setting.put("HOST_NAME",hostNameInSetting);

                                System.out.println(" ");
                            }
                            System.out.println(" ");
                            break;
                        }
                        else if(action_type.equals("ALARM")){
                            action.put("ACTION_TYPE",action_type);
                            System.out.println(" ");
                            break;
                        }
                        else{
                            System.out.println("            ACTION_TYPE error");
                            System.out.println(" ");
                            //System.out.print("            ");
                        }
                    }
                }
            }
        }
        System.out.println(hostConfig);
        File newfile =new File(folderPath  +"/"+hostName);
        try {
            newfile.createNewFile();
            FileChannel fileChannel = new FileOutputStream(folderPath  +"/"+hostName).getChannel();
            fileChannel.write(ByteBuffer.wrap(hostConfig.toString().getBytes()));
            fileChannel.close();
        }
        catch (Exception e){

        }
    }
}
