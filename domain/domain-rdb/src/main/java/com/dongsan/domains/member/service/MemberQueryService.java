package com.dongsan.domains.member.service;

import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.repository.MemberRepository;
import com.dongsan.common.error.code.MemberErrorCode;
import com.dongsan.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public Member getMember(Long id){
        return memberRepository.findById(id).orElseThrow(
                ()-> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND)
        );
    }

}
