package aladdinsys.api.service;

import aladdinsys.api.dto.MemberDTO;
import aladdinsys.api.entity.MemberEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {
    String signUp (MemberDTO memberDTO) throws Exception;
    String login(String userId, String password) throws Exception;
    MemberDTO getInfo(String jwtToken);
}