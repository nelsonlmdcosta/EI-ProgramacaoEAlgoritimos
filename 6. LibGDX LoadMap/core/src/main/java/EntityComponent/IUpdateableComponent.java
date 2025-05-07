package EntityComponent;

public interface IUpdateableComponent
{
    public void Update(float DeltaTime);
    public boolean CanUpdate();
}
