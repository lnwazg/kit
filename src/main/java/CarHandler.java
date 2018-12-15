import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CarHandler implements InvocationHandler
{
    private Car car;
    
    public CarHandler(Car car)
    {
        this.car = car;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable
    {
        System.out.println("before");
        method.invoke(car, args);
        System.out.println("after");
        return null;
    }
}
