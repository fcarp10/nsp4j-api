package services;


public class Service {

    private String id;
    private Function[] functions;

    public Service() {
    }

    public Service(String id, Function[] functions) {
        this.id = id;
        this.functions = functions;
    }

    public String getId() {
        return id;
    }

    public Function[] getFunctions() {
        return functions;
    }
}
