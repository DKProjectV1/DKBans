package ch.dkrieger.bansystem.lib.filter;

public class Filter {

    private int id;
    private String message;
    private FilterOperation operation;
    private FilterType type;

    public Filter(int id, String message, FilterOperation operation, FilterType type) {
        if(message.startsWith("/")) message = message.substring(1);
        this.id = id;
        this.message = message.toLowerCase();
        this.operation = operation;
        this.type = type;
    }

    public int getID() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public FilterOperation getOperation() {
        return operation;
    }

    public FilterType getType() {
        return type;
    }

    @SuppressWarnings("Only for creatin id changing")
    public void setID(int id) {
        this.id = id;
    }

    public boolean isBocked(String message){
        if(message.startsWith("/")) message = message.substring(1);
        message = message.toLowerCase().replaceFirst("/","");
        if(this.operation == FilterOperation.CONTAINS) return message.replace(" ","").contains(this.message);
        else{
            String[] words = null;
            if(message.contains(" ")) words = message.split(" ");
            else words = new String[]{message};
            if(this.operation == FilterOperation.EQUALS) {
                for(String word : words) if (word.equalsIgnoreCase(this.message)) return true;
            }else if(this.operation == FilterOperation.STARTSWITH){
                for(String word : words) if (word.startsWith(this.message)) return true;
            }else if(this.operation == FilterOperation.ENDSWITH){
                for(String word : words) if (word.endsWith(this.message)) return true;
            }
        }
        return false;
    }
}
