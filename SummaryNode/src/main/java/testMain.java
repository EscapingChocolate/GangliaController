import Alarm_Module.Wechat;

/**
 * Created by zhoulisu on 16-4-6.
 */
public class testMain {
    public static void main(String[] args) {
        Wechat wechat = new Wechat();
        wechat.SendMessage("hello again");
    }
}
