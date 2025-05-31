package EntityComponent;

import WorldScene.WorldScene;

public abstract class AEntityComponent
{
    protected Entity Entity = null;
    protected WorldScene WorldScene = null;
    protected boolean IsActive = true;

    public void Start(){}

    public Entity Entity()
    {
        return Entity;
    }
    public void Entity(Entity Entity)
    {
        this.Entity = Entity;
    }

    public boolean IsActive()
    {
        return IsActive;
    }
    public void IsActive(boolean active)
    {
        IsActive = active;
    }

    public void PreDispose() { }

    public void World(WorldScene WorldScene)
    {
        this.WorldScene = WorldScene;
    }
}
