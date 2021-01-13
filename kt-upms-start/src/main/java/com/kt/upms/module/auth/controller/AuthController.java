package com.kt.upms.module.auth.controller;

import com.kt.component.dto.ServerResponse;
import com.kt.component.dto.SingleResponse;
import com.kt.component.logger.CatchAndLog;
import com.kt.component.web.base.BaseController;
import com.kt.upms.module.auth.dto.LoginDTO;
import com.kt.upms.module.auth.dto.LoginUserDTO;
import com.kt.upms.module.auth.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CatchAndLog
public class AuthController extends BaseController {

    @Autowired
    private IAuthService iAuthService;

    @PostMapping("/login")
    public SingleResponse<LoginUserDTO> list(@RequestBody @Validated LoginDTO dto) {
        return SingleResponse.ok(iAuthService.login(dto));
    }
}

