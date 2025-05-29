package EntityComponent;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HealthWidget extends AEntityComponent implements IUpdateableComponent, IRenderableComponent
{
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private float maxHealth = 100;
    private float currentHealth = 100;
    private float x = 0;
    private float y = 0;
    private float width = 100;
    private float height = 100;

    CameraComponent Camera;

    @Override
    public void Start()
    {
        Camera = AssignedEntity.WorldScene.FindFirstEntityWithTag("Camera").GetFirstComponentOfType(CameraComponent.class);
    }


    @Override
    public boolean CanRender()
    {
        return true;
    }

    @Override
    public void Render(SpriteBatch Batch)
    {
        shapeRenderer.setProjectionMatrix(Camera.RenderCamera().projection);

        // Save the current projection matrix
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw background (empty health)
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, width, height);

        // Draw foreground (current health)
        shapeRenderer.setColor(Color.GREEN);
        float healthWidth = (currentHealth / maxHealth) * width;
        shapeRenderer.rect(x, y, healthWidth, height);

        shapeRenderer.end();
    }

    @Override
    public void Update(float DeltaTime)
    {
    }

    @Override
    public boolean CanUpdate() {
        return false;
    }
}
