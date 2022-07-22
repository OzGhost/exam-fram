package ruf.exam.fram.services;

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
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Hierarchy {

    public Map<String, Object> organize(Map<String, String> relation) {
        if (relation == null)
            return Collections.emptyMap();
        Map<String, EntryPack> marks = new HashMap<>();
        for (Entry<String, String> e: relation.entrySet()) {
            String employee = e.getKey();
            String supervisor = e.getValue();
            EntryPack racket = new EntryPack();
            MergeValueLeak leaker = new MergeValueLeak();
            marks.merge(supervisor, racket, leaker);
            if (leaker.invoked)
                racket = leaker.val;
            EntryPack coil = new EntryPack();
            leaker.reset();
            marks.merge(employee, coil, leaker);
            if (leaker.invoked)
                coil = leaker.val;
            racket.subs.put(employee, coil.subs);
            coil.isRoot = false;
        }
        String root = null;
        Map<String, Object> rsubs = null;
        List<String> noSupers = new ArrayList<>();
        for (Entry<String, EntryPack> e: marks.entrySet()) {
            EntryPack pack = e.getValue();
            if (pack.isRoot) {
                root = e.getKey();
                rsubs = pack.subs;
                noSupers.add(root);
            }
        }
        if (noSupers.size() != 1)
            throw new BadRequestException("Expected single root hierarchy but found <" + noSupers.size() + "> root which includes:[" + String.join(",", noSupers) + "]");
        Map<String, Object> out = new HashMap<>();
        out.put(root, rsubs);
        return out;
    }

    private static class MergeValueLeak implements BiFunction<EntryPack, EntryPack, EntryPack> {
        private boolean invoked;
        private EntryPack val;
        @Override
        public EntryPack apply(EntryPack oldVal, EntryPack newVal) {
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
}

