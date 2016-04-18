package Receive;

import com.sun.deploy.net.HttpRequest;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider;
import org.json.JSONObject;
import org.json.XML;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import java.io.StringReader;

import static javafx.scene.input.KeyCode.T;


/**
 * Created by zhoulisu on 16-4-13.//166.111.68.197:11284
 */
@Path("/wechatreceive")
public class ReceiveFromWechat {
    public static String wechatMessage = "";
    @POST
    @Consumes("application/xml")
    public String Receive(@Context HttpContext httpContext){
        String xml = httpContext.getRequest().getEntity(String.class);
        JSONObject receiveContext = XML.toJSONObject(xml);
        System.out.println(receiveContext);
        return "ok";
    }

    @GET
    @Consumes("application/x-www-form-urlencoded")
    public String Check(){
        return "";
    }



    private <T> T converyToJavaBean(String xml, Class<T> c){
        T t = null;
        try {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
