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

import java.util.List;

import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.MessageDao;
import org.vosao.entity.MessageEntity;

import siena.core.async.QueryAsync;


public class MessageDaoImpl extends BaseDaoImpl<MessageEntity> 
		implements MessageDao {

	public MessageDaoImpl() {
		super(MessageEntity.class);
	}

	@Override
	public List<MessageEntity> selectByCode(final String code) {
		QueryAsync q = newQuery();
		q.filter("code", code);
		return select(q, "selectByCode", params(code));
	}
	
	@Override
	public MessageEntity getByCode(final String code, 
			final String languageCode) {
		QueryAsync q = newQuery();
		q.filter("code", code);
		q.filter("languageCode", languageCode);
		return selectOne(q, "getByCode", params(code, languageCode));
	}

	@Override
	public List<MessageEntity> select(final String languageCode) {
		QueryAsync q = newQuery();
		q.filter("languageCode", languageCode);
		return select(q, "select", params(languageCode));
	}

}
