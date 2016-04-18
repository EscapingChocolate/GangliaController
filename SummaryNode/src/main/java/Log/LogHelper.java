package Log;

import org.springframework.aop.AfterReturningAdvice;
import java.lang.reflect.Method;

/**
 * Created by zhoulisu on 16-4-12.
 */
public class LogHelper implements AfterReturningAdvice{
    public void afterReturning(Object object, Method method, Object[] objects, Object object1){
        System.out.println(objects[0]+" in "+object1.getClass());
    }
}
