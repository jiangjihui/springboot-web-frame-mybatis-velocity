package com.jjh.framework.shiro;

import cn.hutool.core.util.StrUtil;
import com.jjh.business.system.user.model.SysUser;
import com.jjh.business.system.user.service.SysUserService;
import com.jjh.common.constant.BaseConstants;
import com.jjh.common.key.CacheConstants;
import com.jjh.framework.jwt.JwtToken;
import com.jjh.framework.jwt.JwtUtil;
import com.jjh.framework.redis.RedisRepository;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class MyShiroRealm extends AuthorizingRealm {

    @Lazy   //https://www.jianshu.com/p/8dce8a2e94cf
    @Autowired
    private SysUserService sysUserService;

    @Lazy
    @Autowired
    private RedisRepository redisRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthorizingRealm.class);

    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    //权限配置
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        SysUser sysUser = (SysUser) principalCollection.getPrimaryPrincipal();

        //获取用户角色
        List<String> roleList = sysUserService.listSysRoleCode(sysUser.getId());
        // 获取用户权限
        List<String> permissionList = sysUserService.listSysPermissionCode(sysUser.getId());
        sysUser.setRoleCode(roleList);
        sysUser.setPermissionCode(permissionList);
        // 从用户信息获取，不重复调用接口获取角色权限。（直接将权限信息缓存到用户信息，随用户信息走）
        authorizationInfo.setRoles(new HashSet<String>(sysUser.getRoleCode()));
        authorizationInfo.setStringPermissions(new HashSet<String>(sysUser.getPermissionCode()));

        return authorizationInfo;
    }

    //身份配置
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        /*JWT校验*/
        return jwtAuth(authenticationToken);

        /*Shiro校验*/
        //获取用户的输入的账号.
 /*       String username = (String)authenticationToken.getPrincipal();
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        UserInfo userInfo = userInfoService.findByUsername(username);
        logger.info("Get userInfo: "+userInfo);
        if(userInfo == null){
            return null;
        }

        //获取用户角色
        List<String> roleList = userInfoService.listSysRoleCode(userInfo.getId());
        // 获取用户权限
        List<String> permissionList = userInfoService.listSysPermissionCode(userInfo.getId());
        userInfo.setRoleCode(roleList);
        userInfo.setPermissionCode(permissionList);

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userInfo, //用户名
                userInfo.getPassword(), //密码
                ByteSource.Util.bytes(userInfo.getCredentialsSalt()),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;*/
    }

    /**
     * JWT用户身份认证
     * @return
     */
    public AuthenticationInfo jwtAuth(AuthenticationToken authenticationToken) {
        String token = (String) authenticationToken.getCredentials();
        SysUser sysUser = this.tokenAuth(token);
        return new SimpleAuthenticationInfo(sysUser, token, "MyRealm");
    }

    /**
     * token 校验
     * @param token
     * @return
     */
    public SysUser tokenAuth(String token) {
        if (StrUtil.isBlank(token)) {
            throw new AuthenticationException("请求异常，缺少token");
        }
        String username = JwtUtil.getUsername(token);
        if (StrUtil.isEmpty(username)) {
            throw new AuthenticationException("非法token，请检查");
        }
        SysUser sysUser = sysUserService.findByUsername(username);
        if (sysUser == null) {
            throw new AuthenticationException("用户名为"+username+"的用户不存在");
        }
        if (BaseConstants.STATUS_BLOCKED == sysUser.getStatus()) {
            logger.info("{}用户已被锁定", sysUser.getUsername());
            throw new AuthenticationException("用户已被禁用，请联系管理员");
        }

        if (!this.jwtTokenVerifyAndRefresh(token, username, sysUser.getPassword(), sysUser.getId())) {
            throw new AuthenticationException("token认证失败！");
        }
        return sysUser;
    }

    /**
     *JWT Token校验以及刷新
     * @return
     */
    public boolean jwtTokenVerifyAndRefresh(String token, String username, String password, String userId) {
        String redisKey = CacheConstants.SYS_USER_TOKEN;
        String redisToken = (String) redisRepository.get(redisKey, token);
        if (StrUtil.isNotBlank(redisToken)) {
            // JWT过期需要刷新token
            if (!JwtUtil.verify(redisToken, username, password)) {
                String newToken = JwtUtil.sign(username, password, userId);
                redisRepository.set(redisKey, token, newToken, JwtUtil.EXPIRE_TIME * 2, TimeUnit.MILLISECONDS);
            }
            return true;
        }
        return false;
    }

}
