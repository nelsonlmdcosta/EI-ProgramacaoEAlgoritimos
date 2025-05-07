package EntityComponent;

import WorldScene.WorldScene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Utils.HashString;


public class EntityFactory
{
    public static Entity CreatePlayerObject( WorldScene WorldSceneParent )
    {
        List<Integer> EntityTags = new ArrayList<Integer>(Arrays.asList(HashString.GenerateHashFromString("Player")));
        Entity PlayerObject = new Entity(WorldSceneParent, "Player", EntityTags);

        PlayerObject.AddComponent(new SpriteRendererComponent("Images/bucket.png"));
        PlayerObject.AddComponent(new PointAndClickCharacterMover());

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
}
