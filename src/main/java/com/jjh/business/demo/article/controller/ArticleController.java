package com.jjh.business.demo.article.controller;


import com.jjh.business.demo.article.model.Article;
import com.jjh.business.demo.article.service.ArticleService;
import com.jjh.common.web.controller.BaseController;
import com.jjh.common.web.form.PageRequestForm;
import com.jjh.common.web.form.PageResponseForm;
import com.jjh.common.web.form.SimpleResponseForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
*   文章管理
 *
 * @author jjh
 * @date 2020/02/16
*/
@Api(tags = "文章管理")
@RestController
@RequestMapping("/demo/article/article")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;


    /**
     * 文章列表
     */
    @ApiOperation("文章列表")
    @PostMapping("/list")
    public SimpleResponseForm<PageResponseForm<Article>> list(@RequestBody PageRequestForm<Article> form) {
        List<Article> list = articleService.list(form);
        return page(form, list);
    }

    /**
     * 新增文章
     */
    @ApiOperation("新增文章")
    @PostMapping("/add")
    public SimpleResponseForm<Article> add(@Valid @RequestBody Article entity) {
        Article result = articleService.add(entity);
        return success();
    }

    /**
     * 更新文章
     */
    @ApiOperation("更新文章")
    @PostMapping("/update")
    public SimpleResponseForm<Article> update(@Valid @RequestBody Article entity) {
        Article result = articleService.update(entity);
        return success();
    }

    /**
     * 删除文章
     */
    @ApiOperation("删除文章")
    @GetMapping("/delete")
    public SimpleResponseForm<String> delete(String ids) {
        articleService.del(ids);
        return success();
    }

}
