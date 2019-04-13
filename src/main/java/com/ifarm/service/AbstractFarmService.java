package com.ifarm.service;

import java.lang.reflect.Field;

import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ifarm.annotation.Validator;
import com.ifarm.dao.BaseDao;
import com.ifarm.enums.SystemReturnCodeEnum;
import com.ifarm.exception.ValidatorException;
import com.ifarm.util.JsonObjectUtil;
import com.ifarm.util.SystemResultEncapsulation;

public abstract class AbstractFarmService<T extends BaseDao<V>, V> {

	protected static final Logger FARM_SERVICE_LOG = LoggerFactory
			.getLogger(FarmControlUnitService.class);

	private T t;

	@Autowired
	public void setBaseValue(T t) {
		this.t = t;
	}

	public String baseSave(V v, boolean isThrowExc) throws ValidatorException {
		if (isThrowExc) {
			validator(v);
			t.saveBase(v);
		}
		return baseSave(v);
	}

	public String baseSave(V v) {
		try {
			validator(v);
			t.saveBase(v);
			return SystemResultEncapsulation
					.fillResultCode(SystemReturnCodeEnum.SUCCESS);
		} catch (ConstraintViolationException violationException) {
			FARM_SERVICE_LOG.error("唯一索引异常", violationException);
			return SystemResultEncapsulation
					.fillResultCode(SystemReturnCodeEnum.UNIQUE_ERROR);
		} catch (ValidatorException vException) {
			// TODO: handle exception
			FARM_SERVICE_LOG.error("ValidationException", vException);
			return SystemResultEncapsulation
					.fillResultCode(SystemReturnCodeEnum.PARAM_ERROR);
		} catch (HibernateException hibernateException) {
			// TODO: handle exception
			FARM_SERVICE_LOG.error("hibernateException", hibernateException);
			return SystemResultEncapsulation
					.fillResultCode(SystemReturnCodeEnum.DATA_ERROR);
		} catch (Exception e) {
			// TODO: handle exception
			FARM_SERVICE_LOG.error("未知异常", e);
			return SystemResultEncapsulation
					.fillResultCode(SystemReturnCodeEnum.UNKOWN_ERROR);
		}
	}

	protected void validator(V v) throws ValidatorException {
		Field[] fields = v.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			try {
				Validator validator = fields[i].getAnnotation(Validator.class);
				if (validator != null && fields[i].get(v) == null) {
					throw new ValidatorException(fields[i].getName()
							+ " is null");
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				FARM_SERVICE_LOG.error("validator error", e);
			}
		}
	}

	public String baseDelete(V v) {
		try {
			t.deleteBase(v);
			return SystemResultEncapsulation
					.fillResultCode(SystemReturnCodeEnum.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			FARM_SERVICE_LOG.error("delete error", e);
			return SystemResultEncapsulation
					.fillResultCode(SystemReturnCodeEnum.UNKOWN_ERROR);
		}
	}

	public V queryValById(Object id, Class<V> classType) {
		return t.getTById(id, classType);
	}

	public String queryDynamicListAddLike(V v) {
		return JsonObjectUtil.toJsonArrayString(t.getDynamicListAddLike(v));
	}

	public String queryDynamicList(V v) {
		return JsonObjectUtil.toJsonArrayString(t.getDynamicList(v));
	}

	public String baseUpdate(V v) {
		try {
			t.updateDynamic(v);
			return SystemResultEncapsulation
					.fillResultCode(SystemReturnCodeEnum.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			FARM_SERVICE_LOG.error("update error", e);
			return SystemResultEncapsulation
					.fillResultCode(SystemReturnCodeEnum.UNKOWN_ERROR);
		}
	}
}
