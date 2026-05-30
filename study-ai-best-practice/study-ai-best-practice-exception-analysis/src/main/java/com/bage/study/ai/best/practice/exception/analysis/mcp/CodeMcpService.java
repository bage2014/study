package com.bage.study.ai.best.practice.exception.analysis.mcp;

import com.bage.study.ai.best.practice.exception.analysis.context.CodeInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CodeMcpService {

    public List<CodeInfo> getRelatedCode(String appId, String errorKeyword) {
        List<CodeInfo> codeList = new ArrayList<>();

        CodeInfo code1 = new CodeInfo();
        code1.setAppId(appId);
        code1.setRepositoryUrl("https://github.com/example/app-backend");
        code1.setBranch("main");
        code1.setCommitId("a1b2c3d");
        code1.setFile("src/main/java/com/example/service/UserService.java");
        code1.setMethod("login");
        code1.setCodeSnippet("public User login(String username, String password) {\n    User user = userRepository.findByUsername(username);\n    if (user == null) {\n        throw new AuthenticationException(\"用户不存在\");\n    }\n    if (!passwordEncoder.matches(password, user.getPassword())) {\n        throw new AuthenticationException(\"密码错误\");\n    }\n    return user;\n}");
        code1.setLastModified(LocalDateTime.now().minusDays(3));
        code1.setAuthor("developer");
        codeList.add(code1);

        CodeInfo code2 = new CodeInfo();
        code2.setAppId(appId);
        code2.setRepositoryUrl("https://github.com/example/app-backend");
        code2.setBranch("main");
        code2.setCommitId("e4f5g6h");
        code2.setFile("src/main/java/com/example/controller/UserController.java");
        code2.setMethod("handleLogin");
        code2.setCodeSnippet("@PostMapping(\"/login\")\npublic ResponseEntity<?> handleLogin(@RequestBody LoginRequest request) {\n    try {\n        User user = userService.login(request.getUsername(), request.getPassword());\n        String token = jwtService.generateToken(user);\n        return ResponseEntity.ok(new LoginResponse(token));\n    } catch (AuthenticationException e) {\n        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());\n    }\n}");
        code2.setLastModified(LocalDateTime.now().minusDays(2));
        code2.setAuthor("developer");
        codeList.add(code2);

        return codeList;
    }
}
