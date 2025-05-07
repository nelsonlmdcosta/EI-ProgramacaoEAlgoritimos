package MapParser;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class MapNode
{
    public ArrayList<MapNode> NeighbouringNodes = new ArrayList<MapNode>();
    public int CellX;
    public int CellY;

    public void AddOneWayConnectionTo(MapNode OtherNode)
    {
        if(!NeighbouringNodes.contains(OtherNode))
            NeighbouringNodes.add(OtherNode);
    }

    public void AddTwoWayConnectionTo(MapNode OtherNode)
    {
        if(!NeighbouringNodes.contains(OtherNode))
        {
            NeighbouringNodes.add(OtherNode);
            OtherNode.AddOneWayConnectionTo(this);
        }
    }

    public Vector2 GetPosition()
    {
        return new Vector2(CellX, CellY);
    }
}
