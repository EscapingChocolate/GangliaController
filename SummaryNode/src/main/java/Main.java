import Control_Module.Hosts;

/**
 * Created by leo on 16-3-17.
 */
public class Main {
    public static void main(String[] args) throws Exception{
        Hosts theMainHosts=new Hosts("/home/leo/IdeaProjects/SummaryNode/src/main/resources/HostConfig");
        while (true) {
            theMainHosts.SettingsUpdate("http://localhost:8652/");
        }

    }
}
