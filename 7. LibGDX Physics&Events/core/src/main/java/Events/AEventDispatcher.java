package Events;

import java.util.*;

public abstract class AEventDispatcher<T>
{
    protected final List<T> Observers = new ArrayList<T>();

    public void AddObserver(T Observer)
    {
        Observers.add(Observer);
    }

    public void RemoveObserver(T Observer)
    {
        Observers.remove(Observer);
    }

    public abstract void InvokeEvent();
}
