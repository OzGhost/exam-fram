package ruf.exam.fram.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

public class HierarchyTest {

    @Test
    public void test_organize_simple_tructure() {
        Hierarchy h = new Hierarchy();
        Map<String, String> inp = new HashMap<>();
        inp.put("Pete", "Nick");
        inp.put("Barbara", "Nick");
        inp.put("Nick", "Sophie");
        inp.put("Sophie", "Jonas");
        // Method under test
        Map<String, Object> out = h.organize(inp);
        printk(out);
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
    private void printk(Map<String, Object> m) {
        for (String s: m.keySet()) {
            System.out.println(s);
        }
    }
}

