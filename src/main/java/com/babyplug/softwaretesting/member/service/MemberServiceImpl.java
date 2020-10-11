package com.babyplug.softwaretesting.member.service;

import com.babyplug.softwaretesting.member.domain.Member;
import com.babyplug.softwaretesting.member.domain.MemberRepository;
import com.babyplug.softwaretesting.utils.PhoneNumberValidator;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    private PhoneNumberValidator phoneNumberValidator;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository,
                             PhoneNumberValidator phoneNumberValidator) {
        this.memberRepository = memberRepository;
        this.phoneNumberValidator = phoneNumberValidator;
    }

    @Override
    public Member createMember(Member member) {
        if(!phoneNumberValidator.test(member.getTelNo())){
            throw new IllegalStateException("Phone number " + member.getTelNo() + " is not valid");
        }
        return memberRepository.save(member);
    }

}
