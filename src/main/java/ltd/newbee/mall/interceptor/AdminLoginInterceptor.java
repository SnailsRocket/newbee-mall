/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.newbee.mall.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台系统身份验证拦截器
 * 系统管理员使用
 * 设置以 url中 有 admin的访问路径，这个ConfigClass 写具体的实现逻辑
 *
 */
@Component
public class AdminLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String requestServletPath = request.getServletPath(); // 获取请求路径
//        请求路径以admin开头，并且Session中没有loginUser 信息，则判断是登录请求， Session在服务器端
        if (requestServletPath.startsWith("/admin") && null == request.getSession().getAttribute("loginUser")) {
            request.getSession().setAttribute("errorMsg", "请登陆");
//            重定向到admin/login上面  request.getContextPath() 获取绝对路径
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        } else {
//            如果request路径中没有以admin开头，就直接跳转到这个语句体中，
//            疑问1：如果用户直接访问/goods/listAll 这个时候session中应该没有errorMsg，正确的操作是跳转login.html页面
//            解决：需要定义一个拦截器，如果用户没有登录(session中没有数据，有跟好的解决方法，token令牌)，那就拦截除登录外的请求，
            request.getSession().removeAttribute("errorMsg"); // 如果session中没有errorMag，这样remove会异常吗
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
