package io.github.some_example_name;

import WorldScene.WorldScene;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen
{
    private WorldScene worldScene;

    FitViewport viewport;

    public FirstScreen()
    {
        viewport = new FitViewport(ProjectSettings.ViewportWidth, ProjectSettings.ViewportHeight); // Internal Grid Really ;p

        worldScene = new WorldScene();

        worldScene.CreateWorld();
    }

    @Override
    public void render(float delta)
    {
        worldScene.UpdateWorld(delta);

        ScreenUtils.clear(Color.PINK);
        viewport.apply();

        worldScene.RenderWorld();
    }

    @Override
    public void show() { }

    // TODO: Viewport Now Lives In World?
    @Override
    public void resize(int width, int height) { viewport.update(width, height, true); }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { worldScene.DestroyWorld(); }
}
