package team.yyzq.xxkhxt.common.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.kit.PropKit;

public class SqlDialectKit {
	
	public static String nvl(String... param){
		String ret = null;
		String dbtype = PropKit.get("main.DBType");
		if("mysql".equalsIgnoreCase(dbtype)){
			ret = String.format(" ifnull(%s,%s)",param);
		}else if("oracle".equalsIgnoreCase(dbtype)){
			ret = String.format(" nvl(%s,%s)",param);
		}else if("db2".equalsIgnoreCase(dbtype)){
			ret = String.format(" value(%s,%s)",param);
		}
		return ret;
	}
	
	public static String to_dateStr(String... param){
		String ret = null;
		String dbtype = PropKit.get("main.DBType");
		if("mysql".equalsIgnoreCase(dbtype)){
			ret = String.format(" date_format(%s,'%Y-%m-%d')",param);
		}else if("oracle".equalsIgnoreCase(dbtype)){
			ret = String.format(" to_char(%s,'yyyy-MM-dd')",param);
		}else if("db2".equalsIgnoreCase(dbtype)){
			ret = String.format(" to_char(%s,'YYYY-MM-DD')",param);
		}
		return ret;
	}
	
	public static Timestamp getCurrentTimestamp(){
		return new Timestamp(Calendar.getInstance().getTime().getTime());
	}
	
	public static Timestamp parseTimeStamp(String dateStr) throws ParseException{
		if(StringUtils.isEmpty(dateStr)){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfLen = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(dateStr.length() == 10){
			return parseTimeStamp(sdf.parse(dateStr));	
		}else{
			return parseTimeStamp(sdfLen.parse(dateStr));
		}
		
	}
	
	public static Timestamp parseTimeStamp(Date date){
		if(date == null){
			return null;
		}
		
		return new Timestamp(date.getTime());
	}
	

    //由于Oracle中in操作只允许1000项，所以当超过1000时分成多个in由or组合进行查询
	public static String inList(String col, Collection<String> list){
		StringBuffer sb = new StringBuffer();
		if(!list.isEmpty()){
			boolean gf = false;
			if(list.size()>=1000){
				gf = true;
				sb.append("(");
			}
			sb.append(col).append(" in (");
			Iterator<String> itor = list.iterator();
			int i = 0;
			while(itor.hasNext()){
				String pk = itor.next();
				if(i != 0 && i % 1000 == 0){
					sb.deleteCharAt(sb.length()-1);
					sb.append(") or ").append(col).append(" in (");
				}
				sb.append("'").append(pk).append("',");
				i++;
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(")");
			if(gf){
				sb.append(")");
			}
		}
		
		return sb.toString();
	}
}
