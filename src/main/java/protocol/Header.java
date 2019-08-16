package protocol;

public class Header {
	public static final int rpc_request = 0x1234a001;
	public static final int rpc_response = 0x1234a002;
	public static final int reg_addservice = 0x1234a003;
	public static final int reg_normalconfig = 0x1234a004;
	public static final int reg_discover = 0x1234a005;
	public static final int reg_tokenconfig = 0x1234a006;
	public static final int heart_beat = 0x1234a007;
}
