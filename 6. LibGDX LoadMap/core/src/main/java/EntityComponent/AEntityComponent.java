package EntityComponent;

public abstract class AEntityComponent
{
    protected Entity AssignedEntity = null;
    protected boolean IsActive = true;

    public void Start(){}

    public Entity AssignedEntity()
    {
        return AssignedEntity;
    }
    public void AssignedEntity(Entity Entity)
    {
        AssignedEntity = Entity;
    }

    public boolean IsActive()
    {
        return IsActive;
    }
    public void IsActive(boolean active)
    {
        IsActive = active;
    }
}
