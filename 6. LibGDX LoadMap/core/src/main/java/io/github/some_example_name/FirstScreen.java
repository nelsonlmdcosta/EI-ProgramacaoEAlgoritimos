package io.github.some_example_name;

import EntityComponent.CameraComponent;
import EntityComponent.CameraManualMoverComponent;
import EntityComponent.Entity;
import EntityComponent.PlayerComponent;

import MapParser.MapParser;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen
{
    Texture backgroundTexture;
    Texture dropTexture;
    Sound dropSound;
    Music music;

    SpriteBatch spriteBatch;
    FitViewport viewport;

    ArrayList<Entity> SceneEntities = new ArrayList<>();

    CameraComponent EasyAccess;

    MapParser Map;

    Texture GridLayoutSprite;

    public FirstScreen()
    {
        backgroundTexture = new Texture("Images/background.png");
        dropTexture = new Texture("Images/drop.png");

        dropSound = Gdx.audio.newSound(Gdx.files.internal("Audio/drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("Audio/music.mp3"));

        Map = new MapParser();
        Map.Initialize();

        GridLayoutSprite = (new Texture("Images/GridLayout.png"));

        spriteBatch = new SpriteBatch();
        //viewport = new FitViewport(8, 5); // Internal Grid Really ;p

        viewport = new FitViewport(ProjectSettings.ViewportWidth, ProjectSettings.ViewportHeight); // Internal Grid Really ;p

        float Speed = 10.0f;
        float NormalizedTime = 0.0f;
        NormalizedTime += Gdx.graphics.getDeltaTime() * Speed; // DO this every frame

        //Vector2D CurrentPosition = PathGraph.CalculatePositionFromNodes(pathToExecute[0], pathToExecute[1], NormalizedTime);

        //bucketSprite.setX(CurrentPosition.X);
        //bucketSprite.setY(CurrentPosition.Y);

        // Add New Stuff Here
        Entity CameraEntity = new Entity();
        EasyAccess = CameraEntity.AddComponent ( new CameraComponent() );
        CameraEntity.AddComponent ( new CameraManualMoverComponent() );
//        CameraEntity.AddComponent ( new CameraFollowTargetComponent() );
        SceneEntities.add(CameraEntity);

        Entity PlayerEntity = new Entity();
        PlayerComponent PlayerComp = PlayerEntity.AddComponent(new PlayerComponent());
        PlayerComp.Camera = EasyAccess; // Silly Hacky Way Till We Build More of The Framework
        PlayerComp.MapGraph = Map.MapGraph;
        SceneEntities.add(PlayerEntity);

        // Once We're Done Adding Let's Run The Start Methods
        for(int i = 0; i < SceneEntities.size(); ++i)
        {
            SceneEntities.get(i).Start();
        }
    }



    @Override
    public void render(float delta)
    {
        for(int i = 0; i < SceneEntities.size(); ++i)
        {
            SceneEntities.get(i).Update(delta);
        }

        // Draw your screen here. "delta" is the time since last render in seconds.
        ScreenUtils.clear(Color.PINK);
        viewport.apply();

        // Prepare View Matrix To Draw And Sprite Batcher
        spriteBatch.setProjectionMatrix(EasyAccess.RenderCamera().combined);//viewport.getCamera().combined);
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

    @Override
    public void show() {
        // Prepare your screen here.
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.

        viewport.update(width, height, true); // true centers the camera
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }
}
