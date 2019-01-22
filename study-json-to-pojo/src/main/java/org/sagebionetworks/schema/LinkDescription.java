package org.sagebionetworks.schema;

import org.sagebionetworks.schema.adapter.JSONEntity;
import org.sagebionetworks.schema.adapter.JSONObjectAdapter;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;

/**
 * 6.1.1. Link Description Object
 * 
 * 
 * A link description object is used to describe link relations. In the context
 * of a schema, it defines the link relations of the instances of the schema,
 * and can be parameterized by the instance values. The link description format
 * can be used on its own in regular (non-schema documents), and use of this
 * format can be declared by referencing the normative link description schema
 * as the the schema for the data structure that uses the links. The URI of the
 * normative link description schema is: http://json-schema.org/links (latest
 * version) or http://json-schema.org/draft-03/links (draft-03 version).
 * 
 */
public class LinkDescription implements JSONEntity {

	private static final String JSON_HREF = "href";
	public static final String JSON_REL = "rel";

	/**
	 * 
	 6.1.1.2. rel
	 * 
	 * 
	 * The value of the "rel" property indicates the name of the relation to the
	 * target resource. The relation to the target SHOULD be interpreted as
	 * specifically from the instance object that the schema (or sub-schema)
	 * applies to, not just the top level resource that contains the object
	 * within its hierarchy. If a resource JSON representation contains a sub
	 * object with a property interpreted as a link, that sub-object holds the
	 * relation with the target. A relation to target from the top level
	 * resource MUST be indicated with the schema describing the top level JSON
	 * representation.
	 * 
	 * Relationship definitions SHOULD NOT be media type dependent, and users
	 * are encouraged to utilize existing accepted relation definitions,
	 * including those in existing relation registries (see RFC 4287 [RFC4287]).
	 * However, we define these relations here for clarity of normative
	 * interpretation within the context of JSON hyper schema defined relations:
	 * 
	 * self If the relation value is "self", when this property is encountered
	 * in the instance object, the object represents a resource and the instance
	 * object is treated as a full representation of the target resource
	 * identified by the specified URI.
	 * 
	 * full This indicates that the target of the link is the full
	 * representation for the instance object. The object that contains this
	 * link possibly may not be the full representation.
	 * 
	 * describedby This indicates the target of the link is the schema for the
	 * instance object. This MAY be used to specifically denote the schemas of
	 * objects within a JSON object hierarchy, facilitating polymorphic type
	 * data structures.
	 * 
	 * root This relation indicates that the target of the link SHOULD be
	 * treated as the root or the body of the representation for the purposes of
	 * user agent interaction or fragment resolution. All other properties of
	 * the instance objects can be regarded as meta-
	 * 
	 */
	public enum LinkRel {
		SELF("self"),
		FULL("full"),
		DESCRIBED_BY("describedby"),
		ROOT("root");
		
		private String jsonName;
		LinkRel(String jsonName){
			this.jsonName = jsonName;
		}
		
		/**
		 * The name of this REL as it should appear in the JSON schema.
		 * @return
		 */
		public String getJsonName(){
			return this.jsonName;
		}
		
		/**
		 * Look up a REL using the json name. See getJsonName()
		 * @param json
		 * @return
		 */
		public static LinkRel getRelForJson(String json){
			for(LinkRel rel: LinkRel.values()){
				if(rel.getJsonName().equals(json)) return rel;
			}
			throw new IllegalArgumentException("Cannot find a LinkRel for json: "+json);
		}
	}
	
	private LinkRel rel = null;
	private String href = null;
	
	public LinkDescription(){};
	
	public LinkDescription(LinkRel rel, String href) {
		super();
		this.rel = rel;
		this.href = href;
	}
	public LinkRel getRel() {
		return rel;
	}
	public void setRel(LinkRel rel) {
		this.rel = rel;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}

	@Override
	public JSONObjectAdapter initializeFromJSONObject(JSONObjectAdapter adapter) throws JSONObjectAdapterException {
		if (adapter.has(JSON_REL)) {
			this.rel = LinkRel.getRelForJson(adapter.getString(JSON_REL));
		}
		if(adapter.has(JSON_HREF)){
			this.href = adapter.getString(JSON_HREF);
		}
		return adapter;
	}

	@Override
	public JSONObjectAdapter writeToJSONObject(JSONObjectAdapter writeTo)throws JSONObjectAdapterException {
		if(this.rel != null){
			writeTo.put(JSON_REL, this.rel.getJsonName());
		}
		if(this.href != null){
			writeTo.put(JSON_HREF, href);
		}
		return writeTo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((href == null) ? 0 : href.hashCode());
		result = prime * result + ((rel == null) ? 0 : rel.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinkDescription other = (LinkDescription) obj;
		if (href == null) {
			if (other.href != null)
				return false;
		} else if (!href.equals(other.href))
			return false;
		if (rel != other.rel)
			return false;
		return true;
	}

}
