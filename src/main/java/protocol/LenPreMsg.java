package protocol;

public class LenPreMsg {
	//protocol   Header 0x1234a00_ ;
	public static final int BASE_PRE_LEN = 8;
	private int protocol;
	private int datalength;
	private Object body;
	public LenPreMsg(int protocol, int datalength, Object body) {
		super();
		this.protocol = protocol;
		this.datalength = datalength;
		this.body = body;
	}
	public int getProtocol() {
		return protocol;
	}
	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}
	public int getDatalength() {
		return datalength;
	}
	public void setDatalength(int datalength) {
		this.datalength = datalength;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "LenPreMsg [protocol=" + protocol + ", datalength=" + datalength + ", body=" + body + "]";
	}
	
}
