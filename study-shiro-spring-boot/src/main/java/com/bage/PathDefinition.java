package com.bage;

public class PathDefinition {

    int id;
    String antPath;
    String roleNames;
    String filterNames;

    public PathDefinition() {

    }

    public PathDefinition(int id, String antPath, String roleNames, String filterNames) {
        this.id = id;
        this.antPath = antPath;
        this.roleNames = roleNames;
        this.filterNames = filterNames;
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

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public String getFilterNames() {
        return filterNames;
    }

    public void setFilterNames(String filterNames) {
        this.filterNames = filterNames;
    }

    @Override
    public String toString() {
        return "PathDefinition{" +
                "id=" + id +
                ", antPath='" + antPath + '\'' +
                ", roleNames='" + roleNames + '\'' +
                ", filterNames='" + filterNames + '\'' +
                '}';
    }
}