package com.babyplug.softwaretesting.member.service;

import com.babyplug.softwaretesting.member.domain.Member;
import com.babyplug.softwaretesting.member.domain.MemberRepository;
import com.babyplug.softwaretesting.utils.PhoneNumberValidator;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

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
    public Member createMember(Member request) {
        if(!phoneNumberValidator.test(request.getTelNo())){
            throw new IllegalStateException("Phone number " + request.getTelNo() + " is not valid");
        }

        if(request.getName() == null || request.getName().equalsIgnoreCase("")) {
            throw new IllegalStateException("Member name must not be null or empty");
        }

        if(request.getAge() == null || request.getAge() < 0 || request.getAge() > 999) {
            throw new IllegalStateException("Member age must not be null or less than 0 and greater than 999");
        }

        Optional<Member> memberOptional = memberRepository.findByName(request.getName());
        if (memberOptional.isPresent()) {
            throw new IllegalStateException(String.format("Name [%s] is exists", request.getName()));
        }

        return memberRepository.save(request);
    }

}
