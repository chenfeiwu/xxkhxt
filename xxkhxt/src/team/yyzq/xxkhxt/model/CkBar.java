package team.yyzq.xxkhxt.model;


import com.jfinal.plugin.activerecord.Model;

import team.yyzq.xxkhxt.jfinal.plugin.tablebind.TableBind;

@TableBind(tableName="CK_BAR", pkName="barid")
public class CkBar extends Model<CkBar> {
	
	private static final long serialVersionUID = 8491497839722389884L;
	
	public final static CkBar dao = new CkBar();
	
}