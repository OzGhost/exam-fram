package ruf.exam.fram;

import ruf.exam.fram.services.RelationService;
import ruf.exam.fram.repo.EmployeeRepo;
import ruf.exam.fram.entity.Employee;
import ruf.exam.fram.service.Guard;

import java.util.Map;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.Valid;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Context;
import javax.transaction.Transactional;

@Path("/relations")
public class RelationshipAPI {

    @Inject RelationService relationSv;
    @Inject EmployeeRepo employeeRepo;
    @Inject Guard guard;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response importRelations(@Context HttpHeaders headers, @NotNull @Valid Map<String, String> rq) {
        guard.check(headers);
        employeeRepo.deleteAll();
        List<Employee> employees = relationSv.toEntities(rq);
        employeeRepo.persist(employees);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchHierarchy(@Context HttpHeaders headers) {
        guard.check(headers);
        List<Object[]> relations = employeeRepo.getAllWithSupervisor();
        Map<String, Object> hierarchy = relationSv.organize(relations);
        return Response.ok().entity(hierarchy).build();
    }
}

