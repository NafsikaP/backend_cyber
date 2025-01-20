package gr.hua.dit.aimodotes.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Aimodotis {

    //primary key
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    //attributes/columns
    @Column
    private String fname;

    @Column
    private String lname;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String AMKA;

    @Column
    private Character sex;

    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate last_donation;

    @Column
    private Integer age;

    @Column
    private String location;

    //entity relationships
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="app_form_id")
    private AppForm appForm;

    @JsonIgnore
    @ManyToMany(cascade = {//CascadeType.DETACH,
            CascadeType.MERGE,
            //CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinTable(
            name = "donationRequest_aimodotis",
            joinColumns = @JoinColumn(name = "aimodotis_id"),
            inverseJoinColumns = @JoinColumn(name = "donation_request_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"aimodotis_id","donation_request_id"})}
    )
    private List<DonationRequest> donationRequests;

    //setters-getters
    public List<DonationRequest> getDonationRequests() {
        return donationRequests;
    }

    public void setDonationRequests(List<DonationRequest> donationRequests) {
        this.donationRequests = donationRequests;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAMKA() {
        return AMKA;
    }

    public void setAMKA(String AMKA) {
        this.AMKA = AMKA;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    public LocalDate getLast_donation() {
        return last_donation;
    }

    public void setLast_donation(LocalDate last_donation) {
        this.last_donation = last_donation;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public AppForm getAppForm() {
        return appForm;
    }

    public void setAppForm(AppForm appForm) {
        this.appForm = appForm;
    }


    @Override
    public String toString() {
        return "Aimodotis{" +
                "id=" + id +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", AMKA='" + AMKA + '\'' +
                ", sex=" + sex +
                ", last_donation=" + last_donation +
                ", age=" + age +
                ", location='" + location + '\'' +
                ", appForm=" + appForm +
                ", donationRequests=" + donationRequests +
                '}';
    }

    //constructors
    public Aimodotis(String fname, String lname, String email, String phone, String AMKA, Character sex, LocalDate last_donation, Integer age, String location) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
        this.AMKA = AMKA;
        this.sex = sex;
        this.last_donation = last_donation;
        this.age = age;
        this.location = location;
    }
    public Aimodotis() {
    }


}
