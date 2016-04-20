package Distribute;

import Dealt.WechatCommands.Command;
import Dealt.WechatReceiveDealt;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import static Receive.ReceiveFromWechat.wechatMessage;

/**
 * Created by zhoulisu on 16-4-13.
 */
@Path("/wechat/{hostname}/")
public class WechatMessageDistrubute {

    @GET
    @Produces("text/json")
    public String getWechatCommand(@PathParam("hostname")String hostname){
        try{
            JSONArray host = WechatReceiveDealt.wechatCommandStorage.getJSONArray(hostname);
            for(Object object:host){
                JSONObject commandRecord = (JSONObject)object;
                String action = commandRecord.getString("Action");
                Class ownClass = Class.forName("Dealt.WechatCommands."+action);
                Command command = (Command)ownClass.newInstance();
                command.messageToUserAsCommandGetByHost(commandRecord,hostname);
            }
            WechatReceiveDealt.wechatCommandStorage.remove(hostname);
            return host.toString();
        }
        catch (JSONException e) {

        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (IllegalAccessException e){
            e.printStackTrace();
        }
        catch (InstantiationException e){
            e.printStackTrace();
        }
        return "[]";
    }


}
