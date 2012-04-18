package blackadderDeploymentUtility.data;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class BANode extends ConfigWritter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2371030810653562886L;

	public enum BANodeOption {
		TESTBED_IP, RUNNING_MODE, LABEL;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	};
	
	public enum BANodeRole {RV,TM;};
	
	private Set<BANodeRole> roles;
	private List<BAConnection> connections;
	private HashMap<BANodeOption, Object> options;
	private String comment;
	
	public BANode() {
		this("#NEW#");
	}
	
	public BANode(String label) {
		options = new HashMap<BANodeOption, Object>();
		roles = new HashSet<BANodeRole>();
		connections = new Vector<BAConnection>();
		
		// setting default values
		setOption(BANodeOption.LABEL, label);
		setOption(BANodeOption.RUNNING_MODE, "user");
		setComment("Node: " + label);
	}
	
	public void setOption(BANodeOption key, String value) {
		if (value == null) {
			value = "";
		}
		System.out.printf("[%s] Set %s = %s\n", getOption(BANodeOption.LABEL), key, value);
		options.put(key, value);
	}
	
	public Object getOption(BANodeOption key) {
		return options.get(key);
	}
	
	public BANode addRole(BANodeRole role) {
		System.out.printf("[%s] Set %s\n", getOption(BANodeOption.LABEL), role);
		if (!roles.contains(role)) {
			roles.add(role);
		}
		return this;
	}
	
	public void removeRole(BANodeRole role) {
		System.out.printf("[%s] Unset %s\n", getOption(BANodeOption.LABEL), role);
		if (roles.contains(role)) {
			roles.remove(role);
		}
	}
	
	public Set<BANodeRole> getRoles() {
		return roles;
	}
	
	public void clearRole() {
		roles.clear();
	}
	
	public void addConnection(BAConnection conn) {
		if (!connections.contains(conn)) {
			System.out.printf("[%s] New connection %s\n", getOption(BANodeOption.LABEL), conn.hashCode());
			connections.add(conn);
		}
	}
	
	public void removeConnection(BAConnection conn) {
		if (connections.contains(conn)) {
			System.out.printf("[%s] Connection removed: %s\n", getOption(BANodeOption.LABEL), conn.hashCode());
			connections.remove(conn);
		}
	}
	
	public List<BAConnection> getConnections() {
		return connections;
	}
	
	public void clearConnection() {
		connections.clear();
	}
	
	public void setComment(String s) {
		comment = s;
	}
	
	public String getComment() {
		return comment;
	}
	
	@Override
	public boolean equals(Object node) {
		if (node == null) {
			return false;
		}
		return options.get(BANodeOption.LABEL).equals(((BANode)node).options.get(BANodeOption.LABEL));
	}
	
	@Override
	public String toString() {
		return options.get(BANodeOption.LABEL).toString();
		/*
		if (roles.size() > 0) {
			str += "[" + getRoleString() + "]";
		}
		return str;
		*/
	}

	@Override
	public void writeConfig(PrintWriter out, int indent) {
		super.writeConfig(out, indent);
		
		// write normal options
		Object obj;
		for (BANodeOption o : BANodeOption.values()) {
			obj = options.get(o);
			if (obj != null) {
				if (obj instanceof String) {
					println(o + " = \"" + obj + "\";");
				} else {
					println(o + " = " + obj + ";");
				}
			}
		}
		
		
		println("role = [" + getRoleString() + "];");
		
		// only write connections if there is at least one connection
		if (connections.size() > 0) { 
			println("connections = (");
			for (int i = 0; i < connections.size(); i++) {
				println("{");
				connections.get(i).writeConfig(out, this.indent+1);
				if (i < connections.size()-1) {
					println("},");
				} else {
					println("}");
				}
			}
			println(");");
		}
	}
	
	
	private String getRoleString() {
		// write roles
		StringBuffer roleStr = new StringBuffer();
		if (roles.size() > 0) {
			Iterator<BANodeRole> iter = roles.iterator();
			while (true) {	// loop end controlled by explicit code below
				roleStr.append("\"" + iter.next() + "\"");
				if (iter.hasNext()) {
					roleStr.append(",");
				} else {
					break;
				}
			}
		}
		return roleStr.toString();
	}
}
