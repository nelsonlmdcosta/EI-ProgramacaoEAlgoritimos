package EntityComponent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PlayerComponent extends AEntityComponent implements IUpdateableComponent, IRenderableComponent
{
    Sprite bucketSprite;
    Texture bucketTexture;

    float Speed = 10.0f;

    boolean OverrideWithClickPosition = false;

    // TODO: Clean up
    public CameraComponent Camera;

    public Vector2 LastClickPosition = new Vector2();

    @Override
    public void Start()
    {
        bucketTexture = new Texture("Images/bucket.png");
        bucketSprite = new Sprite(bucketTexture); // Initialize the sprite based on the texture
        bucketSprite.setSize(1,1); // Define the size of the sprite
    }

    @Override
    public void Update(float DeltaTime)
    {
        Vector2 MovementDirection = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.A))
            MovementDirection.x = -1;

        if (Gdx.input.isKeyPressed(Input.Keys.D))
            MovementDirection.x =  1;

        if (Gdx.input.isKeyPressed(Input.Keys.W))
            MovementDirection.y =  1;

        if (Gdx.input.isKeyPressed(Input.Keys.S))
            MovementDirection.y = -1;

        // Just Toggle It
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER))
            OverrideWithClickPosition = !OverrideWithClickPosition;

        if(OverrideWithClickPosition && Gdx.input.isKeyPressed(Input.Buttons.LEFT))
        {
            Vector2 MouseAtScreenPoint = new Vector2(Gdx.input.getX(),  Gdx.input.getY());
            Vector2 MouseAtWorldPoint = DeprojectMousePositionToWorld(MouseAtScreenPoint);

            MovementDirection = MouseAtWorldPoint.sub(bucketSprite.getX(), bucketSprite.getY()).nor();;
        }

        bucketSprite.translateX(MovementDirection.x * Speed * DeltaTime);
        bucketSprite.translateY(MovementDirection.y * Speed * DeltaTime);
        // Update Other Things Such As Render Location And Collisions
    }

    @Override
    public boolean CanUpdate()
    {
        return IsActive;
    }

    @Override
    public boolean CanRender()
    {
        return IsActive;
    }

    @Override
    public void Render(SpriteBatch Batch)
    {
        bucketSprite.draw(Batch);
    }

    public Vector2 DeprojectMousePositionToWorld(Vector2 v)
    {
        Vector3 vc = Camera.RenderCamera().unproject(new Vector3(v.x, v.y, 0));
        return new Vector2(vc.x, vc.y);
    }
}
