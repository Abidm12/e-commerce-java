package model;

public class User {

        private int id;
        private String name;
        private String email;
        private String password;
        private Role role;

        public User() {
        }

        // Registration constructor (default USER)
        public User(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.role = Role.USER;
        }


        // Constructor with role
        public User(int id, String name, String email, String password, Role role) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.password = password;
            this.role = role;
        }



    // Getters
        public int getId() {
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

        public Role getRole() {
            return role;
        }



        // Setters
        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPassword(String password){
            this.password = password;
        }

        public void setRole(Role role) {
            this.role = role;
        }
}


