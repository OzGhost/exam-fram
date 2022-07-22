package ruf.exam.fram.repo;

import ruf.exam.fram.entity.Employee;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class EmployeeRepo implements PanacheRepositoryBase<Employee, String> {

    public List<Object[]> getAllWithSupervisor() {
        EntityManager em = getEntityManager();
        TypedQuery<Object[]> q = em.createNamedQuery(Employee.QRY_GET_ALL_WITH_SUPERVISOR, Object[].class);
        return q.getResultList();
    }
}

