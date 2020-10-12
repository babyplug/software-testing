package com.babyplug.softwaretesting.Member;

import com.babyplug.softwaretesting.member.domain.Member;
import com.babyplug.softwaretesting.member.domain.MemberRepository;
import com.babyplug.softwaretesting.member.service.MemberService;
import com.babyplug.softwaretesting.member.service.MemberServiceImpl;
import com.babyplug.softwaretesting.utils.PhoneNumberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

//@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PhoneNumberValidator phoneNumberValidator;

    @InjectMocks
    private final MemberService memberService = new MemberServiceImpl();

    @Captor
    private ArgumentCaptor<Member> memberArgumentCaptor;

//    @BeforeEach
//    void setup() {
//        MockitoAnnotations.openMocks(this);
//        // memberService = new MemberServiceImpl(memberRepository, phoneNumberValidator);
//    }

    @Test
    public void itShouldCreateMember() {
        // given
        Member member = new Member(1L,"a", 13L, "1234567890");

        // valid phone
        given(phoneNumberValidator.test(member.getTelNo())).willReturn(true);

        // When
        memberService.createMember(member);

        // Then
        then(memberRepository).should().save(memberArgumentCaptor.capture());
        Member memberArgumentCaptorValue = memberArgumentCaptor.getValue();
        assertThat(memberArgumentCaptorValue).isEqualTo(member);
    }

    @Test
    public void itShouldCreateMemberOld() {
        // given
         doAnswer(returnsFirstArg()).when(memberRepository).save(any(Member.class));

        // given
        Member member = new Member(1L,"a", 13L, "1234567890");

        // valid phone
        given(phoneNumberValidator.test(member.getTelNo())).willReturn(true);

        // When
        Member memberResponse = memberService.createMember(member);

        // Then
        then(memberRepository).should().save(any(Member.class));
        assertThat(memberResponse).isEqualTo(member);
//         verify(memberRepository).save(any(Member.class));
    }

    @Test
    public void itShouldNotCreateMemberWhenPhoneNumberIsInvalid() {
        // given
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
