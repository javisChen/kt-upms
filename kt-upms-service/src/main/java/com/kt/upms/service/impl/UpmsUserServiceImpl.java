package com.kt.upms.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.exception.BizException;
import com.kt.component.logger.CatchAndLog;
import com.kt.model.dto.UserAddDTO;
import com.kt.model.enums.BizEnum;
import com.kt.upms.constants.UpmsConsts;
import com.kt.upms.entity.UpmsUser;
import com.kt.upms.mapper.UpmsUserMapper;
import com.kt.upms.service.IUpmsUserService;
import org.springframework.cglib.beans.BeanCopier;
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
@CatchAndLog
public class UpmsUserServiceImpl extends ServiceImpl<UpmsUserMapper, UpmsUser> implements IUpmsUserService {

    private static final BeanCopier BEAN_COPIER = BeanCopier.create(UserAddDTO.class, UpmsUser.class, false);
    private static final BeanCopier copier2 = BeanCopier.create(UpmsUser.class, UserAddDTO.class, false);

    @Override
    public UserAddDTO save(UserAddDTO userAddDTO) {
        UpmsUser upmsUser = CglibUtil.copy(userAddDTO, UpmsUser.class);

        String phone = upmsUser.getPhone();
        UpmsUser queryUser = this.getOne(new LambdaQueryWrapper<>(UpmsUser.class).eq(UpmsUser::getPhone, phone));
        if (queryUser != null) {
            throw new BizException(BizEnum.PHONE_ALREADY_EXISTS.getCode(), BizEnum.PHONE_ALREADY_EXISTS.getMsg());
        }
        upmsUser.setStatus(UpmsUser.StatusEnum.NORMAL.getValue());
        upmsUser.setPassword(DigestUtil.bcrypt(phone + upmsUser.getPassword() + UpmsConsts.USER_SALT));
        this.save(upmsUser);
        upmsUser.setPassword(null);

        CglibUtil.copy(upmsUser, userAddDTO);
        return userAddDTO;
    }

    @Override
    public UpmsUser updateUser(UpmsUser upmsUser) {
        // 该接口禁止修改密码，修改密码用单独的接口

        upmsUser.setPassword(null);
        this.updateById(upmsUser);;
        return upmsUser;
    }

}
