package Dealt;

import Dealt.WechatCommands.Command;
import org.json.JSONObject;

/**
 * Created by zlsong on 16-4-20.
 */
public class WechatReceiveDealt {

    public static JSONObject wechatCommandStorage = new JSONObject();
    public static String wechatContentDealt(String content,String userID){
        String[] params = content.split(" ");
        String commandName = params[0];
        try {
            Class ownClass =  Class.forName("Dealt.WechatCommands."+commandName);
            Command command = (Command)ownClass.newInstance();
            return command.contentDealt(params,userID);
        }
        catch (Exception e){
            e.printStackTrace();
            return "Command Not Found";
        }
    }
}
