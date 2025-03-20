public class Main
{
    // Main function for starting any program in Java
    // static keyword just makes this function accessible from anywhere
    public static void main(String[] args)
    {
        // Variables
        // https://www.w3schools.com/java/java_variables.asp
        int Integer = 1;            // Only Integer Values Are Represented By This Type, there are no decimal places.
        float Float = 1.0f;         // Integer and decimal places are represented in this type, there is a limit to the number of decimal places and correctness to said decimal places.
        double Double = 1.0;        // This is a float with "double" the floating point precision, more decimal places and less error.
        boolean Boolean = true;     // Either true or false to explain a state.
        String String = "Hello!";   // A "String of characters" aka a number of characters to describe a message.

        // Enuemrations are a way to have a visible "Text" whilst being an efficient comparison under the hood
        // https://www.w3schools.com/java/java_enums.asp
        // For adding custom values, see https://www.baeldung.com/java-enum-values
        enum EExampleEnumeration { StateOne, StateTwo, StateThree, StateFour };
        EExampleEnumeration EnumVariableType = EExampleEnumeration.StateOne;

        // Control Flow (2 Main Types)
        // If statements that resolve a condition. This example should be the most common version to keep "code clean"
        // https://www.w3schools.com/java/java_conditions.asp
        if(Boolean)
        {
            // Do some work
        }

        // You can attach an else at the end to do the opposite
        // Generally speaking we avoid else statements as they always come after the if, therefore we can keep the code clean and use functions to help (see below)
        if(1 > 2)
        {
            // Do some work
        }
        else
        {
            // Do some other work
        }

        // Alternatively If you need to check within certain boudnds, you can attach another if after the else Crazy right?
        // If you do this you probably should be using a switch statement instead (see below)
        if(1 > 2)
        {
            // Do some work
        }
        else if( 2 < 3)
        {
            // Do some work
        }
        else
        {
            // Do some work
        }

        // You can also attach multiple condiitons near each other using and (&&) and or (||)
        // Note that if you put things between brackets, they take precedence in execution order
        // Note that if one condition fails, then the if statement will "short circuit" which is a fancy name for exit out early
        if( 3 == 4 && (4 > 3 || 4 <= 3))
        {
            // Do some work
        }

        // There is a special if statement that is called a ternary operator
        // Basically we have a condition and a marker, Anything before the ? after it is the results
        // Results are ALWAYS split in two section, where the left of the : is when the condition is true and the right side is false;
        boolean SomeValue = 1 == 1 ? true : false;

        // Finally we get the switch statement, which is great and super fast at dealing with quite a few comparisons
        // You stick the variable in the Switch()
        // Then you create the cases for each type of state, once you're done you use a break keyword, to exit out.
        // If you do not, then you can "fall through" to the next case (can be intentioanl)
        // Finally the default is for any cases you did not catch. Sometimes in development you need to update multiple parts of the codebase
        // So this is a great place for you to add error messages to let people know they need to update this section of code too
        // https://www.w3schools.com/java/java_switch.asp
        switch(EnumVariableType)
        {
            case StateOne:
                break;
            case StateTwo:
                // Example of fall through AKA StateTwo and StateThree execute the same logic
            case StateThree:
                break;
            default:
                break;
        }

        // Loops (2 flavours)
        // For Loops (Foreach loops are a variant you should normally not use in most languages due to GC)
        // These are usually used when you know exactly how many loops you need to do (in "" :p)
        // You can also stick them inside each other! (to search multidimensional data structures)
        // https://www.w3schools.com/java/java_for_loop.asp
        String[] cars = {"Volvo", "BMW", "Ford", "Mazda"};
        for ( int i = 0; i < cars.length; ++i )
        {
            // Special keyword used when we want to ignore this iteration
            if(i == 1)
                continue;

            System.out.println(i);
        }

        // Foreach Example
        // https://www.w3schools.com/java/java_foreach_loop.asp
        for (String i : cars)
        {
            System.out.println(i);
        }

        // While Loops
        // Two types of while loops, one that does the condition first, and the second that does the condition at the end
        // Really good to search through an unknown amount of things
        // https://www.w3schools.com/java/java_while_loop.asp
        int SomeVariable = 1;
        while(SomeVariable > 0)
        {
            // Do Work
            --SomeVariable;
        }

        // https://www.w3schools.com/java/java_while_loop_do.asp
        SomeVariable = 1;
        do
        {
            --SomeVariable;
        } while( SomeVariable > 0);

        // Scopes
        // You've probably noticed the fancy parenthesis {} these are called scopes, if we create variable sin them, they
        // only exist in that area, when leaving that area, any variables created inside are instantly deleted.
        // https://www.w3schools.com/java/java_scope.asp
        {
            boolean IExist = true;
        }

        // We cannot acces IExist otuside of the scope, as the variable does not exist there, therefore we cannot access it
        // IExist = false;

        // Functions
        // Functions are very useful to keep similar code or code in general in small segregated chunks, so that it's easy to ready or access.
        // You can then call the function by it's name and passing in the parameters
        // https://www.w3schools.com/java/java_methods.asp
        // https://www.w3schools.com/java/java_methods_param.asp
        PrintString("Hello World! :D");
        PrintString("Hello World", 2);
    }

    // If it's void it returns nothing, otherwise it can return any type!
    public static void PrintString(String TextToPrint)
    {

    }

    // This is called polymorphism Same name, different parameters
    // or Method Overloading in Java
    // https://www.w3schools.com/java/java_methods_overloading.asp
    // Also Java doesn't have defualt parameters ... sad :l
    public static boolean PrintString(String TextToPrint, int someNumber)
    {
        --someNumber;

        System.out.println(someNumber);

        if(someNumber > 0) {
            PrintString(TextToPrint, someNumber);
        }

        // Returns a specific value! How exciting!
        return true;
    }

    // Note: Functions can also call themselves, this is called recursion! Useful to search in specific ways
    // https://www.w3schools.com/java/java_recursion.asp
}