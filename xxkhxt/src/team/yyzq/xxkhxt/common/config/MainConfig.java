package team.yyzq.xxkhxt.common.config;


import org.apache.log4j.Logger;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.cache.EhCache;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.Engine;

import team.yyzq.xxkhxt.jfinal.plugin.quartz.QuartzPlugin;
import team.yyzq.xxkhxt.jfinal.plugin.sqlinxml.SqlInXmlPlugin;
import team.yyzq.xxkhxt.jfinal.plugin.tablebind.SimpleNameStyles;
import team.yyzq.xxkhxt.jfinal.plugin.tablebind.TableBindPlugin;
import team.yyzq.xxkhxt.jfinal.route.AutoBindRoutes;


/**API 引导配置
 * @author feiwu
 *
 * 2017年1月3日下午1:30:16 
 */
public class MainConfig extends JFinalConfig {
	static Logger log = Logger.getLogger(MainConfig.class);
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取
		loadPropertyFile("initConfig.properties");
		me.setDevMode(getPropertyToBoolean("devMode", false));
	    me.setMaxPostSize(100*1024*1024);
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		AutoBindRoutes routes = new AutoBindRoutes();
		me.add(routes);
	}

	public static C3p0Plugin createC3p0Plugin() {
		return new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		C3p0Plugin C3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim(), getProperty("driverClass").trim());
		C3p0Plugin.setMinPoolSize(3).setMaxIdleTime(300).setMaxPoolSize(20);
		me.add(C3p0Plugin);
		
		TableBindPlugin tableBindDefault = new TableBindPlugin(C3p0Plugin, SimpleNameStyles.LOWER);
		tableBindDefault.setContainerFactory(new CaseInsensitiveContainerFactory(true));//忽略字段大小写排除或或者引入包
		tableBindDefault.setShowSql(getPropertyToBoolean("devMode", false));
		tableBindDefault.setCache(new EhCache());
		//非mysql的数据库方言
		//tableBindDefault.setDialect(new AnsiSqlDialect());
		me.add(tableBindDefault);
		// 加载缓存插件
		me.add(new EhCachePlugin());
		// sqlinxml插件
		me.add(new SqlInXmlPlugin());
		//定时任务
		me.add(new QuartzPlugin("quartzjob.properties","quartz.properties"));
	}
	
	/**
	 *  配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		// 添加业务层全局拦截器      
//		me.addGlobalServiceInterceptor(new AutoInjectInterceptor());
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		
	}

	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub
		
	}
	
}