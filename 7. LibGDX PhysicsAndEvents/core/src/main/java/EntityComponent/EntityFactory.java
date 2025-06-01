package EntityComponent;

import EntityComponent.Animation.CharacterSpriteAnimator;
import EntityComponent.Camera.CameraComponent;
import EntityComponent.Camera.CameraManualMoverComponent;
import EntityComponent.Collision.CircleCollider;
import EntityComponent.Collision.CollisionDebugComponent;
import EntityComponent.Collision.RectangleCollider;
import EntityComponent.Health.HealthWidget;
import EntityComponent.Player.EightDirectionalMovement;
import EntityComponent.Rendering.SpriteRendererComponent;
import WorldScene.WorldScene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Utils.HashString;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class EntityFactory
{
    public static Entity CreatePlayerObject( WorldScene WorldSceneParent )
    {
        List<Integer> EntityTags = new ArrayList<Integer>(Arrays.asList(HashString.GenerateHashFromString("Player")));
        Entity PlayerObject = new Entity(WorldSceneParent, "Player", EntityTags);

        // Visual Components
        PlayerObject.AddComponent( new SpriteRendererComponent("Images/bucket.png") );
        PlayerObject.AddComponent( new CharacterSpriteAnimator() );
        //PlayerObject.AddComponent( new RandomSpriteTint() );

        // Physics Components
        PlayerObject.AddComponent( new CircleCollider(BodyDef.BodyType.DynamicBody, false, true, 0.5f) );
        PlayerObject.AddComponent( new CollisionDebugComponent() );

        // Movement Components
        //PlayerObject.AddComponent( new PointAndClickCharacterMover() );
        //PlayerObject.AddComponent( new FourDirectionalMovement() );
        PlayerObject.AddComponent( new EightDirectionalMovement() );


        return PlayerObject;
    }

    public static Entity CreateCameraObject( WorldScene WorldSceneParent )
    {
        List<Integer> EntityTags = new ArrayList<Integer>(Arrays.asList(HashString.GenerateHashFromString("Camera")));
        Entity CameraEntity = new Entity(WorldSceneParent, "Camera", EntityTags);

        CameraEntity.AddComponent ( new CameraComponent() );
        CameraEntity.AddComponent ( new CameraManualMoverComponent() );

        return CameraEntity;
    }

    public static Entity CreateHealthWidget( WorldScene WorldSceneParent )
    {
        List<Integer> EntityTags = new ArrayList<Integer>(Arrays.asList(HashString.GenerateHashFromString("UI"), HashString.GenerateHashFromString("HealthWidget")));
        Entity UIEntity = new Entity(WorldSceneParent, "UI", EntityTags);

        UIEntity.AddComponent ( new HealthWidget() );

        return UIEntity;
    }

    public static Entity CreateRockObject( WorldScene WorldSceneParent )
    {
        List<Integer> EntityTags = new ArrayList<Integer>(Arrays.asList(HashString.GenerateHashFromString("Rock")));
        Entity RockObject = new Entity(WorldSceneParent, "Rock Obstacle", EntityTags);

        RockObject.AddComponent( new SpriteRendererComponent("Images/Rock/Rock.png") );
        RockObject.AddComponent( new RectangleCollider(BodyDef.BodyType.StaticBody, false, true, 0.5f, 0.5f, new Vector2(0.5f, 0.5f)) );

        return RockObject;
    }

    public static Entity CreateBasicTrigger( WorldScene WorldSceneParent )
    {
        List<Integer> EntityTags = new ArrayList<Integer>(Arrays.asList(HashString.GenerateHashFromString("Trigger")));
        Entity TriggerEntity = new Entity(WorldSceneParent, "Trigger Obstacle", EntityTags);

        TriggerEntity.AddComponent( new RectangleCollider(BodyDef.BodyType.StaticBody, true, true, 0.5f, 0.5f, new Vector2(0.5f, 0.5f)) );

        return TriggerEntity;
    }
}
