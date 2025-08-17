package project.deepdot.alarm.application;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlarmStringService {
    private final MessageSource ms;

    public Map<String, String> defaultByType(String type, String name, Locale locale) {
        String titleCode, bodyCode;
        switch (type) {
            case "routine" -> {
                titleCode = "alarm.routine.title";
                bodyCode = "alarm.routine.body";
            }
            case "taking" -> {
                titleCode = "alarm.taking.title";
                bodyCode = "alarm.taking.body";
            }
            case "task" -> {
                titleCode = "alarm.task.title";
                bodyCode = "alarm.task.body";
            }
            default -> throw new IllegalArgumentException("unknown type");
        }
        //String title = ms.getMessage(titleCode, new Object[]{name == null ? "" : name}, titleCode, locale);
        String cleanName = (name == null ? "" : name.replaceAll("\\s+", " ").trim());
        String title = ms.getMessage(titleCode, new Object[]{cleanName}, titleCode, locale);
        String body = ms.getMessage(bodyCode, new Object[]{name == null ? "" : name}, bodyCode, locale);
        return Map.of("title", title, "body", body);
    }

    public Map<String, String> resolve(String titleCode, String bodyCode, String name, Locale locale) {
        String title = ms.getMessage(titleCode, new Object[]{name == null ? "" : name}, titleCode, locale);
        String body = ms.getMessage(bodyCode, new Object[]{name == null ? "" : name}, bodyCode, locale);
        return Map.of("title", title, "body", body);
    }
}
