// Example of Abstraction which is used next into Inheritance
// We want to have animals, so we abstract the concept, how do we generalize a cat and a dog? Animal you say?
// We take that and make it the base class, we then use the abstract keyword to make sure it's clear we can never create an Animal.
// We can however reference the base class (see main)


public abstract class Animal
{
    protected String Sound = "";

    Animal(String Sound)
    {
        this.Sound = Sound;
    }

    // If a function is marked as abstract our child/derived class MUST implement the body of this function (Useful for state machines wink wink nudge nudge ;))
    public abstract void MakeNoise();
}
