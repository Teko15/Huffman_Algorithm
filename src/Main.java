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
        List<Vertex> vertexes = new ArrayList<>();
        lines.forEach(e -> vertexes.add(createData(e)));
        vertexes.sort(Comparator.comparingInt(Vertex::getFrequency));
        code(vertexes.get(0), vertexes.get(1), vertexes);
        allVertexes.sort(Comparator.comparingInt(Vertex::getFrequency).reversed());

        for (int i = 3; i < allVertexes.size(); i++) {
            Vertex v = null;
            for (Vertex vertex : allVertexes) {
                if (vertex.getCharacters().contains(allVertexes.get(i).getCharacters()) &&
                        !vertex.getCharacters().equals(allVertexes.get(i).getCharacters()))
                    v = vertex;
            }
            Stack<Byte> tmpStack = new Stack<>();
            tmpStack.addAll(allVertexes.get(i).getLr());
            if (v != null)
                tmpStack.addAll(v.getLr());
            allVertexes.get(i).setLr(tmpStack);
        }

        for (int i = 1; i < allVertexes.size(); i++)
            allVertexes.get(i).setLr(reverseStack(allVertexes.get(i).getLr()));
        //allVertexes.forEach(e -> System.out.println(e.getCharacters() + ", " + e.getFrequency() + ", " + e.getLr()));
        allVertexes.stream()
                .filter(e -> e.getCharacters().length() == 1)
                .forEach(e -> System.out.println(e.getCharacters() + ", " + e.getFrequency() + ", " + e.getLr()));


    }

    private static Stack<Byte> reverseStack(Stack<Byte> stack) {
        Stack<Byte> reversedStack = new Stack<>();
        while (stack.size() != 0)
            reversedStack.push(stack.pop());
        return reversedStack;
    }

    private static void code(Vertex vertex1, Vertex vertex2, List<Vertex> vertexes) {
        Stack<Byte> firstPath = new Stack<>();
        firstPath.push((byte) 0);
        vertex1.setLr(firstPath);

        Stack<Byte> secondPath = new Stack<>();
        secondPath.push((byte) 1);
        vertex2.setLr(secondPath);

        Vertex newVertex = new Vertex((vertex1.getCharacters() + vertex2.getCharacters()), vertex1.getFrequency() + vertex2.getFrequency());
        vertexes.add(newVertex);
        allVertexes.add(vertex1);
        allVertexes.add(vertex2);
        vertexes.remove(vertex1);
        vertexes.remove(vertex2);
        vertexes.sort(Comparator.comparingInt(Vertex::getFrequency));
        if (vertexes.size() > 1)
            code(vertexes.get(0), vertexes.get(1), vertexes);
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
    private final String characters;
    private final int frequency;
    //0 = left, 1 = right
    private Stack<Byte> lr;

    public Vertex(String characters, int frequency) {
        this.characters = characters;
        this.frequency = frequency;
    }

    public String getCharacters() {
        return characters;
    }

    public int getFrequency() {
        return frequency;
    }

    public Stack<Byte> getLr() {
        return lr;
    }

    public void setLr(Stack<Byte> lr) {
        this.lr = lr;
    }
}
