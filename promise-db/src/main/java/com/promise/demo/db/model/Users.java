package com.promise.demo.db.model;

import com.promise.demo.db.annotations.FieldMeta;
import com.promise.demo.db.annotations.TableMeta;

import java.io.Serializable;

/**
* Created by leiwei on 2019-6-18.
*/
@TableMeta("users")
public class Users implements Serializable{

        @FieldMeta(fieldName = "用户ID",  columnName = "user_id")
        private Integer userId;

        @FieldMeta(fieldName = "用户名",  columnName = "user_name")
        private String userName;

        @FieldMeta(fieldName = "用户密码",  columnName = "user_password")
        private String userPassword;

        @FieldMeta(fieldName = "用户邮箱",  columnName = "user_email")
        private String userEmail;

        @FieldMeta(fieldName = "此条数据是否有效",  columnName = "status")
        private Integer status;

        @FieldMeta(fieldName = "用户类型0为",  columnName = "user_type")
        private Integer userType;

        public Integer getUserId() {
            return userId;
        }


        public void setUserId(Integer userId) {
            this.userId = userId;
        }
        public String getUserName() {
            return userName;
        }


        public void setUserName(String userName) {
            this.userName = userName;
        }
        public String getUserPassword() {
            return userPassword;
        }


        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }
        public String getUserEmail() {
            return userEmail;
        }


        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }
        public Integer getStatus() {
            return status;
        }


        public void setStatus(Integer status) {
            this.status = status;
        }
        public Integer getUserType() {
            return userType;
        }


        public void setUserType(Integer userType) {
            this.userType = userType;
        }
}
