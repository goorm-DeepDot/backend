package project.deepdot.alarm.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.deepdot.alarm.api.dto.request.AlarmSyncItem;
import project.deepdot.alarm.api.dto.request.SingleAlarmUpsertRequest;
import project.deepdot.alarm.api.dto.request.WeeklyAlarmUpsertRequest;
import project.deepdot.alarm.api.dto.response.SingleAlarmResponse;
import project.deepdot.alarm.api.dto.response.WeeklyAlarmResponse;
import project.deepdot.alarm.application.AlarmSyncService;
import project.deepdot.alarm.application.SingleAlarmService;
import project.deepdot.alarm.application.WeeklyAlarmService;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final SingleAlarmService singleService;
    private final WeeklyAlarmService weeklyService;
    private final AlarmSyncService syncService;

    private Long uid(Authentication auth) {
        return Long.parseLong(auth.getName());
    }

    // ---- Single ----
    @PostMapping("/single")
    public ResponseEntity<SingleAlarmResponse> upsertSingle(
            @AuthenticationPrincipal UserPrincipal principal, @Valid @RequestBody SingleAlarmUpsertRequest req) {
        User user = principal.getUser();
        Long userId=user.getUserId();
        return ResponseEntity.ok(singleService.upsert(userId, req));
    }


//    //필요없는 코드
//    @GetMapping("/single")
//    public ResponseEntity<List<SingleAlarmResponse>> listSingle(Authentication auth) {
//        return ResponseEntity.ok(singleService.list(uid(auth)));
//    }

    // ✅ [FIX] @AuthenticationPrincipal 로 변경
    @GetMapping("/single")
    public ResponseEntity<List<SingleAlarmResponse>> listSingle(@AuthenticationPrincipal UserPrincipal principal) { // [FIX]
        Long userId = principal.getUser().getUserId(); // [FIX]
        return ResponseEntity.ok(singleService.list(userId));
    }

    @DeleteMapping("/single/{notificationId}")
    public ResponseEntity<Void> deleteSingle(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Integer notificationId) {
        User user = principal.getUser();
        Long userId=user.getUserId();
        singleService.delete(userId, notificationId);
        return ResponseEntity.noContent().build();
    }

    // ---- Weekly ----
    @PostMapping("/weekly")
    public ResponseEntity<WeeklyAlarmResponse> upsertWeekly(
            @AuthenticationPrincipal UserPrincipal principal, @Valid @RequestBody WeeklyAlarmUpsertRequest req) {
        User user = principal.getUser();
        Long userId=user.getUserId();
        return ResponseEntity.ok(weeklyService.upsert(userId, req));
    }


//    //필요없는 코드
//    @GetMapping("/weekly")
//    public ResponseEntity<List<WeeklyAlarmResponse>> listWeekly(Authentication auth) {
//        return ResponseEntity.ok(weeklyService.list(uid(auth)));
//    }

    // ✅ [FIX] @AuthenticationPrincipal 로 변경
    @GetMapping("/weekly")
    public ResponseEntity<List<WeeklyAlarmResponse>> listWeekly(@AuthenticationPrincipal UserPrincipal principal) { // [FIX]
        Long userId = principal.getUser().getUserId(); // [FIX]
        return ResponseEntity.ok(weeklyService.list(userId));
    }

    @DeleteMapping("/weekly/{baseId}")
    public ResponseEntity<Void> deleteWeekly(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Integer baseId) {
        User user = principal.getUser();
        Long userId=user.getUserId();
        weeklyService.delete(userId, baseId);
        return ResponseEntity.noContent().build();
    }

//    // ---- Sync all ----
//    @GetMapping("/all")
//    public ResponseEntity<List<AlarmSyncItem>> all(Authentication auth) {
//        return ResponseEntity.ok(syncService.all(uid(auth)));
//    }

    // ---- Sync all ----
    // ✅ [FIX] @AuthenticationPrincipal 로 변경
    @GetMapping("/all")
    public ResponseEntity<List<AlarmSyncItem>> all(@AuthenticationPrincipal UserPrincipal principal) { // [FIX]
        Long userId = principal.getUser().getUserId(); // [FIX]
        return ResponseEntity.ok(syncService.all(userId));
    }
}
