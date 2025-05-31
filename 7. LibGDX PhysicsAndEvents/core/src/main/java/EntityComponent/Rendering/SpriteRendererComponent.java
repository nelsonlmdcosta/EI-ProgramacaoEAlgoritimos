package EntityComponent.Rendering;

import EntityComponent.AEntityComponent;
import EntityComponent.IRenderableComponent;
import EntityComponent.Transform.Transform;
import Events.Transform.ITransformDirtyFlagCleared;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class SpriteRendererComponent extends AEntityComponent implements IRenderableComponent, ITransformDirtyFlagCleared
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
            TransformComponent.OnDirtyFlagCleared.AddObserver(this);
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

        //SpriteToRender.getTexture().bind();

        Batch.setColor(ColorToTint);
        Batch.draw(SpriteToRender, SpriteToRender.getX(), SpriteToRender.getY(), 1, 1);
        Batch.setColor(Color.WHITE);


        //Batch.setColor(ColorToTint);

        //SpriteToRender = new Sprite(new Texture("assets/Images/bucket.png"));
        //Batch.draw(SpriteToRender.getTexture(), SpriteToRender.getX(), SpriteToRender.getY(), 2, 2); // TODO: These Should Be More Configurable

        //Batch.draw(SpriteToRender.getTexture(), SpriteToRender.getX(), SpriteToRender.getY(), 1, 1, 0, 0, 32, 32);

        //Texture testTexture = new Texture(Gdx.files.internal("Images/bucket.png"));

        // Test drawing the direct texture
        //Batch.draw(testTexture,0, 0);

        //Batch.setColor(Color.WHITE);
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
