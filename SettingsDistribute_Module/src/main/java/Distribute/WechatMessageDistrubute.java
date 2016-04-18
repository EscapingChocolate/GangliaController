package Distribute;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static Receive.ReceiveFromWechat.wechatMessage;

/**
 * Created by zhoulisu on 16-4-13.
 */
@Path("/wechat/{hostname}/")
public class WechatMessageDistrubute {

    @GET
    @Produces("text/json")
    public String getWechatMessage(){
        return WechatMessageFilter();
    }

    private String WechatMessageFilter(){
        return wechatMessage;
    }
}
