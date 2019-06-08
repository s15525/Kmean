import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleCSV {
    class Element<T> {
        public Element(T val, String head){
            value = val;
            name = head;
        }
        public String name;
        public T value;

        @Override
        public String toString() {
            return "Element<" + value.getClass().getSimpleName() + ">(" + value + ")";
        }
    }

    private List<List<Element>> result = new ArrayList<>();
    private List<String> header = new ArrayList<>();

    public SimpleCSV(String path, boolean hasHeader) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));

        if(hasHeader) {
            header = List.of(lines.get(0).split("(?!\\B\"[^\"]*),(?![^\"]*\"\\B)"))
                    .stream().map(e -> parseString(e)).collect(Collectors.toList());
            lines.remove(0);
        }

        for(String line : lines) {
            List<Element> record = new ArrayList<>();
            List<String> fields = List.of(line.split("(?!\\B\"[^\"]*),(?![^\"]*\"\\B)"));

            int i = 0;
            for(String field : fields) {
                if(isBoolean(field)) {
                    record.add(new Element<>(Boolean.parseBoolean(field), header.get(i)));
                } else if(isDouble(field)) {
                    record.add(new Element<>(Double.parseDouble(field), header.get(i)));
                } else if(isInteger(field)) {
                    record.add(new Element<>(Integer.parseInt(field), header.get(i)));
                } else {
                    field = parseString(field);

                    record.add(new Element<>(field, header.get(i)));
                }
                i++;
            }

            result.add(record);
        }
    }
    public List<String> getHeader() {
        return header;
    }
    public List<List<Element>> getRecords() {
        return result;
    }

    private String parseString(String field) {
        field = field.replaceAll("(^\")|(\"$)", "");
        field = field.replaceAll("\"\"", "\"");

        return field;
    }
    private boolean isDouble(String d) {
        try {
            Double.parseDouble(d);
            return true;
        } catch (NumberFormatException e){}
        return false;
    }
    private boolean isInteger(String d) {
        try {
            Integer.parseInt(d);
            return true;
        } catch (NumberFormatException e){}
        return false;
    }
    private boolean isBoolean(String d) {
        return d.matches("(true)|(false)|(\"true\")|(\"false\")");
    }
}