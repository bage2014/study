/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.bage;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AccountInfoController {

    @RequestMapping("/api1/hello")
    public String api1() {
        System.out.println("api1");
        return "api1";
    }
    @RequestMapping("/api2/hello")
    public String api2() {
        System.out.println("api1");
        return "api2";
    }
    @RequestMapping("/api3/hello")
    public String api3() {
        System.out.println("api1");
        return "api3";
    }
    @RequestMapping("/api4/hello")
    public String api4() {
        System.out.println("api4");
        return "api4";
    }
    @RequestMapping("/api5/hello")
    public String api5() {
        System.out.println("api5");
        return "api5";
    }

    @RequiresRoles("admin")
    @RequestMapping("/admin")
    public String admin() {

        String name = "World";

        Subject subject = SecurityUtils.getSubject();

        PrincipalCollection principalCollection = subject.getPrincipals();

        if (principalCollection != null && !principalCollection.isEmpty()) {
            name = principalCollection.getPrimaryPrincipal().toString();
        }

        return "admin-info" + name;
    }
    @RequiresRoles("user")
    @RequestMapping("/user")
    public String user() {

        String name = "World";

        Subject subject = SecurityUtils.getSubject();

        PrincipalCollection principalCollection = subject.getPrincipals();

        if (principalCollection != null && !principalCollection.isEmpty()) {
            name = principalCollection.getPrimaryPrincipal().toString();
        }

        return "user-info" + name;
    }

}
