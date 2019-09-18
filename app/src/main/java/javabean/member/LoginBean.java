package javabean.member;

/**
 * Created by surpr on 2017/12/6.
 */

public class LoginBean {
    // 狀態
    private String status;
    // 帳號
    private String account;
    // 密碼
    private String password;

    public void setStatus(String status) {this.status = status;}

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
