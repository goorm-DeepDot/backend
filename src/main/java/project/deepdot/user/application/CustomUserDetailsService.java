package project.deepdot.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.deepdot.user.domain.User;
import project.deepdot.user.domain.UserPrincipal;
import project.deepdot.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /** 기존 스프링 시큐리티 인터페이스 구현 (다른 곳에서 쓸 수도 있으니 남겨둠) */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    /** 우리가 실제로 사용할 id 기반 로딩 */
    public UserDetails loadUserById(Long id) {
        return userRepository.findById(id)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}