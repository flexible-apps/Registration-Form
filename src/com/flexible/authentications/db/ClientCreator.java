package com.flexible.authentications.db;

import java.util.Objects;

public final class ClientCreator {
    private String username;
    private String email;
    private String password;

    private ClientCreator(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static ClientCreator getInstance(String username, String email, String password) {
        return new ClientCreator(username, email, password);
    }

    public ClientCreator() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientCreator that = (ClientCreator) o;
        return Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, password);
    }

    @Override
    public String toString() {
        return "AvailableClient{" + "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
