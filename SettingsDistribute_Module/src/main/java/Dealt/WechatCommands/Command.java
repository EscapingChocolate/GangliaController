package Dealt.WechatCommands;

import org.json.JSONObject;

/**
 * Created by zlsong on 16-4-20.
 */
public interface Command {

    String contentDealt(String[] params,String userID);

    void messageToUserAsCommandGetByHost(JSONObject commandRecord,String hostName);
}
