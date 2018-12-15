import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.lnwazg.kit.reflect.ClassKit;

public class Test
{
    public static void main(String[] args)
    {
        try
        {
            Class<?> personClass = Class.forName("Person");
            Object personObj = personClass.newInstance();
            Method sayMethod = personClass.getDeclaredMethod("say");
            sayMethod.invoke(personObj);
            
            //            Method talkMethod = personClass.getDeclaredMethod("talk");
            //            talkMethod.invoke(personObj, "hahaha");
            
            Method[] methods = personClass.getDeclaredMethods();
            for (Method method : methods)
            {
                if (method.getName().equals("talk"))
                {
                    method.invoke(personObj, "hahaha");
                }
            }
            
            ClassKit.invokeMethod(personObj, "talk", "xxx");
            
            Car audi = (Car)Proxy.newProxyInstance(Car.class.getClassLoader(), new Class<?>[] {Car.class}, new CarHandler(new Audi()));
            audi.drive("name1", "audi");
            System.out.println(audi.getClass());
            
            methods = audi.getClass().getMethods();
            for (Method method : methods)
            {
                if (method.getName().equals("drive"))
                {
                    method.invoke(audi, "aaa", "bbb");
                }
            }
            
            System.out.println(audi.getClass().getMethod("drive", new Class[] {String.class, String.class}));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
}
