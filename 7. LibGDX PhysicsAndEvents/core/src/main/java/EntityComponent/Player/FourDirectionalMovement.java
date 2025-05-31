package EntityComponent.Player;

import EntityComponent.AEntityComponent;
import EntityComponent.Animation.IAnimationMovementParameter;
import EntityComponent.Collision.ACollider;
import EntityComponent.IUpdateableComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class FourDirectionalMovement extends AEntityComponent implements IUpdateableComponent, IAnimationMovementParameter
{
    Vector2 InputDirection = new Vector2();
    float Speed = 2.0f;

    ACollider ColliderComponent;

    public void Start()
    {
        ColliderComponent = Entity.GetFirstComponentOfType(ACollider.class);
    }

    @Override
    public void Update(float DeltaTime)
    {
        UpdateDirectionFromInput();

        ColliderComponent.Velocity(InputDirection.scl(Speed));
    }

    @Override
    public boolean CanUpdate()
    {
        return true;
    }

    private void UpdateDirectionFromInput()
    {
        float Horizontal = BoolToInt(Gdx.input.isKeyPressed(Input.Keys.A)) * -1 + BoolToInt(Gdx.input.isKeyPressed(Input.Keys.D));
        float Vertical   = BoolToInt(Gdx.input.isKeyPressed(Input.Keys.S)) * -1 + BoolToInt(Gdx.input.isKeyPressed(Input.Keys.W));

        // Only Allow Input In One Direction
        InputDirection.set
            (
                Horizontal != 0 && Vertical != 0 ? 0.0f : Horizontal,
                Horizontal != 0 && Vertical != 0 ? 0.0f : Vertical
            );
    }

    private int BoolToInt(boolean BooleanToConvert)
    {
        return BooleanToConvert ? 1 : 0;
    }

    @Override
    public Vector2 MovementDirection()
    {
        return InputDirection;
    }
}
