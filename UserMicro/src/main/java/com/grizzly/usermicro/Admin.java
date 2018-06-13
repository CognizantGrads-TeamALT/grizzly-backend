package com.grizzly.usermicro;

import javax.persistence.*;

@Entity(name="admin")
public class Admin extends User {
    // For creating the variables

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")      private String userId;

    @Column(name="position")     private String position;
    @Column(name="office")       private String office;


    public Admin() {
        super();
    }

    public Admin(String position, String office) {
        super();

        setPosition(position);
        setOffice(office);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }
}
