package ruf.exam.fram.services;

import static org.junit.jupiter.api.Assertions.*;

import ruf.exam.fram.entity.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import javax.ws.rs.BadRequestException;

public class RelationServiceTest {

    @Test
    public void test_organize_simple_tructure() {
        List<Object[]> inp = new ArrayList<>();
        inp.add(new Object[]{"Pete", "Nick"});
        inp.add(new Object[]{"Barbara", "Nick"});
        inp.add(new Object[]{"Nick", "Sophie"});
        inp.add(new Object[]{"Sophie", "Jonas"});
        RelationService sv = new RelationService();
        // Method under test
        Map<String, Object> out = sv.organize(inp);
        assertEquals(1, out.size());
        Map<String, Object> buf = (Map<String, Object>)out.get("Jonas");
        assertEquals(1, buf.size());
        buf = (Map<String, Object>)buf.get("Sophie");
        assertEquals(1, buf.size());
        buf = (Map<String, Object>)buf.get("Nick");
        assertEquals(2, buf.size());
        Map<String, Object> b1 = (Map<String, Object>)buf.get("Pete");
        assertTrue(b1.isEmpty());
        Map<String, Object> b2 = (Map<String, Object>)buf.get("Barbara");
        assertTrue(b2.isEmpty());
    }

    @Test
    public void test_toEntities_when_take_good_input_then_return_list_of_entity() {
        Map<String, String> inp = new HashMap<>();
        inp.put("Pete", "Nick");
        inp.put("Barbara", "Nick");
        inp.put("Nick", "Sophie");
        inp.put("Sophie", "Jonas");
        RelationService sv = new RelationService();
        // Method under test
        List<Employee> out = sv.toEntities(inp);
        assertEquals(5, out.size());
        for (Employee e: out) {
            switch(e.getName()) {
                case "Pete":
                    assertEquals("Nick", e.getSupervisor().getName()); break;
                case "Barbara":
                    assertEquals("Nick", e.getSupervisor().getName()); break;
                case "Nick":
                    assertEquals("Sophie", e.getSupervisor().getName()); break;
                case "Sophie":
                    assertEquals("Jonas", e.getSupervisor().getName()); break;
                case "Jonas":
                    assertNull(e.getSupervisor()); break;
                default:
                    fail("Unexpected name <"+e.getName()+">");
            }
        }
    }

    @Test
    public void test_toEntities_when_input_have_circle_then_die() throws Exception {
        Map<String, String> inp = new HashMap<>();
        inp.put("x", "y");
        inp.put("a", "b");
        inp.put("b", "c");
        inp.put("c", "a");
        BadRequestException ex = assertThrows(BadRequestException.class, () -> new RelationService().toEntities(inp));
        assertEquals("Found circle of supervisor in [a < b < c < a]", ex.getResponse().readEntity(String.class));
    }

    @Test
    public void test_toEntities_when_input_have_multi_root_then_die() throws Exception {
        Map<String, String> inp = new HashMap<>();
        inp.put("x", "y");
        inp.put("a", "b");
        BadRequestException ex = assertThrows(BadRequestException.class, () -> new RelationService().toEntities(inp));
        assertEquals("Expected single root but found 2 root includes: [b,y]", ex.getResponse().readEntity(String.class));
    }
}

