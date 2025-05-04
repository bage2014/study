package com.bage.json.compare;

import java.util.List;

public class CompareDiffItem {

     private String summary;
     private List<String> paths;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    @Override
    public String toString() {
        return "CompareDiffItem{" +
                "summary='" + summary + '\'' +
                ", paths=" + paths +
                '}';
    }
}
