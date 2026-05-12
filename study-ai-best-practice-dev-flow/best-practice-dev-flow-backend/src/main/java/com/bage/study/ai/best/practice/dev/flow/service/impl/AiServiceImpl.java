package com.bage.study.ai.best.practice.dev.flow.service.impl;

import com.bage.study.ai.best.practice.dev.flow.dto.response.AiRelationshipPredictionDTO;
import com.bage.study.ai.best.practice.dev.flow.dto.response.AiStoryDTO;
import com.bage.study.ai.best.practice.dev.flow.entity.Member;
import com.bage.study.ai.best.practice.dev.flow.repository.MemberRepository;
import com.bage.study.ai.best.practice.dev.flow.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiServiceImpl implements AiService {

    private final MemberRepository memberRepository;

    @Override
    public AiRelationshipPredictionDTO predictRelationships(Long familyId) {
        List<Member> members = memberRepository.findByFamilyIdAndDeletedFalse(familyId);
        
        AiRelationshipPredictionDTO result = new AiRelationshipPredictionDTO();
        result.setPredictionId(UUID.randomUUID().toString());
        result.setPredictedRelationships(new ArrayList<>());
        result.setMissingDataSuggestions(new ArrayList<>());

        List<String> missingSuggestions = new ArrayList<>();
        for (Member member : members) {
            if (member.getBirthDate() == null) {
                missingSuggestions.add("请补充成员 " + member.getName() + " 的出生日期");
            }
            if (member.getGender() == null) {
                missingSuggestions.add("请补充成员 " + member.getName() + " 的性别");
            }
        }
        result.setMissingDataSuggestions(missingSuggestions);

        for (int i = 0; i < members.size(); i++) {
            for (int j = i + 1; j < members.size(); j++) {
                Member m1 = members.get(i);
                Member m2 = members.get(j);
                
                AiRelationshipPredictionDTO.PredictedRelationship prediction = predictRelationship(m1, m2);
                if (prediction != null && prediction.getConfidence() > 0.5) {
                    result.getPredictedRelationships().add(prediction);
                }
            }
        }

        return result;
    }

    private AiRelationshipPredictionDTO.PredictedRelationship predictRelationship(Member m1, Member m2) {
        AiRelationshipPredictionDTO.PredictedRelationship prediction = new AiRelationshipPredictionDTO.PredictedRelationship();
        
        AiRelationshipPredictionDTO.MemberInfo info1 = new AiRelationshipPredictionDTO.MemberInfo();
        info1.setId(m1.getId());
        info1.setName(m1.getName());
        info1.setBirthYear(m1.getBirthDate() != null ? m1.getBirthDate().getYear() : null);
        
        AiRelationshipPredictionDTO.MemberInfo info2 = new AiRelationshipPredictionDTO.MemberInfo();
        info2.setId(m2.getId());
        info2.setName(m2.getName());
        info2.setBirthYear(m2.getBirthDate() != null ? m2.getBirthDate().getYear() : null);
        
        prediction.setMember1(info1);
        prediction.setMember2(info2);
        
        List<String> reasoning = new ArrayList<>();
        double confidence = 0.3;
        
        if (m1.getBirthDate() != null && m2.getBirthDate() != null) {
            long yearsDiff = ChronoUnit.YEARS.between(
                m1.getBirthDate(), 
                m2.getBirthDate()
            );
            
            if (yearsDiff > 15 && yearsDiff < 40) {
                if ("MALE".equals(m1.getGender()) && "FEMALE".equals(m2.getGender())) {
                    prediction.setRelationship("夫妻");
                    confidence = 0.75;
                    reasoning.add("年龄差距在15-40岁之间，符合夫妻年龄差距");
                    reasoning.add("性别不同");
                } else if (yearsDiff > 20) {
                    if (yearsDiff > 0) {
                        prediction.setRelationship("父子/父女");
                    } else {
                        prediction.setRelationship("母子/母女");
                    }
                    confidence = 0.8;
                    reasoning.add("年龄差距约20-35岁，符合父母子女年龄差距");
                } else if (yearsDiff > 40) {
                    prediction.setRelationship("祖孙");
                    confidence = 0.9;
                    reasoning.add("年龄差距超过40岁，符合祖孙年龄差距");
                } else {
                    prediction.setRelationship("兄弟姐妹");
                    confidence = 0.6;
                    reasoning.add("年龄差距较小，可能为兄弟姐妹");
                }
            } else if (Math.abs(yearsDiff) <= 5) {
                prediction.setRelationship("兄弟姐妹");
                confidence = 0.7;
                reasoning.add("年龄相近，可能为兄弟姐妹");
            }
        } else {
            prediction.setRelationship("未知");
            confidence = 0.2;
            reasoning.add("缺少出生日期信息，无法准确判断");
        }
        
        prediction.setConfidence(confidence);
        prediction.setReasoning(reasoning);
        prediction.setAiComment("基于年龄和性别数据进行关系推断，建议补充更多信息以提高准确性");
        
        return prediction;
    }

    @Override
    public AiStoryDTO generateStory(Long familyId, String storyType) {
        AiStoryDTO story = new AiStoryDTO();
        story.setStoryId(UUID.randomUUID().toString());
        story.setStoryType(storyType);
        
        List<Member> members = memberRepository.findByFamilyIdAndDeletedFalse(familyId);
        
        switch (storyType.toUpperCase()) {
            case "ORIGIN":
                story.setTitle("家族起源故事");
                story.setContent(generateOriginStory(members));
                story.setAiComment("根据家族成员信息生成的家族起源故事");
                break;
            case "TIMELINE":
                story.setTitle("家族时间线");
                story.setContent(generateTimelineStory(members));
                story.setAiComment("根据成员出生日期生成的家族时间线");
                break;
            case "ACHIEVEMENTS":
                story.setTitle("家族成就故事");
                story.setContent(generateAchievementsStory(members));
                story.setAiComment("根据成员职业信息生成的成就故事");
                break;
            default:
                story.setTitle("家族故事");
                story.setContent(generateGeneralStory(members));
                story.setAiComment("综合家族信息生成的故事");
        }
        
        return story;
    }

    private String generateOriginStory(List<Member> members) {
        StringBuilder sb = new StringBuilder();
        sb.append("在岁月的长河中，这个家族承载着独特的历史和传承。\n\n");
        sb.append("根据记载，这个家族的先辈们在遥远的年代开始了他们的故事。\n");
        sb.append("他们经历了风雨，见证了时代的变迁，始终坚守着家族的信念。\n\n");
        
        List<Member> sorted = members.stream()
            .filter(m -> m.getBirthDate() != null)
            .sorted((a, b) -> a.getBirthDate().compareTo(b.getBirthDate()))
            .toList();
        
        if (!sorted.isEmpty()) {
            Member oldest = sorted.get(0);
            sb.append("家族中最年长的成员是 ").append(oldest.getName());
            if (oldest.getBirthDate() != null) {
                sb.append("，出生于 ").append(oldest.getBirthDate().getYear()).append("年。");
            }
            sb.append("\n");
        }
        
        sb.append("\n每一位家族成员都在自己的时代书写着独特的篇章，");
        sb.append("他们的故事汇聚在一起，构成了这个家族丰富多彩的历史画卷。");
        
        return sb.toString();
    }

    private String generateTimelineStory(List<Member> members) {
        StringBuilder sb = new StringBuilder();
        sb.append("家族时间线\n\n");
        
        List<Member> sorted = members.stream()
            .filter(m -> m.getBirthDate() != null)
            .sorted((a, b) -> a.getBirthDate().compareTo(b.getBirthDate()))
            .toList();
        
        for (Member member : sorted) {
            sb.append("- ").append(member.getBirthDate().getYear()).append("年：")
              .append(member.getName()).append("出生");
            if (member.getGender() != null) {
                sb.append("（").append("MALE".equals(member.getGender()) ? "男" : "女").append("）");
            }
            sb.append("\n");
        }
        
        sb.append("\n这是家族成员来到这个世界的时间印记，");
        sb.append("每一个日期都标志着家族延续的重要时刻。");
        
        return sb.toString();
    }

    private String generateAchievementsStory(List<Member> members) {
        StringBuilder sb = new StringBuilder();
        sb.append("家族成就故事\n\n");
        sb.append("这个家族人才辈出，每个成员都在各自的领域发光发热。\n\n");
        
        for (Member member : members) {
            sb.append("◆ ").append(member.getName()).append("\n");
            if (member.getOccupation() != null && !member.getOccupation().isEmpty()) {
                sb.append("  职业：").append(member.getOccupation()).append("\n");
            }
            if (member.getEducation() != null && !member.getEducation().isEmpty()) {
                sb.append("  学历：").append(member.getEducation()).append("\n");
            }
            sb.append("\n");
        }
        
        sb.append("他们用智慧和汗水书写着家族的荣耀，");
        sb.append("为家族增添了光彩，也为后代树立了榜样。");
        
        return sb.toString();
    }

    private String generateGeneralStory(List<Member> members) {
        StringBuilder sb = new StringBuilder();
        sb.append("家族故事\n\n");
        sb.append("这是一个充满温情和故事的家族。\n\n");
        
        sb.append("家族成员概况：\n");
        sb.append("- 总人数：").append(members.size()).append("人\n");
        
        long maleCount = members.stream().filter(m -> "MALE".equals(m.getGender())).count();
        long femaleCount = members.stream().filter(m -> "FEMALE".equals(m.getGender())).count();
        sb.append("- 男性：").append(maleCount).append("人\n");
        sb.append("- 女性：").append(femaleCount).append("人\n");
        
        sb.append("\n家族的每一位成员都有着独特的个性和故事，");
        sb.append("他们相互关爱、相互支持，共同编织着家族的美好未来。");
        
        return sb.toString();
    }
}