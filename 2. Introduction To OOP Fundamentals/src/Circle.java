import java.awt.*;

public class Circle implements ICalculableArea
{
    private float Radius;

    Circle(float Radius)
    {
        this.Radius = Radius;
    }

    @Override
    public float CalculateArea()
    {
        return (float)(Math.PI * Math.pow(Radius, 2));
    }
}
