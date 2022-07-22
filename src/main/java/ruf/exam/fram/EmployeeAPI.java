package ruf.exam.fram;

import ruf.exam.fram.repo.EmployeeRepo;
import ruf.exam.fram.entity.Employee;
import ruf.exam.fram.service.Guard;

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

@Path("/employees")
public class EmployeeAPI {

    @Inject EmployeeRepo repo;
    @Inject Guard guard;

    @GET
    @Path("/{name}/supervisors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSupervisors(@Context HttpHeaders headers, @PathParam("name") String name) {
        guard.check(headers);
        Employee e = repo.findById(name);
        if (e == null)
            return Response.status(404).build();
        SupervisorsResponse rs = new SupervisorsResponse();
        e = e.getSupervisor();
        if (e != null) {
            rs.setSupervisor(e.getName());
            e = e.getSupervisor();
            if (e != null)
                rs.setSupervisorOfSupervisor(e.getName());
        }
        return Response.ok().entity(rs).build();
    }

    private static class SupervisorsResponse {
        private String supervisor;
        private String supervisorOfSupervisor;

        public String getSupervisor() { return supervisor; }
        public void setSupervisor(String supervisor) { this.supervisor = supervisor; }
        public String getSupervisorOfSupervisor() { return supervisorOfSupervisor; }
        public void setSupervisorOfSupervisor(String supervisorOfSupervisor) { this.supervisorOfSupervisor = supervisorOfSupervisor; }
    }
}


