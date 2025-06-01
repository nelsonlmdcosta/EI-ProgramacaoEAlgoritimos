package EntityComponent.Collision.Events;

import EntityComponent.Collision.ACollider;

public interface ITriggerExitCallback
{
    public void OnTriggerExit (ACollider MainObject, ACollider OtherObject);
}
