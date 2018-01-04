package stickers.exceptions;

public class DatabaseManagerExeption extends RuntimeException {
    private String msg;
    public DatabaseManagerExeption(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage(){
        return this.msg;
    }
}
