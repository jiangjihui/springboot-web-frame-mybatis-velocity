package com.business.demo.publish.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.business.demo.publish.model.Publish;
import com.business.demo.publish.service.IPublishService;
import com.common.web.form.PageRequestForm;
import com.common.web.controller.BaseController;

/**
 * 通知公告信息操作处理
 *
 * @author jjh
 * @date Wed Jun 05 09:53:54 CST 2019
 */
@RestController
@RequestMapping("/demo/publish")
public class PublishController extends BaseController {

	@Autowired
	private IPublishService publishService;


	/**
	 * 查询通知公告列表
	 */
	@PostMapping("/list")
	public Object list(@RequestBody PageRequestForm form) {
		startPage(form);
        List<Publish> list = publishService.selectPublishList(form.getParams());
		return page(list);
	}

	/**
	 * 新增保存通知公告
	 */
	@PostMapping("/add")
	public Object addSave(Publish publish)
	{
		return success(publishService.insertPublish(publish));
	}

	/**
	 * 查找通知公告
	 */
	@GetMapping("/find/{id}")
	public Object edit(@PathVariable("id") String id) {
        return success(publishService.selectPublishById(id));
	}

	/**
	 * 修改保存通知公告
	 */
	@PostMapping("/edit")
	public Object editSave(Publish publish) {
		return success(publishService.updatePublish(publish));
	}

	/**
	 * 删除通知公告
	 */
	@PostMapping( "/remove")
	public Object remove(String ids) {
		return success(publishService.deletePublishByIds(ids));
	}

}
