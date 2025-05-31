package MapParser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class MapParser
{
    Sprite[][] Sprites = null;

    Integer DimensionX = 0;
    Integer DimensionY = 0;

    // TODO: Transform this later to one single texture and use the TextureRegion
    ArrayList<Texture> MapSpriteSheet = new ArrayList<>();
    //TextureRegion MapSpriteSheetRegion = null;

    public MapGraph MapGraph = new MapGraph();

    public MapNode PlayerStart = null;

    public void Initialize()
    {
        LoadSpritesAndRegion();

        FileHandle MapLayoutFile = Gdx.files.internal("Map/Map.txt");
        if(!MapLayoutFile.exists())
            return;

        String MapLayoutString = MapLayoutFile.readString();
        ArrayList<String> Lines = new ArrayList<>();
        while(MapLayoutString.contains("\n"))
        {
            Lines.add(MapLayoutString.substring(0, MapLayoutString.indexOf("\n")));
            MapLayoutString = MapLayoutString.substring(MapLayoutString.indexOf("\n")+1, MapLayoutString.length());
        }

        // Just To See if Data Loaded In Correctly
        //for (int i = 0; i < Lines.size(); i++)
        //   System.out.println(Lines.get(i));

        if(Lines.get(Lines.size()-1).contains(","))
        {
            String LineToTrim = Lines.get(Lines.size()-1);
            String TrimmedX = LineToTrim.substring(0, LineToTrim.indexOf(",")).trim();
            String TrimmedY = LineToTrim.substring(LineToTrim.indexOf(",") + 1, LineToTrim.length()).trim();

            // Trims the BOM character from the front, silly thing caught me out for ages! >:(
            DimensionX = Integer.parseInt(TrimmedX.replaceAll("[^0-9]", ""));            DimensionY = Integer.parseInt( TrimmedY );
            DimensionY = Integer.parseInt(TrimmedY.replaceAll("[^0-9]", ""));            DimensionY = Integer.parseInt( TrimmedY );
        }

        // Parse Map Data Into A Useable Layout
        boolean[][] IsSpotFilled = new boolean[DimensionX][DimensionY];
        for(int Y = 0; Y < DimensionY; ++Y)
        {
            for(int X = 0; X < DimensionX; ++X)
            {
                IsSpotFilled[X][DimensionY - 1 - Y] = Lines.get(Y).charAt(X) != ' ';
            }
        }

        // Now We Have Data To Showcase Our World, Let's Populate It With Sprites
        Sprites = new Sprite[DimensionX][DimensionY];
        for(int Y = 0; Y < DimensionY; ++Y)
        {
            for (int X = 0; X < DimensionX; ++X)
            {
                if(!IsSpotFilled[X][Y])
                    continue;

                boolean LeftValid   = IsValidIndex(X - 1, 0, DimensionX - 1);
                boolean TopValid    = IsValidIndex(Y + 1, 0, DimensionY - 1);
                boolean RightValid  = IsValidIndex(X + 1, 0, DimensionX - 1);
                boolean BottomValid = IsValidIndex(Y - 1, 0, DimensionY - 1);

                // Top Left
                boolean HasLeftNeighbour   = IsValidIndex(X - 1, 0, DimensionX - 1) ? IsSpotFilled[X - 1][Y] : false;
                boolean HasTopNeighbour    = IsValidIndex(Y + 1, 0, DimensionY - 1) ? IsSpotFilled[X][Y + 1] : false;
                boolean HasRightNeighbour  = IsValidIndex(X + 1, 0, DimensionX - 1) ? IsSpotFilled[X + 1][Y] : false;
                boolean HasBottomNeighbour = IsValidIndex(Y - 1, 0, DimensionY - 1) ? IsSpotFilled[X][Y - 1] : false;

                // We Only Support 3x3 No Weird Edges/Protrusions
                Texture SlotTexture = GetTextureFromNeighbours(HasLeftNeighbour, HasTopNeighbour, HasRightNeighbour, HasBottomNeighbour);
                if (SlotTexture == null)
                    continue;

                Sprites[X][Y] = new Sprite(SlotTexture);
                Sprites[X][Y].setSize(1,1);
            }
        }

        GeneratePathingFromMapLayout(IsSpotFilled);

        // Let's Quickly Find The Player Start, Its The P In The Lines
        for(int Y = 0; Y < DimensionY; ++Y)
        {
            for(int X = 0; X < DimensionX; ++X)
            {
                if(Lines.get(Y).charAt(X) == 'P')
                {
                    PlayerStart = MapGraph.GetNode(X, DimensionY - 1 - Y);

                    break;
                }
            }
        }
    }

    private void GeneratePathingFromMapLayout(boolean[][] IsSpotFilled)
    {
        // Final List
        ArrayList<MapNode> MapNodes = new ArrayList<>();
        // Helper Structure
        MapNode[][] MapNodeGrid = new MapNode[DimensionX][DimensionY];

        // Add All Nodes To Calculate The Neighbours
        for(int Y = 0; Y < DimensionY; ++Y)
        {
            for(int X = 0; X < DimensionX; ++X)
            {
                if(IsSpotFilled[X][Y])
                {
                    MapNode NewMapNode = new MapNode();
                    NewMapNode.CellX = X;
                    NewMapNode.CellY = Y;

                    MapNodes.add(NewMapNode);
                    MapNodeGrid[X][Y] = NewMapNode;
                }
            }
        }

        // 4 Way Connections Only
        for(int Y = 0; Y < DimensionY; ++Y)
        {
            for(int X = 0; X < DimensionX; ++X)
            {
                if(MapNodeGrid[X][Y] == null)
                    continue;

                boolean HasLeftNeighbour   = IsValidIndex(X - 1, 0, DimensionX - 1) ? IsSpotFilled[X - 1][Y] : false;
                boolean HasTopNeighbour    = IsValidIndex(Y - 1, 0, DimensionY - 1) ? IsSpotFilled[X][Y - 1] : false;
                boolean HasRightNeighbour  = IsValidIndex(X + 1, 0, DimensionX - 1) ? IsSpotFilled[X + 1][Y] : false;
                boolean HasBottomNeighbour = IsValidIndex(Y + 1, 0, DimensionY - 1) ? IsSpotFilled[X][Y + 1] : false;

                if(HasLeftNeighbour)
                    MapNodeGrid[X][Y].AddTwoWayConnectionTo(MapNodeGrid[X-1][Y]);
                if(HasTopNeighbour)
                    MapNodeGrid[X][Y].AddTwoWayConnectionTo(MapNodeGrid[X][Y-1]);
                if(HasRightNeighbour)
                    MapNodeGrid[X][Y].AddTwoWayConnectionTo(MapNodeGrid[X+1][Y]);
                if(HasBottomNeighbour)
                    MapNodeGrid[X][Y].AddTwoWayConnectionTo(MapNodeGrid[X][Y+1]);
            }
        }

        MapGraph.SetNodes(MapNodes);
    }

    private void LoadSpritesAndRegion()
    {
        for(int i = 0; i < 9; ++i)
        {
            MapSpriteSheet.add(new Texture(Gdx.files.internal("Images/Map/tile00"+i+".png")));
        }

        //TextureRegion MapSpriteSheetRegion = new TextureRegion(MapSpriteSheet, 3, 3);
    }

    private Texture GetTextureFromNeighbours(boolean LeftNeighbour, boolean TopNeighbour, boolean RightNeighbour, boolean BottomNeighbour)
    {
        if(!LeftNeighbour && !TopNeighbour && RightNeighbour && BottomNeighbour)
            return MapSpriteSheet.get(EMapTile.TopLeft.ordinal());

        if(LeftNeighbour && !TopNeighbour && RightNeighbour && BottomNeighbour)
            return MapSpriteSheet.get(EMapTile.TopMiddle.ordinal());

        if(LeftNeighbour && !TopNeighbour && !RightNeighbour && BottomNeighbour)
            return MapSpriteSheet.get(EMapTile.TopRight.ordinal());

        if(!LeftNeighbour && TopNeighbour && RightNeighbour && BottomNeighbour)
            return MapSpriteSheet.get(EMapTile.Left.ordinal());

        if(LeftNeighbour && TopNeighbour && RightNeighbour && BottomNeighbour)
            return MapSpriteSheet.get(EMapTile.Middle.ordinal());

        if(LeftNeighbour && TopNeighbour && !RightNeighbour && BottomNeighbour)
            return MapSpriteSheet.get(EMapTile.Right.ordinal());

        if(!LeftNeighbour && TopNeighbour && RightNeighbour && !BottomNeighbour)
            return MapSpriteSheet.get(EMapTile.BottomLeft.ordinal());

        if(LeftNeighbour && TopNeighbour && RightNeighbour && !BottomNeighbour)
            return MapSpriteSheet.get(EMapTile.BottomMiddle.ordinal());

        if(LeftNeighbour && TopNeighbour && !RightNeighbour && !BottomNeighbour)
            return MapSpriteSheet.get(EMapTile.BottomRight.ordinal());

        return MapSpriteSheet.get(EMapTile.Middle.ordinal());
    }

    private boolean IsValidIndex(int Value, int Min, int Max)
    {
        return Value >= Min && Value <= Max;
    }


    public void RenderMap(SpriteBatch SpriteBatcher)
    {
        for(int Y = 0; Y < DimensionY; ++Y)
        {
            for(int X = 0; X < DimensionX; ++X)
            {
                if(Sprites[X][Y] == null)
                    continue;

                int PosX = X;
                int PosY = Y; // Invert Y!

                SpriteBatcher.draw(Sprites[X][Y], PosX, PosY, 1, 1);
                //Sprites[X][Y].draw(SpriteBatcher);
            }
        }
    }
}
