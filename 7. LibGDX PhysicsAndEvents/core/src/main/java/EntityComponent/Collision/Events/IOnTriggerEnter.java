package EntityComponent.Collision.Events;

import EntityComponent.Collision.ACollider;

public interface IOnTriggerEnter
{
    public void OnTriggerEnter(ACollider MainObject, ACollider OtherObject);
}
