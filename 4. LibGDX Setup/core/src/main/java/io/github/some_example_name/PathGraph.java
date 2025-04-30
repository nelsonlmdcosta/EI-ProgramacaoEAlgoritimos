package io.github.some_example_name;

public class PathGraph
{
    // Todos os nossos nos
    PathfindingNode[] Nodes;

    public PathfindingNode GetNode(int CellX, int CellY)
    {
        for(int i = 0; i<Nodes.length;++ i)
        {
            if(Nodes[i].CellX == CellX && Nodes[i].CellY == CellY)
            {
                return Nodes[i];
            }
        }

        return null;
    }

    public void SetNodes(PathfindingNode[] nodes)
    {
        Nodes = nodes;
    }

    public PathfindingNode[] SearchForPath(PathfindingNode StartNode, PathfindingNode EndNode)
    {
        // Go from 3 to 0
        // Return Type Needs All Nodes To Traverse To Point
        // Aka [3, 2, 0]

        // Find And Return Path From To
        return null;
    }

    public static Vector2D CalculatePositionFromNodes(PathfindingNode StartNode, PathfindingNode EndNode, float NormalizeTime)
    {
        return Vector2D.Lerp(new Vector2D(StartNode.CellX, StartNode.CellY), new Vector2D(EndNode.CellX, EndNode.CellY), NormalizeTime);
    }
}
