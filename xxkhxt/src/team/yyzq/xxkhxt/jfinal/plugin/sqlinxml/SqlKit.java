/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package team.yyzq.xxkhxt.jfinal.plugin.sqlinxml;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import team.yyzq.xxkhxt.jfinal.kit.JaxbKit;

/**#(?自动映射值类型)
 * $(不映射)
 * @author feiwu
 *
 * 2017年1月4日下午2:56:59 
 */
public class SqlKit {

	protected static final Logger LOG = Logger.getLogger(SqlKit.class);

	private static Map<String, String> sqlMap;

	public static String sql(String groupNameAndsqlId) {
		if (sqlMap == null) {
			throw new NullPointerException("SqlInXmlPlugin not start");
		}
		return sqlMap.get(groupNameAndsqlId);
	}

	@SuppressWarnings("rawtypes")
	public static int sqlExec(String groupNameAndsqlId, Model model) {
		return sqlExec(groupNameAndsqlId, model, null);
	}

	@SuppressWarnings("rawtypes")
	public static int sqlExec(String groupNameAndsqlId, Model model,
			String configName) {

		AnalyseResult ar = analyseSql(groupNameAndsqlId, model);

		int ret = exec(configName, ar.getSql(), ar.getParams());

		return ret;
	}

	@SuppressWarnings("rawtypes")
	public static int sqlExec(String groupNameAndsqlId, Map map) {
		return sqlExec(groupNameAndsqlId, map, null);
	}

	@SuppressWarnings("rawtypes")
	public static int sqlExec(String groupNameAndsqlId, Map map,
			String configName) {

		AnalyseResult ar = analyseSql(groupNameAndsqlId, map);

		int ret = exec(configName, ar.getSql(), ar.getParams());

		return ret;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Record> sqlFind(String groupNameAndsqlId, Model model) {
		return sqlFind(groupNameAndsqlId, model, null);
	}

	@SuppressWarnings("rawtypes")
	public static List<Record> sqlFind(String groupNameAndsqlId, Model model,
			String configName) {

		AnalyseResult ar = analyseSql(groupNameAndsqlId, model);

		List<Record> retList = find(configName, ar.getSql(), ar.getParams());

		return retList;
	}

	@SuppressWarnings("rawtypes")
	public static List<Record> sqlFind(String groupNameAndsqlId, Map map) {
		return sqlFind(groupNameAndsqlId, map, null);
	}

	@SuppressWarnings("rawtypes")
	public static List<Record> sqlFind(String groupNameAndsqlId, Map map,
			String configName) {

		AnalyseResult ar = analyseSql(groupNameAndsqlId, map);

		List<Record> retList = find(configName, ar.getSql(), ar.getParams());

		return retList;
	}

	@SuppressWarnings("rawtypes")
	public static AnalyseResult analyseSql(String groupNameAndsqlId, Object map) {
		if (sqlMap == null) {
			throw new NullPointerException("SqlInXmlPlugin not start");
		}
		AnalyseResult ar = new AnalyseResult();
		String sql = sqlMap.get(groupNameAndsqlId);
		StringBuffer sb = new StringBuffer();
		List<Object> param = Lists.newArrayList();

		Pattern p = Pattern.compile("([#\\$])([A-Za-z0-9_]+)[#\\$]");
		Matcher m = p.matcher(sql);
		Map mapParams = null;
		Model modelParams = null;
		if (map instanceof Map) {
			mapParams = (Map) map;
		} else if (map instanceof Model) {
			modelParams = (Model) map;
		}
		boolean result = m.find();
		while (result) {
			String flag = m.group(1);
			String field = m.group(2);
			Object obj = null;

			if (mapParams != null) {
				if (!mapParams.containsKey(field)) {
					throw new RuntimeException("Sqlid为" + groupNameAndsqlId
							+ "的SQl中，需要提供" + field + "参数，请传入此参数");
				}
				obj = mapParams.get(field);
			} else if (modelParams != null) {
				obj = modelParams.get(field);
			}

			if ("#".equals(flag)) {
				m.appendReplacement(sb, " ? ");
				param.add(obj);
			} else {
				if (obj == null) {
					throw new RuntimeException("Sqlid为" + groupNameAndsqlId
							+ "的SQl中，需要提供" + field + "参数，请传入此参数");
				}
				m.appendReplacement(sb, obj.toString());
			}

			result = m.find();
		}
		m.appendTail(sb);

		ar.setSql(sb.toString());

//		if (param.size() > 0) {
//			ar.setParams(param.toArray());
//		}
		ar.setParams(param.toArray());
		return ar;
	}


	static <T> List<T> query(String configName, String psql, Object[] params) {
		List<T> list = null;
		if (params == null) {
			if (configName == null) {
				list = Db.query(psql.toString());
			} else {
				list = Db.use(configName).query(psql.toString());
			}
		} else {
			if (configName == null) {
				list = Db.query(psql.toString(), params);
			} else {
				list = Db.use(configName).query(psql.toString(), params);
			}
		}

		return list;
	}

	static int exec(String configName, String psql, Object[] params) {
		int ret;
		if (params == null) {
			if (configName == null) {
				ret = Db.update(psql.toString());
			} else {
				ret = Db.use(configName).update(psql.toString());
			}
		} else {
			if (configName == null) {
				ret = Db.update(psql.toString(), params);
			} else {
				ret = Db.use(configName).update(psql.toString(), params);
			}
		}

		return ret;
	}

	
	static List<Record> find(String configName, String psql, Object[] params) {
		List<Record> list = null;
		if (params == null) {
			if (configName == null) {
				list = Db.find(psql.toString());
			} else {
				list = Db.use(configName).find(psql.toString());
			}
		} else {
			if (configName == null) {
				list = Db.find(psql.toString(), params);
			} else {
				list = Db.use(configName).find(psql.toString(), params);
			}
		}

		return list;
	}

	static void clearSqlMap() {
		sqlMap.clear();
	}

	static void init() {
		sqlMap = new HashMap<String, String>();
		File file = new File(SqlKit.class.getClassLoader().getResource("")
				.getFile());
		File[] files = file.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				if (pathname.getName().endsWith("sql.xml")) {
					return true;
				}
				return false;
			}
		});
		for (File xmlfile : files) {
			SqlGroup group = JaxbKit.unmarshal(xmlfile, SqlGroup.class);
			String name = group.name;
			if (name == null || name.trim().equals("")) {
				name = xmlfile.getName();
			}
			for (SqlItem sqlItem : group.sqlItems) {
				sqlMap.put(name + "." + sqlItem.id, sqlItem.value);
			}
		}
		LOG.debug("sqlMap" + sqlMap);
	}

}
