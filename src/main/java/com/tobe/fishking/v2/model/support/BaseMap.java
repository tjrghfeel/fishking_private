package com.tobe.fishking.v2.model.support;

import java.util.HashMap;


public class BaseMap extends HashMap<Object, Object> {

	private static final long serialVersionUID = 1L;
	private String key;

	public BaseMap() {
		super();
	}

	public void removeByKeys( String... keys ) {

		if ( keys != null && keys.length > 0 ) {
			for ( String key : keys ) {
				this.remove( key );
			}
		}

	}

	public void renameKey( String oldKey, String newKey ) {
		Object value = this.remove( oldKey );
		this.put( newKey, value );
	}

	@Override
	public Object put(Object key, Object value) {

		if ( value instanceof String ){

			if ( "".equals(value)){
				return super.put(key, null);
			}
		}else if( value instanceof Boolean ){
			return super.put(key, value);
		}


		return super.put(key, value);
	}

	public String getString( String key ) {
		return this.get( key ) == null ? null : this.get( key ).toString();
	}

	public int getInt( String key ){

		int rtn = 0 ;

		try {

			rtn = Integer.parseInt(this.getString(key));

		}catch(Exception e){}

		return rtn;
	}

	public Integer getInteger( String key ) {

		Integer rtn = null ;

		try {

			rtn = Integer.parseInt(this.getString(key));

		}catch(Exception e){}

		return rtn;
	}

}