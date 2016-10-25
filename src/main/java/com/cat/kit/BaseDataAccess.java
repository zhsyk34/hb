package com.cat.kit;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.RootEntityResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.framework.hibernate.util.Entity;
import com.framework.hibernate.util.Page;
import com.zuipin.action.JxmallAction;
import com.zuipin.util.ConvertUtils;
import com.zuipin.util.DateUtils;
import com.zuipin.util.UserVo;

/**
 * @author Kingsley
 */
public abstract class BaseDataAccess<T extends Entity> {
	private HibernateTemplate	hibernateTemplate;
	private boolean				isCacheable	= false;
	private Class<T>			entityClass;
	private final static Logger	logger		= Logger.getLogger(BaseDataAccess.class);
	
	public void setCacheable(boolean isCacheable) {
		this.isCacheable = isCacheable;
	}
	
	public boolean getCacheable() {
		return isCacheable;
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return this.hibernateTemplate;
	}
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	@SuppressWarnings("unchecked")
	public T load(Serializable id) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (T) hibernateTemplate.get(getPOJO(), id);
	}
	
	/**
	 * @功能描述：新增保存对象
	 * @作者 : cjj 陈俊杰
	 * @创建时间 : 2016年2月17日下午8:02:08
	 * @修改内容：如果存在属性Entity.CREATORID,Entity.CREATOR,Entity.CREATEDTIME,则动态设置
	 * @修改人 : cjj 陈俊杰
	 * @修改时间 : 2016年2月17日下午8:02:08
	 * @param entity
	 */
	public void save(Object entity) {
		try {
			// 动态设置创建者信息
			autoSetCreateInfo(entity);
			autoSetUpdateInfo(entity);
		} catch (Exception e) {
			// TODO 不捕获异常 未登录状态也可以保存
		}
		hibernateTemplate.save(entity);
	}
	
	public void evict(Object entity) {
		hibernateTemplate.evict(entity);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void save(final List<?> objs) {
		hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				for (int i = 0; i < objs.size(); i++) {
					try {
						autoSetCreateInfo(objs.get(i));
					} catch (IllegalAccessException e) {
						// TODO 不处理
					}
					session.save(objs.get(i));
					if (i % 20 == 0) {
						session.flush();
						session.clear();
					}
				}
				return null;
			}
		});
	}
	
	public void saveOrUpdate(Object entity, Integer id) {
		if (id == null || id <= 0) {
			save(entity);
		} else
			update(entity);
	}
	
	public void saveOrUpdate(Object entity, Long id) {
		if (id == null || id <= 0) {
			save(entity);
		} else
			update(entity);
	}
	
	public void delete(Object entity) {
		hibernateTemplate.delete(entity);
	}
	
	public void deleteAll(Collection<T> clazzes) {
		hibernateTemplate.deleteAll(clazzes);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findByCriteria(final com.framework.hibernate.util.Criteria<T> criteria) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria hibernateCriteria = criteria.getExecutableCriteria(session);
				hibernateCriteria.setCacheable(isCacheable);
				hibernateCriteria.setResultTransformer(new RootEntityResultTransformer());
				return hibernateCriteria.list();
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T findUniqueByCriteria(final com.framework.hibernate.util.Criteria<T> criteria) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (T) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria hibernateCriteria = criteria.getExecutableCriteria(session);
				hibernateCriteria.setCacheable(isCacheable);
				hibernateCriteria.setResultTransformer(new RootEntityResultTransformer());
				return hibernateCriteria.uniqueResult();
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Long findLongCriteria(final com.framework.hibernate.util.Criteria<T> criteria) {
		return (Long) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria hibernateCriteria = criteria.getExecutableCriteria(session);
				hibernateCriteria.setResultTransformer(new RootEntityResultTransformer());
				List<Object> list = hibernateCriteria.list();
				if (!list.isEmpty()) {
					return list.get(0);
				} else {
					return null;
				}
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Integer findIntegerCriteria(final com.framework.hibernate.util.Criteria<T> criteria) {
		return (Integer) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria hibernateCriteria = criteria.getExecutableCriteria(session);
				hibernateCriteria.setResultTransformer(new RootEntityResultTransformer());
				List<Object> list = hibernateCriteria.list();
				if (!list.isEmpty()) {
					return list.get(0);
				} else {
					return null;
				}
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> List<T> findByHQL(String hql, Object... params) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.find(hql, params);
	}
	
	/**
	 * @功能描述：悲观锁查询
	 * @作者 : cjj
	 * @创建时间 : 2016年10月14日下午2:51:20
	 * @param hql
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByHQLByLock(final String table, final String hqlstr, final Page page, final Object... parameters) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hqlstr);
				query.setLockMode(table, LockMode.UPGRADE);// 加锁
				for (int i = 0; i < parameters.length; i++) {
					query.setParameter(i, parameters[i]);
				}
				ScrollableResults scrollableResults = query.scroll();
				scrollableResults.last();
				int rownum = scrollableResults.getRowNumber();
				page.setTotalCount(rownum + 1);
				query.setMaxResults(page.getPageSize());
				query.setFirstResult(page.getFirstResult());
				query.setResultTransformer(new RootEntityResultTransformer());
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("rawtypes")
	public Long findLongByHQL(String hql, Object... params) {
		hibernateTemplate.setCacheQueries(isCacheable);
		List list = hibernateTemplate.find(hql, params);
		Long count = 0l;
		try {
			if (list != null && list.size() > 0) {
				Object obj = list.get(0);
				if (obj != null) {
					count = Long.valueOf(String.valueOf(list.get(0)));
				}
			}
		} catch (Exception e) {
			// TODO
			Logger.getLogger(getClass()).error(e);
			System.out.println("查询结果无法转化为Long型!");
		}
		return count;
	}
	
	@SuppressWarnings("rawtypes")
	public Double findDoubleByHQL(String hql, Object... params) {
		hibernateTemplate.setCacheQueries(isCacheable);
		List list = hibernateTemplate.find(hql, params);
		Double count = 0d;
		try {
			if (list != null && list.size() > 0) {
				Object obj = list.get(0);
				if (obj != null) {
					count = ConvertUtils.objectToDouble(list.get(0));
				}
			}
		} catch (Exception e) {
			// TODO
			Logger.getLogger(getClass()).error(e);
			System.out.println("查询结果无法转化为Double型!");
		}
		return count;
	}
	
	// @SuppressWarnings("unchecked")
	// public Integer findIntegerByHQL(String hql, Object... params) {
	// hibernateTemplate.setCacheQueries(isCacheable);
	// List list = hibernateTemplate.find(hql, params);
	// Integer count = 0;
	// try {
	// if (list != null && list.size() > 0) {
	// count = Integer.valueOf(String.valueOf(list.get(0)));
	// }
	// } catch (Exception e) {
	// // TODO
	// System.out.println("查询结果无法转化为Long型!");
	// }
	// return count;
	// }
	
	/**
	 * @param hql
	 * @param params
	 * @return 只取一个对象
	 */
	@SuppressWarnings("unchecked")
	public T findOneByHQL(String hql, Object... params) {
		hibernateTemplate.setCacheQueries(isCacheable);
		List<T> list = findByHQL(hql, new Page(1, 1), params);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * @功能描述：修改保存对象
	 * @作者 : cjj 陈俊杰
	 * @创建时间 : 2016年2月17日下午8:10:36
	 * @修改内容：动态设置修改信息
	 * @修改人 : cjj 陈俊杰
	 * @修改时间 : 2016年2月17日下午8:10:36
	 * @param entity
	 */
	public void update(Object entity) {
		try {
			// 动态设置操作信息
			autoSetUpdateInfo(entity);
		} catch (Exception e) {
			// TODO 不捕获异常 未登录状态也可以保存
		}
		hibernateTemplate.update(entity);
	}
	
	/**
	 * @功能描述：审核保存对象
	 * @作者 : cjj 陈俊杰
	 * @创建时间 : 2016年2月17日下午8:10:36
	 * @修改内容：动态设置审核信息
	 * @修改人 : cjj 陈俊杰
	 * @修改时间 : 2016年2月17日下午8:10:36
	 * @param entity
	 */
	public void audit(Object entity) {
		try {
			// 动态设置审核信息
			autoSetAuditInfo(entity);
		} catch (Exception e) {
			// TODO 不捕获异常 未登录状态也可以保存
		}
		hibernateTemplate.update(entity);
	}
	
	public void merge(Object entity) {
		try {
			// 动态设置操作信息
			autoSetUpdateInfo(entity);
		} catch (Exception e) {
			// TODO 不捕获异常 未登录状态也可以保存
		}
		hibernateTemplate.merge(entity);
	}
	
	public T get(Class<T> clazz, Serializable id) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (T) hibernateTemplate.get(clazz, id);
	}
	
	public List<T> findAll() {
		hibernateTemplate.setCacheQueries(isCacheable);
		return hibernateTemplate.loadAll(getPOJO());
	}
	
	public void delete(Serializable id, Class<T> poClass) {
		Entity po = (Entity) hibernateTemplate.load(poClass, id);
		hibernateTemplate.delete(po);
	}
	
	public List<T> selectAll(Class<T> cls) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return hibernateTemplate.loadAll(cls);
	}
	
	public List<T> findAll(final Page page) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return findByCriteria(new com.framework.hibernate.util.Criteria<T>(getPOJO()), page);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	public <T> List<T> findByHQL(final String hqlstr, final Page page) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hqlstr);
				ScrollableResults scrollableResults = query.scroll();
				scrollableResults.last();
				int rownum = scrollableResults.getRowNumber();
				page.setTotalCount(rownum + 1);
				query.setMaxResults(page.getPageSize());
				query.setFirstResult(page.getFirstResult());
				// System.out.println(page.getPageSize() + "," + page.getFirstResult());
				query.setResultTransformer(new RootEntityResultTransformer());
				return query.list();
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	public <T> List<T> findByHQLNoCount(final String hqlstr, final Page page) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hqlstr);
				// ScrollableResults scrollableResults = query.scroll();
				// scrollableResults.last();
				// int rownum = scrollableResults.getRowNumber();
				// page.setTotalCount(rownum + 1);
				query.setMaxResults(page.getPageSize());
				query.setFirstResult(page.getFirstResult());
				// System.out.println(page.getPageSize() + "," + page.getFirstResult());
				query.setResultTransformer(new RootEntityResultTransformer());
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findByHQL(final String hqlstr, final Page page, final Object... parameters) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hqlstr);
				for (int i = 0; i < parameters.length; i++) {
					query.setParameter(i, parameters[i]);
				}
				ScrollableResults scrollableResults = query.scroll();
				scrollableResults.last();
				int rownum = scrollableResults.getRowNumber();
				page.setTotalCount(rownum + 1);
				query.setMaxResults(page.getPageSize());
				query.setFirstResult(page.getFirstResult());
				query.setResultTransformer(new RootEntityResultTransformer());
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findByHQL2(final String hqlstr, final Map<String, Object> mapParam) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hqlstr);
				query.setResultTransformer(new RootEntityResultTransformer());
				for (String key : mapParam.keySet()) {
					// System.out.println(mapParam.get(key).getClass().getName());
					if (mapParam.get(key).getClass().getName().equals("java.util.LinkedList") || mapParam.get(key).getClass().getName().equals("java.util.ArrayList")
							|| mapParam.get(key).getClass().getName().equals("com.opensymphony.xwork2.util.XWorkList")) {
						query.setParameterList(key, (Collection) mapParam.get(key));
					} else {
						query.setParameter(key, mapParam.get(key));
					}
				}
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findByHqlMapParam(final String hqlstr, final Map<String, Object> mapParam) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hqlstr);
				query.setResultTransformer(new RootEntityResultTransformer());
				for (String key : mapParam.keySet()) {
					// System.out.println(mapParam.get(key).getClass().getName());
					if (mapParam.get(key).getClass().getName().equals("java.util.LinkedList") || mapParam.get(key).getClass().getName().equals("java.util.ArrayList")
							|| mapParam.get(key).getClass().getName().equals("com.opensymphony.xwork2.util.XWorkList")) {
						query.setParameterList(key, (Collection) mapParam.get(key));
					} else {
						query.setParameter(key, mapParam.get(key));
					}
				}
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findByHqlMapParam(final String hqlstr, final Page page, final Map<String, Object> mapParam) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hqlstr);
				query.setResultTransformer(new RootEntityResultTransformer());
				for (String key : mapParam.keySet()) {
					// System.out.println(mapParam.get(key).getClass().getName());
					if (mapParam.get(key).getClass().getName().equals("java.util.LinkedList") || mapParam.get(key).getClass().getName().equals("java.util.ArrayList")
							|| mapParam.get(key).getClass().getName().equals("com.opensymphony.xwork2.util.XWorkList")) {
						query.setParameterList(key, (Collection) mapParam.get(key));
					} else {
						query.setParameter(key, mapParam.get(key));
					}
				}
				ScrollableResults scrollableResults = query.scroll();
				scrollableResults.last();
				int rownum = scrollableResults.getRowNumber();
				page.setTotalCount(rownum + 1);
				query.setMaxResults(page.getPageSize());
				query.setFirstResult(page.getFirstResult());
				query.setResultTransformer(new RootEntityResultTransformer());
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByHQL2(final String hqlstr, final Page page, final Map<String, Object> mapParam) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hqlstr);
				query.setResultTransformer(new RootEntityResultTransformer());
				for (String key : mapParam.keySet()) {
					// System.out.println(mapParam.get(key).getClass().getName());
					if (mapParam.get(key).getClass().getName().equals("java.util.LinkedList") || mapParam.get(key).getClass().getName().equals("java.util.ArrayList")
							|| mapParam.get(key).getClass().getName().equals("com.opensymphony.xwork2.util.XWorkList")) {
						query.setParameterList(key, (Collection) mapParam.get(key));
					} else {
						query.setParameter(key, mapParam.get(key));
					}
				}
				if (page != null) {
					query.setMaxResults(page.getPageSize());
					query.setFirstResult(page.getFirstResult());
				}
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findLockInventoryBySQL(final String sql, final Class<T> mappedClass) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				return session.createSQLQuery(sql).addEntity(mappedClass).list();
			}
		});
	}
	
	public <T> List<T> findBySQL(final String sql, final Object... parameters) {
		hibernateTemplate.setCacheQueries(isCacheable);
		// Class<T> cla = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return findEntitiesBySQL(sql, null, parameters);
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findEntitiesBySQL(final String sql, final Class<T> mappedClass, final Object... parameters) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);
				query.setCacheable(isCacheable);
				for (int i = 0; i < parameters.length; i++) {
					Object para = parameters[i];
					query.setParameter(i, para);
				}
				if (mappedClass != null) {
					// query.addEntity(mappedClass);
					query.setResultTransformer(Transformers.aliasToBean(mappedClass));
				}
				List<T> list = query.list();
				// System.out.println("===");
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findEntitiesBySQL(final String sql, final Class<T> mappedClass, final Map<String, Object> parameters) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);
				query.setCacheable(isCacheable);
				for (String key : parameters.keySet()) {
					query.setParameter(key, parameters.get(key));
				}
				if (mappedClass != null) {
					// query.addEntity(mappedClass);
					query.setResultTransformer(Transformers.aliasToBean(mappedClass));
				}
				List<T> list = query.list();
				// System.out.println("===");
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findObjectBySQL(final String sql, final ResultTransformer transformer, final Object... parameters) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);
				query.setCacheable(isCacheable);
				for (int i = 0; i < parameters.length; i++) {
					Object para = parameters[i];
					query.setParameter(i, para);
				}
				query.setResultTransformer(transformer);
				return query.list();
			}
		});
	}
	
	public BigDecimal findIntegerBySQL(final String sql, final Object... parameters) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (BigDecimal) hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);
				query.setCacheable(isCacheable);
				for (int i = 0; i < parameters.length; i++) {
					Object para = parameters[i];
					query.setParameter(i, para);
				}
				query.addScalar("result", Hibernate.BIG_DECIMAL);
				BigDecimal result = (BigDecimal) query.uniqueResult();
				return result;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findEntitiesBySQL(final Page page, final String sql, final Class<T> mappedClass, final Object... parameters) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);
				query.setCacheable(isCacheable);
				if (mappedClass != null) {
					// query.addEntity(mappedClass);
					query.setResultTransformer(Transformers.aliasToBean(mappedClass));
				}
				for (int i = 0; i < parameters.length; i++) {
					Object para = parameters[i];
					query.setParameter(i, para);
				}
				ScrollableResults sar = query.scroll();
				sar.last();
				page.setTotalCount(sar.getRowNumber() + 1);
				query.setMaxResults(page.getPageSize());
				query.setFirstResult(page.getFirstResult());
				
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<?, ?>> findMapBySQL(final Page page, final String sql, final Object... parameters) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<Map<?, ?>>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);
				query.setCacheable(isCacheable);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				for (int i = 0; i < parameters.length; i++) {
					Object para = parameters[i];
					query.setParameter(i, para);
				}
				ScrollableResults sar = query.scroll();
				sar.last();
				if (page != null) {
					page.setTotalCount(sar.getRowNumber() + 1);
					query.setMaxResults(page.getPageSize());
					query.setFirstResult(page.getFirstResult());
				}
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findMapBySQL(final String sql, final Object... parameters) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<Map<String, Object>>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);
				query.setCacheable(isCacheable);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				for (int i = 0; i < parameters.length; i++) {
					Object para = parameters[i];
					query.setParameter(i, para);
				}
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<?, ?>> findMapBySQL(final Page page, final String sql, final Map<String, Object> parameters) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<Map<?, ?>>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);
				query.setCacheable(isCacheable);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				for (String key : parameters.keySet()) {
					query.setParameter(key, parameters.get(key));
				}
				ScrollableResults sar = query.scroll();
				sar.last();
				if (page != null) {
					page.setTotalCount(sar.getRowNumber() + 1);
					query.setMaxResults(page.getPageSize());
					query.setFirstResult(page.getFirstResult());
				}
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findMapBySQL2(final Page page, final String sql, final Map<String, Object> parameters) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<Map<String, Object>>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);
				query.setCacheable(isCacheable);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				for (String key : parameters.keySet()) {
					query.setParameter(key, parameters.get(key));
				}
				ScrollableResults sar = query.scroll();
				sar.last();
				if (page != null) {
					page.setTotalCount(sar.getRowNumber() + 1);
					query.setMaxResults(page.getPageSize());
					query.setFirstResult(page.getFirstResult());
				}
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findBySQL(final String sql, final Page page, final Object... parameters) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);
				for (int i = 0; i < parameters.length; i++) {
					Object para = parameters[i];
					query.setParameter(i, para);
				}
				ScrollableResults scrollableResults = query.scroll();
				scrollableResults.last();
				int rownum = scrollableResults.getRowNumber();
				page.setTotalCount(rownum + 1);
				query.setMaxResults(page.getPageSize());
				query.setFirstResult(page.getFirstResult());
				return query.list();
			}
		});
	}
	
	public int updateBySQL(final String sql, final Object... parameters) {
		return (Integer) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);
				for (int i = 0; i < parameters.length; i++) {
					Object para = parameters[i];
					query.setParameter(i, para);
				}
				return query.executeUpdate();
			}
		});
	}
	
	public int updateByHQL(final String hql, final Object... parameters) {
		return (Integer) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				for (int i = 0; i < parameters.length; i++) {
					Object para = parameters[i];
					query.setParameter(i, para);
				}
				return query.executeUpdate();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public int updateByHQL2(final String hql, final Map<String, Object> mapParam) {
		return (Integer) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				for (String key : mapParam.keySet()) {
					// System.out.println(mapParam.get(key).getClass().getName());
					if (mapParam.get(key).getClass().getName().equals("java.util.LinkedList") || mapParam.get(key).getClass().getName().equals("java.util.ArrayList")
							|| mapParam.get(key).getClass().getName().equals("com.opensymphony.xwork2.util.XWorkList")) {
						query.setParameterList(key, (Collection) mapParam.get(key));
					} else {
						query.setParameter(key, mapParam.get(key));
					}
				}
				return query.executeUpdate();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByCriteria(final Criteria criteria) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				criteria.setCacheable(isCacheable);
				criteria.setResultTransformer(new RootEntityResultTransformer());
				return criteria.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findByCriteria(final com.framework.hibernate.util.Criteria<T> criteria, final Page page) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria countCriteria = criteria.getExecutableCriteria(session);
				countCriteria.setProjection(Projections.rowCount());
				int totalCount = ConvertUtils.objectToInteger(countCriteria.uniqueResult());
				page.setTotalCount(totalCount);
				criteria.setProjection(null);
				Criteria hibernateCriteria = criteria.getExecutableCriteria(session);
				hibernateCriteria.setCacheable(isCacheable);
				hibernateCriteria.setFirstResult(page.getFirstResult());
				hibernateCriteria.setMaxResults(page.getPageSize());
				hibernateCriteria.setResultTransformer(new RootEntityResultTransformer());
				return hibernateCriteria.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findByCriteriaWithoutTotal(final com.framework.hibernate.util.Criteria<T> criteria, final Page page) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria hibernateCriteria = criteria.getExecutableCriteria(session);
				hibernateCriteria.setCacheable(isCacheable);
				hibernateCriteria.setFirstResult(page.getFirstResult());
				hibernateCriteria.setMaxResults(page.getPageSize());
				hibernateCriteria.setResultTransformer(new RootEntityResultTransformer());
				return hibernateCriteria.list();
			}
		});
	}
	
	public T findByHQLWithUniqueObject(String hql, Object... parameters) {
		List<T> list = findByHQL(hql, parameters);
		if (list != null && list.size() > 0)
			return (T) list.get(0);
		return null;
	}
	
	public abstract void setSessionFactory(SessionFactory sessionFactory);
	
	@SuppressWarnings("unchecked")
	private Class<T> getPOJO() {
		if (entityClass == null) {
			entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
		return entityClass;
	}
	
	public void batchUpdate(List<String> sqls) {
		Statement stm = null;
		Connection conn = hibernateTemplate.getSessionFactory().getCurrentSession().connection();
		try {
			conn.setAutoCommit(false);
			stm = conn.createStatement();
			for (String sql : sqls) {
				logger.info("batch sql: " + sql);
				stm.addBatch(sql);
			}
			int[] results = stm.executeBatch();
			boolean welldone = true;
			for (int i : results) {
				if (i < 0) {
					welldone = false;
				}
			}
			if (welldone) {
				conn.commit();
			} else {
				conn.rollback();
			}
			conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(e.getMessage(), e);
			}
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findBySQL2(final String sql, final Page page, final Map<String, Object> params) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);
				for (String key : params.keySet()) {
					// System.out.println(mapParam.get(key).getClass().getName());
					if (params.get(key).getClass().getName().equals("java.util.LinkedList") || params.get(key).getClass().getName().equals("java.util.ArrayList")
							|| params.get(key).getClass().getName().equals("com.opensymphony.xwork2.util.XWorkList")) {
						query.setParameterList(key, (Collection) params.get(key));
					} else {
						query.setParameter(key, params.get(key));
					}
				}
				ScrollableResults scrollableResults = query.scroll();
				scrollableResults.last();
				int rownum = scrollableResults.getRowNumber();
				page.setTotalCount(rownum + 1);
				query.setMaxResults(page.getPageSize());
				query.setFirstResult(page.getFirstResult());
				return query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findBySQL2(final String sql, final Map<String, Object> params) {
		hibernateTemplate.setCacheQueries(isCacheable);
		return (List<T>) hibernateTemplate.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);
				for (String key : params.keySet()) {
					// System.out.println(mapParam.get(key).getClass().getName());
					if (params.get(key).getClass().getName().equals("java.util.LinkedList") || params.get(key).getClass().getName().equals("java.util.ArrayList")
							|| params.get(key).getClass().getName().equals("com.opensymphony.xwork2.util.XWorkList")) {
						query.setParameterList(key, (Collection) params.get(key));
					} else {
						query.setParameter(key, params.get(key));
					}
				}
				ScrollableResults scrollableResults = query.scroll();
				scrollableResults.last();
				
				return query.list();
			}
		});
	}
	
	/**
	 * @功能描述：动态设置创建者信息
	 * @作者 : cjj 陈俊杰
	 * @创建时间 : 2016年2月17日下午8:06:52
	 * @param entity
	 * @throws IllegalAccessException
	 */
	public void autoSetCreateInfo(Object entity) throws IllegalAccessException {
		// 对象本身的属性
		Field[] fields = entity.getClass().getDeclaredFields();
		JxmallAction args = new JxmallAction();
		UserVo vo = args.getUserVo();
		for (Field field : fields) {
			String name = field.getName();
			field.setAccessible(true);
			if (name.equals(Entity.CREATORID)) {
				// 设置创建人ID
				field.set(entity, vo.getId());
			} else if (name.equals(Entity.CREATOR)) {
				// 设置创建人姓名
				field.set(entity, vo.getUserName());
			} else if (name.equals(Entity.CREATEDTIME)) {
				// 设置创建时间
				field.set(entity, DateUtils.getCurrentDateTime());
			} else {
				continue;
			}
		}
		// 父对象的属性
		Field[] parentfields = entity.getClass().getSuperclass().getDeclaredFields();
		for (Field field : parentfields) {
			String name = field.getName();
			field.setAccessible(true);
			if (name.equals(Entity.CREATORID)) {
				// 设置创建人ID
				field.set(entity, vo.getId());
			} else if (name.equals(Entity.CREATOR)) {
				// 设置创建人姓名
				field.set(entity, vo.getUserName());
			} else if (name.equals(Entity.CREATEDTIME)) {
				// 设置创建时间
				field.set(entity, DateUtils.getCurrentDateTime());
			} else {
				continue;
			}
		}
	}
	
	/**
	 * @功能描述：动态设置操作信息
	 * @作者 : cjj 陈俊杰
	 * @创建时间 : 2016年2月17日下午8:06:52
	 * @param entity
	 * @throws IllegalAccessException
	 */
	public void autoSetUpdateInfo(Object entity) throws IllegalAccessException {
		// 对象本身的属性
		Field[] fields = entity.getClass().getDeclaredFields();
		JxmallAction args = new JxmallAction();
		UserVo vo = args.getUserVo();
		for (Field field : fields) {
			String name = field.getName();
			field.setAccessible(true);
			if (name.equals(Entity.UPDATORID)) {
				// 设置创建人ID
				field.set(entity, vo.getId());
			} else if (name.equals(Entity.UPDATOR)) {
				// 设置创建人姓名
				field.set(entity, vo.getUserName());
			} else if (name.equals(Entity.UPATOR)) {
				// 设置创建人姓名
				field.set(entity, vo.getUserName());
			} else if (name.equals(Entity.UPDATETIME)) {
				// 设置创建时间
				field.set(entity, DateUtils.getCurrentDateTime());
			} else {
				continue;
			}
		}
		// 父对象的属性
		Field[] parentfields = entity.getClass().getSuperclass().getDeclaredFields();
		for (Field field : parentfields) {
			String name = field.getName();
			field.setAccessible(true);
			if (name.equals(Entity.UPDATORID)) {
				// 设置创建人ID
				field.set(entity, vo.getId());
			} else if (name.equals(Entity.UPDATOR)) {
				// 设置创建人姓名
				field.set(entity, vo.getUserName());
			} else if (name.equals(Entity.UPATOR)) {
				// 设置创建人姓名
				field.set(entity, vo.getUserName());
			} else if (name.equals(Entity.UPDATETIME)) {
				// 设置创建时间
				field.set(entity, DateUtils.getCurrentDateTime());
			} else {
				continue;
			}
		}
	}
	
	/**
	 * @功能描述：动态设置审核信息
	 * @作者 : cjj 陈俊杰
	 * @创建时间 : 2016年2月17日下午8:06:52
	 * @param entity
	 * @throws IllegalAccessException
	 */
	public void autoSetAuditInfo(Object entity) throws IllegalAccessException {
		// 对象本身的属性
		Field[] fields = entity.getClass().getDeclaredFields();
		JxmallAction args = new JxmallAction();
		UserVo vo = args.getUserVo();
		for (Field field : fields) {
			String name = field.getName();
			field.setAccessible(true);
			if (name.equals(Entity.AUDITORID)) {
				// 设置创建人ID
				field.set(entity, vo.getId());
			} else if (name.equals(Entity.AUDITOR)) {
				// 设置创建人姓名
				field.set(entity, vo.getUserName());
			} else if (name.equals(Entity.AUDITTIME)) {
				// 设置创建时间
				field.set(entity, DateUtils.getCurrentDateTime());
			} else {
				continue;
			}
		}
		// 父对象的属性
		Field[] parentfields = entity.getClass().getSuperclass().getDeclaredFields();
		for (Field field : parentfields) {
			String name = field.getName();
			field.setAccessible(true);
			if (name.equals(Entity.AUDITORID)) {
				// 设置创建人ID
				field.set(entity, vo.getId());
			} else if (name.equals(Entity.AUDITOR)) {
				// 设置创建人姓名
				field.set(entity, vo.getUserName());
			} else if (name.equals(Entity.AUDITTIME)) {
				// 设置创建时间
				field.set(entity, DateUtils.getCurrentDateTime());
			} else {
				continue;
			}
		}
	}
}
