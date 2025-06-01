package EntityComponent.Collision.Events;

import Events.AEventDispatcher;

public class OnCollisionEnterEvent extends AEventDispatcher<ICollisionEnterCallback>
{
    @Override
    public void InvokeEvent()
    {
        for(int i = 0; i < Observers.size(); i++)
        {
            //Observers.get(i).OnCollisionEnter(ACollider MainObject, ACollider OtherObject);
        }
    }
}
