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
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        // when & then
        assertThrows(SecurityException.class, () -> {
            memberService.signUp(dto);
        }, "아이디 중복");
    }

    @Test
    @DisplayName("회원가입 테스트 - 조건 불일치")
    public void testSignUpInvalid() {
        // given
        MemberDTO dto = new MemberDTO("park", "1111", "박둘리", "921108-1582816");

        // when & then
        assertThrows(SecurityException.class, () -> {
            memberService.signUp(dto);
        }, "회원가입 조건 불일치");
    }

    @Test
    public void testLoginSuccess() {
        // given
        LoginDTO dto = new LoginDTO("kim", "1111");

        // when
        String result = memberService.login(dto.userId(), dto.password());

        // then
        assertNotNull(result);
        System.out.println("토큰 발급 : " + result);
    }

    @Test
    public void testLoginFail() {
        // given
        LoginDTO dto = new LoginDTO("kim", "11111");
        // when & then
        assertThrows(SecurityException.class, () -> {
            memberService.login(dto.userId(), dto.password());
        }, "로그인 실패: 유효하지 않은 아이디 또는 비밀번호");
    }

    @Test
    public void testGetInfoSuccess() {
        // given
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb24iLCJpYXQiOjE3MDYxNjY3NzAsImV4cCI6MTcwNjI1MzE3MH0.WZhbeK650vvliqjz78-tNYyXpMQ0sQIG8ebfbI5K0n7VkMNQBb2tQ04MG6R2OfmE5aoWcsVC7qFz3wIX_ioC3w";

        // when
        MemberDTO result = memberService.getInfo(token);

        // then
        assertNotNull(result);
        System.out.println("회원조회 : " + result);
    }

    @Test
    public void testGetInfoFail() {
        // given
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJraW0iLCJpYXQiOjE3MDYxNjY0MzUsImV4cCI6MTcwNjI1MjgzNX0.KEdT4bJcSML-CuLUKK0sLWe0nII8YiZWYfJtnVKIFMaCGgS2UDASFRlZ4uZcY5mmFAMwvvJ9pSbdQcbPZwZb6Q";

        // when & then
        assertThrows(SecurityException.class, () -> {
            memberService.getInfo(token);
        }, "사용자 권한이 없어 정보를 조회할 수 없습니다.");
    }
}
