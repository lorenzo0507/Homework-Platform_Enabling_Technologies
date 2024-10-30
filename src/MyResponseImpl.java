import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsExchange;

import java.io.*;
import java.util.Collection;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class MyResponseImpl implements HttpServletResponse {
	private static final int BUFFER_SIZE = 1024;
	HttpExchange exchange;
	OutputStream output;
	PrintWriter writer;
	private boolean responseSent = false;

	public MyResponseImpl(HttpExchange ex) {
		this.exchange = ex;
		this.output = ex.getResponseBody();
		writer = new PrintWriter(new OutputStreamWriter(output));
	}

	/** implementation of ServletResponse */
	public void flushBuffer() throws IOException { }
	public int getBufferSize() {
		return 0;
	}
	public String getCharacterEncoding() {
		return null;
	}
	public Locale getLocale() {
		return null;
	}
	public ServletOutputStream getOutputStream() throws IOException {

		if(!responseSent) {
			responseSent = true;
			exchange.sendResponseHeaders(200, 0);
		}

		return new ServletOutputStream() {
			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setWriteListener(WriteListener writeListener) {

			}

			@Override
			public void write(int b) throws IOException {
				output.write(b);
			}
		};
	}

	public PrintWriter getWriter() throws IOException {
		// autoflush is true, println() will flush,
		// but print() will not.
		return writer;
	}

	public boolean isCommitted() {
		return false;
	}


	public void reset() { }
	public void resetBuffer() { }
	public void setBufferSize(int size) { }
	public void setContentLength(int length) { }
	public void setContentType(String type) { }
	public void setLocale(Locale locale) { }


	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setCharacterEncoding(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setContentLengthLong(long arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addCookie(Cookie cookie) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean containsHeader(String name) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String encodeURL(String url) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String encodeRedirectURL(String url) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String encodeUrl(String url) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String encodeRedirectUrl(String url) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void sendError(int sc, String msg) throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sendError(int sc) throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sendRedirect(String location) throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setDateHeader(String name, long date) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addDateHeader(String name, long date) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setHeader(String name, String value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addHeader(String name, String value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setIntHeader(String name, int value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addIntHeader(String name, int value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setStatus(int sc) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setStatus(int sc, String sm) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getHeader(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Collection<String> getHeaders(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Collection<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}
}
