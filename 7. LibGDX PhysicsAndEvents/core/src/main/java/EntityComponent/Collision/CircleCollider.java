package EntityComponent.Collision;

import EntityComponent.Transform.Transform;
import EntityComponent.Transform.Events.IOnTransformDirtyFlagCleared;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class CircleCollider extends ACollider implements IOnTransformDirtyFlagCleared
{
    Transform TransformComponent = null;

    public CircleCollider(BodyDef.BodyType BodyType, boolean IsTrigger, boolean FixedRotation, float Radius )
    {
        // TODO: Dispose Of Circle At Some Point
        // We Initialize At 0,0 As It's Just Simpler, Transform Later Will Deal With Warning Where This Object Will Be
        CircleShape Circle = new CircleShape();
        Circle.setRadius(Radius);
        Circle.setPosition(new Vector2(0.5f, 0.5f)); // Pivots To The Center Of Our Grid

        CollisionShape = Circle;

        PhysicsBodyDefinition = GenerateBodyDefinition(BodyType, FixedRotation);
        PhysicsFixtureDefinition = GenerateFixtureDefinition(CollisionShape, IsTrigger);
    }

    public void Start()
    {
        // Required Call To Handle Everything Related To Registering To The Physics Engine
        super.Start();

        // Sync to the transform component, But Honetly tehre shouldn't be a need for it unless we need to sync more objects?
        TransformComponent = Entity.GetFirstComponentOfType(Transform.class);
        if(TransformComponent != null)
        {
            TransformComponent.OnTransformDirtyFlagClearedEvent.AddObserver(this);

            PhysicsBody.setTransform(TransformComponent.GetPosition(), TransformComponent.GetRotation());
        }
    }

    @Override
    public void OnTransformDirtyFlagCleared()
    {
        PhysicsBody.setTransform(TransformComponent.GetPosition(), TransformComponent.GetRotation());
    }
}
