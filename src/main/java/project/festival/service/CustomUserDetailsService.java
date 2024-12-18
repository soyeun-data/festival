package project.festival.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.festival.domain.LoginEntity;
import project.festival.domain.Member;
import project.festival.dto.CustomUserDetails;
import project.festival.repository.MemberRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(()->new UsernameNotFoundException("User not found with loginId: " + loginId));

        LoginEntity loginEntity = new LoginEntity();
        loginEntity.setLoginId(member.getLoginId());
        loginEntity.setPassword(member.getPassword());

        return new CustomUserDetails(loginEntity);
    }
}
