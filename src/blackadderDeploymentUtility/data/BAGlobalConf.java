package blackadderDeploymentUtility.data;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;

import blackadderDeploymentUtility.data.BANode.BANodeOption;
import blackadderDeploymentUtility.data.BANode.BANodeRole;

public class BAGlobalConf extends ConfigWritter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7261538614689622795L;

	public enum BAGlobalConfOption {
		BLACKADDER_ID_LENGTH, 
		LIPSIN_ID_LENGTH,
		CLICK_HOME,
		WRITE_CONF,
		USER,
		SUDO,
		OVERLAY_MODE;
	}
	private HashMap<BAGlobalConfOption, Object> options = new HashMap<BAGlobalConfOption, Object>();
	private BANetwork baNet;
	
	public BAGlobalConf() {
		// default values here
		setOption(BAGlobalConfOption.BLACKADDER_ID_LENGTH, 8);
		setOption(BAGlobalConfOption.LIPSIN_ID_LENGTH, 8);
		setOption(BAGlobalConfOption.CLICK_HOME, "/usr/local/");
		setOption(BAGlobalConfOption.WRITE_CONF, "/tmp/");
		setOption(BAGlobalConfOption.USER, "pursuit");
		setOption(BAGlobalConfOption.SUDO, true);
		setOption(BAGlobalConfOption.OVERLAY_MODE, "mac");
		baNet = new BANetwork();
	}
	
	public void setOption(BAGlobalConfOption key, Object value) {
		System.out.println("Set " + key + " = " + value);
		options.put(key, value);
		if (key == BAGlobalConfOption.OVERLAY_MODE) {
			BAConnection.setOverlayMode(value.toString());
		}
	}
	
	public Object getOption(BAGlobalConfOption key) {
		return options.get(key);
	}

	@Override
	public void writeConfig(PrintWriter out, int indent) {
		super.writeConfig(out, indent);
		Object obj;
		
		for (BAGlobalConfOption o : BAGlobalConfOption.values()) {
			obj = options.get(o);
			if (obj != null) {
				if (obj instanceof String) {
					println(o + " = \"" + obj + "\";");
				} else {
					println(o + " = " + obj + ";");
				}
			}
		}
		
		println("");
		baNet.writeConfig(out, indent);
	}
	
	
	
	public static void main(String[] args) {
		BAGlobalConf conf = new BAGlobalConf();
		BANetwork net = new BANetwork();
		BANode n1 = new BANode("00000001");
		n1.addRole(BANodeRole.TM);
		net.addNode(n1);
		
		BANode n2 = new BANode("00000002");
		n2.addRole(BANodeRole.TM).addRole(BANodeRole.RV);
		net.addNode(n2);
		
		BANode n3 = new BANode("00000003");
		net.addNode(n3);
		
		BAConnection conn1 = new BAConnection(n2);
		n1.addConnection(conn1);
		
		n1.setOption(BANodeOption.TESTBED_IP, "10.9.0.1");
		n1.setOption(BANodeOption.RUNNING_MODE, "user");
		
		try {
			PrintWriter out = new PrintWriter("test1.conf");
			conf.writeConfig(out, 0);
			out.flush();
			out.close();
			System.out.println("Completed, file test1.conf generated");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public BANetwork getBaNet() {
		return baNet;
	}

	public void setBaNet(BANetwork baNet) {
		this.baNet = baNet;
	}
}
