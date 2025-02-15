package edu.sjsu.cmpe275.lab2.service;

/**
 * This is Success Response.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */
public class Success {

    String code;
    String message;
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Success(String code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public Success()
    {
        
    }
    

}
