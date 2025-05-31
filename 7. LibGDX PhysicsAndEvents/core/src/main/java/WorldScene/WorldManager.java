package WorldScene;

// Singleton Instance That Can Retrieve Worlds Or Stack Them On Top Of Each Other At Any Time
public class WorldManager
{
    private static final WorldManager instance = new WorldManager();
    public WorldManager Instance(){ return instance; }

    public void AddWorldToStack()
    {}

    public void RemoveWorldFromStack()
    {}

    // Special Version Of The Above That Clears All Current Worlds
    public void CleanLoadWorld()
    {}
}
