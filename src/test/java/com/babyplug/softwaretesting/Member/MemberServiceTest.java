package com.babyplug.softwaretesting.Member;

import com.babyplug.softwaretesting.member.domain.Member;
import com.babyplug.softwaretesting.member.domain.MemberRepository;
import com.babyplug.softwaretesting.member.service.MemberService;
import com.babyplug.softwaretesting.member.service.MemberServiceImpl;
import com.babyplug.softwaretesting.utils.PhoneNumberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PhoneNumberValidator phoneNumberValidator;

    private MemberService memberService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        memberService = new MemberServiceImpl(memberRepository, phoneNumberValidator);
    }

    @Test
    void itShouldCreateMember() {
        // given
        doAnswer(returnsFirstArg()).when(memberRepository).save(any(Member.class));

        // request
        Member member = new Member(1L,"a", 13L, "1234567890");

        // valid phone
        given(phoneNumberValidator.test(member.getTelNo())).willReturn(true);

        // When
        Member memberResponse = memberService.createMember(member);

        // Then
        assertThat(memberResponse).isEqualTo(member);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void itShouldNotCreateMemberWhenPhoneNumberIsInvalid() {
        // given
        doAnswer(returnsFirstArg()).when(memberRepository).save(any(Member.class));

        // request
        Member member = new Member(1L,"a", 13L, "1234567890");

        // valid phone
        given(phoneNumberValidator.test(member.getTelNo())).willReturn(false);

        // When
        assertThatThrownBy(() -> memberService.createMember(member))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Phone number " + member.getTelNo() + " is not valid");

        // Then
        then(memberRepository).shouldHaveNoInteractions();

    }

}
