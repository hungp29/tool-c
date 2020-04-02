package org.tool.c.app.entity;

import java.util.List;
import java.util.Objects;

public class Condition {

    private List<String> subset;

    public void setSubset(List<String> subset) {
        this.subset = subset;
    }

    public List<String> getSubset() {
        return subset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition = (Condition) o;
        return Objects.equals(subset, condition.subset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subset);
    }
}
