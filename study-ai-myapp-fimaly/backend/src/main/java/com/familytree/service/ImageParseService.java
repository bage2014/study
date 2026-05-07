package com.familytree.service;

import com.familytree.ai.AiService;
import com.familytree.dto.ImageParseResponse;
import com.familytree.dto.ParsedMember;
import com.familytree.dto.ParsedRelationship;
import com.familytree.model.Member;
import com.familytree.model.Relationship;
import com.familytree.repository.MemberRepository;
import com.familytree.repository.RelationshipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageParseService {

    private final AiService aiService;
    private final MemberRepository memberRepository;
    private final RelationshipRepository relationshipRepository;

    public ImageParseResponse parseImage(Long familyId, String imageBase64, String imageName) {
        log.info("[图片解析服务] 开始解析图片，家族ID: {}", familyId);

        // 调用AI服务分析图片
        Map<String, Object> aiResult = aiService.analyzeImage(imageBase64, imageName);

        if (!(Boolean) aiResult.get("success")) {
            return new ImageParseResponse(
                    UUID.randomUUID().toString(),
                    false,
                    (String) aiResult.get("error"),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    0,
                    0
            );
        }

        String content = (String) aiResult.get("content");
        log.info("[图片解析服务] AI返回内容: {}", content);

        // 解析AI返回的内容
        List<ParsedMember> parsedMembers = parseMembers(content);
        List<ParsedRelationship> parsedRelationships = parseRelationships(content, parsedMembers);

        // 与现有数据比对
        Map<String, Member> existingMembers = getExistingMembers(familyId);
        Map<String, Set<String>> existingRelationships = getExistingRelationships(familyId);

        // 更新解析结果，标记已存在的成员和关系
        updateExistenceStatus(parsedMembers, parsedRelationships, existingMembers, existingRelationships);

        log.info("[图片解析服务] 解析完成，识别到 {} 个成员，{} 个关系",
                parsedMembers.size(), parsedRelationships.size());

        return new ImageParseResponse(
                UUID.randomUUID().toString(),
                true,
                "图片解析成功",
                parsedMembers,
                parsedRelationships,
                parsedMembers.size(),
                parsedRelationships.size()
        );
    }

    @Transactional
    public ImageParseResponse saveParseResult(Long familyId, List<ParsedMember> members, List<ParsedRelationship> relationships) {
        log.info("[图片解析服务] 开始保存解析结果，家族ID: {}", familyId);

        Map<String, Long> nameToIdMap = new HashMap<>();

        // 保存成员
        for (ParsedMember parsedMember : members) {
            if (parsedMember.isExists() && parsedMember.getExistingId() != null) {
                // 更新现有成员
                memberRepository.findById(parsedMember.getExistingId())
                        .ifPresent(member -> {
                            updateMemberFromParsed(member, parsedMember);
                            memberRepository.save(member);
                            nameToIdMap.put(parsedMember.getName(), member.getId());
                        });
            } else {
                // 创建新成员
                Member newMember = createMemberFromParsed(familyId, parsedMember);
                Member savedMember = memberRepository.save(newMember);
                nameToIdMap.put(parsedMember.getName(), savedMember.getId());
                log.info("[图片解析服务] 创建新成员: {}", savedMember.getName());
            }
        }

        // 保存关系
        for (ParsedRelationship parsedRelationship : relationships) {
            if (!parsedRelationship.isExists()) {
                Long member1Id = nameToIdMap.get(parsedRelationship.getMember1Name());
                Long member2Id = nameToIdMap.get(parsedRelationship.getMember2Name());

                if (member1Id != null && member2Id != null) {
                    Relationship relationship = new Relationship();
                    relationship.setFamilyId(familyId);
                    relationship.setMember1Id(member1Id);
                    relationship.setMember2Id(member2Id);
                    relationship.setRelationshipType(parsedRelationship.getRelationshipType());
                    relationshipRepository.save(relationship);
                    log.info("[图片解析服务] 创建新关系: {} - {} - {}",
                            parsedRelationship.getMember1Name(),
                            parsedRelationship.getRelationshipType(),
                            parsedRelationship.getMember2Name());
                }
            }
        }

        log.info("[图片解析服务] 保存完成");

        return new ImageParseResponse(
                UUID.randomUUID().toString(),
                true,
                "数据保存成功",
                members,
                relationships,
                members.size(),
                relationships.size()
        );
    }

    private List<ParsedMember> parseMembers(String content) {
        List<ParsedMember> members = new ArrayList<>();

        // 匹配成员列表部分
        Pattern memberSectionPattern = Pattern.compile("成员列表：?(.*?)(?=\n\n|关系列表|$)", Pattern.DOTALL);
        Matcher memberSectionMatcher = memberSectionPattern.matcher(content);

        if (memberSectionMatcher.find()) {
            String memberSection = memberSectionMatcher.group(1).trim();

            // 匹配每个成员
            Pattern memberPattern = Pattern.compile("-\\s*(.+?)(?=\\n-|$)", Pattern.DOTALL);
            Matcher memberMatcher = memberPattern.matcher(memberSection);

            long idCounter = 1;
            while (memberMatcher.find()) {
                String memberLine = memberMatcher.group(1).trim();
                ParsedMember member = parseMemberLine(memberLine, idCounter++);
                if (member != null) {
                    members.add(member);
                }
            }
        }

        return members;
    }

    private ParsedMember parseMemberLine(String line, long id) {
        try {
            String name = "";
            String gender = null;
            Integer birthYear = null;

            // 尝试解析姓名
            Pattern namePattern = Pattern.compile("([\\u4e00-\\u9fa5a-zA-Z]+)");
            Matcher nameMatcher = namePattern.matcher(line);
            if (nameMatcher.find()) {
                name = nameMatcher.group(1);
            }

            // 尝试解析性别
            if (line.contains("男")) {
                gender = "male";
            } else if (line.contains("女")) {
                gender = "female";
            }

            // 尝试解析出生年份
            Pattern yearPattern = Pattern.compile("(\\d{4})年");
            Matcher yearMatcher = yearPattern.matcher(line);
            if (yearMatcher.find()) {
                birthYear = Integer.parseInt(yearMatcher.group(1));
            }

            if (!name.isEmpty()) {
                return new ParsedMember(id, name, gender, birthYear, null, 0.8, false, null);
            }
        } catch (Exception e) {
            log.warn("[图片解析服务] 解析成员失败: {}", line, e);
        }

        return null;
    }

    private List<ParsedRelationship> parseRelationships(String content, List<ParsedMember> members) {
        List<ParsedRelationship> relationships = new ArrayList<>();

        // 匹配关系列表部分
        Pattern relationshipSectionPattern = Pattern.compile("关系列表：?(.*?)(?=\n\n|$)", Pattern.DOTALL);
        Matcher relationshipSectionMatcher = relationshipSectionPattern.matcher(content);

        if (relationshipSectionMatcher.find()) {
            String relationshipSection = relationshipSectionMatcher.group(1).trim();

            // 匹配每个关系
            Pattern relationshipPattern = Pattern.compile("-\\s*(.+?)(?=\\n-|$)", Pattern.DOTALL);
            Matcher relationshipMatcher = relationshipPattern.matcher(relationshipSection);

            while (relationshipMatcher.find()) {
                String relationshipLine = relationshipMatcher.group(1).trim();
                ParsedRelationship relationship = parseRelationshipLine(relationshipLine, members);
                if (relationship != null) {
                    relationships.add(relationship);
                }
            }
        }

        return relationships;
    }

    private ParsedRelationship parseRelationshipLine(String line, List<ParsedMember> members) {
        try {
            // 匹配 "姓名1 与 姓名2 的关系: 关系类型" 格式
            Pattern pattern = Pattern.compile("([\\u4e00-\\u9fa5a-zA-Z]+)\\s*与\\s*([\\u4e00-\\u9fa5a-zA-Z]+)\\s*的关系[：:]\\s*(.+)");
            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                String name1 = matcher.group(1).trim();
                String name2 = matcher.group(2).trim();
                String relationshipType = normalizeRelationshipType(matcher.group(3).trim());

                // 查找成员ID
                Long member1Id = findMemberIdByName(members, name1);
                Long member2Id = findMemberIdByName(members, name2);

                List<String> reasoning = new ArrayList<>();
                reasoning.add("从图片识别得出");

                return new ParsedRelationship(
                        member1Id, name1, member2Id, name2,
                        relationshipType, 0.7, reasoning, false
                );
            }
        } catch (Exception e) {
            log.warn("[图片解析服务] 解析关系失败: {}", line, e);
        }

        return null;
    }

    private Long findMemberIdByName(List<ParsedMember> members, String name) {
        return members.stream()
                .filter(m -> m.getName().equals(name))
                .map(ParsedMember::getId)
                .findFirst()
                .orElse(null);
    }

    private String normalizeRelationshipType(String type) {
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("父子", "parent");
        typeMap.put("母子", "parent");
        typeMap.put("父女", "parent");
        typeMap.put("母女", "parent");
        typeMap.put("父母", "parent");
        typeMap.put("儿子", "child");
        typeMap.put("女儿", "child");
        typeMap.put("兄弟姐妹", "sibling");
        typeMap.put("兄弟", "sibling");
        typeMap.put("姐妹", "sibling");
        typeMap.put("祖孙", "grandparent");
        typeMap.put("爷爷", "grandparent");
        typeMap.put("奶奶", "grandparent");
        typeMap.put("外公", "grandparent");
        typeMap.put("外婆", "grandparent");
        typeMap.put("孙子", "grandchild");
        typeMap.put("孙女", "grandchild");
        typeMap.put("配偶", "spouse");
        typeMap.put("夫妻", "spouse");
        typeMap.put("丈夫", "spouse");
        typeMap.put("妻子", "spouse");

        for (Map.Entry<String, String> entry : typeMap.entrySet()) {
            if (type.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "relative";
    }

    private Map<String, Member> getExistingMembers(Long familyId) {
        return memberRepository.findAll().stream()
                .filter(m -> familyId.equals(m.getFamilyId()))
                .collect(Collectors.toMap(Member::getName, m -> m, (m1, m2) -> m1));
    }

    private Map<String, Set<String>> getExistingRelationships(Long familyId) {
        Map<String, Set<String>> relationships = new HashMap<>();

        List<Relationship> existing = relationshipRepository.findAll().stream()
                .filter(r -> familyId.equals(r.getFamilyId()))
                .collect(Collectors.toList());

        Map<Long, String> idToNameMap = memberRepository.findAll().stream()
                .filter(m -> familyId.equals(m.getFamilyId()))
                .collect(Collectors.toMap(Member::getId, Member::getName));

        for (Relationship rel : existing) {
            String name1 = idToNameMap.get(rel.getMember1Id());
            String name2 = idToNameMap.get(rel.getMember2Id());
            if (name1 != null && name2 != null) {
                String key1 = name1 + "-" + name2;
                String key2 = name2 + "-" + name1;
                relationships.computeIfAbsent(key1, k -> new HashSet<>()).add(rel.getRelationshipType());
                relationships.computeIfAbsent(key2, k -> new HashSet<>()).add(rel.getRelationshipType());
            }
        }

        return relationships;
    }

    private void updateExistenceStatus(List<ParsedMember> members,
                                       List<ParsedRelationship> relationships,
                                       Map<String, Member> existingMembers,
                                       Map<String, Set<String>> existingRelationships) {
        // 更新成员状态
        for (ParsedMember member : members) {
            Member existing = existingMembers.get(member.getName());
            if (existing != null) {
                member.setExists(true);
                member.setExistingId(existing.getId());
            }
        }

        // 更新关系状态
        for (ParsedRelationship relationship : relationships) {
            String key = relationship.getMember1Name() + "-" + relationship.getMember2Name();
            if (existingRelationships.containsKey(key)) {
                relationship.setExists(true);
            }
        }
    }

    private Member createMemberFromParsed(Long familyId, ParsedMember parsed) {
        Member member = new Member();
        member.setFamilyId(familyId);
        member.setName(parsed.getName());
        member.setGender(parsed.getGender());
        if (parsed.getBirthYear() != null) {
            member.setBirthDate(java.sql.Date.valueOf(parsed.getBirthYear() + "-01-01"));
        }
        return member;
    }

    private void updateMemberFromParsed(Member member, ParsedMember parsed) {
        if (parsed.getGender() != null && member.getGender() == null) {
            member.setGender(parsed.getGender());
        }
        if (parsed.getBirthYear() != null && member.getBirthDate() == null) {
            member.setBirthDate(java.sql.Date.valueOf(parsed.getBirthYear() + "-01-01"));
        }
    }
}