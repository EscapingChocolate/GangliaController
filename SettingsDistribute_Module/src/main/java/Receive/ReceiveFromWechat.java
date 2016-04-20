package Receive;

import Dealt.WechatReceiveDealt;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.json.JSONObject;
import org.json.XML;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.xml.ws.spi.http.HttpContext;

/**
 * Created by zhoulisu on 16-4-13.//166.111.68.197:11284
 */
@Path("/wechatreceive")
public class ReceiveFromWechat {
    public static String wechatMessage = "";
    private final String token = "zhoulisu";
    private final String encodingAESKey = "enE8jlfxe1sP7bI8nKRd0UCZ3r4MgJkR8WvxB0STz2t";
    private final String corpID = "wxd36e6a72647bf8d7";

    //receive text from wechat
    @POST
    public String Receive(String raw, @QueryParam("msg_signature")String msg_signature, @QueryParam("timestamp")String timestamp, @QueryParam("nonce")String nonce){
        try {
            //String raw = httpContext.getRequest().getEntity(String.class);
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(token, encodingAESKey, corpID);
            String xml = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, raw);
            JSONObject root = XML.toJSONObject(xml);
            JSONObject xmljson = root.getJSONObject("xml");

            String content = xmljson.getString("Content");
            Long createTime = xmljson.getLong("CreateTime");
            String toUserName = xmljson.getString("ToUserName");
            String fromUserName = xmljson.getString("FromUserName");
            String msgType = xmljson.getString("MsgType");
            int agentID = xmljson.getInt("AgentID");
            Long msgId = xmljson.getLong("MsgId");

            String responseText = WechatReceiveDealt.wechatContentDealt(content,fromUserName);
            String sRespData = "<xml><ToUserName><![CDATA["+fromUserName+"]]></ToUserName><FromUserName><![CDATA["+toUserName+"]]></FromUserName><CreateTime>"+System.currentTimeMillis()+"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA["+responseText+"]]></Content><MsgId>"+msgId+"</MsgId><AgentID>"+agentID+"</AgentID></xml>";
            return wxcpt.EncryptMsg(sRespData,timestamp,nonce);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    //verifyURL

    @GET
    public String Check(@QueryParam("msg_signature")String msg_signature,@QueryParam("timestamp")String timestamp,@QueryParam("nonce")String nonce,@QueryParam("echostr")String echostr)
    {
        try {
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(token, encodingAESKey, corpID);
            String returnechostr =wxcpt.VerifyURL(msg_signature, timestamp, nonce, echostr);
            return returnechostr;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
