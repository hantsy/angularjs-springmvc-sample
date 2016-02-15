
package com.hantsylabs.restexample.springmvc;

/**
 *
 * @author Hantsy Bai<hantsy@gmail.com>
 */
public final class ApiErrors {
	
    private static final String PREFIX = "errors.";

    public static final String INVALID_REQUEST = PREFIX + "INVALID_REQUEST";
    
    private ApiErrors() {
        throw new InstantiationError( "Must not instantiate this class" );
    }
}
