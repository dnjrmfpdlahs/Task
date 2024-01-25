package aladdinsys.api.service;

import aladdinsys.api.dto.MemberDTO;
import aladdinsys.api.entity.MemberEntity;

public interface MemberService {
    MemberEntity signUp (MemberDTO memberDTO);
    String login(String userId, String password);
    MemberDTO getInfo(String jwtToken);
}