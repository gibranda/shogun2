package de.terrestris.shogun2.util.http;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.terrestris.shogun2.util.model.Response;

/**
 *
 * @author danielkoch
 *
 */
@SuppressWarnings("static-method")
public class HttpUtilTest {

	/**
	 *
	 */
	private static final String TEST_SERVER_SCHEME = "http";

	/**
	 *
	 */
	private static final String TEST_SERVER_HOST = "127.0.0.1";

	/**
	 *
	 */
	private static final Integer TEST_SERVER_PORT = 1234;

	/**
	 *
	 */
	private static final String TEST_SERVER_INFO = "Test/1.1";

	/**
	 *
	 */
	private static final Credentials CREDENTIALS = new UsernamePasswordCredentials("Shinji", "Kagawa");

	/**
	 *
	 */
	private static final List<NameValuePair> POST_KEY_VALUE_PAIRS = new ArrayList<NameValuePair>(
		Arrays.asList(
			new BasicNameValuePair("key1", "value1"),
			new BasicNameValuePair("key2", "value2"),
			new BasicNameValuePair("key3", "value3")
		)
	);

	/**
	 *
	 */
	private static final String POST_XML_BODY =
			"<root>" +
				"<element1>value1</element1>" +
				"<element2>value2</element2>" +
				"<element3>value3</element3>" +
			"</root>";

	/**
	 *
	 */
	private static final ContentType POST_XML_BODY_CONTENT_TYPE = ContentType
			.parse("application/xml");

	/**
	 *
	 */
	private static URI URI;

	/**
	 *
	 */
	private static String URL;

	/**
	 *
	 */
	protected static HttpServer server;

	/**
	 *
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {

		// simple implementation of the HttpRequestHandler
		class TestRequestHandler implements HttpRequestHandler {
			@Override
			public void handle(HttpRequest request, HttpResponse response,
					HttpContext context) throws HttpException, IOException {
				response.setEntity(new StringEntity("SHOGun2 rocks!", "UTF-8"));
			}
		};

		TestRequestHandler handler = new TestRequestHandler();

		HttpUtilTest.server = ServerBootstrap.bootstrap()
				.setLocalAddress(InetAddress.getByName(TEST_SERVER_HOST))
				.setListenerPort(TEST_SERVER_PORT)
				.setServerInfo(TEST_SERVER_INFO)
				.registerHandler("/", handler)
				.create();

		HttpUtilTest.server.start();

		URIBuilder builder = new URIBuilder();
		builder.setScheme(TEST_SERVER_SCHEME);
		builder.setHost(TEST_SERVER_HOST);
		builder.setPort(TEST_SERVER_PORT);

		HttpUtilTest.URI = builder.build();
		HttpUtilTest.URL = HttpUtilTest.URI.toString();

	}

	/**
	 *
	 */
	@AfterClass
	public static void shutDown() {
		if (HttpUtilTest.server != null) {
			HttpUtilTest.server.shutdown(10, TimeUnit.SECONDS);
		}
	}

	@Test
	public void get_url_timeout() {

	}

	@Test
	public void get_url() throws URISyntaxException, HttpException {
		Response response = HttpUtil.get(URL);
		assertNotNull(response);
	}

	@Test
	public void get_uri() throws URISyntaxException, HttpException {
		Response response = HttpUtil.get(URI);
		assertNotNull(response);
	}

	@Test
	public void get_url_auth() throws URISyntaxException, HttpException {
		Response response = HttpUtil.get(URL, CREDENTIALS);
		assertNotNull(response);
	}

	@Test
	public void get_uri_auth() throws URISyntaxException, HttpException {
		Response response = HttpUtil.get(URI, CREDENTIALS);
		assertNotNull(response);
	}

	@Test
	public void post_url_empty() throws URISyntaxException, UnsupportedEncodingException, HttpException {
		Response response = HttpUtil.post(URL);
		assertNotNull(response);
	}

	@Test
	public void post_uri_empty() throws URISyntaxException, UnsupportedEncodingException, HttpException {
		Response response = HttpUtil.post(URI);
		assertNotNull(response);
	}

	@Test
	public void post_url_empty_auth() throws URISyntaxException, UnsupportedEncodingException, HttpException {
		Response response = HttpUtil.post(URL, CREDENTIALS);
		assertNotNull(response);
	}

	@Test
	public void post_uri_empty_auth() throws URISyntaxException, UnsupportedEncodingException, HttpException {
		Response response = HttpUtil.post(URI, CREDENTIALS);
		assertNotNull(response);
	}

