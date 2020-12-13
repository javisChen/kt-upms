package com.kt.upms.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.dto.PageResponse;
import com.kt.component.exception.BizException;
import com.kt.component.logger.CatchAndLog;
import com.kt.model.dto.UserAddDTO;
import com.kt.model.dto.UserQueryDTO;
import com.kt.model.dto.UserUpdateDTO;
import com.kt.model.enums.BizEnum;
import com.kt.upms.constants.UpmsConsts;
import com.kt.upms.entity.UpmsUser;
import com.kt.upms.enums.UserStatusEnum;
import com.kt.upms.mapper.UpmsUserMapper;
import com.kt.upms.service.IUpmsUserService;
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

    @Override
    public UserAddDTO save(UserAddDTO userAddDTO) {
        UpmsUser upmsUser = CglibUtil.copy(userAddDTO, UpmsUser.class);

        String phone = upmsUser.getPhone();
        UpmsUser queryUser = this.getOne(new LambdaQueryWrapper<>(UpmsUser.class).eq(UpmsUser::getPhone, phone));
        if (queryUser != null) {
            throw new BizException(BizEnum.PHONE_ALREADY_EXISTS.getCode(), BizEnum.PHONE_ALREADY_EXISTS.getMsg());
        }
        upmsUser.setStatus(UserStatusEnum.NORMAL.getValue());
        upmsUser.setPassword(DigestUtil.bcrypt(phone + upmsUser.getPassword() + UpmsConsts.USER_SALT));
        this.save(upmsUser);
        upmsUser.setPassword(null);

        CglibUtil.copy(upmsUser, userAddDTO);
        return userAddDTO;
    }

    @Override
    public UserUpdateDTO updateUserById(UserUpdateDTO userUpdateDTO) {
        // 该接口禁止修改密码，修改密码用单独的接口
        UpmsUser upmsUser = CglibUtil.copy(userUpdateDTO, UpmsUser.class);
        this.updateById(upmsUser);

        CglibUtil.copy(upmsUser, userUpdateDTO);
        return userUpdateDTO;
    }

    @Override
    public PageResponse<UpmsUser> pageList(IPage<UpmsUser> page, UserQueryDTO params) {
        IPage<UpmsUser> result = this.page(page, new QueryWrapper<UpmsUser>()
                .like(StrUtil.isNotBlank(params.getPhone()), "phone", params.getPhone())
                .like(StrUtil.isNotBlank(params.getName()), "name", params.getName())
                .select("id", "phone", "name", "status"));
        return PageResponse.success(result);
    }

    @Override
    public void disableUser(UserUpdateDTO userUpdateDTO) {
        updateUserStatus(userUpdateDTO, UserStatusEnum.LOCKED);
    }

    @Override
    public void enableUser(UserUpdateDTO userUpdateDTO) {
        updateUserStatus(userUpdateDTO, UserStatusEnum.NORMAL);
    }

    private void updateUserStatus(UserUpdateDTO userUpdateDTO, UserStatusEnum normal) {
        this.update(new LambdaUpdateWrapper<UpmsUser>()
                .eq(UpmsUser::getStatus, userUpdateDTO.getId())
                .set(UpmsUser::getStatus, normal.getValue()));
    }

}
