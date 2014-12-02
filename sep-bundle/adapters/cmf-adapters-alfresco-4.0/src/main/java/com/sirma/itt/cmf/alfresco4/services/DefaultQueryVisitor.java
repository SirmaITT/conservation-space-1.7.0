package com.sirma.itt.cmf.alfresco4.services;

import com.sirma.itt.cmf.alfresco4.AlfrescoUtils;
import com.sirma.itt.emf.search.AbstractQueryVistor;
import com.sirma.itt.emf.search.Query;
import com.sirma.itt.emf.search.Query.QueryBoost;

/**
 * Default visitor implementation for queries.
 *
 * @author Borislav Banchev
 */
public abstract class DefaultQueryVisitor extends AbstractQueryVistor {

	/*
	 * (non-Javadoc)
	 * @see com.sirma.itt.cmf.search.Query.Visitor#visitStart(com.sirma.itt.cmf .search.Query,
	 * com.sirma.itt.cmf.search.Query.Boost)
	 */
	@Override
	public void visitStart(Query query, QueryBoost boost) {

		int lastIndexOf = getQuery().lastIndexOf("(");
		if (lastIndexOf > 0) {
			String substring = getQuery().substring(lastIndexOf + 1);
			if (substring.trim().length() > 0) {
				getQuery().append(query.getBoost().toString());
			} else if (query.getBoost() == QueryBoost.EXCLUDE) {
				getQuery().append(query.getBoost().toString());
			}
		} else if (query.getBoost() == QueryBoost.EXCLUDE) {
			getQuery().append(query.getBoost().toString());
		}

		getQuery().append(AlfrescoUtils.SEARCH_START);
	}

	/*
	 * (non-Javadoc)
	 * @see com.sirma.itt.cmf.search.Query.Visitor#visitEnd(com.sirma.itt.cmf .search.Query)
	 */
	@Override
	public void visitEnd(Query query) {
		getQuery().append(AlfrescoUtils.SEARCH_END);
	}

	@Override
	public abstract void visit(Query query) throws Exception;

}
