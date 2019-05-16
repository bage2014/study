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
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Default implementation of the {@link UserService} interface.  This service implements
 * operations related to User data.
 */
@Service("userService")
public class DefaultUserService implements UserService {

    List<User> list = new ArrayList<>();

    public DefaultUserService(){
        Role roleAdmin = new Role();
        roleAdmin.setId(1L);
        roleAdmin.setName("admin");
        roleAdmin.setPermissions(new HashSet<>(Arrays.asList("read,write")));
        Role roleUser = new Role();
        roleUser.setId(2L);
        roleUser.setName("user");
        roleUser.setPermissions(new HashSet<>(Arrays.asList("read")));

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword(new Sha256Hash("admin").toHex());
        user.setEmail("sample1@shiro.apache.org");
        user.setRoles(new HashSet<>(Arrays.asList(roleAdmin)));
        list.add(user);

        user = new User();
        user.setId(2L);
        user.setUsername("user");
        user.setPassword(new Sha256Hash("user").toHex());
        user.setEmail("sample2@shiro.apache.org");
        user.setRoles(new HashSet<>(Arrays.asList(roleUser)));
        list.add(user);
    }

    public User getCurrentUser() {
        final Long currentUserId = (Long) SecurityUtils.getSubject().getPrincipal();
        if (currentUserId != null) {
            return getUser(currentUserId);
        } else {
            return null;
        }
    }

    public void createUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(new Sha256Hash(password).toHex());
        list.add(user);
    }

    public List<User> getAllUsers() {
        return list;
    }

    public User getUser(Long userId) {
        User targetUser = new User();
        targetUser.setId(userId);

        int index = list.indexOf(targetUser);
        return index >= 0 ? list.get(index) : null;
    }

    @Override
    public User findUser(final String username) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUsername().equals(username)) {
                return list.get(i);
            }
        }
        return null;
    }

    public void deleteUser(Long userId) {
        User targetUser = new User();
        targetUser.setId(userId);

        list.remove(targetUser);
    }

    public void updateUser(User user) {
        int index = list.indexOf(user);
        if (index >= 0) {
            list.set(index, user);
        }
    }

}
