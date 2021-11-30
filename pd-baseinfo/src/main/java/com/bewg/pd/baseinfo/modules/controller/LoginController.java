package com.bewg.pd.baseinfo.modules.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bewg.pd.baseinfo.modules.service.ISysUserService;
import com.bewg.pd.common.constant.CommonConstant;
import com.bewg.pd.common.dict.ERROR_TYPE;
import com.bewg.pd.common.entity.po.SysUser;
import com.bewg.pd.common.entity.vo.Result;
import com.bewg.pd.common.exception.PdException;
import com.bewg.pd.common.system.service.ISysLogService;
import com.bewg.pd.common.system.vo.LoginUser;
import com.bewg.pd.common.util.JwtUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Description: 登录
 * @Author: lizy
 * @Date: 2019-12-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "登录")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginController {

    private final RedisTemplate redisTemplate;

    private final ISysLogService sysLogService;
    private final ISysUserService sysUserService;

    /**
     * 登录
     *
     * @return
     */
    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping(value = "/login")
    @ApiIgnore
    public Result<LoginUser> login(@RequestBody LoginParam param) {
        Result result = new Result<>();
        if (StringUtils.isBlank(param.getUserName())) {
            throw new PdException("用户名或者手机号为空");
        }
        if (StringUtils.isBlank(param.getPassword())) {
            throw new PdException("密码为空");
        }
        LoginUser loginUser = new LoginUser();
        result.setResult(loginUser);
        result.setSuccess(true);
        return result;
    }

    /**
     * 登出
     *
     * @return
     */
    @ApiOperation(value = "登出", notes = "登出")
    @GetMapping("/logout")
    @ApiIgnore
    public Result logOut(HttpServletRequest request) {
        String accessToken = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        String username = JwtUtil.getUsername(accessToken);
        SysUser user = sysUserService.getUserByName(username);
        if (user != null) {
            sysLogService.addLog("用户名: " + user.getRealname() + ",退出成功！", CommonConstant.LOG_TYPE_1, null);
            SecurityUtils.getSubject().logout();
            String tokenPrefix = CommonConstant.PREFIX_USER_TOKEN + username + CommonConstant.SUFFIX_USER_TOKEN;
            /*删除缓存*/
            redisTemplate.delete(tokenPrefix + accessToken);
            return Result.ok("退出成功");
        } else {
            throw new PdException(ERROR_TYPE.TOKEN_ILLEGAL.getDescription());
        }

    }

    @Data
    @EqualsAndHashCode
    @Accessors(chain = true)
    @ApiModel(value = "登录参数")
    static class LoginParam {
        @ApiModelProperty(value = "用户名")
        private String userName;

        @ApiModelProperty(value = "密码")
        private String password;
    }
}
