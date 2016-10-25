package com.cat.kit;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.framework.hibernate.util.Entity;
import com.framework.hibernate.util.Page;

public class DataAccess<T extends Entity> extends BaseDataAccess<T> {
	@Resource(name = "jdbcTemplate")
	protected JdbcTemplate	jdbcTemplate;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.setHibernateTemplate(new HibernateTemplate(sessionFactory));
	}
	
	/**
	 * @Description:
	 * @author: Santy
	 * @date: 2015年4月2日
	 * @param @param string
	 * @param @param group
	 * @param @return
	 * @return
	 * @throws
	 */
	public String generalCountSql(String string) {
		String sql = "select count(*) ";
		int index = string.toLowerCase().indexOf("from");
		int lastIndex = string.length();
		sql = sql + string.substring(index, lastIndex);
		return sql;
	}
	
	/**
	 * @Description: 是否去重
	 * @author: Santy
	 * @date: 2015年4月27日
	 * @param
	 * @return
	 * @throws
	 */
	public String generalCountSqlDistinct(String string, String distinct) {
		String sql = "select count(" + distinct + ") ";
		int index = string.toLowerCase().indexOf("from");
		int lastIndex = string.length();
		sql = sql + string.substring(index, lastIndex);
		return sql;
	}
	
	/**
	 * @Description: 子查询的方式查询总数速度比较慢
	 * @author :蓝鑫林
	 * @param string
	 * @return
	 * @date 2015年10月30日 下午2:27:44
	 */
	public String generalCountSqlChildrenSelectDistinct(String string) {
		StringBuffer sb = new StringBuffer("");
		sb.append(" select count(*) from ");
		sb.append(" (");
		sb.append(string);
		sb.append(") ");
		sb.append(" as Child");
		return sb.toString();
	}
	
	/**
	 * @Description:
	 * @author: Santy
	 * @date: 2015年3月31日
	 * @param string
	 *            sql
	 * @param group
	 *            是否需要分组
	 * @return
	 * @throws
	 */
	public String generalCountSql(String string, boolean group) {
		String sql = "select count(*) ";
		int index = string.indexOf("from");
		int lastIndex = string.length();
		int groupIndex = string.indexOf("GROUP");
		groupIndex = groupIndex != -1 ? groupIndex : string.length();
		lastIndex = group ? lastIndex : groupIndex;
		
		sql = sql + string.substring(index, lastIndex);
		return sql;
	}
	
	/**
	 * @Description: 分页sql拼接
	 * @author: Santy
	 * @date: 2015年4月28日
	 * @param
	 * @return
	 * @throws
	 */
	public String generalPageSql(Page pager) {
		pager = Page.getPage(pager);
		StringBuffer sBuffer = new StringBuffer(" limit ");
		sBuffer.append(pager.getStartIndex(pager.getPageIndex()));
		sBuffer.append(",");
		sBuffer.append(pager.getPageSize());
		
		return sBuffer.toString();
	}
	
	/**
	 * @Description: 批量修改，
	 * @author: Santy
	 * @date: 2015年4月30日
	 * @param sql
	 *            ： 要修改的sql语句
	 * @param list
	 *            ：参数列表, Object[] 数组对应 ？ 个数及位置
	 * @return 变化的结果的数组
	 * @throws Exception
	 */
	public int[] batchUpdate(String sql, final List<Object[]> list) throws Exception {
		if (list == null || list.size() <= 0) {
			throw new Exception("参数不匹配!");
		}
		final int paramNum = ((Object[]) list.get(0)).length;
		int[] results = this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			public int getBatchSize() {
				return list.size();
			}
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Object[] param = (Object[]) list.get(i);
				for (int j = 0; j < paramNum; j++) {
					ps.setObject((j + 1), param[j]);
				}
			}
		});
		
		return results;
	}
	
	/**
	 * 根据传入的参数升序排序
	 * 
	 * @author: Santy
	 * @date: 2015年5月19日
	 * @param param
	 *            字段
	 * @return
	 * @throws
	 */
	protected String orderByAsc(String param) {
		return " order by " + param + " asc ";
	}
	
	/**
	 * 根据传入的参数降序排序
	 * 
	 * @author: Santy
	 * @date: 2015年5月19日
	 * @param param
	 *            字段
	 * @return
	 * @throws
	 */
	protected String orderByDesc(String param) {
		return " order by " + param + " desc ";
	}
}