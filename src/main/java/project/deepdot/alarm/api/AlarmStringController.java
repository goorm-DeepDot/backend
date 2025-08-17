package project.deepdot.alarm.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.deepdot.alarm.application.AlarmStringService;

import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/alarm-strings")
@RequiredArgsConstructor
public class AlarmStringController {
    private final AlarmStringService svc;

    @GetMapping
    public ResponseEntity<Map<String,String>> byType(
            @RequestParam String type, @RequestParam(required=false) String name, Locale locale) {
        return ResponseEntity.ok(svc.defaultByType(type, name, locale));
    }

    @PostMapping("/resolve")
    public ResponseEntity<Map<String,String>> resolve(@RequestBody Map<String,Object> body, Locale locale) {
        String titleCode = (String) body.get("titleCode");
        String bodyCode  = (String) body.get("bodyCode");
        @SuppressWarnings("unchecked")
        Map<String,String> params = (Map<String,String>) body.getOrDefault("params", Map.of());
        String name = params.getOrDefault("name", "");
        return ResponseEntity.ok(svc.resolve(titleCode, bodyCode, name, locale));
    }
}