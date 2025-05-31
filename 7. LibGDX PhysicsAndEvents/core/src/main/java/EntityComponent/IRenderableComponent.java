package EntityComponent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IRenderableComponent
{
    public boolean CanRender();
    public void Render(SpriteBatch Batch);
}
