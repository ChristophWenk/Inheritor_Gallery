package service.uml;



public class EdgeDTO {
    private String source, target, type;

    public EdgeDTO(String source, String target, String type){
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getType() {
        return type;
    }
}
