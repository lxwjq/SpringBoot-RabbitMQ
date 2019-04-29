package com.hxy.rabbitmq.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class User implements Serializable {

    private String name;

    private String pass;
}
