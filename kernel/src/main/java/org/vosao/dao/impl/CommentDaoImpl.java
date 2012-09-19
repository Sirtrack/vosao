/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.dao.impl;



import java.util.Collections;
import java.util.List;

import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.CommentDao;
import org.vosao.entity.CommentEntity;
import org.vosao.entity.helper.CommentHelper;

import siena.core.async.QueryAsync;

/**
 * @author Alexander Oleynik
 */
public class CommentDaoImpl extends BaseDaoImpl<CommentEntity> 
		implements CommentDao {

	public CommentDaoImpl() {
		super(CommentEntity.class);
	}

	@Override
	public List<CommentEntity> getByPage(final String pageUrl) {
		QueryAsync q = newQuery();
		q.filter("pageUrl", pageUrl);
		List<CommentEntity> result = select(q, "getByPage", params(pageUrl));
		Collections.sort(result, new CommentHelper.PublishDateDesc());
		return result;
	}
	
	@Override
	public void disable(List<Long> ids) {
		getQueryCache().removeQueries(CommentEntity.class);
		for (Long id : ids) {
			CommentEntity comment = getById(id);
			if (comment != null) {
				comment.setDisabled(true);
				save(comment);
				getEntityCache().removeEntity(CommentEntity.class, id);
			}
		}
	}

	@Override
	public void enable(List<Long> ids) {
		getQueryCache().removeQueries(CommentEntity.class);
		for (Long id : ids) {
			CommentEntity comment = getById(id);
			if (comment != null) {
				comment.setDisabled(false);
				save(comment);
				getEntityCache().removeEntity(CommentEntity.class, id);
			}
		}
	}

	@Override
	public List<CommentEntity> getByPage(String pageUrl, boolean disabled) {
		QueryAsync q = newQuery();
		q.filter("pageUrl", pageUrl);
		q.filter("disabled", disabled);
		List<CommentEntity> result = select(q, "getByPage", params(pageUrl, 
				disabled));
		Collections.sort(result, new CommentHelper.PublishDateDesc());
		return result;
	}

	@Override
	public void removeByPage(String url) {
		QueryAsync q = newQuery();
		q.filter("pageUrl", url);
		removeSelected(q);
	}

	@Override
	public List<CommentEntity> getRecent(int limit) {
		QueryAsync q = newQuery();
		q.filter("disabled", false);
		List<CommentEntity> result = select(q, "getRecent", limit, params(false));
		Collections.sort(result, new CommentHelper.PublishDateDesc());
		return result;
	}
	
}
