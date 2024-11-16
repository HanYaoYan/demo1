package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
/*加上这个@ResponseBody会让编译器认为返回的字符串当作直接写入响应体的数据，而不是渲染一个视图。*/
public class FormController {
    @Autowired
    private UserService userService;

    //这个get的method是为了后面登陆页面的重定向准备的
    @GetMapping("/auth/login")
    public String showLoginPage(Model model) {
        // 你可以在这里添加任何需要传递给视图的数据
        return "login"; // 返回 login.html 视图
    }

    @GetMapping("/auth/register")
    public String showRegisterPage(Model model) {
        return "register";
    }

    /**
     *接收welcome请求，返回主页面
     * @param model 由于绝大部分调用welcome时都是重定向，而每次重定向后model都是空，因此每次重定向前要将想保留的数据要么保存在flash中，要么保存在url中
     * @return
     */
    @GetMapping("/welcome")
    public String showWelcome(ModelMap model) {
        //从redirectAttributes中获取flash数据
        String username1=(String)model.getAttribute("username1");
        //将其添加到model中以传递给视图
        model.addAttribute("username1", username1);

        return "index";
    }

    @PostMapping("/submit")
    public String createuser(@RequestParam(value="password")String password,
                             @RequestParam(value="username") String username,
                             @RequestParam(value="repassword") String repassword,
                             RedirectAttributes redirectAttributes,
                             HttpServletResponse response) {

        //将表单传入数据username与全局数据username绑定,以在视图上读出
        redirectAttributes.addFlashAttribute("username1", username);

        User user = new User(username, password);
        User createdUser = userService.saveUser(user);

        JWTUtil jwtUtil=new JWTUtil();
        String jwt=jwtUtil.generateToken(username);

        Cookie cookie=new Cookie("jwt",jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setMaxAge(60*60);
        response.addCookie(cookie);

        return "redirect:/welcome";
    }

    /**
     * 登陆提交控制器
     * @param username 用户名
     * @param password 用户密码
     * @param redirectAttributes 重定向类,使用该类可以再次发送一个get请求,同时使用addFlash函数将username信息保存在会话中,供下次对话使用
     * @param HttpServletResponse https响应体,用于将cookies保存在响应中
     * @return 返回重定向请求
     */
    @PostMapping("/Login_submit")
    public String verifyLogin(@RequestParam(value="username")String username,
                              @RequestParam(value="password")String password,
                              RedirectAttributes redirectAttributes,
                              HttpServletResponse response){
        //调用后端服务器的功能查询是否存在该用户
        if(userService.verifyLogin(username,password)) {
            //添加flash属性以在重定向之后使用
            redirectAttributes.addFlashAttribute("username1", username);

            //创建JWT令牌
            JWTUtil jwtUtil = new JWTUtil();
            String jwt = jwtUtil.generateToken(username);

            //创建cookies保存JWT令牌,并将cookies添加在响应中
            Cookie cookie=new Cookie("jwt",jwt);
            cookie.setHttpOnly(true); // 防止 JavaScript 访问
            cookie.setSecure(true); // 仅在 HTTPS 下传输
            cookie.setPath("/"); // 设置可用路径
            cookie.setMaxAge(60 * 60); // 1小时有效
            response.addCookie(cookie); // 将 Cookie 添加到响应中

            return "redirect:/welcome";
        }
        redirectAttributes.addFlashAttribute("error","用户名或密码错误,请重新输入");
        return "redirect:/Login_error";
    }

    /**
     * 若登陆失败,将报错信息封装,传给前端
     * @param model model类相当于载体,在前后端之间连接
     * @return 返回登陆页面
     */
    @GetMapping("/Login_error")
    public String showLogin_error(Model model){
        // 直接从模型中获取 Flash 属性
        String errorMessage = (String) model.getAttribute("error");
        model.addAttribute("error", errorMessage); // 将错误信息添加到模型中
        return "login";
    }
}
