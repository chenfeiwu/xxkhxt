package team.yyzq.xxkhxt.controller;


import org.apache.log4j.Logger;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;


public class IndexController extends Controller {
	static Logger log  =  Logger.getLogger(IndexController.class);
	
	@ActionKey("/")
	public void main(){
		render("/main.html");
	}
}
