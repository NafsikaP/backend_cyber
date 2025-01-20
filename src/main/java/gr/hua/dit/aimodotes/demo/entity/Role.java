package gr.hua.dit.aimodotes.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    //primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20)
    private String name;

    //constructor
    public Role() {

    }

    //setters-getters
    public Role(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
