package com.kt.upms.service;

import com.kt.demo.DemoService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @title:
 * @desc:
 * @author: Javis
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class IUpmsUserServiceTest {

    @Autowired
    private IUpmsUserService iUpmsUserService;


    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void saveAndReturn() {
//        System.out.println(iUpmsUserService.save(null));
    }
}