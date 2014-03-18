package com.github.jamesnorris.mcpath;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class Pathfinder {
    public static void main(String[] args) {
        System.out.println("MCPath - Please enter the following arguments to test the pathfinder:\nEnter the x coord: ");
        int x = readInt();
        System.out.println("Enter the y coord: ");
        int y = readInt();
        System.out.println("Enter the z coord: ");
        int z = readInt();
        System.out.println("Path:\n----------------------");
        for (PathNode node : new Pathfinder(new PathNode(0, 0, 0), new PathNode(x, y, z)).calculate().getNodes()) {
            System.out.println(node);
        }
    }

    private static Integer readInt() {
        try {
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            String s = bufferRead.readLine();
            return Integer.parseInt(s);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    private static PathNode toNode(Location loc) {
        return new PathNode(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    private final PathNode start, finish;
    private List<PathfinderGoal> goals;

    public Pathfinder(Location start, Location finish) {
        this(toNode(start), toNode(finish));
    }

    public Pathfinder(Location start, Location finish, List<PathfinderGoal> goals) {
        this(toNode(start), toNode(finish), goals);
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
        return calculate(-1);
    }

    public Path calculate(int maxNodes) {
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
                if (current == null || node.H < current.H) {
                    current = node;
                }
            }
            if (current.distance(finish) < 1
                    || (navigated.size() >= maxNodes && maxNodes != -1)) {
                if (navigated.size() < maxNodes) {
                    navigated.add(finish);
                } else {
                    navigated.add(current);
                }
                return reconstruct(navigated, navigated.size() - 1);
            }
            open.remove(current);
            closed.add(current);
            for (PathNode node : current.getNeighbors()) {
                if (closed.contains(node)) {
                    continue;
                }
                double tentG = current.G + current.distance(node);
                if (!open.contains(node) || tentG < node.G) {
                    if (!navigated.contains(current)) {
                        navigated.add(current);
                    }
                    node.G = tentG;
                    node.H = node.distance(finish);
                    node.F = tentG + node.H;
                    if (goals != null) {
                        for (PathfinderGoal goal : goals) {
                            node = goal.transform(navigated, finish, node);
                        }
                    }
                    if (!open.contains(node)) {
                        open.add(node);
                    }
                }
            }
        }
        return null;
    }

    public PathNode getFinish() {
        return finish;
    }

    public PathNode getStart() {
        return start;
    }

    @SuppressWarnings("serial") private Path reconstruct(List<PathNode> navigated, int index) {
        final PathNode current = navigated.get(index);
        Path withCurrent = new Path(new ArrayList<PathNode>() {
            {
                add(current);
            }
        });
        if (index > 0 && navigated.contains(current)) {
            return reconstruct(navigated, index - 1).append(withCurrent);
        }
        return withCurrent;
    }
}
