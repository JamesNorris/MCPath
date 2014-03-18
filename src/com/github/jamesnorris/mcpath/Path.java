package com.github.jamesnorris.mcpath;

import java.util.List;

public class Path {
    private List<PathNode> nodes;

    public Path(List<PathNode> nodes) {
        this.nodes = nodes;
    }

    public Path append(Path other) {
        nodes.addAll(other.getNodes());
        return this;
    }

    public List<PathNode> getNodes() {
        return nodes;
    }
}