	@Test
	public void post_url_kvp() throws URISyntaxException, UnsupportedEncodingException, HttpException {
		Response response = HttpUtil.post(URL, POST_KEY_VALUE_PAIRS);
		assertNotNull(response);
	}

	@Test
	public void post_url_kvp_auth() throws URISyntaxException, UnsupportedEncodingException, HttpException {
		Response response = HttpUtil.post(URL, POST_KEY_VALUE_PAIRS, CREDENTIALS);
		assertNotNull(response);
	}

	@Test
	public void post_uri_kvp() throws URISyntaxException, UnsupportedEncodingException, HttpException {
		Response response = HttpUtil.post(URI, POST_KEY_VALUE_PAIRS);
		assertNotNull(response);
	}

	@Test
	public void post_uri_kvp_auth() throws URISyntaxException, UnsupportedEncodingException, HttpException {
		Response response = HttpUtil.post(URI, POST_KEY_VALUE_PAIRS, CREDENTIALS);
		assertNotNull(response);
	}

	@Test
	public void post_url_body() throws URISyntaxException, UnsupportedEncodingException, HttpException {
		Response response = HttpUtil.post(URL, POST_XML_BODY, POST_XML_BODY_CONTENT_TYPE);
		assertNotNull(response);
	}

	@Test
	public void post_url_body_auth() throws URISyntaxException, UnsupportedEncodingException, HttpException {
		Response response = HttpUtil.post(URL, POST_XML_BODY, POST_XML_BODY_CONTENT_TYPE, CREDENTIALS);
		assertNotNull(response);
	}

	@Test
	public void post_uri_body() throws URISyntaxException, UnsupportedEncodingException, HttpException {
		Response response = HttpUtil.post(URI, POST_XML_BODY, POST_XML_BODY_CONTENT_TYPE);
		assertNotNull(response);
	}

	@Test
	public void post_uri_body_auth() throws URISyntaxException, UnsupportedEncodingException, HttpException {
		Response response = HttpUtil.post(URI, POST_XML_BODY, POST_XML_BODY_CONTENT_TYPE, CREDENTIALS);
		assertNotNull(response);
	}

	@Test
	public void post_url_multipart() throws URISyntaxException, HttpException {
		Response response = HttpUtil.post(URL, getTestFile());
		assertNotNull(response);
	}

	@Test
	public void post_url_multipart_auth() throws URISyntaxException, HttpException {
		Response response = HttpUtil.post(URL, getTestFile(), CREDENTIALS);
		assertNotNull(response);
	}

	@Test
	public void post_uri_multipart() throws URISyntaxException, HttpException {
		Response response = HttpUtil.post(URI, getTestFile());
		assertNotNull(response);
	}

	@Test
	public void post_uri_multipart_auth() throws URISyntaxException, HttpException {
		Response response = HttpUtil.post(URI, getTestFile(), CREDENTIALS);
		assertNotNull(response);
	}

	@Test
	public void put_uri_empty() throws URISyntaxException, HttpException{
		Response response = HttpUtil.put(URI);
		assertNotNull(response);
	}

	@Test
	public void put_url_empty() throws URISyntaxException, HttpException{
		Response response = HttpUtil.put(URL);
		assertNotNull(response);
	}

	@Test
	public void put_uri_empty_auth() throws URISyntaxException, HttpException{
		Response response = HttpUtil.put(URI, CREDENTIALS);
		assertNotNull(response);
	}

	@Test
	public void put_url_empty_auth() throws URISyntaxException, HttpException{
		Response response = HttpUtil.put(URL, CREDENTIALS);
		assertNotNull(response);
	}

	@Test
	public void put_uri_body() throws URISyntaxException, HttpException{
		Response response = HttpUtil.put(URI, POST_XML_BODY, POST_XML_BODY_CONTENT_TYPE);
		assertNotNull(response);
	}

	@Test
	public void put_uri_body_auth() throws URISyntaxException, HttpException{
		Response response = HttpUtil.put(URI, POST_XML_BODY, POST_XML_BODY_CONTENT_TYPE, CREDENTIALS);
		assertNotNull(response);
	}

	@Test
	public void put_url_body() throws URISyntaxException, HttpException{
		Response response = HttpUtil.put(URL, POST_XML_BODY, POST_XML_BODY_CONTENT_TYPE);
		assertNotNull(response);
	}

	@Test
	public void put_url_body_auth() throws URISyntaxException, HttpException{
		Response response = HttpUtil.put(URL, POST_XML_BODY, POST_XML_BODY_CONTENT_TYPE, CREDENTIALS);
		assertNotNull(response);
	}

	private File getTestFile() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(
				"META-INF/logo.png").getFile());
		return file;
	}

}
