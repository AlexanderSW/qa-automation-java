// ui-tests/src/test/java/com/example/qa/ui/screenplay/Actor.java
package com.example.qa.ui.screenplay;

import com.example.qa.ui.screenplay.tasks.ExpectLoginSuccessTask;

public class Actor {
    private final String name;

    public Actor(String name) { this.name = name; }
    public String name() { return name; }

    public void attemptsTo(Task task, ExpectLoginSuccessTask on) { task.performAs(this); }
}
