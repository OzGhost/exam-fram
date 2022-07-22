package ruf.exam.fram.services;

import ruf.exam.fram.entity.Employee;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.function.BiFunction;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RelationService {

    public Map<String, Object> organize(List<Object[]> relations) {
        if (relations == null)
            return Collections.emptyMap();
        Map<String, EntryPack> marks = new HashMap<>();
        MergeValueLeak<EntryPack> leaker = new MergeValueLeak<>();
        for (Object[] r: relations) {
            if (r[1] == null)
                continue;
            EntryPack racket = new EntryPack();
            leaker.reset();
            String supervisor = (String)r[1];
            marks.merge(supervisor, racket, leaker);
            if (leaker.invoked)
                racket = leaker.val;
            EntryPack coil = new EntryPack();
            leaker.reset();
            String employee = (String)r[0];
            marks.merge(employee, coil, leaker);
            if (leaker.invoked)
                coil = leaker.val;
            racket.subs.put(employee, coil.subs);
            coil.isRoot = false;
        }
        String root = null;
        Map<String, Object> rsubs = null;
        for (Entry<String, EntryPack> e: marks.entrySet()) {
            EntryPack pack = e.getValue();
            if (pack.isRoot) {
                root = e.getKey();
                rsubs = pack.subs;
                break;
            }
        }
        Map<String, Object> out = new HashMap<>();
        out.put(root, rsubs);
        return out;
    }

    public List<Employee> toEntities(Map<String, String> relations) {
        if (relations == null)
            return Collections.emptyList();
        Map<String, LinkedNode> marks = new HashMap<>();
        MergeValueLeak<LinkedNode> leaker = new MergeValueLeak<>();
        for (Entry<String, String> e: relations.entrySet()) {
            String supervisorName = e.getValue();
            LinkedNode bossNode = new LinkedNode();
            bossNode.entity = new Employee().setName(supervisorName);
            leaker.reset();
            marks.merge(supervisorName, bossNode, leaker);
            if (leaker.invoked)
                bossNode = leaker.val;
            String employeeName = e.getKey();
            LinkedNode employeeNode = new LinkedNode();
            employeeNode.entity = new Employee().setName(employeeName);
            leaker.reset();
            marks.merge(employeeName, employeeNode, leaker);
            if (leaker.invoked)
                employeeNode = leaker.val;
            employeeNode.entity.setSupervisor(bossNode.entity);
            employeeNode.next = bossNode;
        }
        Iterable<LinkedNode> nodes = marks.values();
        crashOnCicularBossing(nodes);
        crashOnMultiRoot(nodes);
        List<Employee> out = new ArrayList<>(marks.size());
        for (LinkedNode n: nodes) {
            out.add(n.entity);
        }
        return out;
    }

    private void crashOnCicularBossing(Iterable<LinkedNode> nodes) {
        int tid = 1;
        for (LinkedNode n: nodes) {
            LinkedNode i = n;
            boolean enclosed = false;
            while (i != null) {
                if (i.tid == tid) {
                    enclosed = true;
                    break;
                }
                i.tid = tid;
                i = i.next;
            }
            if (enclosed)
                throw new BadRequestException( Response.status(400).entity("Found circle of supervisor in [" + printCircle(n, tid + 1) + "]").build() );
            tid++;
        }
    }

    private String printCircle(LinkedNode n, int tid) {
        StringBuilder sb = new StringBuilder();
        sb.append(n.entity.getName());
        n.tid = tid;
        LinkedNode i = n.next;
        while (i != null) {
            sb.append(" < ").append(i.entity.getName());
            if (i.tid == tid)
                break;
            i.tid = tid;
            i = i.next;
        }
        return sb.toString();
    }

    private void crashOnMultiRoot(Iterable<LinkedNode> nodes) {
        List<String> roots = new ArrayList<>();
        for (LinkedNode n: nodes) {
            if (n.next == null) {
                roots.add(n.entity.getName());
            }
        }
        if (roots.size() != 1)
            throw new BadRequestException( Response.status(400).entity("Expected single root but found " + roots.size() + " root includes: [" + String.join(",", roots) + "]").build() );
    }

    private static class MergeValueLeak<T> implements BiFunction<T, T, T> {
        private boolean invoked;
        private T val;

        @Override
        public T apply(T oldVal, T newVal) {
            invoked = true;
            val = oldVal;
            return oldVal;
        }

        public void reset() {
            invoked = false;
            val = null;
        }
    }

    private static class EntryPack {
        private boolean isRoot = true;
        private Map<String, Object> subs = new HashMap<>();
    }

    private static class LinkedNode {
        private int tid;
        private Employee entity;
        private LinkedNode next;
    }
}

