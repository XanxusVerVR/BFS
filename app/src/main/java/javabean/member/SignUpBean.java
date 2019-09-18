package javabean.member;

/**
 * Created by surpr on 2017/12/6.
 */

public class SignUpBean {
    // 狀態
    private String status;
    // 帳號
    private String account;
    // 密碼
    private String password;
    // 性別
    private String gender;
    // 年齡
    private int age;
    // 信箱
    private String email;

    public void setStatus(String status) {this.status = status;}

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(String gender) {this.gender = gender;}

    public void setAge(int age) {this.age = age;}

    public void setEmail(String email) {this.email = email;}
}
