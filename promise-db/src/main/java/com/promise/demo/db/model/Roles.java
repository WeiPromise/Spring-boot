package com.promise.demo.db.model;

import com.promise.demo.db.annotations.FieldMeta;
import com.promise.demo.db.annotations.TableMeta;

import java.io.Serializable;

/**
* Created by leiwei on 2019-6-18.
*/
@TableMeta("roles")
public class Roles implements Serializable{

        @FieldMeta(fieldName = "角色ID",  columnName = "roles_id")
        private Integer rolesId;

        @FieldMeta(fieldName = "角色名",  columnName = "roles_name")
        private String rolesName;

        @FieldMeta(fieldName = "角色权限表",  columnName = "status")
        private Integer status;

        public Integer getRolesId() {
            return rolesId;
        }


        public void setRolesId(Integer rolesId) {
            this.rolesId = rolesId;
        }
        public String getRolesName() {
            return rolesName;
        }


        public void setRolesName(String rolesName) {
            this.rolesName = rolesName;
        }
        public Integer getStatus() {
            return status;
        }


        public void setStatus(Integer status) {
            this.status = status;
        }
}
