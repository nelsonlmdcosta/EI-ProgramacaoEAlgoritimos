package EntityComponent;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraComponent extends AEntityComponent
{
    OrthographicCamera RenderCamera;

    // We Can Construct It Hoewever
    @Override // Gotta tell java to override :p
    public void Start()
    {
        RenderCamera = new OrthographicCamera(80, 45);
    }

    public OrthographicCamera RenderCamera()
    {
        return RenderCamera;
    }
}
