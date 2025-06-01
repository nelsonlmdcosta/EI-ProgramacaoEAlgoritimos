package EntityComponent.Collision.Events;

import EntityComponent.Collision.ACollider;

public interface IOnTriggerExit
{
    public void OnTriggerExit (ACollider MainObject, ACollider OtherObject);
}
