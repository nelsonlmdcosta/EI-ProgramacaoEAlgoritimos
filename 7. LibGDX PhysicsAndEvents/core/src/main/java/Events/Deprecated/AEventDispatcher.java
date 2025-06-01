package Events.Deprecated;

import java.util.*;

// Older Simpler Version That Uses Abstraction To Call The Invoke Method, it's not flexible enough so look at
// EventDispatcherV2 that uses reflection to invoke dynamic events and parameters
@Deprecated
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
