package com.dongsan.core.domains.member;

import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
import org.springframework.stereotype.Service;

@Service
public class MemberReader {
    private final MemberRepository memberRepository;

    public MemberReader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member readMember(Long id){
        return memberRepository.findById(id).orElseThrow(
                ()-> new CoreException(CoreErrorCode.MEMBER_NOT_FOUND)
        );
    }

}
