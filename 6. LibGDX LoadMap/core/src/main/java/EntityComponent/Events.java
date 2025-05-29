package EntityComponent;

import java.util.List;
import java.util.Map;


// Step 1: Define a functional interface with your required signature
@FunctionalInterface
interface OneParamEvent
{
    void Func(int b);
}

@FunctionalInterface
interface TwoParamEvent
{
    void Func(int a, int b);
}

public class FunctionPointerExample {
    public static void main(String[] args) {
        // Step 2: Create a List to store function references
        List<MathOperation> operations = new ArrayList<>();

        // Step 3: Add method references or lambda expressions
        operations.add((a, b) -> a + b);  // Addition
        operations.add((a, b) -> a - b);  // Subtraction
        operations.add((a, b) -> a * b);  // Multiplication
        operations.add((a, b) -> a / b);  // Division

        // You can also reference existing methods
        operations.add(FunctionPointerExample::power);

        // Step 4: Execute the stored functions
        int a = 10, b = 5;

        for (int i = 0; i < operations.size(); i++) {
            MathOperation operation = operations.get(i);
            int result = operation.calculate(a, b);

            String opName = i == 0 ? "Addition" :
                i == 1 ? "Subtraction" :
                    i == 2 ? "Multiplication" :
                        i == 3 ? "Division" : "Power";

            System.out.println(opName + ": " + result);
        }
    }

    // Method that matches our functional interface signature
    public static int power(int a, int b) {
        return (int)Math.pow(a, b);
    }
}
