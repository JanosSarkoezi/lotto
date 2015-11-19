package com.mycompany.lotto.action;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author saj
 */
public class Macro implements Action {

    private String name;
    private List<Action> actions = new ArrayList<>();

    public String getName() {
        return name;
    }

    public Macro name(String name) {
        this.name = name;
        return this;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    @Override
    public void perform() {
        actions.forEach(a -> {
            System.out.println("Action: " + a.getClass().getSimpleName());
            a.perform();
        });
    }
}
