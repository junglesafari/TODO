package com.example.martin.todo;

public class todo {
   String todotitle;
   String todotext;
    String tododate;
    String todotime;
    public todo(String todotitle, String todotext, String tododate, String todotime) {
        this.todotitle = todotitle;
        this.todotext = todotext;
        this.tododate = tododate;
        this.todotime = todotime;
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

    public String getTododate() {
        return tododate;
    }

    public void setTododate(String tododate) {
        this.tododate = tododate;
    }

    public String getTodotime() {
        return todotime;
    }

    public void setTodotime(String todotime) {
        this.todotime = todotime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    long id;


}
