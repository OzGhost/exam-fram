package ruf.exam.fram.entity;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(
        name = "qry-get-all-with-supervisor",
        query = "SELECT e.name, e.supervisor.name FROM Employee e"
    )
})
public class Employee {

    public static final String QRY_GET_ALL_WITH_SUPERVISOR = "qry-get-all-with-supervisor";
    
    @Id
    private String name;
    @OneToOne
    @JoinColumn(name = "supervisor")
    private Employee supervisor;

    public String getName() { return name; }
    public Employee setName(String name) { this.name = name; return this; }
    public Employee getSupervisor() { return supervisor; }
    public Employee setSupervisor(Employee supervisor) { this.supervisor = supervisor; return this; }
}


