// A class is a way to represent an object, this is a simple example of a representation of a person.
// It contains a Name and Age, as well as a way to construct it immediately with information, as well as Property Accessors
// Please Note that public, protected, private are very important.
// Public means anyone can see and edit everything
// Protected means only direct children/derived classes can see and edit the information
// Private means only the specific object can see the information.

// In this example, We Have a person. They have their own information (variables) and in order to access said inforamtion
// We need to "Ask Them" for it, aka property accessors.

// This way of programming is known as Encapsulation, it's where the information is kept in an object to access later or mutate if it makes sense to

// You should think of classes as a "template" that represents an "Object" of some type.
public class Person
{
    // Variables That Belong To This Class (aka representation of an object)
    private String Name;
    private int Age;

    Person(String Name, int Age)
    {
        this.Name = Name;
        this.Age = Age;
    }

    // Accessors To Properties/Internal Variables
    public String GetName(){ return Name; }
    public void SetName(String name){ Name = name; }

    public int GetAge(){ return Age; }
    public void SetAge(int age){ Age = age; }

}
