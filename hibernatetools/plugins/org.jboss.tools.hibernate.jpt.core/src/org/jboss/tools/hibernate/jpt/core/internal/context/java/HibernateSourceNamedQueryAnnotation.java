/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.jpt.core.internal.context.java;

import java.util.ListIterator;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.internal.resource.java.source.SourceAnnotation;
import org.eclipse.jpt.core.internal.utility.jdt.ConversionDeclarationAnnotationElementAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.EnumDeclarationAnnotationElementAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.MemberAnnotationAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.MemberIndexedAnnotationAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.NestedIndexedDeclarationAnnotationAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.ShortCircuitAnnotationElementAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.core.resource.java.NestableAnnotation;
import org.eclipse.jpt.core.resource.java.NestableQueryHintAnnotation;
import org.eclipse.jpt.core.resource.java.QueryHintAnnotation;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.core.utility.jdt.AnnotationAdapter;
import org.eclipse.jpt.core.utility.jdt.AnnotationElementAdapter;
import org.eclipse.jpt.core.utility.jdt.DeclarationAnnotationAdapter;
import org.eclipse.jpt.core.utility.jdt.DeclarationAnnotationElementAdapter;
import org.eclipse.jpt.core.utility.jdt.IndexedAnnotationAdapter;
import org.eclipse.jpt.core.utility.jdt.IndexedDeclarationAnnotationAdapter;
import org.eclipse.jpt.core.utility.jdt.Member;
import org.eclipse.jpt.utility.internal.iterators.EmptyListIterator;
import org.jboss.tools.hibernate.jpt.core.internal.context.basic.Hibernate;
import org.jboss.tools.hibernate.jpt.core.internal.resource.java.HibernateNamedQueryAnnotation;

/**
 * @author Dmitry Geraskov
 *
 */
