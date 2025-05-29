package EntityComponent;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

public class RandomSpriteTint extends AEntityComponent implements IUpdateableComponent
{
    SpriteRendererComponent SpriteRenderer;

    Random RandomNumberGenerator = new Random();

    @Override
    public void Start()
    {
        SpriteRenderer = AssignedEntity.WorldScene.FindFirstEntityWithTag("Player").GetFirstComponentOfType(SpriteRendererComponent.class);
    }

    @Override
    public void Update(float DeltaTime)
    {
        SpriteRenderer.SetTintColor
            (
                new Color
                    (
                        RandomNumberGenerator.nextFloat(),
                        RandomNumberGenerator.nextFloat(),
                        RandomNumberGenerator.nextFloat(),
                        1.0f
                    )
            );
    }

    @Override
    public boolean CanUpdate()
    {
        return IsActive;
    }
}
