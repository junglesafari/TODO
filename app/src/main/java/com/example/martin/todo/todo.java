package com.example.martin.todo;

public class todo {
   String todotitle;
   String todotext;

    public todo(String todotitle, String todotext) {
        this.todotitle = todotitle;
        this.todotext = todotext;
    }

    public String getTodotitle() {
        return todotitle;
    }

    public void setTodotitle(String todotitle) {
        this.todotitle = todotitle;
    }

    public String getTodotext() {
        return todotext;
    }

    public void setTodotext(String todotext) {
        this.todotext = todotext;
    }
}
