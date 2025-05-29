package WorldScene;

import MapParser.MapParser;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.some_example_name.ProjectConstants;
import io.github.some_example_name.ProjectSettings;

import java.util.ArrayList;
import java.util.Dictionary;

import EntityComponent.Entity;
import EntityComponent.CameraComponent;
import EntityComponent.EntityFactory;

// Could Take A File Loading Approach But Boy I Dont Feel Like Dealing With Serialization Right Now On That Scale
public class WorldScene
{
    Texture backgroundTexture;

    // TODO: Move SOunds
    Sound dropSound;
    Music music;

    SpriteBatch spriteBatch;

    ArrayList<Entity> SceneEntities = new ArrayList<>();
    ArrayList<CameraComponent> Cameras = new ArrayList<>();

    public MapParser Map;

    public World PhysicsWorld = new World(new Vector2(0, -10), true);
    private float accumulator = 0;
    //Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

    Texture GridLayoutSprite;

    public void CreateWorld()
    {
        Map = new MapParser();
        Map.Initialize();

        // Debug The Grid
        GridLayoutSprite = (new Texture("Images/GridLayout.png"));

        spriteBatch = new SpriteBatch();

        SceneEntities.add(EntityFactory.CreateCameraObject(this));
        SceneEntities.add(EntityFactory.CreatePlayerObject(this));

        //SceneEntities.add(EntityFactory.CreateHealthWidget(this));

        Cameras.add( SceneEntities.get(0).GetFirstComponentOfType(CameraComponent.class) );

        // Once We're Done Adding Let's Run The Start Methods
        for(int i = 0; i < SceneEntities.size(); ++i)
        {
            SceneEntities.get(i).Start();
        }
    }

    public void UpdateWorld(float DeltaTime)
    {
        for(int i = 0; i < SceneEntities.size(); ++i)
        {
            SceneEntities.get(i).Update(DeltaTime);
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
    }

    public void RenderWorld()
    {
        // Draw your screen here. "delta" is the time since last render in seconds.

        // Prepare View Matrix To Draw And Sprite Batcher
        spriteBatch.setProjectionMatrix(Cameras.get(0).RenderCamera().combined);//viewport.getCamera().combined);
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
                spriteBatch.draw(GridLayoutSprite, X, Y, 1, 1);
                //GridLayoutSprite.draw(spriteBatch);
            }
        }

        spriteBatch.end();
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
}
