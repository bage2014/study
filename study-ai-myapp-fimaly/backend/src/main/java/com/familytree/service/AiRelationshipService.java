package com.familytree.service;

import com.familytree.dto.DataCompletionSuggestion;
import com.familytree.dto.PredictedMember;
import com.familytree.dto.PredictedRelationship;
import com.familytree.dto.RelationshipPredictionResponse;
import com.familytree.model.Member;
import com.familytree.model.Relationship;
import com.familytree.repository.MemberRepository;
import com.familytree.repository.RelationshipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiRelationshipService {

    private final MemberRepository memberRepository;
    private final RelationshipRepository relationshipRepository;

    public RelationshipPredictionResponse predictRelationships(Long familyId) {
        log.info("[AI关系预测] 开始预测家族ID: {} 的成员关系", familyId);

        // 获取家族所有成员
        List<Member> members = memberRepository.findAll().stream()
                .filter(member -> familyId.equals(member.getFamilyId()))
                .collect(Collectors.toList());

        if (members.size() < 2) {
            log.info("[AI关系预测] 成员数量不足，无法预测");
            return new RelationshipPredictionResponse(
                    UUID.randomUUID().toString(),
                    Collections.emptyList(),
                    Collections.singletonList("需要至少2个成员才能进行关系预测"),
                    0
            );
        }

        // 获取现有关系
        List<Relationship> existingRelationships = relationshipRepository.findAll().stream()
                .filter(rel -> familyId.equals(rel.getFamilyId()))
                .collect(Collectors.toList());

        // 构建成员ID到成员的映射
        Map<Long, Member> memberMap = members.stream()
                .collect(Collectors.toMap(Member::getId, member -> member));

        // 分析和预测关系
        List<PredictedRelationship> predictedRelationships = analyzeAndPredict(members, existingRelationships, memberMap, familyId);

        // 生成数据补全建议
        List<String> missingDataSuggestions = generateMissingDataSuggestions(members);

        log.info("[AI关系预测] 完成预测，共生成 {} 条预测关系", predictedRelationships.size());

        return new RelationshipPredictionResponse(
                UUID.randomUUID().toString(),
                predictedRelationships,
                missingDataSuggestions,
                predictedRelationships.size()
        );
    }

    private List<PredictedRelationship> analyzeAndPredict(List<Member> members,
                                                          List<Relationship> existingRelationships,
                                                          Map<Long, Member> memberMap,
                                                          Long familyId) {
        List<PredictedRelationship> predictions = new ArrayList<>();

        // 遍历所有成员对
        for (int i = 0; i < members.size(); i++) {
            for (int j = i + 1; j < members.size(); j++) {
                Member member1 = members.get(i);
                Member member2 = members.get(j);

                // 检查是否已存在关系
                if (hasExistingRelationship(member1.getId(), member2.getId(), existingRelationships)) {
                    continue;
                }

                // 分析可能的关系
                PredictedRelationship prediction = predictRelationshipBetween(member1, member2);
                if (prediction != null) {
                    predictions.add(prediction);
                }
            }
        }

        // 按置信度排序
        predictions.sort((a, b) -> Double.compare(b.getConfidence(), a.getConfidence()));

        return predictions;
    }

    private PredictedRelationship predictRelationshipBetween(Member member1, Member member2) {
        List<String> reasoning = new ArrayList<>();
        double confidence = 0.0;
        String relationship = "relative";

        // 维度1: 年龄差异分析
        Map<String, Object> ageAnalysis = analyzeAgeDifference(member1, member2);
        if (ageAnalysis.containsKey("relationship")) {
            relationship = (String) ageAnalysis.get("relationship");
            confidence += (Double) ageAnalysis.get("confidence");
            reasoning.addAll((List<String>) ageAnalysis.get("reasoning"));
        }

        // 维度2: 姓名分析
        Map<String, Object> nameAnalysis = analyzeName(member1, member2);
        if (nameAnalysis.containsKey("confidence")) {
            confidence += (Double) nameAnalysis.get("confidence");
            reasoning.addAll((List<String>) nameAnalysis.get("reasoning"));
        }

        // 维度3: 性别分析
        Map<String, Object> genderAnalysis = analyzeGender(member1, member2);
        if (genderAnalysis.containsKey("confidence")) {
            confidence += (Double) genderAnalysis.get("confidence");
            reasoning.addAll((List<String>) genderAnalysis.get("reasoning"));
        }

        // 规范化置信度到0-1范围
        confidence = Math.min(confidence / 3.0, 1.0);

        // 如果置信度太低，不返回该预测
        if (confidence < 0.2) {
            return null;
        }

        // 构建预测结果
        PredictedMember predictedMember1 = convertToPredictedMember(member1);
        PredictedMember predictedMember2 = convertToPredictedMember(member2);

        return new PredictedRelationship(
                predictedMember1,
                predictedMember2,
                relationship,
                confidence,
                reasoning,
                generateAiComment(relationship, confidence),
                "pending"
        );
    }

    private Map<String, Object> analyzeAgeDifference(Member member1, Member member2) {
        Map<String, Object> result = new HashMap<>();
        List<String> reasoning = new ArrayList<>();

        if (member1.getBirthDate() != null && member2.getBirthDate() != null) {
            int ageDiff = Math.abs(member1.getBirthDate().getYear() - member2.getBirthDate().getYear());
            int year1 = member1.getBirthDate().getYear();
            int year2 = member2.getBirthDate().getYear();

            // 亲子关系
            if (ageDiff >= 18 && ageDiff <= 40) {
                result.put("relationship", year1 < year2 ? "parent" : "child");
                result.put("confidence", 0.8);
                reasoning.add(String.format("年龄差 %d 岁，符合亲子关系特征", ageDiff));
            }
            // 兄弟姐妹关系
            else if (ageDiff <= 10) {
                result.put("relationship", "sibling");
                result.put("confidence", 0.6);
                reasoning.add(String.format("年龄差 %d 岁，符合兄弟姐妹关系特征", ageDiff));
            }
            // 祖孙关系
            else if (ageDiff > 40 && ageDiff <= 70) {
                result.put("relationship", year1 < year2 ? "grandparent" : "grandchild");
                result.put("confidence", 0.5);
                reasoning.add(String.format("年龄差 %d 岁，符合祖孙关系特征", ageDiff));
            }
        } else {
            reasoning.add("缺少出生日期信息，无法进行年龄分析");
        }

        result.put("reasoning", reasoning);
        return result;
    }

    private Map<String, Object> analyzeName(Member member1, Member member2) {
        Map<String, Object> result = new HashMap<>();
        List<String> reasoning = new ArrayList<>();

        if (member1.getName() != null && member2.getName() != null) {
            // 获取姓氏（假设中文姓名，第一个字符是姓）
            String surname1 = member1.getName().length() > 0 ? member1.getName().substring(0, 1) : "";
            String surname2 = member2.getName().length() > 0 ? member2.getName().substring(0, 1) : "";

            if (surname1.equals(surname2)) {
                result.put("confidence", 0.4);
                reasoning.add(String.format("姓氏相同（%s），同一家族可能性高", surname1));
            } else {
                result.put("confidence", 0.1);
                reasoning.add("姓氏不同，可能是姻亲关系或不同家族");
            }
        }

        result.put("reasoning", reasoning);
        return result;
    }

    private Map<String, Object> analyzeGender(Member member1, Member member2) {
        Map<String, Object> result = new HashMap<>();
        List<String> reasoning = new ArrayList<>();

        // 简单的性别分析，主要用于辅助判断
        if (member1.getGender() != null && member2.getGender() != null) {
            if ("male".equals(member1.getGender()) && "male".equals(member2.getGender())) {
                reasoning.add("两位均为男性，可能是父子、兄弟、祖孙关系");
            } else if ("female".equals(member1.getGender()) && "female".equals(member2.getGender())) {
                reasoning.add("两位均为女性，可能是母女、姐妹、祖孙关系");
            } else {
                reasoning.add("一男一女，可能是父女、母子、配偶关系");
            }
            result.put("confidence", 0.2);
        }

        result.put("reasoning", reasoning);
        return result;
    }

    private boolean hasExistingRelationship(Long member1Id, Long member2Id, List<Relationship> existingRelationships) {
        return existingRelationships.stream()
                .anyMatch(rel -> (rel.getMember1Id().equals(member1Id) && rel.getMember2Id().equals(member2Id)) ||
                        (rel.getMember1Id().equals(member2Id) && rel.getMember2Id().equals(member1Id)));
    }

    private PredictedMember convertToPredictedMember(Member member) {
        Integer birthYear = member.getBirthDate() != null ? member.getBirthDate().getYear() : null;
        return new PredictedMember(
                member.getId(),
                member.getName(),
                birthYear,
                member.getGender(),
                null // nativePlace，后续可以扩展
        );
    }

    private String generateAiComment(String relationship, double confidence) {
        String relationshipName = getRelationshipName(relationship);
        if (confidence >= 0.8) {
            return String.format("根据数据分析，两位成员极可能是 %s 关系（置信度 %.0f%%）", relationshipName, confidence * 100);
        } else if (confidence >= 0.5) {
            return String.format("根据数据分析，两位成员可能是 %s 关系（置信度 %.0f%%），建议进一步验证", relationshipName, confidence * 100);
        } else {
            return String.format("数据不足以确定关系，两位成员可能是 %s 或其他亲戚关系", relationshipName);
        }
    }

    private String getRelationshipName(String relationship) {
        return switch (relationship) {
            case "parent" -> "父子/母子";
            case "child" -> "儿子/女儿";
            case "sibling" -> "兄弟姐妹";
            case "grandparent" -> "祖父/祖母";
            case "grandchild" -> "孙子/孙女";
            case "spouse" -> "配偶";
            default -> "亲戚";
        };
    }

    private List<String> generateMissingDataSuggestions(List<Member> members) {
        List<String> suggestions = new ArrayList<>();

        for (Member member : members) {
            if (member.getBirthDate() == null) {
                suggestions.add(String.format("建议补充成员 '%s' 的出生日期信息", member.getName()));
            }
            if (member.getGender() == null || member.getGender().isEmpty()) {
                suggestions.add(String.format("建议补充成员 '%s' 的性别信息", member.getName()));
            }
        }

        if (suggestions.isEmpty()) {
            suggestions.add("现有数据较为完整，可以进一步补充成员关系信息");
        }

        return suggestions;
    }

    public List<DataCompletionSuggestion> getDataCompletionSuggestions(Long familyId) {
        log.info("[AI数据补全] 为家族ID: {} 生成数据补全建议", familyId);

        List<Member> members = memberRepository.findAll().stream()
                .filter(member -> familyId.equals(member.getFamilyId()))
                .collect(Collectors.toList());

        List<DataCompletionSuggestion> suggestions = new ArrayList<>();

        for (Member member : members) {
            // 检查必填字段
            if (member.getBirthDate() == null) {
                suggestions.add(new DataCompletionSuggestion("field", member.getId(), member.getName(),
                        "出生日期为空，建议补充", "medium"));
            }
            if (member.getGender() == null || member.getGender().isEmpty()) {
                suggestions.add(new DataCompletionSuggestion("field", member.getId(), member.getName(),
                        "性别信息为空，建议补充", "medium"));
            }
        }

        log.info("[AI数据补全] 完成，共生成 {} 条建议", suggestions.size());
        return suggestions;
    }
}
