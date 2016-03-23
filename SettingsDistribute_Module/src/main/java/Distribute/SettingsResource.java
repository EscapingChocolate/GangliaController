package Distribute;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by leo on 16-3-21.
 */
@Path("/hosts/{hostname}")
public class SettingsResource {

    String path="/home/leo/IdeaProjects/SummaryNode/src/main/resources/SettingsOutput/";
    @GET
    @Produces("text/json")
    public String getSettings(@PathParam("hostname")String hostName) {
        String jsonString="";
        try {
            FileChannel fileChannel=new FileInputStream(path+hostName).getChannel();
            ByteBuffer buffer=ByteBuffer.allocate(1024);
            while(fileChannel.read(buffer)!=-1)
            {
                buffer.flip();
                jsonString+=new String(buffer.array(),0,buffer.limit());
                buffer.clear();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            jsonString="{}";
        }
        finally {
            System.out.println("return");
            return jsonString;
        }
    }


}
