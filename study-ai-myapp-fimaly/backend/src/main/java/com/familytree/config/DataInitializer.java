package com.familytree.config;

import com.familytree.model.*;
import com.familytree.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Date;

@Component
@Slf4j
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Starting data initialization...");
            
            User bage = createDefaultUser("bage", "bage@qq.com", "bage1234");
            User admin = createDefaultUser("admin", "admin@qq.com", "admin");
            User zhangsan = createDefaultUser("zhangsan", "zhangsan@qq.com", "zhangsan");
            
            Family family = createDefaultFamily("张家大院", "这是一个测试家族", bage.getId());
            
            Member zhangBa = createDefaultMember(family.getId(), "张八", "male", "1980-01-15", null, "13800138001", "zhangba@example.com");
            Member zhangLiu = createDefaultMember(family.getId(), "张六", "female", "1982-03-20", null, "13800138002", "zhangliu@example.com");
            Member zhangXiao = createDefaultMember(family.getId(), "张小", "male", "2005-06-10", null, "13800138003", "zhangxiao@example.com");
            Member zhangMei = createDefaultMember(family.getId(), "张美", "female", "2008-09-25", null, "13800138004", "zhangmei@example.com");
            
            createDefaultMilestone(zhangBa, "出生", "1980年1月15日出生于北京", bage.getId());
            createDefaultMilestone(zhangBa, "大学毕业", "2002年毕业于清华大学计算机系", bage.getId());
            createDefaultMilestone(zhangBa, "结婚", "2005年与张六结婚", bage.getId());
            
            createDefaultMilestone(zhangLiu, "出生", "1982年3月20日出生于上海", bage.getId());
            createDefaultMilestone(zhangLiu, "大学毕业", "2004年毕业于北京大学经济系", bage.getId());
            
            createDefaultMilestone(zhangXiao, "出生", "2005年6月10日出生于北京", bage.getId());
            createDefaultMilestone(zhangXiao, "入学", "2011年入学北京第一小学", bage.getId());
            
            createDefaultMilestone(zhangMei, "出生", "2008年9月25日出生于北京", bage.getId());
            createDefaultMilestone(zhangMei, "入学", "2014年入学北京第一小学", bage.getId());
            
            createDefaultLocation(zhangBa, 39.9042, 116.4074, "北京市东城区", "home", "家", true);
            createDefaultLocation(zhangBa, 39.9842, 116.3074, "北京市海淀区中关村", "work", "公司", false);
            
            createDefaultLocation(zhangLiu, 39.9042, 116.4074, "北京市东城区", "home", "家", true);
            createDefaultLocation(zhangLiu, 39.9942, 116.3174, "北京市朝阳区国贸", "work", "公司", false);
            
            createDefaultLocation(zhangXiao, 39.9042, 116.4074, "北京市东城区", "home", "家", true);
            createDefaultLocation(zhangXiao, 39.9142, 116.4174, "北京市东城区第一小学", "school", "学校", false);
            
            createDefaultLocation(zhangMei, 39.9042, 116.4074, "北京市东城区", "home", "家", true);
            createDefaultLocation(zhangMei, 39.9142, 116.4174, "北京市东城区第一小学", "school", "学校", false);
            
            log.info("Data initialization completed successfully!");
        }
    }

    private User createDefaultUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setCreatedAt(new Date());
        userRepository.save(user);
        log.info("Created default user: {}", username);
        return user;
    }

    private Family createDefaultFamily(String name, String description, Long creatorId) {
        Family family = new Family();
        family.setName(name);
        family.setDescription(description);
        family.setCreatorId(creatorId);
        familyRepository.save(family);
        log.info("Created default family: {}", name);
        return family;
    }

    private Member createDefaultMember(Long familyId, String name, String gender, String birthDate, String deathDate, String phone, String email) {
        Member member = new Member();
        member.setFamilyId(familyId);
        member.setName(name);
        member.setGender(gender);
        
        try {
            if (birthDate != null) {
                member.setBirthDate(new java.text.SimpleDateFormat("yyyy-MM-dd").parse(birthDate));
            }
            if (deathDate != null) {
                member.setDeathDate(new java.text.SimpleDateFormat("yyyy-MM-dd").parse(deathDate));
            }
        } catch (Exception e) {
            log.error("Error parsing date: {}", e.getMessage());
        }
        
        member.setPhone(phone);
        member.setEmail(email);
        memberRepository.save(member);
        log.info("Created default member: {}", name);
        return member;
    }

    private void createDefaultMilestone(Member member, String title, String content, Long creatorId) {
        Milestone milestone = new Milestone();
        milestone.setMember(member);
        milestone.setTitle(title);
        milestone.setContent(content);
        milestone.setCreatorId(creatorId);
        milestone.setPublic(true);
        milestoneRepository.save(milestone);
        log.info("Created default milestone: {} for member: {}", title, member.getName());
    }

    private void createDefaultLocation(Member member, Double latitude, Double longitude, String address, String locationType, String name, boolean isPrimary) {
        Location location = new Location();
        location.setMember(member);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAddress(address);
        location.setLocationType(locationType);
        location.setName(name);
        location.setPrimary(isPrimary);
        location.setShared(true);
        locationRepository.save(location);
        log.info("Created default location: {} for member: {}", name, member.getName());
    }
}
