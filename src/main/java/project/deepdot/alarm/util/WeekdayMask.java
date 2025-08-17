package project.deepdot.alarm.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class WeekdayMask {
    private WeekdayMask() {}

    // 1=월..7=일 → bit 0..6
    public static int toMask(List<Integer> weekdays) {
        int mask = 0;
        if (weekdays == null) return 0;
        for (Integer w : new HashSet<>(weekdays)) { // 중복 제거
            if (w < 1 || w > 7) throw new IllegalArgumentException("weekday 1..7");
            mask |= (1 << (w - 1));
        }
        return mask;
    }

    public static List<Integer> toList(int mask) {
        List<Integer> res = new ArrayList<>();
        for (int bit = 0; bit < 7; bit++) {
            if ((mask & (1 << bit)) != 0) res.add(bit + 1);
        }
        return res;
    }
}