package EntityComponent.Rendering;

import EntityComponent.AEntityComponent;
import EntityComponent.IRenderableComponent;
import EntityComponent.Transform.Transform;
import EntityComponent.Transform.Events.IOnTransformDirtyFlagCleared;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class SpriteRendererComponent extends AEntityComponent implements IRenderableComponent, IOnTransformDirtyFlagCleared
{
    public Sprite SpriteToRender;



    private Color ColorToTint = Color.WHITE;

    private Transform TransformComponent = null;

    public SpriteRendererComponent(String SpriteToRenderPath)
    {
        SpriteToRender = new Sprite(new Texture(SpriteToRenderPath));
    }

    @Override
    public void Start()
    {
        TransformComponent = Entity.GetFirstComponentOfType(Transform.class);
        if(TransformComponent != null)
        {
            TransformComponent.OnTransformDirtyFlagClearedEvent.AddObserver(this);
        }
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

    private void SetPosition(Vector2 Position)
    {
        SpriteToRender.setPosition(Position.x, Position.y);
    }

    public void SetTintColor (Color ColorToTint)
    {
        this.ColorToTint = ColorToTint;
    }

    @Override
    public void OnTransformDirtyFlagCleared()
    {
        SetPosition(TransformComponent.GetPosition());
    }
}
