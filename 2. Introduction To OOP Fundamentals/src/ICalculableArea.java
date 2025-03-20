
// https://www.w3schools.com/java/java_interface.asp
public interface ICalculableArea
{
    // All non private and non static functions are considered "virtual" which means they can be overriden by a child class
    // We tag it as default, because if nothing overrides it it'll be the default version to use
    public default float CalculateArea()
    {
        System.out.println("Not Implemented yet!");
        return 0.0f;
    }
}
