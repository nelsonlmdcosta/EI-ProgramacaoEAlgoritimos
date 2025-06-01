package EntityComponent.Collision;

import EntityComponent.AEntityComponent;
import EntityComponent.Collision.Events.IOnCollisionEnter;
import EntityComponent.Collision.Events.IOnCollisionExit;
import EntityComponent.Collision.Events.IOnTriggerEnter;
import EntityComponent.Collision.Events.IOnTriggerExit;

public class CollisionDebugComponent extends AEntityComponent implements IOnCollisionEnter, IOnCollisionExit, IOnTriggerEnter, IOnTriggerExit
{
    public void Start()
    {
        ACollider ColliderComponent = Entity.GetFirstComponentOfType(ACollider.class);
        if(ColliderComponent == null)
            return;

        ColliderComponent.OnCollisionEnterEvent.AddObserver(CollisionDebugComponent.this);
        ColliderComponent.OnCollisionExitEvent.AddObserver(CollisionDebugComponent.this);
        ColliderComponent.OnTriggerEnterEvent.AddObserver(CollisionDebugComponent.this);
        ColliderComponent.OnTriggerExitEvent.AddObserver(CollisionDebugComponent.this);
    }

    @Override
    public void OnCollisionEnter(ACollider MainObject, ACollider OtherObject)
    {
        System.out.println("Collision Enter Detected");
    }

    @Override
    public void OnCollisionExit(ACollider MainObject, ACollider OtherObject)
    {
        System.out.println("Collision Exit Detected");
    }

    @Override
    public void OnTriggerEnter(ACollider MainObject, ACollider OtherObject)
    {
        System.out.println("Trigger Enter Detected");
    }

    @Override
    public void OnTriggerExit(ACollider MainObject, ACollider OtherObject)
    {
        System.out.println("Trigger Exit Detected");
    }
}
