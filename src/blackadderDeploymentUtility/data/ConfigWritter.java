package blackadderDeploymentUtility.data;

import java.io.PrintWriter;

public abstract class ConfigWritter {
	public void writeConfig(PrintWriter out, int indent) {
		this.out = out;
		this.indent = indent;
	}
	
	protected PrintWriter out;
	protected int indent;
	protected void print(String msg) {
		for (int i = 0; i < indent; i++) {
			out.print('\t');
		}
		out.print(msg);
	}
	protected void println(String msg) {
		print(msg + "\n");
	}
}
