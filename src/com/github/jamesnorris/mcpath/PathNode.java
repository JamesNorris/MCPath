package com.github.jamesnorris.mcpath;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

public class PathNode {
    
    public static PathNode fromLocation(Location location) {
        return new PathNode(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
    
    public int x, y, z;
    public double G, F, H;

    public PathNode(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PathNode(int x, int y, int z, PathNode start, PathNode target) {
        this(x, y, z);
        G = distance(start);
        H = distance(target);
        F = G + H;
    }

    public double distance(PathNode other) {
        return Math.sqrt(Math.pow(other.x - x, 2) + Math.pow(other.y - y, 2)
                + Math.pow(other.z - z, 2));
    }

    @Override public boolean equals(Object other) {
        if (!(other instanceof PathNode)) {
            return false;
        }
        PathNode otherNode = (PathNode) other;
        return otherNode.x == x && otherNode.y == y && otherNode.z == z;
    }

    @SuppressWarnings("serial") public List<PathNode> getNeighbors() {
        final int X = x, Y = y, Z = z;
        return new ArrayList<PathNode>() {
            {
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            add(new PathNode(X + x, Y + y, Z + z));
                        }
                    }
                }
            }
        };
    }

    @Override public String toString() {
        return "PathNode:(x: " + x + ", y: " + y + ", z: " + z + ", g: " + G
                + ", f: " + F + ", h: " + H + ")";
    }
    
    public Location toLocation(World world) {
        return new Location(world, x, y, z);
    }
}
