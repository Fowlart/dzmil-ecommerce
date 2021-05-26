package main.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "clients")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Client implements Serializable {

    @Id
    //  @GeneratedValue(strategy = GenerationType.IDENTITY)//??
    @Column(name = "client_id")
    private Integer id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Transient
    private String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Client() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return id == client.id && name.equals(client.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
