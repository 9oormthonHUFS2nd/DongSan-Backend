package com.dongsan.domains.member.service;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.repository.MemberRepository;
import com.dongsan.error.code.MemberErrorCode;
import com.dongsan.error.exception.CustomException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public Optional<Member> readMember(Long id) {
        return memberRepository.findById(id);
    }

    public Member getMember(Long id){
        return memberRepository.findById(id).orElseThrow(
                ()-> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND)
        );
    }

}
