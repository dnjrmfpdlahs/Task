package aladdinsys.api.controller;

import aladdinsys.api.dto.LoginDTO;
import aladdinsys.api.dto.MemberDTO;
import aladdinsys.api.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/szs")
public class MemberController {
    @Autowired
    private MemberService memberService;


    @Operation(summary = "회원 가입 요청", description = "회원가입 진행.")
    @PostMapping("/signup")
    public void signUp(MemberDTO dto) throws Exception {
        memberService.signUp(dto);
    }
    @Operation(summary = "로그인 요청", description = "로그인 진행.")
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginDTO dto) {
        try {
            String token = memberService.login(dto.userId(), dto.password());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @Operation(summary = "정보확인 요청", description = "정보확인 진행.")
    @GetMapping("/me")
    public ResponseEntity<MemberDTO> getMyInfo(@RequestHeader("Authorization") String jwtToken) {
        if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtToken.substring(7);

        MemberDTO memberDTO = memberService.getInfo(token);

        if (memberDTO == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(memberDTO);
    }
}
