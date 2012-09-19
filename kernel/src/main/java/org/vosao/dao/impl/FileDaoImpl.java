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



import java.util.Date;
import java.util.List;

import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.FileChunkDao;
import org.vosao.dao.FileDao;
import org.vosao.entity.FileEntity;

import siena.core.async.QueryAsync;

public class FileDaoImpl extends BaseDaoImpl<FileEntity> 
		implements FileDao {

	public FileDaoImpl() {
		super(FileEntity.class);
	}

	@Override
	public void remove(final Long fileId) {
		if (fileId == null) {
			return;
		}
		getFileChunkDao().removeByFile(fileId);
		super.remove(fileId);
	}

	@Override
	public void remove(final List<Long> ids) {
		for (Long fileId : ids) {
			remove(fileId);
		}
	}

	@Override
	public List<FileEntity> getByFolder(Long folderId) {
		QueryAsync q = newQuery();
		q.filter("folderId", folderId);
		return select(q, "getByFolder", params(folderId));
	}

	@Override
	public FileEntity getByName(Long folderId, String name) {
		QueryAsync q = newQuery();
		q.filter("folderId", folderId);
		q.filter("filename", name);
		return selectOne(q, "getByName", params(folderId, name));
	}

	@Override
	public void save(FileEntity file, byte[] content) {
		file.setLastModifiedTime(new Date());
		file.setSize(content.length);
		save(file);
		getFileChunkDao().save(file, content);
	}

	@Override
	public void removeByFolder(Long folderId) {
		List<FileEntity> files = getByFolder(folderId);
		for (FileEntity file : files) {
			remove(file.getId());
		}
	}

	@Override
	public void removeAll() {
		super.removeAll();
		getFileChunkDao().removeAll();
	}

	public FileChunkDao getFileChunkDao() {
		return getDao().getFileChunkDao();
	}

	@Override
	public byte[] getFileContent(FileEntity file) {
		return getFileChunkDao().getFileContent(file);
	}

}
