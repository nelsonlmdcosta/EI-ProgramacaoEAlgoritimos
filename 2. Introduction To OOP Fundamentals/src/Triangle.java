public class Triangle implements ICalculableArea
{
    private float Base;
    private float Height;

    Triangle(float Base, float Height)
    {
        this.Base = Base;
        this.Height = Height;
    }

    public float CalculateArea()
    {
        return (Base * Height) / 2;
    }
}
