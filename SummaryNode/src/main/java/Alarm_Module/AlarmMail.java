package Alarm_Module;

import java.util.Properties;

/**
 * Created by leo on 16-3-21.
 */
public class AlarmMail {
    private Properties properties=new Properties();
    AlarmMail(){
        properties.put("mail.pop3.host","pop3.live.com");
    }

}
