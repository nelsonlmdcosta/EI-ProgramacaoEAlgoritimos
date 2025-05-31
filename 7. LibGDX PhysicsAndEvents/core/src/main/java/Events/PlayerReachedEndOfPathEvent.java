package Events;

public class PlayerReachedEndOfPathEvent extends AEventDispatcher<IPlayerReachedEndOfPathReceiver>
{
    @Override
    public void InvokeEvent()
    {
        for(int i = 0; i < Observers.size(); ++i)
            Observers.get(i).OnPlayerReachedEndOfPath();
    }
}
