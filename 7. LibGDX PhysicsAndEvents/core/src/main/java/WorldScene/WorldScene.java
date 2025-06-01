package WorldScene;

import EntityComponent.Collision.ACollider;
import EntityComponent.Transform.Transform;
import MapParser.MapParser;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import Main.ProjectConstants;
import Main.ProjectSettings;

import java.util.ArrayList;
import java.util.List;

import EntityComponent.Entity;
import EntityComponent.Camera.CameraComponent;
import EntityComponent.EntityFactory;

// Could Take A File Loading Approach But Boy I Dont Feel Like Dealing With Serialization Right Now On That Scale
public class WorldScene implements ContactListener
{
    Texture backgroundTexture;

    // TODO: Move SOunds
    Sound dropSound;
    Music music;

    SpriteBatch spriteBatch;

    ArrayList<Entity> SceneEntities = new ArrayList<>();
    ArrayList<CameraComponent> Cameras = new ArrayList<>();

    public MapParser Map;

    // TOOD: This Game Doesn't Require Any Sort of Gravity
    public World PhysicsWorld = new World(new Vector2(0, 0/*-10*/), true);
    private float accumulator = 0;
    Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

    Texture GridLayoutSprite;

    // Avoid Constantly Creating New Memory
    Array<Body> PhysicsBodies;


    List<Transform> DirtyTransforms = new ArrayList<Transform>();
    public void NotifyDirtyTransform(Transform DirtyTransform)
    {
        DirtyTransforms.add(DirtyTransform);
    }

    public void CreateWorld()
    {
        Map = new MapParser();
        Map.Initialize();

        // Debug The Grid
        GridLayoutSprite = (new Texture("Images/GridLayout.png"));

        spriteBatch = new SpriteBatch();

        SceneEntities.add(EntityFactory.CreateCameraObject(this));
        SceneEntities.add(EntityFactory.CreatePlayerObject(this));
        SceneEntities.get(SceneEntities.size()-1).GetFirstComponentOfType(Transform.class).SetPosition(Map.PlayerStart.GetPosition());

        // TODO: Set Rock Via Data
        SceneEntities.add(EntityFactory.CreateRockObject(this));
        SceneEntities.get(SceneEntities.size()-1).GetFirstComponentOfType(Transform.class).SetPosition(5,3);

        SceneEntities.add(EntityFactory.CreateBasicTrigger(this));
        SceneEntities.get(SceneEntities.size()-1).GetFirstComponentOfType(Transform.class).SetPosition(3,3);


        //SceneEntities.add(EntityFactory.CreateHealthWidget(this));

        Cameras.add( SceneEntities.get(0).GetFirstComponentOfType(CameraComponent.class) );

        // Once We're Done Adding Let's Run The Start Methods
        for(int i = 0; i < SceneEntities.size(); ++i)
        {
            SceneEntities.get(i).Start();
        }

        PhysicsWorld.setContactListener(this);
        PhysicsBodies = new Array<>(PhysicsWorld.getBodyCount());
    }

