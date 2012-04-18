package blackadderDeploymentUtility.data;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;


public class BAConnection extends ConfigWritter implements Serializable {
	//
	private static String overlayMode;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7096918530761489182L;

	public enum BAConnOptionIP {
		SRC_IP, DST_IP;
		
		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	};
	
	public static void setOverlayMode(String mode) {
		overlayMode = mode;
	}
	
	public enum BAConnOptionMAC {
		SRC_IF, DST_IF, SRC_MAC, DST_MAC;
		
		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	};
	
	private HashMap<BAConnOptionIP, Object> ipOptions;
	private HashMap<BAConnOptionMAC, Object> macOptions;
	private BANode to;
	
	public BAConnection() {
		this(null);
	}
	
	public BAConnection(BANode to) {
		ipOptions = new HashMap<BAConnOptionIP, Object>();
		macOptions = new HashMap<BAConnOptionMAC, Object>();
		
		this.to = to;
		
		setOptionMAC(BAConnOptionMAC.SRC_IF, "tap0");
		setOptionMAC(BAConnOptionMAC.DST_IF, "tap0");
	}
	
	public void setOptionIP(BAConnOptionIP key, Object value) {
		if (value == null) {
			value = "";
		}
		System.out.printf("Conn Set %s = %s\n", key, value);
		ipOptions.put(key, value);
	}
	
	public Object getOptionIP(BAConnOptionIP key) {
		return ipOptions.get(key);
	}
	
	public void setOptionMAC(BAConnOptionMAC key, Object value) {
		if (value == null) {
			value = "";
		}
		System.out.printf("Conn Set %s = %s\n", key, value);
		macOptions.put(key, value);
	}
	
	public Object getOptionMAC(BAConnOptionMAC key) {
		return macOptions.get(key);
	}
	
	public void setTo(BANode to) {
		System.out.printf("Conn Set TO = %s\n", to);
		this.to = to;
	}
	
	public BANode getTo() {
		return to;
	}
	
	@Override
	public String toString() {
		if (overlayMode.equals("ip")) {
			return getOptionIP(BAConnOptionIP.SRC_IP) + " -> [" + to + "] "+ getOptionIP(BAConnOptionIP.SRC_IP);
		} else if (overlayMode.equals("mac")) {
			Object srcMac = getOptionMAC(BAConnOptionMAC.SRC_MAC);
			Object dstMac = getOptionMAC(BAConnOptionMAC.DST_MAC);
			return getOptionMAC(BAConnOptionMAC.SRC_IF) 
					+ ((srcMac != null && !((String)srcMac).isEmpty()) ? "(" + srcMac + ")" : "")
					+ " -> "
					+ "[" + to + "] "
					+ getOptionMAC(BAConnOptionMAC.SRC_IF)
					+ ((dstMac != null && !((String)dstMac).isEmpty()) ? "(" + dstMac + ")" : "");
		} else {
			throw new IllegalStateException("UNKNOWN OVERLAY_MODE: \"" + overlayMode + "\", SOMETHING IS WRONG!");
		}
	}

	@Override
	public void writeConfig(PrintWriter out, int indent) {
		super.writeConfig(out, indent);
		println("to = \"" + to + "\";");
		Object obj;
		if (overlayMode.equals("ip")) {
			// write ip config
			for (BAConnOptionIP o : BAConnOptionIP.values()) {
				obj = ipOptions.get(o);
				if (obj != null && !obj.toString().isEmpty()) {
					println(o + " = \"" + obj + "\";");
				}
			}
		} else if (overlayMode.equals("mac")) {
			// write mac config
			for (BAConnOptionMAC o : BAConnOptionMAC.values()) {
				obj = macOptions.get(o);
				if (obj != null && !obj.toString().isEmpty()) {
					println(o + " = \"" + obj + "\";");
				}
			}
		}
	}
	
}
