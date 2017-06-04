package team.yyzq.xxkhxt.controller;


import org.apache.log4j.Logger;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

import team.yyzq.xxkhxt.jfinal.plugin.sqlinxml.SqlKit;
import team.yyzq.xxkhxt.model.CkBar;


public class CxtjController extends Controller {
	static Logger log  =  Logger.getLogger(CxtjController.class);
	
	@ActionKey("/test")
	public void test(){
		CkBar model = CkBar.dao.findById("0e9d1609-d9ac-487e-a8ea-562764f7cc40");
		System.err.println(SqlKit.sql("bdc.getList"));
		System.out.println(model.get("xm"));
	}
}