    public void UpdateWorld(float DeltaTime)
    {
        for(int i = 0; i < SceneEntities.size(); ++i)
        {
            SceneEntities.get(i).Update(DeltaTime);
        }

        // Prepare Dirty Transforms Before The Physics Step, So Physics Is Aware Of Our Changes
        for(int i = 0; i < DirtyTransforms.size(); ++i)
        {
            Transform transformComponent = DirtyTransforms.get(i);
            if(transformComponent != null && transformComponent.GetIsTransformDirty()) // This check is to avoid double registers from cleaning up
            {
                transformComponent.ClearDirtyTransform();
            }
        }

        // https://libgdx.com/wiki/extensions/physics/box2d
        // Allow the physics world to fixup any of our movements
        float frameTime = Math.min(DeltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= ProjectConstants.Physics_Timestep)
        {
            PhysicsWorld.step(ProjectConstants.Physics_Timestep, ProjectConstants.Physics_VelocityIterations, ProjectConstants.Physics_PositionIterations);
            accumulator -= ProjectConstants.Physics_Timestep;
        }

        // Go Through Dynamic Objects And In Turn Update The Transforms
        PhysicsWorld.getBodies(PhysicsBodies);
        for(int i = 0; i < PhysicsBodies.size; ++i)
        {
            // Only Dynamic Bodies Are Affected By Motion Technically So Ignore All Others
            if(PhysicsBodies.get(i).getType() != BodyDef.BodyType.DynamicBody)
            {
                continue;
            }

            ACollider Collider = (ACollider)PhysicsBodies.get(i).getUserData();
            Entity AssociatedEntity = Collider.Entity();
            if(AssociatedEntity != null)
            {
                Transform EntityTransform = AssociatedEntity.GetFirstComponentOfType(Transform.class);
                if(EntityTransform != null)
                {
                    EntityTransform.SetPosition(PhysicsBodies.get(i).getPosition());
                }
            }
        }

        // Finally All Those Dirty Transforms, Let's Clear Them Up So Everything Is In The Correct Spot By End Of Update
        for(int i = 0; i < SceneEntities.size(); ++i)
        {
            Transform transformComponent = SceneEntities.get(i).GetFirstComponentOfType(Transform.class);
            if(transformComponent != null && transformComponent.GetIsTransformDirty())
            {
                transformComponent.ClearDirtyTransform();
            }
        }
    }

    public void RenderWorld()
    {
        // Draw your screen here. "delta" is the time since last render in seconds.

        // Prepare View Matrix To Draw And Sprite Batcher
        spriteBatch.setProjectionMatrix(Cameras.get(0).GetCombinedMatrix());//viewport.getCamera().combined);
        spriteBatch.begin();

        // Should Be A Sprite Batch Per Layer Or Order Them

        // The Order You Draw This In Is Important!
        Map.RenderMap(spriteBatch);
        //spriteBatch.draw(backgroundTexture, 0, 0, ProjectSettings.WorldWidth, ProjectSettings.WorldHeight); // draw the background

        for(int i = 0; i < SceneEntities.size(); ++i)
        {
            SceneEntities.get(i).Render(spriteBatch);
        }

        for(int Y = 0; Y < ProjectSettings.WorldHeight; ++Y)
        {
            for(int X = 0; X < ProjectSettings.WorldWidth; ++X)
            {
                //spriteBatch.draw(GridLayoutSprite, X, Y, 1, 1);
            }
        }

        spriteBatch.end();

        debugRenderer.render(PhysicsWorld, Cameras.get(0).GetCombinedMatrix());
    }

    public void DestroyWorld()
    {}

    public Entity FindFirstEntityWithTag(String Tag)
    {
        int HashedTag = Tag.hashCode();
        for(int i = 0; i < SceneEntities.size(); ++i)
        {
            Entity CurrentEntity = SceneEntities.get(i);
            for(int TagIndex = 0; TagIndex < CurrentEntity.EntityTags.size(); ++TagIndex)
            {
                if(CurrentEntity.EntityTags.get(TagIndex) == HashedTag)
                {
                    return CurrentEntity;
                }
            }
        }

        return null;
    }

    @Override
    public void beginContact(Contact contact)
    {
        // Let These Objects Know About Each Other I Suppose
        ACollider ColliderA = (ACollider) contact.getFixtureA().getUserData();
        ACollider ColliderB = (ACollider) contact.getFixtureB().getUserData();

        if (ColliderA != null && ColliderB != null)
        {
            ColliderA.NotifyContactEnter(ColliderB);
            ColliderB.NotifyContactEnter(ColliderA);
        }
    }

    @Override
    public void endContact(Contact contact)
    {
        ACollider ColliderA = (ACollider)contact.getFixtureA().getUserData();
        ACollider ColliderB = (ACollider)contact.getFixtureB().getUserData();

        if (ColliderA != null && ColliderB != null)
        {
            ColliderA.NotifyContactExit(ColliderB);
            ColliderB.NotifyContactExit(ColliderA);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold)
    {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse)
    {

    }
}
