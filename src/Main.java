import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {

    public static List<Vertex> allVertexes = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid number of arguments!");
            System.exit(-args.length);
        }
        List<String> lines = readFromFile(args[0]);
        List<Vertex> nodes = new ArrayList<>();
        lines.forEach(e -> nodes.add(createData(e)));
        nodes.sort(Comparator.comparingInt(Vertex::getFrequency));

        coding(nodes.get(0), nodes.get(1), nodes);
        allVertexes.sort(Comparator.comparingInt(Vertex::getFrequency).reversed());

        for (int i = 3; i < allVertexes.size(); i++) {
            Vertex tmpVertex = new Vertex();
            for (Vertex vertex : allVertexes) {
                if (vertex.getCharacters().contains(allVertexes.get(i).getCharacters()) &&
                        !vertex.getCharacters().equals(allVertexes.get(i).getCharacters()))
                    tmpVertex = vertex;
            }

            Stack<Byte> tmpStack = new Stack<>();
            tmpStack.addAll(allVertexes.get(i).getPath());
            tmpStack.addAll(tmpVertex.getPath());
            allVertexes.get(i).setPath(tmpStack);
        }

        for (int i = 1; i < allVertexes.size(); i++)
            allVertexes.get(i).setPath(reverseStack(allVertexes.get(i).getPath()));
        //allVertexes.forEach(e -> System.out.println(e.getCharacters() + e.getPath() + ", number of occurrences: " + e.getFrequency()));
        allVertexes.stream()
                .filter(e -> e.getCharacters().length() == 1)
                .forEach(System.out::println);
    }

    private static Stack<Byte> reverseStack(Stack<Byte> stack) {
        Stack<Byte> reversedStack = new Stack<>();
        while (stack.size() != 0)
            reversedStack.push(stack.pop());
        return reversedStack;
    }

    private static void coding(Vertex vertex1, Vertex vertex2, List<Vertex> vertexes) {
        Stack<Byte> firstPath = new Stack<>();
        firstPath.push((byte) 0);
        vertex1.setPath(firstPath);

        Stack<Byte> secondPath = new Stack<>();
        secondPath.push((byte) 1);
        vertex2.setPath(secondPath);

        Vertex newVertex = new Vertex(
                (vertex1.getCharacters() + vertex2.getCharacters()),
                vertex1.getFrequency() + vertex2.getFrequency()
        );

        vertexes.add(newVertex);
        allVertexes.add(vertex1);
        allVertexes.add(vertex2);
        vertexes.remove(vertex1);
        vertexes.remove(vertex2);

        vertexes.sort(Comparator.comparingInt(Vertex::getFrequency));
        if (vertexes.size() > 1)
            coding(vertexes.get(0), vertexes.get(1), vertexes);
        else
            allVertexes.add(newVertex);
    }

    private static List<String> readFromFile(String path) {
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                lines.add(line);
        } catch (java.io.IOException ignored) {
            System.out.println("File not found!");
            System.exit(-538);
        }
        return lines;
    }

    private static Vertex createData(String line) {
        return new Vertex(line.split(" ")[0], Integer.parseInt(line.split(" ")[1]));
    }
}

class Vertex {
    //0 = left, 1 = right
    private Stack<Byte> path;
    private String characters;
    private int frequency;

    public Vertex(String characters, int frequency) {
        this.characters = characters;
        this.frequency = frequency;
    }

    public Vertex() {
    }

    public String getCharacters() {
        return characters;
    }

    public int getFrequency() {
        return frequency;
    }

    public Stack<Byte> getPath() {
        return path;
    }

    private String printPath() {
        StringBuilder returner = new StringBuilder();
        for (Byte aByte : path)
            returner.append(aByte);
        return returner.toString();
    }

    public void setPath(Stack<Byte> path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return characters + printPath() + ", number of occurrences: " + frequency;
    }
}
