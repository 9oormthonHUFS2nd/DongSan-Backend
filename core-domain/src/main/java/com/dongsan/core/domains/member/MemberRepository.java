package com.dongsan.core.domains.member;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository {
    Optional<Member> findById(Long memberId);
}
