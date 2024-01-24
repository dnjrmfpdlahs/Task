package aladdinsys.api.service.impl;

import aladdinsys.api.dto.MemberDTO;
import aladdinsys.api.entity.MemberEntity;
import aladdinsys.api.repository.MemberRepository;
import aladdinsys.api.role.Role;
import aladdinsys.api.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;


    @Override
    public String signUp(MemberDTO memberDTO) throws Exception {
        String hashedPassword = new BCryptPasswordEncoder().encode(memberDTO.getPassword());
        String hashedRegNo = new BCryptPasswordEncoder().encode(memberDTO.getRegNo());

        Set<Role> roles = new HashSet<>();

        if (
                ("홍길동".equals(memberDTO.getName()) && "860824-1655068".equals(memberDTO.getRegNo())) ||
                ("김둘리".equals(memberDTO.getName()) && "921108-1582816".equals(memberDTO.getRegNo())) ||
                ("마징가".equals(memberDTO.getName()) && "880601-2455116".equals(memberDTO.getRegNo())) ||
                ("베지터".equals(memberDTO.getName()) && "910411-1656116".equals(memberDTO.getRegNo())) ||
                ("손오공".equals(memberDTO.getName()) && "820326-2715702".equals(memberDTO.getRegNo()))
        ) {
            roles.add(Role.USER);

            MemberEntity newMember = new MemberEntity(
                    memberDTO.getUserId(),
                    hashedPassword,
                    memberDTO.getName(),
                    hashedRegNo,
                    roles
            );

            memberRepository.save(newMember);

            String jwtToken = generateJwtToken(newMember.getUserId());
            return jwtToken;
        } else {
            throw new IllegalArgumentException("조건을 만족하지 않습니다.");
        }
    }

    @Override
    public String login(String userId, String password) throws Exception {
        Optional<MemberEntity> userOptional  = memberRepository.findByUserIdAndPassword(userId, password);

        if (userOptional.isPresent()) {
            MemberEntity user = userOptional.get();

            if (passwordEncoder.matches(password, user.getPassword())) {
                return generateJwtToken(userId);
            }
        }

        return generateJwtToken(userId);
    }

    @Override
    public MemberDTO getInfo(String jwtToken) {
        String userId = getUserIdFromToken(jwtToken);

        if (userId == null) {
            return null;
        }

        MemberEntity user = memberRepository.findByUserId(userId);
        if (user == null) {
            return null;
        }

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUserId(user.getUserId());
        memberDTO.setPassword(user.getPassword());
        memberDTO.setName(user.getName());
        memberDTO.setRegNo(user.getRegNo());

        return memberDTO;
    }

    private String getUserIdFromToken(String jwtToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwtToken)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    private String generateJwtToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
