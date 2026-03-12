package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KnownDisplays {
    private final List<DisplayTarget> displays = new ArrayList<>();

    public void add(DisplayTarget d) {
        // keep duplicates out (optional but nice)
        for (DisplayTarget x : displays) {
            if (x.ip().equals(d.ip()) && x.port() == d.port()) return;
        }
        displays.add(d);
    }

    public List<DisplayTarget> all() {
        return Collections.unmodifiableList(displays);
    }

    public int size() { return displays.size(); }
}
