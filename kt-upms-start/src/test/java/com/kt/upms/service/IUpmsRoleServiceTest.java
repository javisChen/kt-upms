package com.kt.upms.service;

import com.kt.upms.mapper.UpmsRoleMapper;
import com.kt.upms.service.impl.UpmsRoleServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @title:
 * @desc:
 * @author: Javis
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class IUpmsRoleServiceTest {

    @Autowired
    private IUpmsRoleService iUpmsRoleService;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void getRoleIdsByUserId() {
     List<Long> roleIdsByUserId = iUpmsRoleService.getRoleIdsByUserId(1L);
        System.out.println(roleIdsByUserId);
    }

    @Test
    public void getRoleIdsByUserGroupIds() {
    }
}