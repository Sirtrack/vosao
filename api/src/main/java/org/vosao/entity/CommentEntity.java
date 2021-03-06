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

package org.vosao.entity;








import java.util.Date;



/**
 * @author Alexander Oleynik
 */
public class CommentEntity extends BaseEntityImpl {

	private static final long serialVersionUID = 8L;

	public String pageUrl;
	public String name;
	public String content;
	public Date publishDate;
	public boolean disabled;

	public CommentEntity() {
		publishDate = new Date();
	}
	
	public CommentEntity(final String aName, final String aContent, 
			final Date aPublishDate, final String aPageUrl) {
		setName(aName);
		setContent(aContent);
		setPublishDate(aPublishDate);
		setPageUrl(aPageUrl);
		setDisabled(false);
	}

	public CommentEntity(final String aName, final String aContent, 
			final Date aPublishDate, final String aPageUrl, 
			final boolean aDisabled) {
		this(aName, aContent, aPublishDate, aPageUrl);
		setDisabled(aDisabled);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String value) {
		this.name = value;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
}
