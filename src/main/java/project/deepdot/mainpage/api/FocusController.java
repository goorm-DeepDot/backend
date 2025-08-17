package project.deepdot.mainpage.api;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.deepdot.mainpage.api.dto.FocusDailyResponse;
import project.deepdot.mainpage.api.dto.FocusDailyUpsertRequest;
import project.deepdot.mainpage.api.dto.FocusWeekResponse;
import project.deepdot.mainpage.application.FocusService;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.UserPrincipal;

@RestController
@RequestMapping("/api/focus")
@RequiredArgsConstructor
public class FocusController {

    private final FocusService service;

    /** 하루치 저장(업서트) */
    @PostMapping("/daily")
    public ResponseEntity<FocusDailyResponse> upsertDaily(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody FocusDailyUpsertRequest req) {
        User user = principal.getUser();
        return ResponseEntity.ok(service.upsertDaily(user.getUserId(), req));
    }

    /** 주간 집계 (anchorDate 기준, 월~일) */
    @GetMapping("/weekly")
    public ResponseEntity<FocusWeekResponse> weekly(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String anchorDate,   // yyyy-MM-dd, 기본=오늘(로컬)
            @RequestParam(required = false) String zoneId        // 기본 Asia/Seoul
    ) {
        User user = principal.getUser();
        return ResponseEntity.ok(service.weekly(user.getUserId(), anchorDate, zoneId));
    }
}