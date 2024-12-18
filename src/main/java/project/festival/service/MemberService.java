package project.festival.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.festival.domain.Member;
import project.festival.repository.MemberRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public Member join(Member member) {
        // 같은 이름이 있는 중복 회원의 경우 회원가입되지 않게 처리
        validateDuplicateMember(member);

        // 비밀번호 암호화
        String password = member.getPassword();
        String hashedPassword = passwordEncoder.encode(password);
        member.setPassword(hashedPassword);
//        log.debug("hashed password={}", hashedPassword);

        return memberRepository.save(member);
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    public Member update(Member member, Long id) {
        Member originMember = memberRepository.findById(id);

        if (member.getLoginId() != null) {
            validateDuplicateMember(member);
            originMember.setLoginId(member.getLoginId());
        }
        if (member.getName() != null) {
            originMember.setName(member.getName());
        }

        if (member.getPhoneNumber() != null) {
            originMember.setPhoneNumber(member.getPhoneNumber());
        }

        return memberRepository.updateById(originMember);
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByLoginId(member.getLoginId())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }


// 토큰 발급을 위해 LoginFilter를 생성하면서 사용할 일이 사라짐
//    public Member login(String loginId, String password) {
//        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
//
//        if (findMemberOptional.isPresent()) {
//            return memberRepository.findByLoginId(loginId)
//                    .filter(m -> passwordEncoder.matches(password, m.getPassword()))
//                    .orElse(null);
//        } else{
//            return null;
//        }
}
