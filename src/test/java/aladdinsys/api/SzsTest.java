package aladdinsys.api;

import aladdinsys.api.dto.LoginDTO;
import aladdinsys.api.dto.MemberDTO;
import aladdinsys.api.entity.MemberEntity;
import aladdinsys.api.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SzsTest {
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 테스트")
    public void testSignUp() {
        // given
        MemberDTO dto = new MemberDTO("kim", "1111", "김둘리", "921108-1582816");

        // when
        MemberEntity result = memberService.signUp(dto);

        // then
        assertNotNull(result);
        System.out.println("회원가입이 정상적으로 이루어졌습니다." + result);
    }

    @Test
    @DisplayName("회원가입 테스트 - 아이디 중복")
    public void testSignUpDuplicated() {
        // given
        MemberDTO dto = new MemberDTO("kim", "1111", "김둘리", "921108-1582816");

        // when
        try {
            memberService.signUp(dto);
        } catch (Exception e) {
            System.out.println("아이디 중복");
        }
        // then
    }

    @Test
    @DisplayName("회원가입 테스트 - 조건 불일치")
    public void testSignUpInvalid() {
        // given
        MemberDTO dto = new MemberDTO("park", "1111", "박둘리", "921108-1582816");

        // when
        try {
            memberService.signUp(dto);
        } catch (Exception e) {
            System.out.println("조건 불일치");
        }
        // then
    }

    @Test
    public void testLogin() {
        // given
        LoginDTO dto = new LoginDTO("kim", "11111");

        // when
        String result = memberService.login(dto.userId(), dto.password());

        // then
        assertNotNull(result);
        System.out.println("토큰 발급 : " + result);
    }

    @Test
    public void testGetInfo() {
        // given
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJraW0iLCJpYXQiOjE3MDYxNDIyODksImV4cCI6MTcwNjIyODY4OX0.80TrsHfqcps3rOP5sqg4favmpJ27A9qZvouX-dqaUsXQngfCU0GMUTNQ7yYsSCR6b5J8QkrCd_44qrNwdBxFCg";

        // when
        MemberDTO result = memberService.getInfo(token);

        // then
        assertNotNull(result);
        System.out.println("회원조회 : " + result);
    }
}
