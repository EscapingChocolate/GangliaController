package DataReader;


import org.json.*;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;


/**
 * Created by leo on 16-3-7.
 */

public class ReadGmetad {
    private Response response;
    private JSONObject root;
    public ReadGmetad(String gmetadURI){
        response = ClientBuilder.newClient().target(gmetadURI).request().get();
        root=XML.toJSONObject(response.readEntity(String.class));
    }

    //获取一次Gmetad中包含所有host的JSONArray，获取不成功时返回null
    public JSONArray getHostsArray(){

        try {
            return root.getJSONObject("GANGLIA_XML").getJSONObject("GRID").getJSONObject("CLUSTER").getJSONArray("HOST");
        }
        catch (Exception e){
            try {
                JSONObject host = root.getJSONObject("GANGLIA_XML").getJSONObject("GRID").getJSONObject("CLUSTER").getJSONObject("HOST");
                return new JSONArray().put(0, host);
            }
            catch (Exception e1){
                return null;
            }
        }
    }
    //以hostname为参数获取指定host的JSONObject，获取不成功时返回null
    /*
    public JSONObject getHost(String hostName){
        JSONArray hosts = getHostsArray();
        if (hosts==null)
            {return null;}
        for(Object e:hosts){
            JSONObject host = (JSONObject)e;
            if(host.getString("NAME").equals(hostName)){
                return host;
            }
        }
        return null;
    }
    */
    //以host及metricname为参数获取指定metric的JSONObject，获取不成功时返回null
    public JSONObject getMetric(JSONObject host,String metricName){
        JSONArray metricArray=null;
        try {
            metricArray = host.getJSONArray("METRIC");
        }
        catch (Exception e){
            return null;
        }
        finally {
            if(metricArray==null){
                return null;
            }
            for (Object e : metricArray) {
                JSONObject metric = new JSONObject(e.toString());
                if (metric.getString("NAME").equals(metricName)) {
                    return metric;
                }
            }
            return null;
        }
    }



}
