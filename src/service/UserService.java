package service;

import dao.UserDAO;
import model.User;

public class UserService {

    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public boolean registerUser(String name, String email, String password) {

        if (name == null || name.isBlank()) {
            System.out.println("Name cannot be empty.");
            return false;
        }

        if (!isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return false;
        }

        if (password == null || password.length() < 4) {
            System.out.println("Password must be at least 4 characters.");
            return false;
        }

        User existingUser = userDAO.findByEmail(email);
        if (existingUser != null) {
            System.out.println("Email already registered.");
            return false;
        }

        User newUser = new User(name, email, password);
        return userDAO.registerUser(newUser);
    }


    // Login User
    public User loginUser(String email, String password) {

        if (email == null || email.isBlank()) {
            System.out.println("Email cannot be empty.");
            return null;
        }

        if (password == null || password.isBlank()) {
            System.out.println("Password cannot be empty.");
            return null;
        }

        return userDAO.loginUser(email, password);
    }

    private boolean isValidEmail(String email) {

        if (email == null || email.isBlank()) {
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        return email.matches(emailRegex);
    }

}
