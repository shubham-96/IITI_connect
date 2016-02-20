package dto;

/**
 * Created by root on 20/2/16.
 */
public class User {
    private int reg_id;
    private String first_name;
    private String last_name;
    private String password;
    private String username;
    private byte[] pic;
    public void setReg_id(int id){
        reg_id = id;
    }
    public void setFirst_name(String fn){
        first_name= fn;
    }
    public void setLast_name(String ln){
        last_name= ln;
    }
    public void setUsername(String un){
        username= un;
    }
    public void setPassword(String pw){
        password = pw;
    }
    public void setPic(byte[] arr){

    }

    public byte[] getPic() {
        return pic;
    }

    public int getReg_id() {
        return reg_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
