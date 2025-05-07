package EntityComponent;

import MapParser.MapGraph;
import MapParser.MapNode;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class PlayerComponent extends AEntityComponent implements IUpdateableComponent, IRenderableComponent
{
    Sprite bucketSprite;
    Texture bucketTexture;

    float Speed = 10.0f;

    boolean OverrideWithClickPosition = false;

    // TODO: Clean up
    public CameraComponent Camera;

    public Vector2 LastClickPosition = new Vector2();

    public MapGraph MapGraph;
    private List<MapNode> CurrentPath = null;

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

        if(OverrideWithClickPosition && Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        {
            Vector2 MouseAtScreenPoint = new Vector2(Gdx.input.getX(),  Gdx.input.getY());
            Vector2 MouseAtWorldPoint = DeprojectMousePositionToWorld(MouseAtScreenPoint);

            MovementDirection = MouseAtWorldPoint.sub(bucketSprite.getX(), bucketSprite.getY()).nor();;
        }

        if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT))
        {
            Vector2 MouseAtScreenPoint = new Vector2(Gdx.input.getX(),  Gdx.input.getY());
            System.out.println(DeprojectMousePositionToWorld(MouseAtScreenPoint));
        }

        // Time To Test The Graph! :D
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && CurrentPath == null)
        {
            Vector2 MouseAtScreenPoint = new Vector2(Gdx.input.getX(),  Gdx.input.getY());
            Vector2 MouseAtWorldPoint = DeprojectMousePositionToWorld(MouseAtScreenPoint);

            CurrentPath = MapGraph.SearchForPath
                (
                    MapGraph.GetNode
                        (
                            // Round To Avoid Weird Floating Point Erros
                            (int) Math.round( bucketSprite.getX() ),
                            (int) Math.round( bucketSprite.getY() )
                        ),
                    MapGraph.GetNode
                        (
                            // Round To Avoid Weird Floating Point Erros
                            (int) MouseAtWorldPoint.x,
                            (int) MouseAtWorldPoint.y
                        )
                );

            if(CurrentPath == null)
                return;

            for(int i = 0; i < CurrentPath.size(); i++)
            {
                System.out.println(CurrentPath.get(i).CellX + " " + CurrentPath.get(i).CellY);
            }
            System.out.println("");
        }

        if(CurrentPath != null)
        {
            Vector2 NodeLocation = new Vector2(CurrentPath.get(0).CellX , CurrentPath.get(0).CellY);
            Vector2 SpriteLocation = new Vector2(bucketSprite.getX(), bucketSprite.getY());

            if(Vector2.dst(SpriteLocation.x, SpriteLocation.y, NodeLocation.x, NodeLocation.y) < 0.1f)
            {
                CurrentPath.remove(0);

                if(CurrentPath.size() == 0)
                {
                    CurrentPath = null;
                    return;
                }

                NodeLocation = new Vector2(CurrentPath.get(0).CellX , CurrentPath.get(0).CellY );
            }

            MovementDirection = NodeLocation.sub(SpriteLocation).nor();
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
        // Get screen coordinates (pixels)
        Vector3 screenCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        // Unproject to world coordinates
        Vector3 worldCoords = Camera.RenderCamera().unproject(screenCoords);

        // Calculate grid coordinates
        //int gridX = (int) Math.floor(worldCoords.x / tileWidth);
        //int gridY = (int) Math.floor(worldCoords.y / tileHeight);

        //Vector3 vc = Camera.RenderCamera().unproject(new Vector3(v.x, v.y, 0));
        return new Vector2(worldCoords.x, worldCoords.y);
    }

    public Vector2 ProjectMousePositionToWorld(Vector2 v)
    {
        Vector3 vc = Camera.RenderCamera().project(new Vector3(v.x, v.y, 0));
        return new Vector2(vc.x, vc.y);
    }
}