public class HibernateSourceNamedQueryAnnotation extends SourceAnnotation<Member>
	implements HibernateNamedQueryAnnotation {

	public static final SimpleDeclarationAnnotationAdapter DECLARATION_ANNOTATION_ADAPTER = new SimpleDeclarationAnnotationAdapter(ANNOTATION_NAME);

	private final DeclarationAnnotationElementAdapter<String> nameDeclarationAdapter;
	private final AnnotationElementAdapter<String> nameAdapter;
	private String name;

	private final DeclarationAnnotationElementAdapter<String> queryDeclarationAdapter;
	private final AnnotationElementAdapter<String> queryAdapter;
	private String query;
	
	private final DeclarationAnnotationElementAdapter<String> flushModeDeclarationAdapter;
	private final AnnotationElementAdapter<String> flushModeAdapter;
	private FlushModeType flushMode;
	
	private final DeclarationAnnotationElementAdapter<String> cacheModeDeclarationAdapter;
	private final AnnotationElementAdapter<String> cacheModeAdapter;
	private CacheModeType cacheMode;
	
	private final DeclarationAnnotationElementAdapter<Boolean> cacheableDeclarationAdapter;
	private final AnnotationElementAdapter<Boolean> cacheableAdapter;
	private Boolean cacheable;
	
	private final DeclarationAnnotationElementAdapter<String> cacheRegionDeclarationAdapter;
	private final AnnotationElementAdapter<String> cacheRegionAdapter;
	private String cacheRegion;
	
	private final DeclarationAnnotationElementAdapter<Integer> fetchSizeDeclarationAdapter;
	private final AnnotationElementAdapter<Integer> fetchSizeAdapter;
	private Integer fetchSize;
	
	private final DeclarationAnnotationElementAdapter<Integer> timeoutDeclarationAdapter;
	private final AnnotationElementAdapter<Integer> timeoutAdapter;
	private Integer timeout;
	
	private final DeclarationAnnotationElementAdapter<String> commentDeclarationAdapter;
	private final AnnotationElementAdapter<String> commentAdapter;
	private String comment;
	
	private final DeclarationAnnotationElementAdapter<Boolean> readOnlyDeclarationAdapter;
	private final AnnotationElementAdapter<Boolean> readOnlyAdapter;
	private Boolean readOnly;
	
	HibernateSourceNamedQueryAnnotation(JavaResourceNode parent, Member member,DeclarationAnnotationAdapter daa, AnnotationAdapter annotationAdapter) {
		super(parent, member, daa, annotationAdapter);
		this.nameDeclarationAdapter = this.buildNameAdapter(daa);
		this.nameAdapter = this.buildAdapter(this.nameDeclarationAdapter);
		this.queryDeclarationAdapter = this.buildQueryAdapter(daa);
		this.queryAdapter = this.buildAdapter(this.queryDeclarationAdapter);
		this.flushModeDeclarationAdapter = this.buildFlushModeAdapter(daa);
		this.flushModeAdapter = new ShortCircuitAnnotationElementAdapter<String>(member, flushModeDeclarationAdapter);
		this.cacheModeDeclarationAdapter = this.buildCacheModeAdapter(daa);
		this.cacheModeAdapter = new ShortCircuitAnnotationElementAdapter<String>(member, cacheModeDeclarationAdapter);
		this.cacheableDeclarationAdapter = this.buildCacheableAdapter(daa);
		this.cacheableAdapter = new ShortCircuitAnnotationElementAdapter<Boolean>(member, cacheableDeclarationAdapter);
		this.cacheRegionDeclarationAdapter = this.buildCacheRegionAdapter(daa);
		this.cacheRegionAdapter = this.buildAdapter(this.cacheRegionDeclarationAdapter);
		this.fetchSizeDeclarationAdapter = this.buildFetchSizeAdapter(daa);
		this.fetchSizeAdapter = new ShortCircuitAnnotationElementAdapter<Integer>(member, fetchSizeDeclarationAdapter);
		this.timeoutDeclarationAdapter = this.buildTimeoutAdapter(daa);
		this.timeoutAdapter = new ShortCircuitAnnotationElementAdapter<Integer>(member, timeoutDeclarationAdapter);
		this.commentDeclarationAdapter = this.buildCommentAdapter(daa);
		this.commentAdapter = new ShortCircuitAnnotationElementAdapter<String>(member, commentDeclarationAdapter);
		this.readOnlyDeclarationAdapter = this.buildReadOnlyAdapter(daa);
		this.readOnlyAdapter = new ShortCircuitAnnotationElementAdapter<Boolean>(member, readOnlyDeclarationAdapter);
	}

	public void initialize(CompilationUnit astRoot) {
		this.name = this.buildName(astRoot);
		this.query = this.buildQuery(astRoot);
		this.flushMode = this.buildFlushMode(astRoot);
		this.cacheMode = this.buildCacheMode(astRoot);
		this.cacheable = this.buildCacheable(astRoot);
		this.cacheRegion = this.buildCacheRegion(astRoot);
		this.fetchSize = this.buildFetchSize(astRoot);
		this.timeout = this.buildTimeout(astRoot);
		this.comment = this.buildComment(astRoot);
		this.readOnly = this.buildReadOnly(astRoot);
	}

	public void update(CompilationUnit astRoot) {
		this.setName(this.buildName(astRoot));
		this.setQuery(this.buildQuery(astRoot));
		this.setFlushMode(this.buildFlushMode(astRoot));
		this.setCacheMode(this.buildCacheMode(astRoot));
		this.setCacheable(this.buildCacheable(astRoot));
		this.setCacheRegion(this.buildCacheRegion(astRoot));
		this.setFetchSize(this.buildFetchSize(astRoot));
		this.setTimeout(this.buildTimeout(astRoot));
		this.setComment(this.buildComment(astRoot));
		this.setReadOnly(this.buildReadOnly(astRoot));
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}

	/**
	 * convenience method
	 */
	AnnotationElementAdapter<String> buildAdapter(DeclarationAnnotationElementAdapter<String> daea) {
		return new ShortCircuitAnnotationElementAdapter<String>(this.member, daea);
	}

	// ********** BaseNamedQueryAnnotation implementation **********

	// ***** name
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (this.attributeValueHasNotChanged(this.name, name)) {
			return;
		}
		String old = this.name;
		this.name = name;
		this.nameAdapter.setValue(name);
		this.firePropertyChanged(NAME_PROPERTY, old, name);
	}

	private String buildName(CompilationUnit astRoot) {
		return this.nameAdapter.getValue(astRoot);
	}

	public TextRange getNameTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(this.nameDeclarationAdapter, astRoot);
	}	

	// ***** query
	public String getQuery() {
		return this.query;
	}

	public void setQuery(String query) {
		if (this.attributeValueHasNotChanged(this.query, query)) {
			return;
		}
		String old = this.query;
		this.query = query;
		this.queryAdapter.setValue(query);
		this.firePropertyChanged(QUERY_PROPERTY, old, query);
	}

	private String buildQuery(CompilationUnit astRoot) {
		return this.queryAdapter.getValue(astRoot);
	}

	public TextRange getQueryTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(this.queryDeclarationAdapter, astRoot);
	}

	String getQueryElementName() {
		return Hibernate.NAMED_QUERY__QUERY;
	}
	
	// ***** hints
	public ListIterator<QueryHintAnnotation> hints() {
		return EmptyListIterator.instance();
	}

	ListIterator<NestableQueryHintAnnotation> nestableHints() {
		return EmptyListIterator.instance();
	}

	public int hintsSize() {
		return 0;
	}

	public NestableQueryHintAnnotation hintAt(int index) {
		throw new UnsupportedOperationException();
	}

	public int indexOfHint(QueryHintAnnotation queryHint) {
		return -1;
	}

	public NestableQueryHintAnnotation addHint(int index) {
		return null;
	}

	public void moveHint(int targetIndex, int sourceIndex) {
		//AnnotationContainerTools.moveNestedAnnotation(targetIndex, sourceIndex, this.hintsContainer);
	}

	public void removeHint(int index) {
		//AnnotationContainerTools.removeNestedAnnotation(index, this.hintsContainer);
	}

	// ******************** HibernateNamedQueryAnnotation implementation *************
	// ***** flushMode	

	public FlushModeType getFlushMode() {
		return flushMode;
	}

	public void setFlushMode(FlushModeType flushMode) {
		if (this.attributeValueHasNotChanged(this.flushMode, flushMode)) {
			return;
		}
		FlushModeType old = this.flushMode;
		this.flushMode = flushMode;
		this.flushModeAdapter.setValue(FlushModeType.toJavaAnnotationValue(flushMode));
		this.firePropertyChanged(FLUSH_MODE_PROPERTY, old, flushMode);
	}

	private FlushModeType buildFlushMode(CompilationUnit astRoot) {
		return FlushModeType.fromJavaAnnotationValue(this.flushModeAdapter.getValue(astRoot));
	}

	public TextRange getFlushModeTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(flushModeDeclarationAdapter, astRoot);
	}

	// ***** caheMode

	public CacheModeType getCacheMode() {
		return cacheMode;
	}

	public void setCacheMode(CacheModeType cacheMode) {
		if (this.attributeValueHasNotChanged(this.cacheMode, cacheMode)) {
			return;
		}
		CacheModeType old = this.cacheMode;
		this.cacheMode = cacheMode;
		this.cacheModeAdapter.setValue(CacheModeType.toJavaAnnotationValue(cacheMode));
		this.firePropertyChanged(CACHE_MODE_PROPERTY, old, cacheMode);
	}
	
	private CacheModeType buildCacheMode(CompilationUnit astRoot) {
		return CacheModeType.fromJavaAnnotationValue(this.cacheModeAdapter.getValue(astRoot));
	}

	public TextRange getCacheModeTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(cacheModeDeclarationAdapter, astRoot);
	}
	
	//************************ cacheable *********************************	
	public Boolean isCacheable(){
		return this.cacheable;
	}
	
	public void setCacheable(Boolean cacheable){
		if (this.attributeValueHasNotChanged(this.cacheable, cacheable)) {
			return;
		}
		Boolean old = this.cacheable;
		this.cacheable = cacheable;
		this.cacheableAdapter.setValue(cacheable);
		this.firePropertyChanged(CACHEABLE_PROPERTY, old, cacheable);
	}
	
	private Boolean buildCacheable(CompilationUnit astRoot) {
		return this.cacheableAdapter.getValue(astRoot);
	}

	//************************ cacheRegion *********************************
	public String getCacheRegion(){
		return this.cacheRegion;
	}
	
	public void setCacheRegion(String cacheRegion){
		if (this.attributeValueHasNotChanged(this.cacheRegion, cacheRegion)) {
			return;
		}
		String old = this.cacheRegion;
		this.cacheRegion = cacheRegion;
		this.cacheRegionAdapter.setValue(cacheRegion);
		this.firePropertyChanged(CACHE_REGION_PROPERTY, old, cacheRegion);
	}
	
	private String buildCacheRegion(CompilationUnit astRoot) {
		return this.cacheRegionAdapter.getValue(astRoot);
	}
	//************************ fetchSize *********************************
	public Integer getFetchSize(){
		return this.fetchSize;
	}
	
	public void setFetchSize(Integer fetchSize){
		if (this.attributeValueHasNotChanged(this.fetchSize, fetchSize)) {
			return;
		}
		Integer old = this.fetchSize;
		this.fetchSize = fetchSize;
		this.fetchSizeAdapter.setValue(fetchSize);
		this.firePropertyChanged(FETCH_SIZE_PROPERTY, old, fetchSize);
	}
	
	private Integer buildFetchSize(CompilationUnit astRoot) {
		return this.fetchSizeAdapter.getValue(astRoot);
	}
	//************************ timeout *********************************
	public Integer getTimeout(){
		return this.timeout;	
	}
	public void setTimeout(Integer timeout){
		if (this.attributeValueHasNotChanged(this.timeout, timeout)) {
			return;
		}
		Integer old = this.timeout;
		this.timeout = timeout;
		this.timeoutAdapter.setValue(timeout);
		this.firePropertyChanged(TIMEOUT_PROPERTY, old, timeout);
	}

	private Integer buildTimeout(CompilationUnit astRoot) {
		return this.timeoutAdapter.getValue(astRoot);
	}
	//************************ comment *********************************
	public String getComment(){
		return this.comment;
	}
	
	public void setComment(String comment){
		if (this.attributeValueHasNotChanged(this.comment, comment)) {
			return;
		}
		String old = this.comment;
		this.comment = comment;
		this.commentAdapter.setValue(comment);
		this.firePropertyChanged(COMMENT_PROPERTY, old, comment);
	}
	
	private String buildComment(CompilationUnit astRoot) {
		return this.commentAdapter.getValue(astRoot);
	}
	//************************ readOnly *********************************	
	public Boolean isReadOnly(){
		return this.readOnly;
	}
	
	public void setReadOnly(Boolean readOnly){
		if (this.attributeValueHasNotChanged(this.readOnly, readOnly)) {
			return;
		}
		Boolean old = this.readOnly;
		this.readOnly = readOnly;
		this.readOnlyAdapter.setValue(readOnly);
		this.firePropertyChanged(READ_ONLY_PROPERTY, old, readOnly);	
	}

	private Boolean buildReadOnly(CompilationUnit astRoot) {
		return this.readOnlyAdapter.getValue(astRoot);
	}
	// ********** NestableAnnotation implementation **********

	/**
	 * convenience implementation of method from NestableAnnotation interface
	 * for subclasses
	 */
	public void initializeFrom(NestableAnnotation oldAnnotation) {
		HibernateSourceNamedQueryAnnotation oldQuery = (HibernateSourceNamedQueryAnnotation) oldAnnotation;
		this.setName(oldQuery.getName());
		this.setQuery(oldQuery.getQuery());
		this.setFlushMode(oldQuery.getFlushMode());
		this.setCacheMode(oldQuery.getCacheMode());
		this.setCacheable(oldQuery.isCacheable());
		this.setComment(oldQuery.getComment());
		this.setFetchSize(oldQuery.getFetchSize());
		this.setTimeout(oldQuery.getTimeout());
		this.setCacheRegion(oldQuery.getCacheRegion());
		this.setReadOnly(oldQuery.isReadOnly());
	}

	/**
	 * convenience implementation of method from NestableAnnotation interface
	 * for subclasses
	 */
	public void moveAnnotation(int newIndex) {
		this.getIndexedAnnotationAdapter().moveAnnotation(newIndex);
	}

	private IndexedAnnotationAdapter getIndexedAnnotationAdapter() {
		return (IndexedAnnotationAdapter) this.annotationAdapter;
	}
	
	public static HibernateNamedQueryAnnotation createNamedQuery(JavaResourceNode parent, Member member) {
		return new HibernateSourceNamedQueryAnnotation(parent, member, DECLARATION_ANNOTATION_ADAPTER, new MemberAnnotationAdapter(member, DECLARATION_ANNOTATION_ADAPTER));
	}
	
	protected DeclarationAnnotationElementAdapter<String> buildNameAdapter(DeclarationAnnotationAdapter daAdapter) {
		return ConversionDeclarationAnnotationElementAdapter.forStrings(daAdapter, Hibernate.NAMED_QUERY__NAME);
	}
	
	protected DeclarationAnnotationElementAdapter<String> buildQueryAdapter(DeclarationAnnotationAdapter daAdapter) {
		return ConversionDeclarationAnnotationElementAdapter.forStrings(daAdapter, Hibernate.NAMED_QUERY__QUERY);
	}
	
	protected DeclarationAnnotationElementAdapter<String> buildFlushModeAdapter(DeclarationAnnotationAdapter daAdapter) {
		return new EnumDeclarationAnnotationElementAdapter(daAdapter, Hibernate.NAMED_QUERY__FLUSH_MODE);
	}

	protected DeclarationAnnotationElementAdapter<String> buildCacheModeAdapter(DeclarationAnnotationAdapter daAdapter) {
		return new EnumDeclarationAnnotationElementAdapter(daAdapter, Hibernate.NAMED_QUERY__CACHE_MODE);
	}	

	protected DeclarationAnnotationElementAdapter<Boolean> buildCacheableAdapter(DeclarationAnnotationAdapter daAdapter) {
		return ConversionDeclarationAnnotationElementAdapter.forBooleans(daAdapter, Hibernate.NAMED_QUERY__CACHEABLE);
	}
	
	protected DeclarationAnnotationElementAdapter<String> buildCacheRegionAdapter(DeclarationAnnotationAdapter daAdapter){
		return ConversionDeclarationAnnotationElementAdapter.forStrings(daAdapter, Hibernate.NAMED_QUERY__CACHE_REGION);
	}
	
	protected DeclarationAnnotationElementAdapter<Integer> buildFetchSizeAdapter(DeclarationAnnotationAdapter daAdapter){
		return ConversionDeclarationAnnotationElementAdapter.forNumbers(daAdapter, Hibernate.NAMED_QUERY__FETCH_SIZE);
	}
	
	protected DeclarationAnnotationElementAdapter<Integer> buildTimeoutAdapter(DeclarationAnnotationAdapter daAdapter){
		return ConversionDeclarationAnnotationElementAdapter.forNumbers(daAdapter, Hibernate.NAMED_QUERY__TIMEOUT);
	}
	
	protected DeclarationAnnotationElementAdapter<String> buildCommentAdapter(DeclarationAnnotationAdapter daAdapter){
		return ConversionDeclarationAnnotationElementAdapter.forStrings(daAdapter, Hibernate.NAMED_QUERY__COMMENT);
	}
	
	protected DeclarationAnnotationElementAdapter<Boolean> buildReadOnlyAdapter(DeclarationAnnotationAdapter daAdapter) {
		return ConversionDeclarationAnnotationElementAdapter.forBooleans(daAdapter, Hibernate.NAMED_QUERY__READ_ONLY);
	}	

	@Override
	public void toString(StringBuilder sb) {
		sb.append(this.name);
	}
	
	// ********** static methods **********

	public static HibernateSourceNamedQueryAnnotation createHibernateNamedQuery(JavaResourceNode parent, Member member) {
		return new HibernateSourceNamedQueryAnnotation(parent, member, DECLARATION_ANNOTATION_ADAPTER, new MemberAnnotationAdapter(member, DECLARATION_ANNOTATION_ADAPTER));
	}

	public static HibernateSourceNamedQueryAnnotation createNestedHibernateNamedQuery(JavaResourceNode parent, Member member, int index, DeclarationAnnotationAdapter attributeOverridesAdapter) {
		IndexedDeclarationAnnotationAdapter idaa = buildNestedHibernateDeclarationAnnotationAdapter(index, attributeOverridesAdapter);
		IndexedAnnotationAdapter annotationAdapter = new MemberIndexedAnnotationAdapter(member, idaa);
		return new HibernateSourceNamedQueryAnnotation(parent, member, idaa, annotationAdapter);
	}

	private static IndexedDeclarationAnnotationAdapter buildNestedHibernateDeclarationAnnotationAdapter(int index, DeclarationAnnotationAdapter hibernateNamedQueriesAdapter) {
		return new NestedIndexedDeclarationAnnotationAdapter(hibernateNamedQueriesAdapter, index, Hibernate.NAMED_QUERY);
	}

}
