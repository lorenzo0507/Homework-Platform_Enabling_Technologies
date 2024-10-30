import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

public class MyRequestImpl implements HttpServletRequest {
	private final InputStream input;
	public HttpExchange exchange;
	private final BufferedReader breader;

	public MyRequestImpl(HttpExchange ex) {
		this.exchange = ex;
		this.input = ex.getRequestBody();
		breader = new BufferedReader(new InputStreamReader(input));
	}



	public Object getAttribute(String attribute) {
		return null;
	}
	public Enumeration<String> getAttributeNames() {
		return null;
	}
	public String getRealPath(String path) {
		return null;
	}
	public RequestDispatcher getRequestDispatcher(String path) {
		return null;
	}
	public boolean isSecure() {
		return false;
	}
	public String getCharacterEncoding() {
		return null;
	}
	public int getContentLength() {
		return 0;
	}
	public String getContentType(){
		return null;
	}
	public ServletInputStream getInputStream() throws IOException {
		return new ServletInputStream() {

			@Override
			public int read() throws IOException {
				return input.read();
			}

			@Override
			public boolean isFinished() {
                try {
                    return input.available() == 0;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

			@Override
			public boolean isReady() {
                try {
                    return input.available() != 0;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

			@Override
			public void setReadListener(ReadListener readListener) {

			}
		};
	}
	public Locale getLocale() {
		return null;
	}
	public Enumeration<Locale> getLocales() {
		return null;
	}
	public String getParameter(String name) {
		return null;
	}
	public Map<String, String[]> getParameterMap() {
		return null;
	}
	public Enumeration<String> getParameterNames() {
		return null;
	}
	public String[] getParameterValues(String parameter) {
		return null;
	}
	public String getProtocol() {
		return null;
	}

	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
	}

	public String getRemoteAddr() {
		return null;
	}
	public String getRemoteHost() {
		return null;
	}
	public String getScheme() {
		return null;
	}
	public String getServerName() {
		return null;
	}
	public int getServerPort() {
		return 0;
	}

	public void removeAttribute(String attribute) { }
	public void setAttribute(String key, Object value) { }
	public void setCharacterEncoding(String encoding)
	throws UnsupportedEncodingException { }
	@Override
	public long getContentLengthLong() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getRemotePort() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getLocalAddr() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getLocalPort() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
			throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isAsyncStarted() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isAsyncSupported() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public AsyncContext getAsyncContext() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public DispatcherType getDispatcherType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getAuthType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Cookie[] getCookies() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getDateHeader(String name) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getHeader(String name) {
		return exchange.getRequestHeaders().getFirst(name);
	}
	@Override
	public Enumeration<String> getHeaders(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Enumeration<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getIntHeader(String name) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getMethod() {
        return exchange.getRequestMethod();
	}
	@Override
	public String getPathInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getContextPath() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getQueryString() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isUserInRole(String role) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getRequestURI() {
		return exchange.getRequestURI().toString();
	}
	@Override
	public StringBuffer getRequestURL() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getServletPath() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public HttpSession getSession(boolean create) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String changeSessionId() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isRequestedSessionIdFromUrl() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void login(String username, String password) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void logout() throws ServletException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Part getPart(String name) throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <T extends HttpUpgradeHandler> T upgrade(Class<T> httpUpgradeHandlerClass)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}
}
