package aladdinsys.api.repository;

import aladdinsys.api.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    Optional<MemberEntity> findByUserId(String userId);
    Optional<MemberEntity> findByUserIdAndPassword(String userId, String password);

}