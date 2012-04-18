package blackadderDeploymentUtility.data;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

public class BANetwork extends ConfigWritter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7418347206168694640L;
	private List<BANode> nodeList;
	
	public BANetwork() {
		nodeList = new Vector<BANode>();
	}
	
	public BANetwork addNode(BANode node) {
		nodeList.add(node);
		return this;
	}
	
	public void removeNode(BANode node) {
		nodeList.remove(node);
	}
	
	public List<BANode> getNodeList() {
		return nodeList;
	}
	
	public void clearNode() {
		nodeList.clear();
	}
	
	public int size() {
		return nodeList.size();
	}

	@Override
	public void writeConfig(PrintWriter out, int indent) {
		super.writeConfig(out, indent);
		println("network = {");
		this.indent++;
		println("nodes = (");
		for (int i = 0; i < nodeList.size(); i++) {
			println("{");
			
			nodeList.get(i).writeConfig(out, this.indent+1);
			
			if (i < nodeList.size() - 1) {
				println("},");
			} else {
				println("}");
			}
		}
		println(");");
		this.indent--;
		println("};");
	}
}
