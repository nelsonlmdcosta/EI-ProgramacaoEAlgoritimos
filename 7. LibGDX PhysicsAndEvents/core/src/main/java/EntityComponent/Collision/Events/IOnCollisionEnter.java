package EntityComponent.Collision.Events;

import EntityComponent.Collision.ACollider;

public interface IOnCollisionEnter
{
    public void OnCollisionEnter(ACollider MainObject, ACollider OtherObject);
}
