package com.wlc.sightrec.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    private Integer id;
    private String name;
    private String password;
    private String salt;
    private String phone;
    private String email;
}
