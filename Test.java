import java.io.File;
import java.text.SimpleDateFormat;
import java.lang.Iterable;
import java.util.LinkedList;
import java.util.Stack;

public class Test {

    /**
     * A structure that contains a node (ie. File) and a list of its traversed children.
     */
    public class Node {
        // this is a basically the node itself
        private NodeData data;

        // this is a list of the traversed Nodes
        private LinkedList<Node> list;

        /**
         * Constructor that initializes the relative "root" and the traversed list which will be empty.
         * @param  a <NodeData> object
         */
        public Node(NodeData object) {
            this.data = object;
            this.list = new LinkedList<Node>();
        }

        /**
         * This basically calls the "traverse" method with an underlying stack 
         * to keep track of which nodes that are to be traversed. It
         * initializes the stack with the root node.
         */
        public Node traverseDepth() {
            TraversingSet<Node> set = new TraversingSetStack<Node>();
            set.put(this); 
            this.traverse(set);
            return this;
        }

        /**
         * This basically calls the "traverse" method with an underlying queue
         * to keep track of which nodes that are to be traversed.  It 
         * initializes the queue with the root node.
         */
        public Node traverseBreadth() {
            TraversingSet<Node> set = new TraversingSetQueue<Node>();
            set.put(this); 
            this.traverse(set);
            return this;
        }

        /**
         * This method recursive looks at a set of Nodes which are to be traversed
         * and basically adds them to the traversed list as they are processed and adds 
         * any children onto the list of nodes that are to be traversed.
         *
         * @param set This object needs to implement basic stack/queue operations
         */
        private void traverse(TraversingSet<Node> set) {
            if (set.isEmpty()) {
                return;
            }
            Node node = set.get();
            this.list.add(node);
            if (node != null && node.data.getChildren() != null) {
                for (NodeData subNodeData : node.data.getChildren()) {
                    Node subNode = new Node(subNodeData); 
                    set.put(subNode);
                }
            }
            traverse(set);
        }

    }

    /**
     * This interface is basically used by the traversing process to access nodes.
     */
    public interface TraversingSet<T> {
        public void put(T t);
        public T get();
        public boolean isEmpty();
    }

    /**
     * An implementation of TraversingSet that uses a stack as the underlying collection.
     */
    public class TraversingSetStack<T> implements TraversingSet<T> {
        private Stack<T> stack;

        public TraversingSetStack() {
            this.stack = new Stack<T>();
        }

        public void put(T t) {
            this.stack.push(t); 
        }

        public T get() {
            return this.stack.pop();
        }

        public boolean isEmpty() {
            return this.stack.isEmpty();
        }
    }


    /**
     * An implementation of TraversingSet that uses an implementation of Queue as the underlying collection.
     */
    public class TraversingSetQueue<T> implements TraversingSet<T> {
        private LinkedList<T> list;

        public TraversingSetQueue() {
            this.list = new LinkedList<T>();
        }

        public void put(T t) {
            this.list.offer(t); 
        }

        public T get() {
            return this.list.poll();
        }

        public boolean isEmpty() {
            return this.list.isEmpty();
        }
    }

    /**
     * Just an interface to ensure a "list" and "output" functions exist
     * Since we are sort of dynamically setting up a "tree" as we are traversing thru nodes, a 
     * "getChildren" method needs to be implemented.
     */
    public interface NodeData {
        public Iterable<NodeData> getChildren();
        public String getOutput();
        public String getSimpleOutput();
    }

    /**
     * This is a java.io.File implementation of "NodeData".
     */
    public class FileNodeData implements NodeData {
        private File file;
        private int subChildren = 0;

        /**
         * Constructor
         * @param theFile a java.io.File that is the "node"
         * @return an instance of FileNodeData
         */
        public FileNodeData(File theFile) {
            this.file = theFile;
        }
        
        /**
         * A basic function to get the childen of a "node"
         * @return an Iterable list of FileNodeData
         */
        public Iterable<NodeData> getChildren() {
            LinkedList<NodeData> children = new LinkedList<NodeData>();
            File[] subFiles = this.file.listFiles();
            if (subFiles == null) {
                return null;
            }
            this.subChildren = subFiles.length;
            for (File f : subFiles) {
                children.add(new FileNodeData(f));
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
            FileNodeData root = test.new FileNodeData(new File(argc[0]));

            String help = "\n(Size is file count for directories)\n";
            if (argc.length > 1 && argc[1].equalsIgnoreCase("breadth")) {
                System.out.println("\n\nLIST BY BREADTH\n" + help);
                Test.Node rootNode = test.new Node(root);
                // this traverses the Folder by BREADTH
                rootNode.traverseBreadth();

                // spit out folder by breadth
                for (Node node : rootNode.list) {
                    System.out.println(node.data.getSimpleOutput());
                }
                System.out.println("\n\n");
                for (Node node : rootNode.list) {
                    System.out.println(node.data.getOutput());
                }
            } else {
                System.out.println("\n\nLIST BY DEPTH\n" + help);
                Test.Node rootNode = test.new Node(root);
                // this traverses the Folder by DEPTH
                rootNode.traverseDepth();

                // spit out folder by depth
                for (Node node : rootNode.list) {
                    System.out.println(node.data.getSimpleOutput());
                }
                System.out.println("\n\n");
                for (Node node : rootNode.list) {
                    System.out.println(node.data.getOutput());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

