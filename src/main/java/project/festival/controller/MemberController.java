package project.festival.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.festival.domain.Member;
import project.festival.service.MemberService;

@RestController
@RequiredArgsConstructor //생성자, @Autowired를 따로 생성하지 않아도 됨
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @PostMapping("/signup")
    public ResponseEntity<Member> create(@RequestBody Member member) {
        Member savedMember = memberService.join(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
    }

    @DeleteMapping("/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long memberId) {
        memberService.delete(memberId);
    }

    //    // 프로필 변경
    @PutMapping("/{memberId}")
    public ResponseEntity<Member> update(@RequestBody Member member, @PathVariable Long memberId) {
        Member updatedMember = memberService.update(member, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMember);
    }

//
//    // 비밀번호 변경
//    @PutMapping("/pw")

    @GetMapping("/member")
    @ResponseBody
    public String member(){
        return "member Controller";
    }

    @GetMapping("/")
    @ResponseBody
    public String mainP() {
        //세션 현재 사용자 이름
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return "Main Controller" + name;
    }



    //토큰을 발급하는 과정에서 LoginFilter가 /login 경로에서 필터가 동작
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData, HttpServletResponse response) {
//        Member loginMember = memberService.login(loginData.get("loginId"), loginData.get("password"));
//        if (loginMember == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 잘못 되었습니다.");
//        } else {
//            return ResponseEntity.ok(loginMember);
//        }
//    }

}
