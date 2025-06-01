package EntityComponent.Collision.Events;

import EntityComponent.Collision.ACollider;

public interface ICollisionExitCallback
{
    public void OnCollisionExit (ACollider MainObject, ACollider OtherObject);
}
