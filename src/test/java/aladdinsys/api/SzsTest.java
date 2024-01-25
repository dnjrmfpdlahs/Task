package aladdinsys.api;

import aladdinsys.api.dto.LoginDTO;
import aladdinsys.api.dto.MemberDTO;
import aladdinsys.api.entity.MemberEntity;
import aladdinsys.api.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SzsTest {
    @Autowired
    private MemberService memberService;

    @Test
    public void testSignUp() {
        // given
        MemberDTO dto = new MemberDTO("kimm", "1111", "김둘리", "921108-1582816");

        // when
        MemberEntity result = memberService.signUp(dto);

        // then
        assertNotNull(result);
        System.out.println("회원가입이 정상적으로 이루어졌습니다." + result);
    }

    @Test
    public void testLogin() {
        // given
        LoginDTO dto = new LoginDTO("kimm", "1111");

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
