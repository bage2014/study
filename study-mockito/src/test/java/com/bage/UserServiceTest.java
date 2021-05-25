package com.bage;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserServiceImpl userService;
    private FileServiceImpl fileService;

    @Before
    public void init() {
        userService = new UserServiceImpl();
        fileService = mock(FileServiceImpl.class);
        ReflectionTestUtils.setField(userService, "fileService", fileService);
    }

    @Test
    public void hello() {
        when(fileService.hello()).thenReturn("hello-mock-file");
        System.out.println(userService.hello());
    }

}
