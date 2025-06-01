package EntityComponent.Collision.Events;

import EntityComponent.Collision.ACollider;

public interface IOnCollisionExit
{
    public void OnCollisionExit (ACollider MainObject, ACollider OtherObject);
}
