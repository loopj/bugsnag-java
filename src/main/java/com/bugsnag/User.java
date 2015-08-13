package com.bugsnag;

class User {
    public String id;
    public String name;
    public String email;

    User() {}

    User(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    User(User u) {
        this(u.id, u.email, u.name);
    }
}
