package com.dongsan.rdb.domains.member;

import org.springframework.stereotype.Repository;

@Repository
public class MemberCoreRepository {
    private final MemberJpaRepository memberJpaRepository;

    public MemberCoreRepository(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }
}
