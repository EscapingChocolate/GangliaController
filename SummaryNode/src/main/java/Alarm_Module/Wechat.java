package Alarm_Module;

import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;

/**
 * Created by zhoulisu on 16-4-6.
 */
public class Wechat {
    private static String corpid = "wxd36e6a72647bf8d7";
    private static String secret = "UWM5LQjsMsu5T8W4yfpxlio82NczGpG-1wxN2-ljn9sL8CAjekMjqymbzkBy2UkT";
    private static String getAccess_token_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpid+"&corpsecret="+secret;
    private static String access_token;
    private static int expires_in;
    private static long lasttime;
    private static String postURL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";

    public Wechat(){
        Response response = ClientBuilder.newClient().target(getAccess_token_URL).request().get();
        JSONObject retrunString = new JSONObject(response.readEntity(String.class));
        try {
            access_token = retrunString.getString("access_token");
            expires_in = retrunString.getInt("expires_in");
        }
        catch (JSONException e){
            System.out.println(e.getMessage());
        }

        System.out.println(access_token);
        System.out.println(expires_in);
    }

    public void SendMessage(String message){

        String param = new String("{\n" +
                "   \"touser\": \"@all\",\n" +
                "   \"msgtype\": \"text\",\n" +
                "   \"agentid\": 0,\n" +
                "   \"text\": {\n" +
                "       \"content\": \""+message+"\"\n" +
                "   },\n" +
                "   \"safe\":\"0\"\n" +
                "}");

        Invocation.Builder builder = ClientBuilder.newClient().target(postURL+access_token).request();
        Response response = builder.post(Entity.entity(param,"application/x-www-form-urlencoded"));
        System.out.println(response.readEntity(String.class));

    }
}
