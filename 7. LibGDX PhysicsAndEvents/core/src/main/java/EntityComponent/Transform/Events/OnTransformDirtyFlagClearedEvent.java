package EntityComponent.Transform.Events;

import Events.AEventDispatcher;

public class OnTransformDirtyFlagClearedEvent extends AEventDispatcher<ITransformDirtyFlagCleared>
{
    @Override
    public void InvokeEvent()
    {
        for(int i = 0; i < Observers.size(); ++i)
        {
            Observers.get(i).OnTransformDirtyFlagCleared();
        }
    }
}
