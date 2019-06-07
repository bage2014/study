package com.bage;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pathdef")
public class PathController {

    @Autowired
    private ShiroFilterFactoryBean shiroFilterFactoryBean;

    @Autowired
    PathDefinitionMapper pathDefinitionMapper;

    @RequestMapping("/insert")
    public String insert(){
        int res = pathDefinitionMapper.insert(new PathDefinition(6, "/api6/**", "authc,roles[bage-role],perms[\"read\"]"));
        return String.valueOf(res);
    }

    @RequestMapping("/refresh")
    public String refresh(){
        AbstractShiroFilter shiroFilter = null;
        try {
            shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
        } catch (Exception e) {
            throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
        }

        PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                .getFilterChainResolver();
        DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
                .getFilterChainManager();

        System.out.println("前空前：" + manager.getFilterChains());
        // 清空老的权限控制
        manager.getFilterChains().clear();
        System.out.println("前空后：" + manager.getFilterChains());

        shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
        // 重新构建生成
        List<PathDefinition> list = pathDefinitionMapper.queryAll();

        for (PathDefinition item : list) {
            System.out.println("item::" + item);
            manager.createChain(item.getAntPath(),item.getDefinition());
        }
        manager.createChain("/login.html", "authc"); // need to accept POSTs from the login form
        manager.createChain("/logout", "logout");
        System.out.println("重新添加后：" + manager.getFilterChains());
        return "OK";
    }

}
