package EntityComponent;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class SpriteRendererComponent extends AEntityComponent implements IRenderableComponent
{
    public Sprite SpriteToRender;

    private Color ColorToTint = Color.WHITE;

    public SpriteRendererComponent(String SpriteToRenderPath)
    {
        SpriteToRender = new Sprite(new Texture(SpriteToRenderPath));
    }

    @Override
    public boolean CanRender()
    {
        return IsActive;
    }

    @Override
    public void Render(SpriteBatch Batch)
    {
        Batch.setColor(ColorToTint);
        Batch.draw(SpriteToRender, SpriteToRender.getX(), SpriteToRender.getY(), 1, 1);
        Batch.setColor(Color.WHITE);
    }

    public Vector2 GetSpritePosition()
    {
        return new Vector2(SpriteToRender.getX(), SpriteToRender.getY());
    }

    public void AddPositionDelta(Vector2 delta)
    {
        SpriteToRender.translate(delta.x, delta.y);
    }

    public void SetPosition(Vector2 Position)
    {
        SpriteToRender.setPosition(Position.x, Position.y);
    }

    public void SetTintColor (Color ColorToTint)
    {
        this.ColorToTint = ColorToTint;
    }
}
