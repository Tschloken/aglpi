package com.akaz.aglpi.api;

import java.net.URI;

/**
 * Holds the configuration for GlpiClient configuration
 */
public class GlpiClientConfig {
	private URI		uri;
	
	/**
	 * @param uri The URI, as a String
	 * @return this object, for chaining
	 */
	public GlpiClientConfig setURI(String uri) {
		this.uri = URI.create(uri);
		return this;
	}

	/**
	 * @return The webservice url
	 */
	public URI getWebserviceURI() {
		return this.uri;
	}

}
