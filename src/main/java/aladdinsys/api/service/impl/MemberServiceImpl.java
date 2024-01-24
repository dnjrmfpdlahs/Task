package aladdinsys.api.service.impl;

import aladdinsys.api.dto.MemberDTO;
import aladdinsys.api.entity.MemberEntity;
import aladdinsys.api.repository.MemberRepository;
import aladdinsys.api.role.Role;
import aladdinsys.api.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
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
        String hashedPassword = new BCryptPasswordEncoder().encode(memberDTO.password());
        String hashedRegNo = new BCryptPasswordEncoder().encode(memberDTO.regNo());

        Set<Role> roles = new HashSet<>();

        if (
                ("홍길동".equals(memberDTO.name()) && "860824-1655068".equals(memberDTO.regNo())) ||
                ("김둘리".equals(memberDTO.name()) && "921108-1582816".equals(memberDTO.regNo())) ||
                ("마징가".equals(memberDTO.name()) && "880601-2455116".equals(memberDTO.regNo())) ||
                ("베지터".equals(memberDTO.name()) && "910411-1656116".equals(memberDTO.regNo())) ||
                ("손오공".equals(memberDTO.name()) && "820326-2715702".equals(memberDTO.regNo()))
        ) {
            roles.add(Role.USER);

            MemberEntity newMember = new MemberEntity(
                    memberDTO.userId(),
                    hashedPassword,
                    memberDTO.name(),
                    hashedRegNo,
                    roles
            );

            memberRepository.save(newMember);

            return generateJwtToken(newMember.getUserId());
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

        MemberEntity user = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("조건을 만족하지 않습니다."));

        if (user == null) {
            return null;
        }

        return new MemberDTO(
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getRegNo());
    }

    private String getUserIdFromToken(String jwtToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
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
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
