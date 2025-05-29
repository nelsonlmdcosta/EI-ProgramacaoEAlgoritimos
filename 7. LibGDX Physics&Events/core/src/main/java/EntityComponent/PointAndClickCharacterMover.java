package EntityComponent;

import Events.IPlayerReachedEndOfPathReceiver;
import Events.PlayerReachedEndOfPathEvent;
import MapParser.MapGraph;
import MapParser.MapNode;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;


import java.util.List;

public class PointAndClickCharacterMover extends AEntityComponent implements IUpdateableComponent, IPlayerReachedEndOfPathReceiver
{
    private SpriteRendererComponent SpriteRenderer;
    private CameraComponent Camera;
    private MapGraph MapGraph;

    private List<MapNode> CurrentPath = null;

    private boolean AllowDebugToConsole = true;
    float Speed = 10.0f;

    PlayerReachedEndOfPathEvent PlayerReachedEndOfPathEvent = new PlayerReachedEndOfPathEvent();

    @Override
    public void Start()
    {
        SpriteRenderer = AssignedEntity.GetFirstComponentOfType(SpriteRendererComponent.class);

        MapGraph = AssignedEntity.WorldScene.Map.MapGraph;

        Camera = AssignedEntity.WorldScene.FindFirstEntityWithTag("Camera").GetFirstComponentOfType(CameraComponent.class);

        SpriteRenderer.SetPosition(AssignedEntity.WorldScene.Map.PlayerStart.GetPosition());

        PlayerReachedEndOfPathEvent.AddObserver(this);
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

            if(CurrentPath == null)
                return;

            if(CurrentPath.size() == 0 )
                CurrentPath = null;

            if(CurrentPath != null)
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

                    PlayerReachedEndOfPathEvent.InvokeEvent();

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
