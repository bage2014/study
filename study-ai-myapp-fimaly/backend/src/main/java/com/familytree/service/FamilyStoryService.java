package com.familytree.service;

import com.familytree.model.Event;
import com.familytree.model.Member;
import com.familytree.repository.EventRepository;
import com.familytree.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FamilyStoryService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EventRepository eventRepository;

    public String generateFamilyStory(Long familyId) {
        // 获取家族所有成员
        List<Member> members = memberRepository.findAll().stream()
                .filter(member -> member.getFamilyId().equals(familyId))
                .collect(Collectors.toList());

        // 获取家族所有事件
        List<Event> events = eventRepository.findAll().stream()
                .filter(event -> event.getFamilyId().equals(familyId))
                .collect(Collectors.toList());

        // 生成家族故事
        return generateStory(members, events, familyId);
    }

    private String generateStory(List<Member> members, List<Event> events, Long familyId) {
        if (members.isEmpty()) {
            return "该家族暂无成员信息，无法生成故事。";
        }

        StringBuilder story = new StringBuilder();

        // 开头
        story.append("在一个充满爱的家族里，");
        story.append("家族的故事开始于...\n\n");

        // 介绍主要成员
        story.append("家族成员包括：\n");
        for (Member member : members) {
            story.append("- ");
            story.append(member.getName());
            if (member.getBirthDate() != null) {
                story.append("，出生于");
                story.append(member.getBirthDate().getYear() + 1900);
                story.append("年");
            }
            story.append("\n");
        }
        story.append("\n");

        // 描述重要事件
        if (!events.isEmpty()) {
            story.append("家族经历了许多重要事件：\n");
            for (Event event : events) {
                story.append("- ");
                story.append(event.getTitle());
                if (event.getDate() != null) {
                    story.append("，发生于");
                    story.append(event.getDate().getYear() + 1900);
                    story.append("年");
                }
                story.append("：");
                story.append(event.getDescription());
                story.append("\n");
            }
            story.append("\n");
        }

        // 结尾
        story.append("这个家族的故事还在继续，");
        story.append("每一位成员都在为家族的繁荣和传承贡献自己的力量。");
        story.append("未来，");
        story.append("家族将继续书写属于自己的辉煌篇章。");

        return story.toString();
    }

    public String generateMemberStory(Long memberId) {
        // 获取成员信息
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 获取与该成员相关的事件
        List<Event> events = eventRepository.findAll().stream()
                .filter(event -> event.getFamilyId().equals(member.getFamilyId()))
                .collect(Collectors.toList());

        // 生成成员故事
        return generateMemberStory(member, events);
    }

    private String generateMemberStory(Member member, List<Event> events) {
        StringBuilder story = new StringBuilder();

        // 开头
        story.append(member.getName());
        story.append("的故事开始于");
        if (member.getBirthDate() != null) {
            story.append(member.getBirthDate().getYear() + 1900);
            story.append("年");
        }
        story.append("...\n\n");

        // 描述成员基本信息
        story.append("作为家族的一员，");
        story.append(member.getName());
        story.append("在家族中扮演着重要的角色。\n\n");

        // 描述相关事件
        if (!events.isEmpty()) {
            story.append(member.getName());
            story.append("经历了以下重要事件：\n");
            for (Event event : events) {
                story.append("- ");
                story.append(event.getTitle());
                if (event.getDate() != null) {
                    story.append("，发生于");
                    story.append(event.getDate().getYear() + 1900);
                    story.append("年");
                }
                story.append("：");
                story.append(event.getDescription());
                story.append("\n");
            }
            story.append("\n");
        }

        // 结尾
        story.append(member.getName());
        story.append("的故事是家族历史中不可或缺的一部分，");
        story.append("他/她的经历和贡献将永远被家族铭记。");

        return story.toString();
    }
}
