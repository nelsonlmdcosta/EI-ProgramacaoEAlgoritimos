package EntityComponent.Camera;

import EntityComponent.AEntityComponent;
import EntityComponent.Transform.Transform;
import Events.Transform.ITransformDirtyFlagCleared;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraComponent extends AEntityComponent implements ITransformDirtyFlagCleared
{
    Transform TransformComponent;
    OrthographicCamera RenderCamera;

    // We Can Construct It Hoewever
    @Override // Gotta tell java to override :p
    public void Start()
    {
        RenderCamera = new OrthographicCamera(80, 45);

        TransformComponent = Entity.GetFirstComponentOfType(Transform.class);
    }

    public OrthographicCamera RenderCamera()
    {
        return RenderCamera;
    }

    public Matrix4 GetCombinedMatrix()
    {
        return RenderCamera.combined;
    }

    public Matrix4 GetProjectionMatrix()
    {
        return RenderCamera.projection;
    }

    public Vector2 DeprojectScreenPositionToWorldPosition(Vector2 ScreenPoint)
    {
        Vector3 worldCoords = RenderCamera.unproject(new Vector3(ScreenPoint.x, ScreenPoint.y, 0));
        return new Vector2(worldCoords.x, worldCoords.y);
    }

    public Vector2 ProjectWorldPositionToScreenPosition(Vector2 WorldPoint)
    {
        Vector3 ScreenPosition = RenderCamera.project(new Vector3(WorldPoint.x, WorldPoint.y, 0));
        return new Vector2(ScreenPosition.x, ScreenPosition.y);
    }

    @Override
    public void OnTransformDirtyFlagCleared()
    {
        RenderCamera.position.set(TransformComponent.getX(), TransformComponent.getY(), 0);
    }
}
