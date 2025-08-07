package project.deepdot.mainpage.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.deepdot.mainpage.api.MainPageResponse;
import project.deepdot.schedule.domain.ScheduleRepository;
import project.deepdot.schedule.domain.scheduleEnum.ScheduleType;
import project.deepdot.user.domain.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final ScheduleRepository scheduleRepository;

    public List<MainPageResponse> findByType(User user, ScheduleType type) {
        return scheduleRepository.findAllByUserAndTypeOrderByTime(user, type)
                .stream()
                .map(MainPageResponse::from)
                .toList();
    }
}