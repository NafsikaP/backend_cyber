package gr.hua.dit.aimodotes.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class BloodTest {

    //primary key
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    //attributes/columns
    @Column
    private LocalDate date;

    @Column
    private String bloodtype;
    @Column
    private String details;

    //entity relationships
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="appform_id")
    private AppForm appForm;

    //setters-getters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getBloodtype() {
        return bloodtype;
    }

    public void setBloodtype(String bloodtype) {
        this.bloodtype = bloodtype;
    }

    public AppForm getAppForm() {
        return appForm;
    }

    public void setAppForm(AppForm appForm) {
        this.appForm = appForm;
    }

    //constructors
    public BloodTest(LocalDate date, String details, String bloodtype) {
        this.date = date;
        this.details = details;
        this.bloodtype = bloodtype;
    }

    public BloodTest() {
    }
}
