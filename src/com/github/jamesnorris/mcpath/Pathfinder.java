package com.github.jamesnorris.mcpath;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;


public class Pathfinder {
    private final PathNode start, finish;
    private List<PathfinderGoal> goals;

    private static PathNode toNode(Location loc) {
        return new PathNode(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public Pathfinder(Location start, Location finish) {
        this(toNode(start), toNode(finish));
    }

    public Pathfinder(PathNode start, PathNode finish) {
        this(start, finish, null);
    }

    public Pathfinder(PathNode start, PathNode finish, List<PathfinderGoal> goals) {
        this.start = start;
        this.finish = finish;
        this.goals = goals;
    }

    public Path calculate() {
        List<PathNode> closed = new ArrayList<PathNode>();
        @SuppressWarnings("serial") List<PathNode> open = new ArrayList<PathNode>() {
            {
                add(start);
            }
        };
        List<PathNode> navigated = new ArrayList<PathNode>();
        start.F = start.distance(finish);
        while (!open.isEmpty()) {
            PathNode current = null;
            for (PathNode node : open) {
                for (PathfinderGoal goal : goals) {
                    node = goal.transform(node);
                }
                if (current == null || node.F < current.F) {
                    current = node;
                }
            }
            if (current.distance(finish) < 1) {
                return reconstruct(navigated, finish);
            }
            open.remove(current);
            closed.add(current);
            for (PathNode node : current.getNeighbors()) {
                if (closed.contains(node)) {
                    continue;
                }
                double tentG = current.G + current.distance(node);
                if (!open.contains(node) || tentG < node.G) {
                    navigated.add(current);
                    node.G = tentG;
                    node.F = tentG + node.distance(finish);
                    if (!open.contains(node)) {
                        open.add(node);
                    }
                }
            }
        }
        return null;
    }

    public PathNode getStart() {
        return start;
    }

    public PathNode getFinish() {
        return finish;
    }

    @SuppressWarnings("serial") private Path reconstruct(List<PathNode> navigated, final PathNode current) {
        Path withCurrent = new Path(new ArrayList<PathNode>() {
            {
                add(current);
            }
        });
        System.out.println(current);
        if (navigated.contains(current)) {
            return reconstruct(navigated, current).append(withCurrent);
        }
        return withCurrent;
    }

    public static void main(String[] args) {
        new Pathfinder(new PathNode(0, 0, 0), new PathNode(20, 5, 5)).calculate();
    }
}
