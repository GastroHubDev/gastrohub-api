package br.com.fiap.gastrohubapi.domain.entity;

import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private UserType userType;

    public User(){}

    public static User create(String name, String email, String password, UserType userType)  {


        User user = new User();
        validateName(name);
        validatePassword(password);
        validateEmail(email);
        validateUserType(userType);

        return user;
    }

    public static User restore(UUID id, String name, String email, String password, UserType userType) {
        validateName(name);
        validateEmail(email);
        validateUserType(userType);

        User user = new User();
        user.id = id;
        user.name = name;
        user.email = email;
        user.password = password;
        user.userType = userType;

        return user;
    }

//    public User(UUID id, String name, String email, String password,UserType userType){
//
//        validateName(name);
//        validateEmail(email);
//        validatePassword(password);
//        validateUserType(userType);
//
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.userType = userType;
//    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        validateUserType(userType);
        this.userType = userType;
    }
//
//    public void setId(UUID id) {
//        this.id = id;
//    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public void setEmail(String email) {
        validateEmail(email);
        this.email = email;
    }

    public void setPassword(String password) {
        validatePassword(password);
        this.password = password;
    }

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty.");

        }
    }

    private static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
    }

    private static void validateUserType(UserType userType) {
        if (userType == null) {
            throw new IllegalArgumentException("User type is required.");
        }
    }

}
