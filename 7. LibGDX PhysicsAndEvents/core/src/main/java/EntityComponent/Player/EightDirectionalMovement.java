package EntityComponent.Player;

import EntityComponent.AEntityComponent;
import EntityComponent.Animation.IAnimationMovementParameter;
import EntityComponent.Collision.ACollider;
import EntityComponent.IUpdateableComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class EightDirectionalMovement extends AEntityComponent implements IUpdateableComponent, IAnimationMovementParameter
{
    Vector2 InputDirection = new Vector2();
    Vector2 FinalVelocity = new Vector2();
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

        // Copy InputDirection Into Final Velocity, Normalize It To Avoid Moving Diagonally Fast, And Then Scale That Vector In A Direction
        // We don't change the InputDirection Directly as that's used by the animation component
        FinalVelocity.set(InputDirection).nor().scl(Speed);

        ColliderComponent.Velocity(FinalVelocity);
    }

    @Override
    public boolean CanUpdate()
    {
        return true;
    }

    private void UpdateDirectionFromInput()
    {
        InputDirection.set
            (
                BoolToInt(Gdx.input.isKeyPressed(Input.Keys.A)) * -1 + BoolToInt(Gdx.input.isKeyPressed(Input.Keys.D)),
                BoolToInt(Gdx.input.isKeyPressed(Input.Keys.S)) * -1 + BoolToInt(Gdx.input.isKeyPressed(Input.Keys.W))
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
