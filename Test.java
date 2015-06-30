import java.io.File;
import java.text.SimpleDateFormat;
import java.lang.Iterable;
import java.util.LinkedList;
import java.util.Queue;

public class Test {

    /**
     * A node of a tree
     */
    public class Node {
        // this is a basically the node itself
        private NodeAble data;

        // this is all the children of the node
        private LinkedList<NodeAble> list;

        /**
         * Constructor that initializes the relative "root" and the children which will be empty.
         * @param  a <NodeAble> object
         */
        public Node(NodeAble object) {
            this.data = object;
            this.list = new LinkedList<NodeAble>();
        }

        /**
         * This recursive function basically starts att the root node and then
         * goes traverses the populates the Nodes via <NodeAble> getChildren.
         */
        public Node traverseDepth() throws Exception {
            this.list.add(data);
            if (this.data.getChildren() == null) {
                return this;
            }
            for (NodeAble subNode : this.data.getChildren()) {
                Node child = new Node(subNode);
                this.list.addAll(child.traverseDepth().list);
            }
            return this;
        }

        /**
         * This sets up a FIFO queue and then invokes the recurive function
         * which populates the child nodes.
         */
        public Node traverseBreadth() throws Exception {
            Queue<Node> queue = new LinkedList<Node>();
            queue.offer(this); 
            this.list.add(this.data);
            this.doBreadthTraversal(queue);
            return this;
        }

        /**
         * This uses a queue 
         */
        private void doBreadthTraversal(Queue<Node> queue) {
            Node node = queue.poll();
            if (node == null || node.data.getChildren() == null) {
                return;
            }
            for (NodeAble subNode : node.data.getChildren()) {
                queue.offer(new Node(subNode));
                this.list.add(subNode);
            }
            doBreadthTraversal(queue);
        }
    }

    /**
     * Just an interface to ensure a "list" and "output" functions
     */
    public interface NodeAble {
        public Iterable<NodeAble> getChildren();
        public String getOutput();
        public String getSimpleOutput();
    }

    /**
     * This is a java.io.File implementation of "NodeAble".
     */
    public class FileNodeAble implements NodeAble {
        private File file;
        private int subChildren = 0;

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
        public Iterable<NodeAble> getChildren() {
            LinkedList<NodeAble> children = new LinkedList<NodeAble>();
            File[] subFiles = this.file.listFiles();
            if (subFiles == null) {
                return null;
            }
            this.subChildren = subFiles.length;
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
            String output = this.file.getAbsolutePath();
            output = output + "\t" + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(this.file.lastModified());
            if (this.file.isDirectory()) {
                output = output + "\t" + this.subChildren;
            } else {
                output = output + "\t" + this.file.length() + " bytes";
            }
            return output;
        }

        /**
         * Just return a String with file path
         * @return String with the file path
         */
        public String getSimpleOutput() {
            return this.file.getAbsolutePath();
        }
    }

    /**
     *  The first argument should be the folder
     *  The second argument should "breadth" for a "breadth" traversal.
     */
    public static void main(String argc[]) {
        try {
            Test test = new Test();
            FileNodeAble root = test.new FileNodeAble(new File(argc[0]));

            String help = "\n(Size is file count for directories)\n";
            if (argc.length > 1 && argc[1].equalsIgnoreCase("breadth")) {
                System.out.println("\n\nLIST BY BREADTH\n" + help);
                Test.Node rootNode = test.new Node(root);
                rootNode.traverseBreadth();

                // spit out folder by breadth
                for (NodeAble nodeAble : rootNode.list) {
                    System.out.println(nodeAble.getSimpleOutput());
                }
                System.out.println("\n\n");
                for (NodeAble nodeAble : rootNode.list) {
                    System.out.println(nodeAble.getOutput());
                }
            } else {
                System.out.println("\n\nLIST BY DEPTH\n" + help);
                Test.Node rootNode = test.new Node(root);
                rootNode.traverseDepth();

                // spit out folder by depth
                for (NodeAble nodeAble : rootNode.list) {
                    System.out.println(nodeAble.getSimpleOutput());
                }
                System.out.println("\n\n");
                for (NodeAble nodeAble : rootNode.list) {
                    System.out.println(nodeAble.getOutput());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
