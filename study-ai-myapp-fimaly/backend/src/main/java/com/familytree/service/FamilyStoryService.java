package com.familytree.service;

import com.familytree.dto.FamilyStoryRequest;
import com.familytree.dto.FamilyStoryResponse;
import com.familytree.model.Family;
import com.familytree.model.Member;
import com.familytree.repository.FamilyRepository;
import com.familytree.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FamilyStoryService {

    private final FamilyRepository familyRepository;
    private final MemberRepository memberRepository;

    public FamilyStoryResponse generateFamilyStory(Long familyId, FamilyStoryRequest request) {
        log.info("[AI家族故事] 开始为家族ID: {} 生成故事", familyId);

        // 获取家族信息
        Optional<Family> familyOpt = familyRepository.findById(familyId);
        if (familyOpt.isEmpty()) {
            throw new IllegalArgumentException("家族不存在");
        }

        Family family = familyOpt.get();
        List<Member> members = memberRepository.findAll().stream()
                .filter(m -> familyId.equals(m.getFamilyId()))
                .collect(Collectors.toList());

        // 准备上下文信息
        Map<String, Object> context = buildContext(family, members, request);

        // 根据故事类型生成内容
        String storyContent = generateStoryByType(request.getStoryType(), context);
        String title = generateTitle(request.getStoryType(), family.getName());

        FamilyStoryResponse response = new FamilyStoryResponse(
                UUID.randomUUID().toString(),
                title,
                storyContent,
                request.getStoryType(),
                request.getKeywords(),
                "rule-based",
                System.currentTimeMillis()
        );

        log.info("[AI家族故事] 完成故事生成");
        return response;
    }

    private Map<String, Object> buildContext(Family family, List<Member> members, FamilyStoryRequest request) {
        Map<String, Object> context = new HashMap<>();
        context.put("familyName", family.getName());
        context.put("memberCount", members.size());
        context.put("members", members.stream()
                .map(m -> {
                    Map<String, String> memberInfo = new HashMap<>();
                    memberInfo.put("name", m.getName());
                    memberInfo.put("gender", m.getGender());
                    if (m.getBirthDate() != null) {
                        memberInfo.put("birthYear", String.valueOf(m.getBirthDate().getYear()));
                    }
                    return memberInfo;
                })
                .collect(Collectors.toList()));
        context.put("keywords", request.getKeywords() != null ? request.getKeywords() : Collections.emptyList());
        return context;
    }

    private String generateTitle(String storyType, String familyName) {
        return switch (storyType != null ? storyType : "migration") {
            case "migration" -> String.format("%s家族的迁徙之路", familyName);
            case "biography" -> String.format("%s家族人物传记", familyName);
            case "legend" -> String.format("%s家族的传奇故事", familyName);
            default -> String.format("%s家族的故事", familyName);
        };
    }

    private String generateStoryByType(String storyType, Map<String, Object> context) {
        return switch (storyType != null ? storyType : "migration") {
            case "migration" -> generateMigrationStory(context);
            case "biography" -> generateBiographyStory(context);
            case "legend" -> generateLegendStory(context);
            default -> generateGeneralStory(context);
        };
    }

    private String generateMigrationStory(Map<String, Object> context) {
        String familyName = (String) context.get("familyName");
        List<?> members = (List<?>) context.get("members");
        int memberCount = (Integer) context.get("memberCount");

        StringBuilder story = new StringBuilder();
        story.append("# ").append(familyName).append("家族的迁徙之路\n\n");
        story.append("## 前言\n\n");
        story.append("这是一个关于").append(familyName).append("家族的故事，记录了家族的变迁和发展。\n\n");
        story.append("目前家族共").append(memberCount).append("位成员，分布在不同地区。\n\n");

        story.append("## 历史追溯\n\n");
        story.append("家族的历史可以追溯到很久以前，祖先们从原居地出发，经过长途跋涉，");
        story.append("最终在新的土地上扎根，建立了新的家园。\n\n");

        if (members != null && !members.isEmpty()) {
            story.append("## 家族成员\n\n");
            for (Object memberObj : members) {
                Map<?, ?> memberInfo = (Map<?, ?>) memberObj;
                String name = (String) memberInfo.get("name");
                story.append("- **").append(name).append("**");
                if (memberInfo.containsKey("birthYear")) {
                    story.append("（出生于").append(memberInfo.get("birthYear")).append("年）");
                }
                story.append("\n");
            }
        }

        story.append("\n## 家族精神\n\n");
        story.append(familyName).append("家族代代相传的精神：\n");
        story.append("- **勤劳勇敢**：通过辛勤劳动创造幸福生活\n");
        story.append("- **团结互助**：家族成员之间互相帮助、共同进步\n");
        story.append("- **尊祖敬宗**：尊敬祖先，传承家族优良传统\n");

        story.append("\n## 结语\n\n");
        story.append("家族的故事还在继续，每一位成员都是家族历史的书写者。");
        story.append("让我们共同努力，把家族的精神传承下去，创造更美好的明天。\n");

        return story.toString();
    }

    private String generateBiographyStory(Map<String, Object> context) {
        String familyName = (String) context.get("familyName");
        List<?> members = (List<?>) context.get("members");

        StringBuilder story = new StringBuilder();
        story.append("# ").append(familyName).append("家族人物传记\n\n");

        story.append("## 序言\n\n");
        story.append("本传记记录了").append(familyName).append("家族成员的生平事迹，");
        story.append("展现了家族成员的风采和成就。\n\n");

        if (members != null && !members.isEmpty()) {
            for (Object memberObj : members) {
                Map<?, ?> memberInfo = (Map<?, ?>) memberObj;
                String name = (String) memberInfo.get("name");
                story.append("## ").append(name).append("\n\n");
                story.append(name).append("是").append(familyName).append("家族的重要成员，");
                story.append("为家族的发展做出了自己的贡献。\n\n");

                String birthYear = (String) memberInfo.get("birthYear");
                if (birthYear != null) {
                    story.append("出生于").append(birthYear).append("年，在成长过程中，");
                    story.append("深受家族传统文化的熏陶，养成了优良的品质。\n\n");
                }

                story.append("[这里可以补充").append(name).append("的具体事迹和成就]\n\n");
            }
        }

        story.append("## 后记\n\n");
        story.append("家族的发展离不开每一位成员的努力。");
        story.append("让我们以前辈为榜样，为家族的繁荣贡献自己的力量。\n");

        return story.toString();
    }

    private String generateLegendStory(Map<String, Object> context) {
        String familyName = (String) context.get("familyName");

        StringBuilder story = new StringBuilder();
        story.append("# ").append(familyName).append("家族的传奇故事\n\n");

        story.append("## 起源传说\n\n");
        story.append("相传，").append(familyName).append("家族的祖先在很久以前，");
        story.append("从远方的故土出发，历经艰难险阻，最终在这片土地上定居。\n\n");

        story.append("## 家族的荣耀\n\n");
        story.append("在历史的长河中，").append(familyName).append("家族曾经有过辉煌的时期，");
        story.append("先辈们通过自己的智慧和努力，为家族赢得了荣誉。\n\n");

        story.append("## 古老的家训\n\n");
        story.append("家族代代相传的家训：\n");
        story.append("```\n");
        story.append("耕读传家，勤俭持家\n");
        story.append("尊老爱幼，和睦相处\n");
        story.append("诚信做人，踏实做事\n");
        story.append("```\n\n");

        story.append("## 故事的延续\n\n");
        story.append("这些传奇故事虽然发生在很久以前，但它们一直激励着家族的后人。");
        story.append("在新的时代，我们要继承和发扬家族的优良传统，创造新的传奇。\n");

        return story.toString();
    }

    private String generateGeneralStory(Map<String, Object> context) {
        String familyName = (String) context.get("familyName");

        StringBuilder story = new StringBuilder();
        story.append("# ").append(familyName).append("家族的故事\n\n");

        story.append("这是").append(familyName).append("家族的故事，记录了家族的发展历程，");
        story.append("展现了家族成员之间深厚的亲情。\n\n");

        story.append("## 家族的根\n\n");
        story.append("每个家族都有自己的根源，").append(familyName).append("家族也不例外。");
        story.append("无论我们走到哪里，都不能忘记自己的根。\n\n");

        story.append("## 家族的树\n\n");
        story.append("家族就像一棵大树，每一位成员都是树上的一片叶子。");
        story.append("树叶虽然各不相同，但都同根同源，共同组成了茂密的树冠。\n\n");

        story.append("## 家族的未来\n\n");
        story.append("家族的未来充满希望，需要每一位成员共同努力。");
        story.append("让我们携手共进，创造家族更美好的明天！\n");

        return story.toString();
    }
}
