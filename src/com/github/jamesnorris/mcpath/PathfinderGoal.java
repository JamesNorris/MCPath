package com.github.jamesnorris.mcpath;

import java.util.List;

public abstract class PathfinderGoal {
    public abstract PathNode transform(List<PathNode> navigated, PathNode finish, PathNode node);
}
