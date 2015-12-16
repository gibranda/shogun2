package de.terrestris.shogun2.util.application;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Daniel Koch
 *
 */
@Component
public class Shogun2ServletContext {

	/**
	 * Returns the full webapplication URI from a given request.
	 *
	 * Example:
	 *
	 * The following GET-request:
	 *      http://localhost:8080/mapmavin/user/resetPassword.action
	 * will result in
	 *      http://localhost:8080/mapmavin/
	 *
	 * @param request
	 * @return
	 * @throws URISyntaxException
	 */
	public URI getApplicationURIFromRequest(HttpServletRequest request)
			throws URISyntaxException {

		URI appURI = null;

		String scheme = request.getScheme();
		String host = request.getServerName();
		int port = request.getServerPort();
		String path = request.getContextPath();

		appURI = new URIBuilder()
				.setScheme(scheme)
				.setHost(host)
				.setPort(port)
				.setPath(path)
				.build();

		return appURI;
	}

}
