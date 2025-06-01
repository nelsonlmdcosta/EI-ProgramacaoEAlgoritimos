package EntityComponent.Collision.Events;

import EntityComponent.Collision.ACollider;

public interface ICollisionEnterCallback
{
    public void OnCollisionEnter(ACollider MainObject, ACollider OtherObject);
}
