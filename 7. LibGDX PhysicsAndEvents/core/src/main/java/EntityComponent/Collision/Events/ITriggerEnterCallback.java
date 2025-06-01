package EntityComponent.Collision.Events;

import EntityComponent.Collision.ACollider;

public interface ITriggerEnterCallback
{
    public void OnTriggerEnter(ACollider MainObject, ACollider OtherObject);
}
