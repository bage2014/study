package com.bage;

// 示例对象类
class User {
    private String name;
    private Integer age;
    private String email;
    private String phone;

    // 构造方法
    public User() {
    }

    public User(String name, Integer age, String email, String phone) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    // getter 和 setter 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return String.format("User{name='%s', age=%d, email='%s', phone='%s'}",
                name, age, email, phone);
    }
}
