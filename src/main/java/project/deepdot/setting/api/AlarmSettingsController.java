package project.deepdot.setting.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.deepdot.setting.api.dto.AlarmSettingsResponse;
import project.deepdot.setting.api.dto.AlarmSettingsUpdateRequest;
import project.deepdot.setting.application.AlarmSettingsService;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.UserPrincipal;

@RestController
@RequestMapping("/api/alarms/settings")
@RequiredArgsConstructor
public class AlarmSettingsController {

    private final AlarmSettingsService service; // [NEW]

    @GetMapping
    public ResponseEntity<AlarmSettingsResponse> get(@AuthenticationPrincipal UserPrincipal principal) {
        User user = principal.getUser();
        return ResponseEntity.ok(service.get(user.getUserId()));
    }

    @PatchMapping
    public ResponseEntity<AlarmSettingsResponse> patch(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AlarmSettingsUpdateRequest req) {
        User user = principal.getUser();
        return ResponseEntity.ok(service.update(user.getUserId(), req));
    }
}