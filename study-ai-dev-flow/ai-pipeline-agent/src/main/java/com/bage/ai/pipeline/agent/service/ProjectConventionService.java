package com.bage.ai.pipeline.agent.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class ProjectConventionService {

    private final ObjectMapper objectMapper;

    public ProjectConventionService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ProjectConvention detectConventions(String projectPath) {
        ProjectConvention convention = new ProjectConvention();
        
        convention.setProjectPath(projectPath);
        
        String basePackage = detectBasePackage(projectPath);
        convention.setBasePackage(basePackage);
        
        if (basePackage != null) {
            convention.setBasePackagePath(basePackage.replace('.', '/'));
            convention.setControllerPackage(basePackage + ".controller");
            convention.setServicePackage(basePackage + ".service");
            convention.setEntityPackage(basePackage + ".entity");
            convention.setRepositoryPackage(basePackage + ".repository");
            convention.setDtoPackage(basePackage + ".dto");
        }
        
        detectCodingPatterns(convention, projectPath);
        
        readConventionFile(convention, projectPath);
        
        return convention;
    }

    private String detectBasePackage(String projectPath) {
        Path srcMainJava = Paths.get(projectPath, "src", "main", "java");
        
        if (!Files.exists(srcMainJava)) {
            return null;
        }

        try (Stream<Path> paths = Files.walk(srcMainJava)) {
            List<Path> javaFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .collect(Collectors.toList());

            if (javaFiles.isEmpty()) {
                return null;
            }

            Path firstJavaFile = javaFiles.get(0);
            Path relativePath = srcMainJava.relativize(firstJavaFile);
            
            List<String> parts = new ArrayList<>();
            for (Path part : relativePath) {
                parts.add(part.toString());
            }
            
            if (parts.size() >= 3) {
                return String.join(".", parts.subList(0, 3));
            } else if (parts.size() == 2) {
                return String.join(".", parts.subList(0, 2));
            }
        } catch (IOException e) {
            log.warn("Failed to detect base package: {}", e.getMessage());
        }
        
        return null;
    }

    private void detectCodingPatterns(ProjectConvention convention, String projectPath) {
        Path srcMainJava = Paths.get(projectPath, "src", "main", "java");
        
        if (!Files.exists(srcMainJava)) {
            return;
        }

        Set<String> imports = new HashSet<>();
        Set<String> annotations = new HashSet<>();
        
        try (Stream<Path> paths = Files.walk(srcMainJava)) {
            paths.filter(Files::isRegularFile)
                 .filter(p -> p.toString().endsWith(".java"))
                 .forEach(path -> {
                     try {
                         String content = Files.readString(path);
                         
                         String importPattern = "import\\s+([a-zA-Z0-9_.]+);";
                         java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(importPattern);
                         java.util.regex.Matcher matcher = pattern.matcher(content);
                         while (matcher.find()) {
                             imports.add(matcher.group(1));
                         }
                         
                         String annotationPattern = "@([a-zA-Z]+)";
                         pattern = java.util.regex.Pattern.compile(annotationPattern);
                         matcher = pattern.matcher(content);
                         while (matcher.find()) {
                             annotations.add(matcher.group(1));
                         }
                     } catch (IOException e) {
                         // ignore
                     }
                 });
        } catch (IOException e) {
            log.warn("Failed to detect coding patterns: {}", e.getMessage());
        }
        
        if (imports.contains("lombok.Data") || annotations.contains("Data")) {
            convention.setUseLombok(true);
        }
        if (imports.contains("org.springframework.data.jpa.repository.JpaRepository")) {
            convention.setUseJpaRepository(true);
        }
        if (imports.contains("org.springframework.data.repository.CrudRepository")) {
            convention.setUseCrudRepository(true);
        }
        if (imports.contains("org.springframework.web.bind.annotation.RestController")) {
            convention.setControllerAnnotation("RestController");
        }
        if (imports.contains("org.springframework.web.bind.annotation.RequestMapping")) {
            convention.setRequestMappingAnnotation("RequestMapping");
        }
        
        if (imports.stream().anyMatch(i -> i.startsWith("io.swagger")) || 
            imports.stream().anyMatch(i -> i.startsWith("org.springdoc"))) {
            convention.setUseSwagger(true);
        }
        
        convention.setDetectedImports(imports);
    }

    private void readConventionFile(ProjectConvention convention, String projectPath) {
        Path conventionFile = Paths.get(projectPath, ".pipeline-convention.yaml");
        if (Files.exists(conventionFile)) {
            try {
                String content = Files.readString(conventionFile);
                YamlConvention yamlConvention = parseYaml(content);
                
                if (yamlConvention.getBasePackage() != null) {
                    convention.setBasePackage(yamlConvention.getBasePackage());
                    convention.setBasePackagePath(yamlConvention.getBasePackage().replace('.', '/'));
                }
                if (yamlConvention.getControllerPackage() != null) {
                    convention.setControllerPackage(yamlConvention.getControllerPackage());
                }
                if (yamlConvention.getServicePackage() != null) {
                    convention.setServicePackage(yamlConvention.getServicePackage());
                }
                if (yamlConvention.getEntityPackage() != null) {
                    convention.setEntityPackage(yamlConvention.getEntityPackage());
                }
                if (yamlConvention.getRepositoryPackage() != null) {
                    convention.setRepositoryPackage(yamlConvention.getRepositoryPackage());
                }
                if (yamlConvention.getDtoPackage() != null) {
                    convention.setDtoPackage(yamlConvention.getDtoPackage());
                }
                if (yamlConvention.getCodeStyle() != null) {
                    convention.setCodeStyle(yamlConvention.getCodeStyle());
                }
                if (yamlConvention.getDependencies() != null) {
                    convention.setDependencies(yamlConvention.getDependencies());
                }
                if (yamlConvention.getApiPrefix() != null) {
                    convention.setApiPrefix(yamlConvention.getApiPrefix());
                }
                if (yamlConvention.getSkillPaths() != null && !yamlConvention.getSkillPaths().isEmpty()) {
                    convention.setSkillPaths(yamlConvention.getSkillPaths());
                }
                if (yamlConvention.getEnabledSkills() != null && !yamlConvention.getEnabledSkills().isEmpty()) {
                    convention.setEnabledSkills(yamlConvention.getEnabledSkills());
                }
                
                log.info("Loaded project convention from: {}", conventionFile);
            } catch (IOException e) {
                log.warn("Failed to read convention file: {}", e.getMessage());
            }
        }
        
        StringBuilder skillContentBuilder = new StringBuilder();
        
        skillContentBuilder.append(readGlobalSkills(convention));
        
        Path skillFile = Paths.get(projectPath, "SKILL.md");
        if (Files.exists(skillFile)) {
            try {
                skillContentBuilder.append("\n\n## Project Specific Skills\n");
                skillContentBuilder.append(Files.readString(skillFile));
                log.info("Loaded SKILL.md from: {}", skillFile);
            } catch (IOException e) {
                log.warn("Failed to read SKILL.md: {}", e.getMessage());
            }
        }
        
        if (skillContentBuilder.length() > 0) {
            convention.setSkillContent(skillContentBuilder.toString());
        }
    }

    private String readGlobalSkills(ProjectConvention convention) {
        StringBuilder skillsBuilder = new StringBuilder();
        
        List<String> skillDirectories;
        if (convention.getSkillPaths() != null && !convention.getSkillPaths().isEmpty()) {
            skillDirectories = convention.getSkillPaths().stream()
                    .map(path -> path.replace("~", System.getProperty("user.home")))
                    .collect(Collectors.toList());
        } else {
            skillDirectories = List.of(
                    System.getProperty("user.home") + "/.trae/skills",
                    System.getProperty("user.home") + "/.trae-cn/skills"
            );
        }
        
        List<String> requiredSkills;
        if (convention.getEnabledSkills() != null && !convention.getEnabledSkills().isEmpty()) {
            requiredSkills = convention.getEnabledSkills();
        } else {
            requiredSkills = List.of(
                    "personal-backend-coding-standard",
                    "common-coding"
            );
        }
        
        for (String skillDir : skillDirectories) {
            Path dirPath = Paths.get(skillDir);
            if (!Files.exists(dirPath)) {
                log.debug("Skill directory not found: {}", skillDir);
                continue;
            }
            
            for (String skillName : requiredSkills) {
                Path skillPath = dirPath.resolve(skillName).resolve("SKILL.md");
                if (Files.exists(skillPath)) {
                    try {
                        String content = Files.readString(skillPath);
                        String skillHeader = "---\n# Global Skill: " + skillName + "\n---\n";
                        skillsBuilder.append(skillHeader);
                        skillsBuilder.append(content);
                        skillsBuilder.append("\n");
                        log.info("Loaded global skill from: {}", skillPath);
                    } catch (IOException e) {
                        log.warn("Failed to read global skill {}: {}", skillName, e.getMessage());
                    }
                }
            }
        }
        
        return skillsBuilder.toString();
    }

    private YamlConvention parseYaml(String content) {
        YamlConvention result = new YamlConvention();
        
        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("basePackage:")) {
                result.setBasePackage(line.substring("basePackage:".length()).trim());
            } else if (line.startsWith("controllerPackage:")) {
                result.setControllerPackage(line.substring("controllerPackage:".length()).trim());
            } else if (line.startsWith("servicePackage:")) {
                result.setServicePackage(line.substring("servicePackage:".length()).trim());
            } else if (line.startsWith("entityPackage:")) {
                result.setEntityPackage(line.substring("entityPackage:".length()).trim());
            } else if (line.startsWith("repositoryPackage:")) {
                result.setRepositoryPackage(line.substring("repositoryPackage:".length()).trim());
            } else if (line.startsWith("dtoPackage:")) {
                result.setDtoPackage(line.substring("dtoPackage:".length()).trim());
            } else if (line.startsWith("codeStyle:")) {
                result.setCodeStyle(line.substring("codeStyle:".length()).trim());
            } else if (line.startsWith("apiPrefix:")) {
                result.setApiPrefix(line.substring("apiPrefix:".length()).trim());
            }
        }
        
        return result;
    }

    public String buildPromptConvention(ProjectConvention convention) {
        StringBuilder sb = new StringBuilder();
        
        if (convention.getBasePackage() != null) {
            sb.append("\n\n## Project Convention\n");
            sb.append("- Base Package: ").append(convention.getBasePackage()).append("\n");
            
            if (convention.getControllerPackage() != null) {
                sb.append("- Controller Package: ").append(convention.getControllerPackage()).append("\n");
            }
            if (convention.getServicePackage() != null) {
                sb.append("- Service Package: ").append(convention.getServicePackage()).append("\n");
            }
            if (convention.getEntityPackage() != null) {
                sb.append("- Entity Package: ").append(convention.getEntityPackage()).append("\n");
            }
            if (convention.getRepositoryPackage() != null) {
                sb.append("- Repository Package: ").append(convention.getRepositoryPackage()).append("\n");
            }
            if (convention.getDtoPackage() != null) {
                sb.append("- DTO Package: ").append(convention.getDtoPackage()).append("\n");
            }
            
            sb.append("\n### IMPORTANT: Use these package names for ALL generated code!\n");
            sb.append("File paths should follow: src/main/java/").append(convention.getBasePackagePath()).append("/...\n");
        }
        
        if (convention.isUseLombok()) {
            sb.append("- Use Lombok annotations (@Data, @NoArgsConstructor, @AllArgsConstructor, @Builder)\n");
        }
        
        if (convention.isUseJpaRepository()) {
            sb.append("- Use JpaRepository for data access\n");
        } else if (convention.isUseCrudRepository()) {
            sb.append("- Use CrudRepository for data access\n");
        }
        
        if (convention.getControllerAnnotation() != null) {
            sb.append("- Controller annotation: @").append(convention.getControllerAnnotation()).append("\n");
        }
        
        if (convention.getApiPrefix() != null) {
            sb.append("- API prefix: ").append(convention.getApiPrefix()).append("\n");
        }
        
        if (convention.getSkillContent() != null && !convention.getSkillContent().isBlank()) {
            sb.append("\n## Project Skills\n");
            sb.append(convention.getSkillContent().substring(0, Math.min(convention.getSkillContent().length(), 2000)));
        }
        
        return sb.toString();
    }

    @Data
    public static class ProjectConvention {
        private String projectPath;
        private String basePackage;
        private String basePackagePath;
        private String controllerPackage;
        private String servicePackage;
        private String entityPackage;
        private String repositoryPackage;
        private String dtoPackage;
        private String codeStyle;
        private String apiPrefix;
        private List<String> dependencies = new ArrayList<>();
        private boolean useLombok;
        private boolean useJpaRepository;
        private boolean useCrudRepository;
        private boolean useSwagger;
        private String controllerAnnotation;
        private String requestMappingAnnotation;
        private Set<String> detectedImports = new HashSet<>();
        private String skillContent;
        private List<String> skillPaths = new ArrayList<>();
        private List<String> enabledSkills = new ArrayList<>();
    }

    @Data
    private static class YamlConvention {
        private String basePackage;
        private String controllerPackage;
        private String servicePackage;
        private String entityPackage;
        private String repositoryPackage;
        private String dtoPackage;
        private String codeStyle;
        private String apiPrefix;
        private List<String> dependencies;
        private List<String> skillPaths;
        private List<String> enabledSkills;
    }
}