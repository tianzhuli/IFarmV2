package com.ifarm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ifarm.bean.Farm;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.FileUtil;

@Repository
@SuppressWarnings("unchecked")
public class FarmDao extends BaseDao<Farm> {

	public List<Farm> getFarmsList(String userId) {
		Session session = getSession();
		String hql = "from Farm f where f.userId=?";
		Query query = session.createQuery(hql);
		query.setString(0, userId);
		List<Farm> farms = query.list();
		for (int i = 0; i < farms.size(); i++) {
			Farm currentFarm = farms.get(i);
			session.evict(currentFarm);
			if (currentFarm.getFarmImageUrl() != null && currentFarm.getUserId() != null) {
				String farmImageUrl = FileUtil.makeRealPathUrl(CacheDataBase.farmImagePath, currentFarm.getFarmImageUrl(), currentFarm.getUserId());
				currentFarm.setFarmImageUrl(farmImageUrl);
			}
		}
		return farms;
	}
}
