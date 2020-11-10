package com.kt.upms.service.impl;

import com.kt.upms.entity.UpmsUser;
import com.kt.upms.mapper.UpmsUserMapper;
import com.kt.upms.service.IUpmsUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-11-09
 */
@Service
public class UpmsUserServiceImpl extends ServiceImpl<UpmsUserMapper, UpmsUser> implements IUpmsUserService {

}
