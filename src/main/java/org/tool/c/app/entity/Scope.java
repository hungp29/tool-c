package org.tool.c.app.entity;

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
}
