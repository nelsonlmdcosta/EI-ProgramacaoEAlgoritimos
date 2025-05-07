package MapParser;

import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class MapGraph
{
    boolean UseAStar = true;

    // Todos os nossos nos
    ArrayList<MapNode> Nodes;

    public MapNode GetNode(int CellX, int CellY)
    {
        for(int i = 0; i<Nodes.size();++ i)
        {
            if(Nodes.get(i).CellX == CellX && Nodes.get(i).CellY == CellY)
            {
                return Nodes.get(i);
            }
        }

        return null;
    }

    public void SetNodes(ArrayList<MapNode> nodes)
    {
        Nodes = nodes;
    }

    public List<MapNode> SearchForPath(MapNode StartNode, MapNode EndNode)
    {
        if(StartNode == null || EndNode == null)
            return null;

        if(StartNode.CellX == EndNode.CellX && StartNode.CellY == EndNode.CellY)
            return null;

        if(UseAStar)
            return FindAStarPath(StartNode, EndNode);

        return FindShortestPathBFS(Nodes, StartNode, EndNode);
    }

    public static Vector2 CalculatePositionFromNodes(MapNode StartNode, MapNode EndNode, float NormalizeTime)
    {
        return new Vector2(StartNode.CellX, StartNode.CellY).lerp( new Vector2(EndNode.CellX, EndNode.CellY), NormalizeTime);
    }

    // BFS Implementation
    public ArrayList<MapNode> FindShortestPathBFS(ArrayList<MapNode> nodes, MapNode start, MapNode end)
    {
        Queue<MapNode> queue = new LinkedList<>();
        Map<MapNode, MapNode> cameFrom = new HashMap<>();
        Set<MapNode> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty())
        {
            MapNode current = queue.poll();

            if (current.equals(end))
            {
                return ReconstructPathBFS(cameFrom, end);
            }

            // Check all 4 cardinal neighbors
            for (MapNode neighbor : current.NeighbouringNodes)
            {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }

        return null; // No path found
    }

    private static ArrayList<MapNode> ReconstructPathBFS(Map<MapNode, MapNode> cameFrom, MapNode end)
    {
        ArrayList<MapNode> path = new ArrayList<>();
        MapNode current = end;

        while (current != null)
        {
            path.add(current);
            current = cameFrom.get(current);
        }

        Collections.reverse(path);
        return path;
    }


    // A* Implementation

    /**
     * Calculates the Manhattan distance heuristic between two nodes
     * @param from Starting node
     * @param to Target node
     * @return Estimated distance
     */
    private static int heuristic(MapNode from, MapNode to) {
        return Math.abs(from.CellX - to.CellX) + Math.abs(from.CellY - to.CellY);
    }

    /**
     * Finds the path from start to goal using A* algorithm
     * @param start Starting node
     * @param goal Target node
     * @return List of nodes representing the path, or empty list if no path exists
     */
    public static List<MapNode> FindAStarPath(MapNode start, MapNode goal)
    {
        // Set of nodes already evaluated
        Set<MapNode> closedSet = new HashSet<>();

        // Map to reconstruct the path
        Map<MapNode, MapNode> cameFrom = new HashMap<>();

        // g-score: cost from start to current node
        Map<MapNode, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);

        // f-score: estimated total cost from start to goal through current node
        Map<MapNode, Integer> fScore = new HashMap<>();
        fScore.put(start, heuristic(start, goal));

        // Priority queue for open nodes, ordered by f-score (lowest first)
        PriorityQueue<MapNode> openSet = new PriorityQueue<>(
            Comparator.comparingInt(n -> fScore.getOrDefault(n, Integer.MAX_VALUE))
        );

        openSet.add(start);

        while (!openSet.isEmpty())
        {
            MapNode current = openSet.poll();

            if (current.equals(goal))
            {
                return ReconstructPathAStar(cameFrom, current);
            }

            closedSet.add(current);

            for (MapNode neighbor : current.NeighbouringNodes)
            {
                if (closedSet.contains(neighbor))
                {
                    continue; // Skip already evaluated nodes
                }

                // All edges have the same weight (1)
                int tentativeGScore = gScore.get(current) + 1;

                if (!openSet.contains(neighbor))
                {
                    openSet.add(neighbor);
                } else if (tentativeGScore >= gScore.getOrDefault(neighbor, Integer.MAX_VALUE))
                {
                    continue; // This is not a better path
                }

                // This path is the best so far, record it
                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, tentativeGScore + heuristic(neighbor, goal));
            }
        }

        // No path found
        return Collections.emptyList();
    }

    private static List<MapNode> ReconstructPathAStar(Map<MapNode, MapNode> cameFrom, MapNode current)
    {
        List<MapNode> path = new ArrayList<>();
        path.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(0, current);
        }

        return path;
    }
}
