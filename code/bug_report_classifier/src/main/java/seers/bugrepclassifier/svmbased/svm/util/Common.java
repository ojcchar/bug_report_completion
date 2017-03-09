package seers.bugrepclassifier.svmbased.svm.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import seers.bugrepclassifier.svmbased.model.syntaxTree.MyTree;
import seers.bugrepclassifier.svmbased.model.syntaxTree.MyTreeNode;

public class Common {
	
	public static BufferedReader getBr(String filename) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filename))); //, "UTF-8"
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return br;
	}
	
	public static String getLine(String filename) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br;
			br = getBr(filename);
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			br.close();
			return sb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static ArrayList<String> getLines(String filename) {
		ArrayList<String> lines = null;
		try {
			lines = new ArrayList<String>();
			BufferedReader br = getBr(filename);
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lines;
	}
	
	public static void outputLine(String line, String filename) {
		FileWriter fw;
		try {
			fw = new FileWriter(filename);
			fw.write(line);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void outputLines(ArrayList<String> lines, String filename,boolean append) {
		try {
			FileWriter fw;
			fw = new FileWriter(filename,append);
			for (String line : lines) {
				fw.write(line + "\n");
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void outputHashMap(HashMap map, String filename) {
		try {
			FileWriter fw = new FileWriter(filename);
			for (Object str : map.keySet()) {
				Object obj = map.get(str);
				if (obj instanceof int[]) {
					int a[] = (int[]) obj;
					StringBuilder sb = new StringBuilder();
					sb.append(str.toString()).append(" ");
					for (int b : a) {
						sb.append(b).append(" ");
					}
					fw.write(sb.toString().trim() + "\n");
				} else {
					fw.write(str.toString() + " " + map.get(str).toString()
							+ "\n");
				}
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// determine if this is a stop sign punctuation
	public static boolean isStopPun(char c) {
		if (c == '.' || c == '?' || c == '!')
			return true;
		return false;
	}
	
	public static MyTree constructTree(String treeStr) {
		MyTree tree = new MyTree();
		MyTreeNode currentNode = tree.root;
		int leafIdx = 1;
		for (int i = 0; i < treeStr.length(); i++) {
			if (treeStr.charAt(i) == '(') {
				int j = i + 1;
				// try {
				while (treeStr.charAt(j) != ' ' && treeStr.charAt(j) != '\n') {
					j++;
				}
				// } catch(Exception e) {
				// System.out.println(treeStr.substring(i-2,i+2));
				// // System.out.println(treeStr.charAt(j));
				// System.out.println(treeStr);
				// System.exit(1);
				// }
				String value = treeStr.substring(i + 1, j);
				MyTreeNode node = new MyTreeNode(value);
				if (tree.root == null) {
					tree.root = node;
				} else {
					currentNode.addChild(node);
				}
				if (value.startsWith("NP")) {
					node.isNNP = true;
				} else {
					if (node != tree.root) {
						node.isNNP = node.parent.isNNP;
					}
				}
				currentNode = node;
				while (treeStr.charAt(j) == '\n' || treeStr.charAt(j) == ' ') {
					j++;
				}
				if (treeStr.charAt(j) == '(' && treeStr.charAt(j + 1) != ')') {
					i = j - 1;
				} else {
					int m = j;
					while (treeStr.charAt(m) != ')') {
						m++;
					}
					String value2 = treeStr.substring(j, m);
					MyTreeNode node2 = new MyTreeNode(value2);
					node2.leafIdx = leafIdx++;
					currentNode.addChild(node2);
					tree.leaves.add(node2);
					node2.isNNP = node2.parent.isNNP;
					// System.out.println(value2);
					i = m;
					currentNode = currentNode.parent;
				}

			} else if (treeStr.charAt(i) == ')') {
				if (currentNode != tree.root) {
					currentNode = currentNode.parent;
				}
			}
		}
		return tree;
	}
	
	public static void pause(Object message) {
		try {
			System.err.println("Pause: " + message.toString());
			System.err.println("Press g to continue");
			throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ToDO
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			String line = "";
			while (!(line.equalsIgnoreCase("g"))) {
				line = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void bangErrorPOS(String message) {
		try {
			System.err.println("Error: " + message);
			throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static HashMap<String, Integer> readFile2Map(String filename) {
		HashMap<String, Integer> map = null;
		try {
			map = new HashMap<String, Integer>();
			BufferedReader br = getBr(filename);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.isEmpty()) {
					continue;
				}
				int pos = line.lastIndexOf(' ');
				if (pos == -1) {
					pos = line.lastIndexOf('\t');
				}
				String str = line.substring(0, pos);
				int value = Integer.valueOf(line.substring(pos + 1,
						line.length()));
				map.put(str, value);
			}
			br.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	public static class Feature {
		int idx;
		int value;
		int space;

		public Feature(int idx, int value, int space) {
			this.idx = idx;
			this.value = value;
			this.space = space;
			
			if(this.idx>=this.space) {
				bangErrorPOS("feature idx cannot equal or larger than feature space.");
			}
		}
		
		public Feature(int idx, int space) {
			this.idx = idx;
			this.value = 1;
			this.space = space;
			
			if(this.idx>=this.space) {
				bangErrorPOS("feature idx cannot equal or larger than feature space.");
			}
		}
	}
	
	public static String feasToSVMString(ArrayList<Feature> feas) {
		StringBuilder sb = new StringBuilder();
		int offset = 1;
		for (Feature fea : feas) {
			if (fea.value != 0) {
				sb.append(fea.idx + offset).append(":").append(fea.value).append(" ");
			}
			offset += fea.space;
		}
		return sb.toString().trim();
	}
	
	public static HashSet<String> readFile2Set(String filename) {
		HashSet<String> set = null;
		try {
			set = new HashSet<String>();
			BufferedReader br = getBr(filename);
			String line;
			while ((line = br.readLine()) != null) {
				set.add(line.trim());
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set;
	}
}
