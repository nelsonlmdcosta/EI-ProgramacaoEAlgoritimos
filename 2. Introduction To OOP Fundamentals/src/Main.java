// OOP Fundamentals Example
// https://www.w3schools.com/java/java_oop.asp
// OOP Is Usfful as it tries to stop you repeating yourself constantly. (DRY Principle Dont Repeat Yourself)
// Adds a level of architecture and clean code to your project.
// It follows one of two main paths Inheritance vs Composition
// https://www.infoworld.com/article/2261980/java-challenger-7-debugging-java-inheritance.html

// Person Class Is A Simple Example Of 1 Of the Pillars Called Encapsulation

import java.awt.*;

public class Main
{
    public static void main(String[] args)
    {

        // Simple Representation Of Encapsulation
        Person Nelson = new Person("Nelson Costa", 34);
        System.out.println(Nelson.ToString());

        Person Marcelo = new Person("Marcelo Lopes", 20);
        System.out.println(Marcelo.ToString());

        // This is a more complex example of Abstraction/Inheritance
        Cat CatObject = new Cat("Meow");
        Dog DogObject = new Dog("Woof");

        // We Can Call Their Specific Functions
        CatObject.MakeNoise();
        DogObject.MakeNoise();

        // A Cat And A Dog Are Both Animals So Technically We Can Access Them Using Their Parent
        Animal CatAsAnimal = CatObject;
        Animal DogAsAnimal = DogObject;

        // So as both are animals, we go through the parent function, to see what the child's implementation is and execute it! :D
        CatAsAnimal.MakeNoise();
        DogAsAnimal.MakeNoise();

        // Now putting two distinct things in an array sounds impossible or Heresy!
        // But because of how abstraction works this is completly plausible and actually cosnidered good practice!
        // Note that I put all 4 to show there is no difference.
        Animal[] AnimalArray = { CatObject, DogObject, CatAsAnimal, DogAsAnimal };
        for(int i = 0; i < AnimalArray.length; ++i)
        {
            AnimalArray[i].MakeNoise();

            String IsACat = AnimalArray[i] instanceof Cat ? "true" : "false";
            System.out.println( IsACat );
        }




//        [ Ani | Ani | Ani | Cat | Cat ]
//        [ Ani | Ani | Ani | Dog | Dog | Dog ]


        // Sometimes we don't want objects to be so directly related, so we can alternatively aim for "Composition" rather than "Inheritance"
        // In this case we only care about objects, that follow the rule that use this specific funcion.
        ICalculableArea[] Shapes = { new Triangle(2.0f,3.0f), new Circle(1.0f) };
        for(int i = 0; i < Shapes.length; ++i)
        {
            System.out.println("ShapeID " + i + " Has An Area Of " + Shapes[i].CalculateArea());
        }



    }
}