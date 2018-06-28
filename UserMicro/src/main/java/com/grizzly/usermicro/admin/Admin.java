package com.grizzly.usermicro.admin;

import com.grizzly.usermicro.user.User;

import javax.persistence.*;

@Entity(name="admin")
@Table(name = "admin")
public class Admin extends User {
    // For creating the variables

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")         private Integer userId;

    @AttributeOverrides({
            @AttributeOverride(name="name", column=@Column(name="name")),
            @AttributeOverride(name="contact_num", column=@Column(name="contact_num")),
            @AttributeOverride(name="email", column=@Column(name="email"))
    })

    @Column(name="job_position")    private String jobPosition;
    @Column(name="office")          private String office;

    public Admin() {
        super();
    }

    public Admin(String name, String contact_num, String email, String job_position, String office) {
        super(name, contact_num, email);

        setJobPosition(job_position);
        setOffice(office);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }
}
