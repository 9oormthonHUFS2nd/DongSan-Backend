package com.dongsan.domains.member.service;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Optional<Member> readMember(Long id) {
        return memberRepository.findById(id);
    }

}
