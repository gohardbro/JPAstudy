package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//@AllArgsConstructor : 모든 필드를 가지고 생성자를 생성.
@RequiredArgsConstructor //final 인 필드만 가지고 생성자 생성.
public class MemberService {


    //생성자 인젝션이 있어서 이 필드는 수정할 일이 없기때문에 final로 선언한다.
    private final MemberRepository memberRepository;

    //생성자 인젝션으로 repository 추가하는게 좋다.
    //최신 버전 스프링에서는 생성자하나만 있을경우 @Autowired 없어도 자동으로 인젝션 해준다.
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /*
    * 회원가입
    * */
    @Transactional //이곳은 readOnly = false
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        /*동시에 아이디를 가입하는 동시성 문제가 있을수 있기 때문에
         DB에서 member.getName()에 해당하는 컬럼에 unique 제약조건을 걸어주는것이 좋다.*/
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }


    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
