package com.bage;

public class PathDefinition {

    int id;
    String antPath;
    String definition;

    public PathDefinition() {

    }

    public PathDefinition(int id, String antPath, String definition) {
        this.id = id;
        this.antPath = antPath;
        this.definition = definition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAntPath() {
        return antPath;
    }

    public void setAntPath(String antPath) {
        this.antPath = antPath;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    public String toString() {
        return "PathDefinition{" +
                "id=" + id +
                ", antPath='" + antPath + '\'' +
                ", definition='" + definition + '\'' +
                '}';
    }
}