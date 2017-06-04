package team.yyzq.xxkhxt.model;


import com.jfinal.plugin.activerecord.Model;

import team.yyzq.xxkhxt.jfinal.plugin.tablebind.TableBind;

@TableBind(tableName="ANNUAL_SUMMARY_P", pkName="ANNUAL_SUMMARY_ID")
public class AnnualSummary extends Model<AnnualSummary> {
	
	private static final long serialVersionUID = 8491497839722389884L;
	
	public final static AnnualSummary dao = new AnnualSummary();
	
}