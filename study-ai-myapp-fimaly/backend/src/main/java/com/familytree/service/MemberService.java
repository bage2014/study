package com.familytree.service;

import com.familytree.exception.BusinessException;
import com.familytree.exception.ErrorCode;
import com.familytree.model.Member;
import com.familytree.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member addMember(Long familyId, String name, String gender, Date birthDate, Date deathDate,
                            String photo, String details, String phone, String email) {
        log.info("[添加成员] familyId={}, name={}", familyId, name);

        validateMemberData(name, gender, birthDate, deathDate, details);

        Member member = new Member();
        member.setFamilyId(familyId);
        member.setName(name);
        member.setGender(gender);
        member.setBirthDate(birthDate);
        member.setDeathDate(deathDate);
        member.setPhoto(photo);
        member.setDetails(details);
        member.setPhone(phone);
        member.setEmail(email);
        member.setCreatedAt(new Date());

        Member savedMember = memberRepository.save(member);
        log.info("[添加成员成功] memberId={}", savedMember.getId());

        return savedMember;
    }

    public List<Member> getMembersByFamilyId(Long familyId) {
        log.debug("[获取家族成员列表] familyId={}", familyId);
        return memberRepository.findByFamilyId(familyId);
    }

    public Member getMemberById(Long memberId) {
        log.debug("[获取成员详情] memberId={}", memberId);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, String.valueOf(memberId)));
    }

    @Transactional
    public Member updateMember(Long memberId, String name, String gender, Date birthDate, Date deathDate,
                               String photo, String details, String phone, String email) {
        log.info("[更新成员] memberId={}, name={}", memberId, name);

        Member member = getMemberById(memberId);

        validateMemberData(name, gender, birthDate, deathDate, details);

        if (name != null) member.setName(name);
        if (gender != null) member.setGender(gender);
        if (birthDate != null) member.setBirthDate(birthDate);
        if (deathDate != null) member.setDeathDate(deathDate);
        if (photo != null) member.setPhoto(photo);
        if (details != null) member.setDetails(details);
        if (phone != null) member.setPhone(phone);
        if (email != null) member.setEmail(email);

        Member updatedMember = memberRepository.save(member);
        log.info("[更新成员成功] memberId={}", memberId);

        return updatedMember;
    }

    @Transactional
    public void deleteMember(Long memberId) {
        log.info("[删除成员] memberId={}", memberId);

        if (!memberRepository.existsById(memberId)) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND, String.valueOf(memberId));
        }

        memberRepository.deleteById(memberId);
        log.info("[删除成员成功] memberId={}", memberId);
    }

    public List<Member> getAllMembers() {
        log.debug("[获取所有成员列表]");
        return memberRepository.findAll();
    }

    public List<Member> searchMembers(String phone, String email) {
        log.debug("[搜索成员] phone={}, email={}", phone, email);

        if (phone != null && !phone.isEmpty() && email != null && !email.isEmpty()) {
            return memberRepository.findByPhoneAndEmail(phone, email);
        } else if (phone != null && !phone.isEmpty()) {
            return memberRepository.findByPhone(phone);
        } else if (email != null && !email.isEmpty()) {
            return memberRepository.findByEmail(email);
        } else {
            return getAllMembers();
        }
    }

    private void validateMemberData(String name, String gender, Date birthDate, Date deathDate, String details) {
        if (name != null && name.length() > 50) {
            throw new BusinessException(ErrorCode.MEMBER_NAME_TOO_LONG);
        }

        if (gender != null && !isValidGender(gender)) {
            throw new BusinessException(ErrorCode.MEMBER_GENDER_INVALID);
        }

        if (birthDate != null && deathDate != null && birthDate.after(deathDate)) {
            throw new BusinessException(ErrorCode.MEMBER_BIRTH_DATE_AFTER_DEATH);
        }

        if (details != null && details.length() > 2000) {
            throw new BusinessException(ErrorCode.MEMBER_UPDATE_FAILED, "详细信息过长");
        }
    }

    private boolean isValidGender(String gender) {
        return gender == null ||
               gender.isEmpty() ||
               gender.equals("男") ||
               gender.equals("女") ||
               gender.equals("MALE") ||
               gender.equals("FEMALE") ||
               gender.equals("OTHER");
    }
}