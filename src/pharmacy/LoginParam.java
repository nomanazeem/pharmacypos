package pharmacy;

public class LoginParam{
    private boolean _success;
    private Integer _userId;
    private String _userName;
    private String _role;

    public LoginParam(boolean success, Integer userId, String userName, String role)
    {
        this._success=success;
        this._userId=userId;
        this._userName=userName;
        this._role = role;
    }
    public boolean getSuccess(){
        return this._success;
    }
    public Integer getUserId(){
        return this._userId;
    }
    public String getUserName(){
        return this._userName;
    }
    public String getRole(){
        return this._role;
    }
}
