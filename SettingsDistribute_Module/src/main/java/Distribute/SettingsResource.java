package Distribute;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

/**
 * Created by leo on 16-3-21.
 */
@Path("/hosts/{hostname}/")
public class SettingsResource {

    String path="/etc/ganglia/GangliaController/SettingsOutput/";
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
            deleteFile(path+hostName);
            fileChannel.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            jsonString="[]";
        }
        finally {
            //System.out.println("return"+System.currentTimeMillis());

            return jsonString;
        }
    }
    private boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

}
