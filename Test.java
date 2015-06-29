import java.io.File;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.lang.Iterable;

public class Test {

    /**
     * A node of a tree
     */
    public class Node {
        private NodeAble data;
        private ArrayList<NodeAble> list;

        public Node(NodeAble object) {
            this.data = object;
            this.list = new ArrayList<NodeAble>();
        }

        /**
         * 
         */
        public Node traverseDepth() throws Exception {
            this.list.add(data);
            if (this.data.list() == null) {
                return this;
            }
            for (NodeAble subNode : this.data.list()) {
                Node child = new Node(subNode);
                this.list.addAll(child.traverseDepth().list);
            }
            return this;
        }

        /**
         *
         */
        public Node traverseBreadth() throws Exception {
            this.list.add(data);
            if (this.data.list() == null) {
                return this;
            }
            for (NodeAble subNode : this.data.list()) {
                Node child = new Node(subNode);
                this.list.addAll(child.traverseDepth().list);
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
        public String getSimpleOutput();
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

        /**
         * Just return a String with file path
         * @return String with the file path
         */
        public String getSimpleOutput() {
            return file.getAbsolutePath();
        }
    }

    public static void main(String argc[]) {
        try {
            Test test = new Test();
            FileNodeAble root = test.new FileNodeAble(new File(argc[0]));
            if (argc.length > 1 && argc[1].equalsIgnoreCase("breadth")) {
                Test.Node rootNode = test.new Node(root);
                rootNode.traverseBreadth();
                for (NodeAble nodeAble : rootNode.list) {
                    System.out.println(nodeAble.getSimpleOutput());
                }
                for (NodeAble nodeAble : rootNode.list) {
                    System.out.println(nodeAble.getOutput());
                }
            } else {
                Test.Node rootNode = test.new Node(root);
                rootNode.traverseDepth();
                for (NodeAble nodeAble : rootNode.list) {
                    System.out.println(nodeAble.getSimpleOutput());
                }
                for (NodeAble nodeAble : rootNode.list) {
                    System.out.println(nodeAble.getOutput());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
