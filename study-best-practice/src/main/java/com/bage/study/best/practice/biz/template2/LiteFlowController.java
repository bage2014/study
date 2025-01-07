package com.bage.study.best.practice.biz.template2;

import com.bage.study.best.practice.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/test")
@Slf4j
public class LiteFlowController {

    @Autowired
    private ApplicationContext context;

    /**
     * 不使用框架，手动实现动态业务编排
     *
     * @param index 类名称
     * @return
     */
    @GetMapping(value = "chain")
    public String pattern(@RequestParam String index) {
        AddOrderContext contxt = new AddOrderContext();
        String[] split = index.split(",");

        for (String className : split) {
            // 此处直接根据类名从 spring 管理的上下文中进行获取。这里的类名是子类注解@Component("1")中自定义的，如果没有定义的话，默认使用类名
            // 使用这种方式可以保证类名不重复。
            ComponentAbstract msgHandler = (ComponentAbstract) context.getBean(className);
            if (ObjectUtils.isNotEmpty(msgHandler)) {
                msgHandler.handlerRequest(contxt);
            } else {
                log.info("没有找到对应的组件: {}", className);
            }
        }
        return JsonUtils.toJson(contxt);
    }
}
