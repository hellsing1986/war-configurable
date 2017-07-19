package vn.vnptnet.archetype.warconfigurable.rest;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.skife.jdbi.v2.Handle;
import vn.vnptnet.archetype.warconfigurable.App;
import vn.vnptnet.archetype.warconfigurable.db.DatabaseFactory;
import vn.vnptnet.archetype.warconfigurable.mapper.UniversalJsonMapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by User on 6/23/2017.
 */
@Path("echo")
public class EchoRest {
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response get() throws ConfigurationException {
        String abc = App.getBuilder().getConfiguration().getString("abc");
        return Response.ok("{\"success\":true, \"abc\":\""+abc+"\"}").build();
    }

    @GET
    @Path("/{dbName}/{tableName}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response get(@PathParam("dbName") String dbName, @PathParam("tableName") String tableName){
        Handle h = DatabaseFactory.getInstance().openHandler(dbName);
        try {
            JSONArray ja = new JSONArray();
            h.createQuery("select * from "+tableName)
                    .map(new UniversalJsonMapper())
                    .list().stream()
                    .forEach(t->{
                        ja.put(t);
                    });
            return Response.ok(ja.toString()).build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(500).entity(new JSONObject(){{
                put("message", e.getMessage());
            }}.toString()).build();
        } finally {
            h.close();
        }
    }
}
