package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.AppVersion;
import com.bage.my.app.end.point.service.AppVersionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(AppVersionController.class)
class AppVersionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppVersionService appVersionService;

    @Value("${app.update.file-dir:./app-updates}")
    private String fileDir;

    @Test
    void testUploadFile_Success() throws Exception {
        // 准备测试数据
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.apk",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "test file content".getBytes()
        );
        String version = "1.0.0";
        boolean forceUpdate = false;
        String releaseNotes = "测试版本";

        // 创建临时目录用于测试
        Path uploadPath = Paths.get(fileDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Mock服务层方法
        AppVersion mockAppVersion = new AppVersion(version, LocalDate.now(), releaseNotes, "/app/files/test.apk", forceUpdate, "test.apk");
        mockAppVersion.setId(1L);
        when(appVersionService.saveVersion(any(AppVersion.class))).thenReturn(mockAppVersion);

        // 执行请求
        mockMvc.perform(MockMvcRequestBuilders.multipart("/app/upload")
                .file(file)
                .param("version", version)
                .param("forceUpdate", String.valueOf(forceUpdate))
                .param("releaseNotes", releaseNotes)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("文件上传和版本保存成功"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.versionId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.version").value(version));

        // 验证服务层方法被调用
        Mockito.verify(appVersionService).saveVersion(any(AppVersion.class));
    }

    @Test
    void testUploadFile_Failure() throws Exception {
        // 准备测试数据，使用null文件来模拟上传失败
        String version = "1.0.0";
        boolean forceUpdate = false;

        // 执行请求，由于文件为null，应返回500错误
        mockMvc.perform(MockMvcRequestBuilders.multipart("/app/upload")
                .file(new MockMultipartFile("file", "", null, (byte[]) null))
                .param("version", version)
                .param("forceUpdate", String.valueOf(forceUpdate))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(500))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("文件上传失败"));
    }
}