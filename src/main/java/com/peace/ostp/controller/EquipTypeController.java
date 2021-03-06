package com.peace.ostp.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.peace.ostp.domain.BasicEquipType;
import com.peace.ostp.domain.BasicSportType;
import com.peace.ostp.domain.Message;
import com.peace.ostp.security.UserInfo;
import com.peace.ostp.service.IBasicEquipType;

@Controller
public class EquipTypeController {
	
	public static final String SUCCESS_MESSAGE = "success";
	public static final String ERROR_MESSAGE = "failed";
	@Autowired
	private IBasicEquipType iBasicEquipType;
	
	@RequestMapping(value = "/admin/equipType-list", method = RequestMethod.GET)
	private String getAllEquipType(Model model, HttpServletRequest request) {
		
		List<BasicEquipType> basicEquipTypeList = iBasicEquipType.getAllEquipType();
		model.addAttribute("basicEquipTypeList", basicEquipTypeList);
		return "admin/basic/equipType";
	}
	
	@RequestMapping(value = "/admin/equipType-add", method = RequestMethod.POST)
	public @ResponseBody Message addEquidType(@RequestBody BasicEquipType basicEquipType) {
		Message message = new Message();
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		basicEquipType.setCreateBy(userInfo.getUserName());
		basicEquipType.setCreateDate(new Date());
		basicEquipType.setUpdateBy(userInfo.getUserName());
		basicEquipType.setUpdateDate(new Date());
		try {
			basicEquipType.setEquiptypeid(UUID.randomUUID().toString().replace("-", ""));
			iBasicEquipType.insertEquipType(basicEquipType);
			message.setResult(SUCCESS_MESSAGE);
			message.setMessageInfo("添加成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
				message.setResult(ERROR_MESSAGE);
				message.setMessageInfo("添加失败");
			e.printStackTrace();
		}
		return message;
	}
	@RequestMapping(value = "/admin/equipType-del/{id}", method = RequestMethod.POST)
	public @ResponseBody Message DelEquidType(Model model,HttpServletResponse response,@PathVariable String id) {
		Message delmessage = new Message();
		try {
			iBasicEquipType.deleteByPrimaryKey(id);
			delmessage.setResult(SUCCESS_MESSAGE);
			delmessage.setMessageInfo("删除成功！");
			System.out.println("success");
		} catch (Exception e) {
			delmessage.setResult(ERROR_MESSAGE);
			delmessage.setMessageInfo("删除失败！");
			e.printStackTrace();
		}
		return delmessage;
	}
	@RequestMapping(value = "/admin/equipType-update", method = RequestMethod.POST)
	public @ResponseBody Message updateEquipType(@RequestBody BasicEquipType basicEquipType) {
		Message message = new Message();
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		basicEquipType.setUpdateBy(userInfo.getUserName());
		basicEquipType.setUpdateDate(new Date());
		try {
			iBasicEquipType.updateEquipType(basicEquipType);
			message.setResult(SUCCESS_MESSAGE);
			message.setMessageInfo("更新成功！");
			System.out.println("success");
		} catch (Exception e) {
			message.setResult(ERROR_MESSAGE);
			message.setMessageInfo("更新失败！");
			e.printStackTrace();
		}
		return message;
	}
}
