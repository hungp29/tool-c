package org.tool.c.app.entity;

import java.util.Objects;

public class Scope {

    private String name;

    private Condition condition;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scope scope = (Scope) o;
        return Objects.equals(name, scope.name) &&
                Objects.equals(condition, scope.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, condition);
    }
}
