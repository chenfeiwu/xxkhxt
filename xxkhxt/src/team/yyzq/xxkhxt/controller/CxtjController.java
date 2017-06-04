package team.yyzq.xxkhxt.controller;


import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

import team.yyzq.xxkhxt.model.AnnualSummary;
import team.yyzq.xxkhxt.util.RenderUtil;


public class CxtjController extends Controller {
	static Logger log  =  Logger.getLogger(CxtjController.class);
	
	@ActionKey("gzjsList")
	public void gzjsList(){
		List<AnnualSummary> list = AnnualSummary.dao.find("select * from ANNUAL_SUMMARY_P");
		renderJson(list);
	}
}
