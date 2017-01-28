package fcgold;

import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.collision.Filter;
import org.dyn4j.geometry.Vector2;

public class GameFilter implements Filter {
	/** The category this object is in */
	protected final long category;
	
	/** The categories this object can collide with */
	protected final long mask;
	protected final boolean noJoints;
	protected final Vector2 v1;
	protected final Vector2 v2;
	
	/**
	 * Default constructor.
	 * <p>
	 * By default the category is 1 and the mask is all categories.
	 */
	public GameFilter() {
		this.category = 1;
		this.mask = Long.MAX_VALUE;
		this.v1 = null;
		this.v2 = null;
		this.noJoints = true;
	}
	
	/**
	 * Full constructor.
	 * @param category the category bits
	 * @param mask the mask bits
	 */
	public GameFilter(long category, long mask) {
		super();
		this.category = category;
		this.mask = mask;
		this.v1 = null;
		this.v2 = null;
		this.noJoints = true;
	}
	public GameFilter(long category, long mask, Vector2 v) {
		super();
		this.category = category;
		this.mask = mask;
		this.v1 = v;
		this.v2 = v;
		this.noJoints = false;
	}
	public GameFilter(long category, long mask, Vector2 v, Vector2 vv) {
		super();
		this.category = category;
		this.mask = mask;
		this.v1 = v;
		this.v2 = vv;
		this.noJoints = false;
	}
	
	/**
	 * Returns true if the given {@link Filter} and this {@link Filter}
	 * allow the objects to interact.
	 * <p>
	 * If the given {@link Filter} is not the same type as this {@link Filter}
	 * then a value of true is returned.
	 * <p>
	 * If the given {@link Filter} is null, a value of true is returned.
	 * @param filter the other {@link Filter}
	 * @return boolean
	 */
	@Override
	public boolean isAllowed(Filter filter) {
		// make sure the given filter is not null
		if (filter == null) return true;
		// check the type
		if (filter instanceof GameFilter) {
			// cast the filter
			GameFilter cf = (GameFilter) filter;
			// perform the check
			if(this.noJoints || cf.noJoints)
			{
				return (this.category & cf.mask) > 0 && (cf.category & this.mask) > 0;
			}
			else if(this.v1.equals(cf.v1) || this.v2.equals(cf.v1) || this.v1.equals(cf.v2) || this.v2.equals(cf.v2))
			{
				return false;
			}
			return (this.category & cf.mask) > 0 && (cf.category & this.mask) > 0;
		}
		if (filter instanceof CategoryFilter) {
			CategoryFilter cf = (CategoryFilter) filter;
			return (this.category & cf.getMask()) > 0 && (cf.getCategory() & this.mask) > 0;
		}
		// if its not of right type always return true
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj instanceof GameFilter) {
			GameFilter filter = (GameFilter)obj;
			return filter.category == this.category && filter.mask == this.mask;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + (int)((this.category >>> 32) ^ this.category);
		hash = hash * 31 + (int)((this.mask >>> 32) ^ this.mask);
		return hash;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CategoryFilter[Category=").append(this.category)
		.append("|Mask=").append(this.mask)
		.append("]");
		return sb.toString();
	}
	
	/**
	 * Returns the category bits.
	 * @return long the category bits
	 */
	public long getCategory() {
		return this.category;
	}
	
	/**
	 * Returns the mask bits.
	 * @return long the mask bits
	 */
	public long getMask() {
		return this.mask;
	}
}
