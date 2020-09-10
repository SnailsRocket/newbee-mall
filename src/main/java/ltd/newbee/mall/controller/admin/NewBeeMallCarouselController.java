/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.newbee.mall.controller.admin;

import ltd.newbee.mall.common.ServiceResultEnum;
import ltd.newbee.mall.entity.Carousel;
import ltd.newbee.mall.entity.IndexConfig;
import ltd.newbee.mall.service.NewBeeMallCarouselService;
import ltd.newbee.mall.service.NewBeeMallIndexConfigService;
import ltd.newbee.mall.util.PageQueryUtil;
import ltd.newbee.mall.util.Result;
import ltd.newbee.mall.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 *  POJO    一个中间对象，可以转换成 VO DTO PO，在阿里巴巴嵩山版中是 DO DTO BO VO的统称，禁止命名 xxxPOJO
 *  VO      View Object  前端页面显示的对象，用于View 与 Controller 之前的传输  MVC 中 M 与 V 之前传输对象的中介
 *  DTO     Data Transfer Object 数据传输对象 如PO有100个属性，页面VO有是10个属性，那么DTO也就传输10个，一般用在service层
 *  DO      Data Object 一般用在Dao层
 *  PO      Persistent Object 持久化对象，他跟数据表形成一一对映的的映射关系，一般用在Dao层，也就是持久层
 *  Entity  跟数据库表对映，一个实体一张表
 *
 */
@Controller
@RequestMapping("/admin")
public class NewBeeMallCarouselController {

    @Resource
    NewBeeMallCarouselService newBeeMallCarouselService;

    @GetMapping("/carousels")
    public String carouselPage(HttpServletRequest request) {
        request.setAttribute("path", "newbee_mall_carousel");
        return "admin/newbee_mall_carousel";
    }

    /**
     * 列表
     * @Param Result 封装的返回结果 class
     * 使用map接收 传过来的参数 ，这个比直接创建一个VO要好，
     * 当遇到该需求的时候，前端加了一个字段，或者删除了，这个VO对象就需要修改掉
     */
//    RequestMapping  method=RequestMethod.GET 这个 可以简写成 GetMapping
    @RequestMapping(value = "/carousels/list", method = RequestMethod.GET)
    @ResponseBody  // 或者直接使用 @RestController 这个注解
    public Result list(@RequestParam Map<String, Object> params) {
//        判断请求参数里面是否有page  和 limit  一个是当前页码 ，一个是 每页条数
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
//            这里的实现方式是写一个类 实现Result的数据封装，而不是在Controller里面，这样写的话，
//            Controller里面的方法就不用了手动去设置Result，像200,500，这种提前现在generateResult这个类里面写好，
//            只需要传递一些动态的参数，就可以生成Result这个类，
            return ResultGenerator.genFailResult("参数异常！");
        }
//        调用PageQueryUtil 这个工具类
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(newBeeMallCarouselService.getCarouselPage(pageUtil));
    }

    /**
     * 添加
     * 将RequestMapping 换成 PostMapping
     * 但是如果一个Controller里面，有的不返回json，而是String，就没有必要用RestController
     */
    @RequestMapping(value = "/carousels/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody Carousel carousel) {
        if (StringUtils.isEmpty(carousel.getCarouselUrl())
                || Objects.isNull(carousel.getCarouselRank())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = newBeeMallCarouselService.saveCarousel(carousel);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


    /**
     * 修改
     * 直接在request 的 body里面放一个Carousel对象，但是注意，这个Carousel对象
     * 不会轻易修改字段，不然就是用 Map接收
     */
    @RequestMapping(value = "/carousels/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody Carousel carousel) {
        if (Objects.isNull(carousel.getCarouselId())
                || StringUtils.isEmpty(carousel.getCarouselUrl())
                || Objects.isNull(carousel.getCarouselRank())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = newBeeMallCarouselService.updateCarousel(carousel);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @GetMapping("/carousels/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Integer id) {
        Carousel carousel = newBeeMallCarouselService.getCarouselById(id);
        if (carousel == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(carousel);
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/carousels/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (newBeeMallCarouselService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

}