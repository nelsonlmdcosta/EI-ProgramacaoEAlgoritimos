package io.github.some_example_name;

public class Vector2D
{
    public float X;
    public float Y;

    Vector2D(){}

    Vector2D(float X, float Y)
    {
        this.X = X;
        this.Y = Y;
    }

    public static Vector2D Lerp (Vector2D StartNode, Vector2D EndNode, float NormalizeTime)
    {
        // Lerp Equation - a + (b - a) * t;
        // 1 + ( 3 - 1 ) * 0.5
        // 1 +  2  * 0.5
        // 1 + 1
        // 2
        // Normalized Number Is Just A Number Between 0.0f, and 1.0f aka a percentage
        return new Vector2D
            (
                StartNode.X + ( EndNode.X - StartNode.X ) * NormalizeTime,
                StartNode.Y + ( EndNode.Y - StartNode.Y ) * NormalizeTime
            );
    }
}
