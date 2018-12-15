
public class Audi implements Car
{
    
    @Override
    public void drive(String driverName, String carName)
    {
        System.out.println("Audi is driving... " + "driverName=" + driverName + " carName=" + carName);
    }
}