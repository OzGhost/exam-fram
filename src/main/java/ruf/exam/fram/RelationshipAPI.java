package ruf.exam.fram;

import ruf.exam.fram.services.Hierarchy;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.Valid;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/relations")
public class RelationshipAPI {

    @Inject Hierarchy hier;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getToken(@NotNull @Valid Map<String, String> rq) {
        return Response.ok().entity(hier.organize(rq)).build();
    }
}

