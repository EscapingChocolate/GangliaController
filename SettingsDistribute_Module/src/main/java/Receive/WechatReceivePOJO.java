package Receive;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by zhoulisu on 16-4-14.
 */
@XmlRootElement
public class WechatReceivePOJO {
    @XmlElement
    private String ToUserName;
    @XmlElement
    private String AgentID;
    @XmlElement
    private String Encrypt;

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getAgentID() {
        return AgentID;
    }

    public void setAgentID(String agentID) {
        AgentID = agentID;
    }

    public String getEncrypt() {
        return Encrypt;
    }

    public void setEncrypt(String encrypt) {
        Encrypt = encrypt;
    }


}
