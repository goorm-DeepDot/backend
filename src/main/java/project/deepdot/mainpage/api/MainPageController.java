package project.deepdot.mainpage.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.deepdot.mainpage.application.MainPageService;
import project.deepdot.schedule.domain.scheduleEnum.ScheduleType;
import project.deepdot.user.domain.User;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mainpage")
public class MainPageController {

    private final MainPageService mainPageService;

    @GetMapping("/type/{type}")
    public ResponseEntity<List<MainPageResponse>> getByType(@PathVariable ScheduleType type,
                                                            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(mainPageService.findByType(user, type));
    }
}