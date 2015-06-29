import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.text.SimpleDateFormat;
import java.lang.Iterable;

public class Test {

    /**
     */
    public class Node {
        private NodeAble data;
        private LinkedList<Node> list;

        public Node(NodeAble object) {
            this.data = object;
            this.list = new LinkedList<Node>();
        }

        public Node traverseDepth() throws Exception {
            System.out.println(this.data.getOutput());
            if (this.data.list() == null) {
                return this;
            }
            for (NodeAble subNode : this.data.list()) {
                Node child = new Node(subNode);
                child.traverseDepth();        
            }
            return this;
        }
    }

    /**
     * Just an interface to ensure a "list" and "getOutput" function
     */
    public interface NodeAble {
        public Iterable<NodeAble> list();
        public String getOutput();
    }

    /**
     * This is a java.io.File  implementation of "NodeAble".
     */
    public class FileNodeAble implements NodeAble {
        private File file;

        /**
         * Constructor
         * @param theFile a java.io.File that is the "node"
         * @return an instance of FileNodeAble
         */
        public FileNodeAble(File theFile) {
            this.file = theFile;
        }
        
        /**
         * A basic function to get the childen of a "node"
         * @return an Iterable list of FileNodeAble
         */
        public Iterable<NodeAble> list() {
            ArrayList<NodeAble> children = new ArrayList<NodeAble>();
            File[] subFiles = this.file.listFiles();
            if (subFiles == null) {
                return null;
            }
            for (File f : subFiles) {
                children.add(new FileNodeAble(f));
            }
            return children;
        }

        /**
         * Just return a String with file information
         * @return String with the file path,size, and modified date
         */
        public String getOutput() {
            String output = file.getAbsolutePath();
            output = output + "\t" + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(file.lastModified());
            output = output + "\t" + file.length();
            return output;
        }
    }

    public static void main(String argc[]) {
        try {
            Test test = new Test();
            FileNodeAble root = test.new FileNodeAble(new File(argc[0]));
            Test.Node rootNode = test.new Node(root);
            rootNode.traverseDepth();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
