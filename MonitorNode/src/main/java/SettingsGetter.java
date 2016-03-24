import org.json.JSONArray;
import org.json.JSONObject;


import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.net.InetAddress;

/**
 * Created by leo on 16-3-22.
 */
public class SettingsGetter {
    private Response response;
    private JSONArray root;
    public SettingsGetter(String summaryNodeURI,String hostName){
        /*
        InetAddress addr = InetAddress.getLocalHost();
        String ip=addr.getHostAddress().toString(); //获取本机ip
        String hostName=addr.getHostName().toString(); //获取本机计算机名称
        */
        try {
            response = ClientBuilder.newClient().target(summaryNodeURI+"/"+hostName).request().get();
            root= new JSONArray(response.readEntity(String.class));
        }
        catch (Exception e){
            root=null;
        }

    }
    public JSONArray getRoot(){
        return root;
    }
}
