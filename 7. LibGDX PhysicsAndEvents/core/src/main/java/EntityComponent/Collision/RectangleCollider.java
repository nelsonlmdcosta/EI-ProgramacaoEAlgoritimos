package EntityComponent.Collision;

import EntityComponent.Transform.Transform;
import EntityComponent.Transform.Events.ITransformDirtyFlagCleared;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class RectangleCollider extends ACollider implements ITransformDirtyFlagCleared
{
    Transform TransformComponent = null;

    public RectangleCollider( BodyDef.BodyType BodyType, boolean IsTrigger, boolean FixedRotation, float Width, float Height, Vector2 Pivot )
    {
        // TODO: Dispose Of Circle At Some Point
        // We Initialize At 0,0 As It's Just Simpler, Transform Later Will Deal With Warning Where This Object Will Be
        PolygonShape Rectangle = new PolygonShape();
        Rectangle.setAsBox(Width, Height, Pivot, 0.0f);

        CollisionShape = Rectangle;

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
            TransformComponent.OnDirtyFlagCleared.AddObserver(this);

            PhysicsBody.setTransform(TransformComponent.GetPosition(), TransformComponent.getRotation());
        }
    }

    @Override
    public void OnTransformDirtyFlagCleared()
    {
        PhysicsBody.setTransform(TransformComponent.GetPosition(), TransformComponent.getRotation());
    }
}
