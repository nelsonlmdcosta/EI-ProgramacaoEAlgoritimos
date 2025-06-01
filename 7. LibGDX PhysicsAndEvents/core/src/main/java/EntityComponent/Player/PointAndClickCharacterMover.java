package EntityComponent.Player;

import EntityComponent.AEntityComponent;
import EntityComponent.Camera.CameraComponent;
import EntityComponent.IUpdateableComponent;
import EntityComponent.Rendering.SpriteRendererComponent;
import EntityComponent.Transform.Transform;
import Events.EventDispatcherV2;
import MapParser.MapGraph;
import MapParser.MapNode;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;


import java.util.List;

public class PointAndClickCharacterMover extends AEntityComponent implements IUpdateableComponent
{
    private SpriteRendererComponent SpriteRenderer;
    private CameraComponent Camera;
    private MapGraph MapGraph;

    private List<MapNode> CurrentPath = null;

    private boolean AllowDebugToConsole = true;
    float Speed = 10.0f;

    Transform TransformComponent = null;

    EventDispatcherV2<IOnPlayerReachedEndOfPath> PlayerReachedEndOfPathEvent = new EventDispatcherV2<>(IOnPlayerReachedEndOfPath.class);

    @Override
    public void Start()
    {
        SpriteRenderer = Entity.GetFirstComponentOfType(SpriteRendererComponent.class);

        MapGraph = Entity.WorldScene.Map.MapGraph;

        Camera = Entity.WorldScene.FindFirstEntityWithTag("Camera").GetFirstComponentOfType(CameraComponent.class);

        // This is guaranteed, but wont be in all cases! Especially if you reuse code! :p Always check the validity of your references, im rushing through this as fast as possible
        TransformComponent = Entity.GetFirstComponentOfType(Transform.class);
        TransformComponent.SetPosition(Entity.WorldScene.Map.PlayerStart.GetPosition());
    }

    @Override
    public boolean CanUpdate()
    {
        return IsActive;
    }

    @Override
    public void Update(float DeltaTime)
    {
        Vector2 MovementDirection = new Vector2();

        // Time To Test The Graph! :D
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !HasPath())
        {
            CurrentPath = TryFindPath();

            if(CurrentPath == null || CurrentPath.size() == 0)
                return;

            DebugPathToConsole();
        }

        if(HasPath())
        {
            while(CurrentPath.size() != 0 && CurrentPath.get(0) == null)
                CurrentPath.remove(CurrentPath.size()-1);

            Vector2 NodeLocation = new Vector2(CurrentPath.get(0).CellX , CurrentPath.get(0).CellY);
            Vector2 SpriteLocation = SpriteRenderer.GetSpritePosition();

            if(Vector2.dst(SpriteLocation.x, SpriteLocation.y, NodeLocation.x, NodeLocation.y) < 0.1f)
            {
                CurrentPath.remove(0);

                // Means we reached the end
                if(CurrentPath.size() == 0)
                {
                    CurrentPath = null;

                    PlayerReachedEndOfPathEvent.Invoke();

                    return;
                }

                NodeLocation = new Vector2(CurrentPath.get(0).CellX , CurrentPath.get(0).CellY );
            }

            MovementDirection = NodeLocation.sub(SpriteLocation).nor();
        }

        SpriteRenderer.AddPositionDelta(MovementDirection.scl(Speed * DeltaTime));
    }

    public List<MapNode> TryFindPath()
    {
        Vector2 MouseAtScreenPoint = new Vector2(Gdx.input.getX(),  Gdx.input.getY());
        Vector2 MouseAtWorldPoint = Camera.DeprojectScreenPositionToWorldPosition(MouseAtScreenPoint);

        return MapGraph.SearchForPath
            (
                MapGraph.GetNode
                    (
                        // Round To Avoid Weird Floating Point Erros
                        (int) Math.round( SpriteRenderer.GetSpritePosition().x ),
                        (int) Math.round( SpriteRenderer.GetSpritePosition().y )
                    ),
                MapGraph.GetNode
                    (
                        // Round To Avoid Weird Floating Point Erros
                        (int) MouseAtWorldPoint.x,
                        (int) MouseAtWorldPoint.y
                    )
            );
    }


    public void DebugPathToConsole()
    {
        if(!AllowDebugToConsole)
            return;

        for(int i = 0; i < CurrentPath.size(); i++)
        {
            System.out.println(CurrentPath.get(i).CellX + " " + CurrentPath.get(i).CellY);
        }

        System.out.println("");
    }

    public boolean HasPath()
    {
        return CurrentPath != null;
    }

    public boolean HasValidMovementVector(Vector2 MovementVector)
    {
        return MovementVector.x != 0 && MovementVector.y != 0;
    }

}
