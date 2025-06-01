package EntityComponent.Collision;

import EntityComponent.AEntityComponent;
import EntityComponent.Collision.Events.*;
import Events.EventDispatcherV2;
import Utils.ConsoleColors;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

// Documentation Where This Is Made From
// https://libgdx.com/wiki/extensions/physics/box2d


public abstract class ACollider extends AEntityComponent
{
    protected Shape CollisionShape = null;

    protected BodyDef PhysicsBodyDefinition = null;
    protected FixtureDef PhysicsFixtureDefinition = null;

    Body PhysicsBody  = null;
    Fixture PhysicsFixture = null;

    public EventDispatcherV2<IOnCollisionEnter> OnCollisionEnterEvent = new EventDispatcherV2<IOnCollisionEnter>(IOnCollisionEnter.class);
    public EventDispatcherV2<IOnCollisionExit>  OnCollisionExitEvent  = new EventDispatcherV2<IOnCollisionExit> (IOnCollisionExit.class );

    public EventDispatcherV2<IOnTriggerEnter> OnTriggerEnterEvent = new EventDispatcherV2<IOnTriggerEnter>(IOnTriggerEnter.class);
    public EventDispatcherV2<IOnTriggerExit>  OnTriggerExitEvent  = new EventDispatcherV2<IOnTriggerExit> (IOnTriggerExit.class );

    public void Start()
    {
        if(PhysicsBodyDefinition == null)
        {
            System.out.println(ConsoleColors.RED_BOLD + "PhysicsBodyDefinition is null");
            return;
        }

        if(PhysicsFixtureDefinition == null)
        {
            System.out.println(ConsoleColors.RED_BOLD + "PhysicsFixtureDefinition is null");
            return;
        }

        // Setup The Body And Fixture That The Physics World Needs With The Generated Definitions From The Derived Class
        PhysicsBody = Entity.WorldScene.PhysicsWorld.createBody(PhysicsBodyDefinition);
        PhysicsFixture = PhysicsBody.createFixture(PhysicsFixtureDefinition);

        // Finally Set The User Data So We Can Always Reaccess Everything From here
        PhysicsBody.setUserData(this);
        PhysicsFixture.setUserData(this);
    }

    protected BodyDef GenerateBodyDefinition(BodyDef.BodyType BodyType, boolean FixedRotation)
    {
        BodyDef BodyDefinition = new BodyDef();

        // Body Type Aka Dynamic Kinematic Or Static
        BodyDefinition.type = BodyType;

        // Is The Rotation For The Object Fixed?
        BodyDefinition.fixedRotation = FixedRotation;

        return BodyDefinition;
    }

    // Lazy Implementation
    protected FixtureDef GenerateFixtureDefinition(Shape CollisionShape, boolean IsTrigger)
    {
        // Create And Setup Parameters
        FixtureDef FixtureDefinition = new FixtureDef();

        // Collision Shape Type
        FixtureDefinition.shape = CollisionShape;

        // Does this collision allow things to bass through it AKA Invisible Triggers
        FixtureDefinition.isSensor = IsTrigger;

        // Density Of Said Collision aka Mass/Volume
        FixtureDefinition.density = 0.5f;

        // Friction Of Said Collision Against Other Surfaces
        FixtureDefinition.friction = 0.4f;

        // Coefficient to determine the objects ability after being "Deformed" aka how much kinetic energy it retains
        FixtureDefinition.restitution = 0.6f;

        return FixtureDefinition;
    }

    protected FixtureDef GenerateFixtureDefinition(Shape CollisionShape, boolean IsTrigger, float density, float friction, float restitution)
    {
        // Create And Setup Parameters
        FixtureDef FixtureDefinition = new FixtureDef();

        // Collision Shape Type
        FixtureDefinition.shape = CollisionShape;

        // Does this collision allow things to bass through it AKA Invisible Triggers
        FixtureDefinition.isSensor = IsTrigger;

        // Density Of Said Collision aka Mass/Volume
        FixtureDefinition.density = density;

        // Friction Of Said Collision Against Other Surfaces
        FixtureDefinition.friction = friction;

        // Coefficient to determine the objects ability after being "Deformed" aka how much kinetic energy it retains
        FixtureDefinition.restitution = restitution;

        return FixtureDefinition;
    }

    public void Velocity(Vector2 Velocity)
    {
        PhysicsBody.setLinearVelocity(Velocity);
    }

    public void NotifyContactEnter(ACollider OtherObject)
    {
        if(PhysicsFixtureDefinition == null || OtherObject.PhysicsFixtureDefinition == null)
        {
            // Somethings Wrong! :l
            return;
        }

        if(PhysicsFixtureDefinition.isSensor || OtherObject.PhysicsFixtureDefinition.isSensor)
        {
            OnTriggerEnterEvent.Invoke(this, OtherObject);
            return;
        }

        OnCollisionEnterEvent.Invoke(this, OtherObject);
    }

    public void NotifyContactExit(ACollider OtherObject)
    {
        if(PhysicsFixtureDefinition == null)
        {
            // Somethings Wrong! :l
            return;
        }


        if(PhysicsFixtureDefinition.isSensor)
        {
            OnTriggerExitEvent.Invoke(this, OtherObject);
            return;
        }

        OnCollisionExitEvent.Invoke(this, OtherObject);
    }

    // TODO:  Do Cleanup Here
    public void PreDispose()
    {

    }
}
