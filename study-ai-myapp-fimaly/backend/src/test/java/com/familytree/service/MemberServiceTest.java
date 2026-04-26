package com.familytree.service;

import com.familytree.exception.BusinessException;
import com.familytree.exception.ErrorCode;
import com.familytree.model.Member;
import com.familytree.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member();
        testMember.setId(1L);
        testMember.setFamilyId(1L);
        testMember.setName("张三");
        testMember.setGender("男");
        testMember.setBirthDate(new Date(90, 1, 1));
        testMember.setCreatedAt(new Date());
    }

    @Nested
    @DisplayName("添加成员测试")
    class AddMemberTests {

        @Test
        @DisplayName("成功添加成员")
        void testAddMember_Success() {
            when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
                Member member = invocation.getArgument(0);
                member.setId(1L);
                return member;
            });

            Member result = memberService.addMember(
                    1L, "张三", "男", new Date(), null,
                    null, "备注", "13800138000", "zhangsan@example.com"
            );

            assertNotNull(result);
            assertEquals("张三", result.getName());
            verify(memberRepository, times(1)).save(any(Member.class));
        }

        @Test
        @DisplayName("添加成员时设置创建时间")
        void testAddMember_ShouldSetCreatedAt() {
            when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
                Member member = invocation.getArgument(0);
                assertNotNull(member.getCreatedAt());
                member.setId(1L);
                return member;
            });

            memberService.addMember(1L, "张三", "男", null, null, null, null, null, null);

            verify(memberRepository, times(1)).save(any(Member.class));
        }
    }

    @Nested
    @DisplayName("获取成员测试")
    class GetMemberTests {

        @Test
        @DisplayName("成功获取成员")
        void testGetMemberById_Success() {
            when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));

            Member result = memberService.getMemberById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("张三", result.getName());
            verify(memberRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("成员不存在时抛出异常")
        void testGetMemberById_NotFound() {
            when(memberRepository.findById(999L)).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(BusinessException.class, () -> {
                memberService.getMemberById(999L);
            });

            assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
            verify(memberRepository, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("更新成员测试")
    class UpdateMemberTests {

        @Test
        @DisplayName("成功更新成员")
        void testUpdateMember_Success() {
            when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));
            when(memberRepository.save(any(Member.class))).thenReturn(testMember);

            Member result = memberService.updateMember(
                    1L, "李四", "女", new Date(), null, null, "新备注", "13900139000", "lisi@example.com"
            );

            assertNotNull(result);
            verify(memberRepository, times(1)).findById(1L);
            verify(memberRepository, times(1)).save(any(Member.class));
        }

        @Test
        @DisplayName("更新不存在的成员时抛出异常")
        void testUpdateMember_NotFound() {
            when(memberRepository.findById(999L)).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(BusinessException.class, () -> {
                memberService.updateMember(999L, "李四", null, null, null, null, null, null, null);
            });

            assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
            verify(memberRepository, never()).save(any(Member.class));
        }

        @Test
        @DisplayName("更新成员时部分字段为空不应覆盖原有值")
        void testUpdateMember_PartialUpdate() {
            testMember.setPhone("13800138000");
            testMember.setEmail("original@example.com");
            when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));
            when(memberRepository.save(any(Member.class))).thenReturn(testMember);

            memberService.updateMember(1L, null, null, null, null, null, null, null, null);

            assertEquals("13800138000", testMember.getPhone());
            assertEquals("original@example.com", testMember.getEmail());
        }
    }

    @Nested
    @DisplayName("删除成员测试")
    class DeleteMemberTests {

        @Test
        @DisplayName("成功删除成员")
        void testDeleteMember_Success() {
            when(memberRepository.existsById(1L)).thenReturn(true);
            doNothing().when(memberRepository).deleteById(1L);

            assertDoesNotThrow(() -> memberService.deleteMember(1L));

            verify(memberRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("删除不存在的成员时抛出异常")
        void testDeleteMember_NotFound() {
            when(memberRepository.existsById(999L)).thenReturn(false);

            BusinessException exception = assertThrows(BusinessException.class, () -> {
                memberService.deleteMember(999L);
            });

            assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
            verify(memberRepository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("获取家族成员列表测试")
    class GetMembersByFamilyIdTests {

        @Test
        @DisplayName("成功获取家族成员列表")
        void testGetMembersByFamilyId_Success() {
            Member member1 = new Member();
            member1.setId(1L);
            member1.setFamilyId(1L);
            member1.setName("成员1");

            Member member2 = new Member();
            member2.setId(2L);
            member2.setFamilyId(1L);
            member2.setName("成员2");

            when(memberRepository.findByFamilyId(1L)).thenReturn(Arrays.asList(member1, member2));

            List<Member> result = memberService.getMembersByFamilyId(1L);

            assertNotNull(result);
            assertEquals(2, result.size());
            verify(memberRepository, times(1)).findByFamilyId(1L);
        }

        @Test
        @DisplayName("空家族返回空列表")
        void testGetMembersByFamilyId_EmptyList() {
            when(memberRepository.findByFamilyId(999L)).thenReturn(List.of());

            List<Member> result = memberService.getMembersByFamilyId(999L);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("搜索成员测试")
    class SearchMembersTests {

        @Test
        @DisplayName("按手机号搜索")
        void testSearchMembers_ByPhone() {
            when(memberRepository.findByPhone("13800138000")).thenReturn(List.of(testMember));

            List<Member> result = memberService.searchMembers("13800138000", null);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("张三", result.get(0).getName());
            verify(memberRepository, times(1)).findByPhone("13800138000");
        }

        @Test
        @DisplayName("按邮箱搜索")
        void testSearchMembers_ByEmail() {
            when(memberRepository.findByEmail("zhangsan@example.com")).thenReturn(List.of(testMember));

            List<Member> result = memberService.searchMembers(null, "zhangsan@example.com");

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberRepository, times(1)).findByEmail("zhangsan@example.com");
        }

        @Test
        @DisplayName("搜索条件为空时返回空列表")
        void testSearchMembers_NoCriteria() {
            List<Member> result = memberService.searchMembers(null, null);

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(memberRepository, never()).findByPhone(anyString());
            verify(memberRepository, never()).findByEmail(anyString());
        }
    }
}