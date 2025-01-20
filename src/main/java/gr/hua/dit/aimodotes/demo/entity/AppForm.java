package gr.hua.dit.aimodotes.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
public class AppForm {

    //primary key
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    //attributes/columns
    public enum Status {ACCEPTED, PENDING};
    @Column
    private Status status;

    @Column
    private LocalDate appDate;

    //entity relationships
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="blood_test_id")
    private BloodTest bloodTest;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="aimodotis_id")
    private Aimodotis aimodotis;

    @ManyToOne(cascade = {//CascadeType.DETACH,
            CascadeType.REFRESH,
            //CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinColumn(name = "secretary_id")
    private Secretary secretary;

    //setters-getters
    public Aimodotis getAimodotis() {
        return aimodotis;
    }

    public void setAimodotis(Aimodotis aimodotis) {
        this.aimodotis = aimodotis;
    }

    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getAppDate() {
        return appDate;
    }

    public void setAppDate(LocalDate appDate) {
        this.appDate = appDate;
    }

    public BloodTest getBloodTest() {
        return bloodTest;
    }

    public void setBloodTest(BloodTest bloodTest) {
        this.bloodTest = bloodTest;
    }

    public Secretary getSecretary() {
        return secretary;
    }

    public void setSecretary(Secretary secretary) {
        this.secretary = secretary;
    }

    //constructors
    public AppForm(Status status, LocalDate appDate) {
        this.status = status;
        this.appDate = appDate;
    }

    public AppForm() {
    }
}
