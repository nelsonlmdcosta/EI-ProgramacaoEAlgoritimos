package Events.Deprecated;

import EntityComponent.Player.IOnPlayerReachedEndOfPath;

@Deprecated
public class PlayerReachedEndOfPathEvent extends AEventDispatcher<IOnPlayerReachedEndOfPath>
{
    @Override
    public void InvokeEvent()
    {
        for(int i = 0; i < Observers.size(); ++i)
            Observers.get(i).OnPlayerReachedEndOfPath();
    }
}
