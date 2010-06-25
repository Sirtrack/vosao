/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * Copyright (C) 2009 Vosao development team
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.search.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.business.mq.Topic;
import org.vosao.business.mq.message.PageMessage;
import org.vosao.business.mq.message.SimpleMessage;
import org.vosao.common.VosaoContext;
import org.vosao.dao.Dao;
import org.vosao.entity.LanguageEntity;
import org.vosao.entity.PageEntity;
import org.vosao.search.SearchEngine;
import org.vosao.search.SearchIndex;
import org.vosao.search.SearchResult;

/**
 *  
 * @author Alexander Oleynik
 *
 */

public class SearchEngineImpl implements SearchEngine {

	private static final Log logger = LogFactory.getLog(
			SearchEngineImpl.class);

	private static final int REINDEX_CHUNK = 20;
	
	private Map<String, SearchIndex> indexes;

	private SearchIndex getSearchIndex(String language) {
		if (indexes == null) {
			indexes = new HashMap<String, SearchIndex>();
		}
		if (!indexes.containsKey(language)) {
			indexes.put(language, new SearchIndexImpl(language));
		}
		return indexes.get(language);
	}
	
	@Override
	public void reindex() {
		for (LanguageEntity language : getDao().getLanguageDao().select()) {
			getSearchIndex(language.getCode()).clear();
			getSearchIndex(language.getCode()).saveIndex();
		}
		sendReindexPage(getBusiness().getPageBusiness().getTree(), 
				new PageMessage(Topic.REINDEX));
		PageMessage saveMessage = new PageMessage(Topic.REINDEX, "save");
		getBusiness().getMessageQueue().publish(saveMessage);
	}
	
	private void sendReindexPage(TreeItemDecorator<PageEntity> page, 
			PageMessage message) {
		PageMessage msg = message;
		if (msg.getPages().size() >= REINDEX_CHUNK) {
			getBusiness().getMessageQueue().publish(msg);
			msg = new PageMessage(Topic.REINDEX);
		}
		msg.addPage(page.getEntity().getFriendlyURL(), page.getEntity().getId());
		for (TreeItemDecorator<PageEntity> child : page.getChildren()) {
			sendReindexPage(child, msg);
		}
	}

	@Override
	public SearchResult search(String query, int start, int count,
			String language, int textSize) {
		return getSearchIndex(language).search(query, start, count, textSize);
	}

	@Override
	public void updateIndex(PageEntity page) {
		for (LanguageEntity language : getDao().getLanguageDao().select()) {
			getSearchIndex(language.getCode()).updateIndex(page.getId());
		}
	}

	@Override
	public void removeFromIndex(Long pageId) {
		for (LanguageEntity language : getDao().getLanguageDao().select()) {
			getSearchIndex(language.getCode()).removeFromIndex(pageId);
		}
	}

	@Override
	public void saveIndex() {
		for (LanguageEntity language : getDao().getLanguageDao().select()) {
			getSearchIndex(language.getCode()).saveIndex();
		}
	}
	
	private Business getBusiness() {
		return VosaoContext.getInstance().getBusiness();
	}
	
	private Dao getDao() {
		return getBusiness().getDao();
	}
}
