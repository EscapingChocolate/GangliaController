import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by leo on 16-3-25.
 */
public class TestMain {
    public static void main(String[] args) throws Exception{
        MetricDealt.Enable("cpu_wio");
        MetricDealt.ChangeProperty("cpu_wio","collect_every",10);
    }
}
