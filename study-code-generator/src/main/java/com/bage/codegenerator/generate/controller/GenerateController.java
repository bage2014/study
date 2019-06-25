package com.bage.codegenerator.generate.controller;

import com.bage.codegenerator.generate.model.ClassInfo;
import com.bage.codegenerator.generate.util.CodeGeneratorUtils;
import com.bage.codegenerator.generate.util.FreemarkerTool;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/code")
public class GenerateController {

    @Autowired
    FreemarkerTool freemarkerTool;

    @RequestMapping("/controller")
    public Object controller() {

        try {

            String sql = "CREATE TABLE `userinfo` (\n" +
                    "  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',\n" +
                    "  `username` varchar(255) NOT NULL COMMENT '用户名',\n" +
                    "  `addtime` datetime NOT NULL COMMENT '创建时间',\n" +
                    "  PRIMARY KEY (`user_id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息'";
            // parse table
            ClassInfo classInfo = CodeGeneratorUtils.processTableIntoClassInfo(sql);

            // code genarete
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("classInfo", classInfo);

            // result
            Map<String, String> result = new HashMap<String, String>();

            String controllerCode = freemarkerTool.processString("ftl/controller.ftl", params);
            result.put("controller_code", controllerCode);
            result.put("service_code", freemarkerTool.processString("ftl/service.ftl", params));
            result.put("service_impl_code", freemarkerTool.processString("ftl/service_impl.ftl", params));

            result.put("dao_code", freemarkerTool.processString("ftl/dao.ftl", params));
            result.put("mybatis_code", freemarkerTool.processString("ftl/mybatis.ftl", params));
            result.put("model_code", freemarkerTool.processString("ftl/model.ftl", params));

            System.out.println(controllerCode);

            return controllerCode;
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping("/service")
    public Object service() {

        try {

            String sql = "CREATE TABLE `userinfo` (\n" +
                    "  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',\n" +
                    "  `username` varchar(255) NOT NULL COMMENT '用户名',\n" +
                    "  `addtime` datetime NOT NULL COMMENT '创建时间',\n" +
                    "  PRIMARY KEY (`user_id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息'";
            // parse table
            ClassInfo classInfo = CodeGeneratorUtils.processTableIntoClassInfo(sql);

            // code genarete
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("classInfo", classInfo);

            return freemarkerTool.processString("ftl/service.ftl", params);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }
}
