package kr.co.pinpick.common.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ListExtension {
    public static <T> List<List<T>> chunk(List<T> self, int size) {
        List<List<T>> result = new ArrayList<>();

        for (int x = 0; x < self.size(); x += size) {
            result.add(self.subList(x, Math.min(x + size, self.size())));
        }

        return result;
    }
    public static <T> T removeFirst(List<T> self, Predicate<T> find) {
        var index = indexOf(self, find);

        if (index < 0) {
            return null;
        }

        return self.remove(index);
    }

    public static <T> int indexOf(List<T> self, Predicate<T> find) {
        for (var i = 0; i < self.size(); i ++) {
            if (find.test(self.get(i))) {
                return i;
            }
        }
        return -1;
    }
}
