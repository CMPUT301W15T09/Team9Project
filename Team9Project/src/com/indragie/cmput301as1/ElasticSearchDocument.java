/**
 * 
 */
package com.indragie.cmput301as1;

/**
 * Interface that all model objects stored in ElasticSearch must implement
 * in order to uniquely identify them.
 */
public interface ElasticSearchDocument {
	/**
	 * @return A unique identifier for the document. This identifier must
	 * uniquely identify the document within its document type.
	 */
	public ElasticSearchDocumentID getObjectID();
}
