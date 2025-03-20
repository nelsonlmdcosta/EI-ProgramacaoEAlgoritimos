
// A Child/Derived class means that just like genetics in real life (kinda), the child keeps all the traits its parents has
// So technically as long as all the variables are protected/public as well as the functions the child has access to them!

public class Dog extends Animal
{
    // We can construct our cat, and pass the value over to the parent class to construct the variable there.
    Dog(String Sound)
    {
        // This is how we call the parent class's constructor, we can also use this to access variables and functions
        super(Sound);
    }

    @Override
    public void MakeNoise()
    {
        System.out.println("Dog Goes " + Sound);
    }
}