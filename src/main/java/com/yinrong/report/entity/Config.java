package com.yinrong.report.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "report_config")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false, unique = true, length = 20,name = "type")
    private String type;

    @Column(nullable = false, length = 20,name = "key")
    private String key;

    @Column(nullable = false, length = 20,name = "value")
    private String value;
    @Column(nullable = true, length = 20,name = "desc")
    private String desc;



}
