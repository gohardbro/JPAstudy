package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController //눌러서 확인해보자
@RequiredArgsConstructor
public class MemberApiController {

     private final MemberService memberService;

     //엔티티를 직접 반환
     @GetMapping("/api/v1/members")
     public List<Member> membersV1() {
         return memberService.findMembers();
     }

     //dto 에 담아서 반환
     @GetMapping("/api/v2/members")
     public Result memberV2() {
         List<Member> findMembers = memberService.findMembers();
         List<MemberDto> collect = findMembers.stream()
                 .map(m -> new MemberDto(m.getName()))
                 .collect(Collectors.toList());

         return new Result(collect);
     }

     @Data
     @AllArgsConstructor
     static class Result<T> {
         private T data;
     }

     @Data
     @AllArgsConstructor
     static class MemberDto {
         private String name;
     }


    //엔티티를 직접 쓰니깐 위험. 엔티티를 수정하면 api 스펙이 달라짐
     @PostMapping("/api/v1/members")
     public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
         Long id = memberService.join(member);
         return new CreateMemberResponse(id);
     }

    //엔티티를 직접쓰지않고 dto 형식으로 클래스를 만들어서 사용. 위의 단점으로 부터 안전
     @PostMapping("/api/v2/members")
     public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

         Member member = new Member();
         member.setName(request.getName());

         Long id = memberService.join(member);
         return new CreateMemberResponse(id);
     }

     @PutMapping("/api/v2/members/{id}")
     public UpdateMemberResponse updateMemberV2(
             @PathVariable Long id,
             @RequestBody @Valid UpdateMemberRequest request) {

         memberService.update(id, request.getName());
         Member findMember = memberService.findOne(id);
         return new UpdateMemberResponse(findMember.getId(), findMember.getName());
     }

     @Data
     static class UpdateMemberRequest {
         private String name;
     }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
         private Long id;
         private String name;
    }

     @Data
     static class CreateMemberRequest {
         private String name;
     }

     @Data
     static class CreateMemberResponse {
         private Long id;

         public CreateMemberResponse(Long id) {
             this.id = id;
         }
     }


}
