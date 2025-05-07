package WorldScene;

import MapParser.MapParser;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
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
