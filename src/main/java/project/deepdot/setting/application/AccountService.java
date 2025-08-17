package project.deepdot.setting.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.deepdot.alarm.domain.MedicationAlarmRepository;
import project.deepdot.alarm.domain.SingleAlarmRepository;
import project.deepdot.alarm.domain.WeeklyAlarmRepository;
import project.deepdot.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepo;
    private final SingleAlarmRepository singleAlarmRepo;
     private final WeeklyAlarmRepository weeklyAlarmRepo; // 있으면 주입
    private final MedicationAlarmRepository medicationAlarmRepo;
    private final TokenBlacklistService blacklist;

    @Transactional
    public void deleteAccount(Long userId, String authHeader) {
        // 1) 연관 데이터 먼저 삭제
        singleAlarmRepo.deleteByUserId(userId);
        weeklyAlarmRepo.deleteByUserId(userId); // 있으면
        medicationAlarmRepo.deleteByUserId(userId); // orphanRemoval로 하위 time들도 제거

        // 2) 유저 삭제
        userRepo.deleteById(userId);

        // 3) 현재 액세스 토큰 블랙리스트(선택)
        String token = extractBearer(authHeader);
        if (token != null) {
            blacklist.blacklist(token, 900);
        }
    }

    private String extractBearer(String header) {
        if (header == null) return null;
        if (header.startsWith("Bearer ")) return header.substring(7);
        return null;
    }
}
