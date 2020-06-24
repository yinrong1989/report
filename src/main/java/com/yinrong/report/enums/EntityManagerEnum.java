package com.yinrong.report.enums;


import com.yinrong.report.util.SpringContextUtil;

import javax.persistence.EntityManager;

/**
 * @Classname EntityManagerEnum
 * @Description
 * @Date 2020/4/27 10:25 上午
 * @Created by yinrong
 */
public enum EntityManagerEnum {

    DATA{
        @Override
        public EntityManager getEntityManager() {
            return SpringContextUtil.getBean("nfsp",EntityManager.class);
        }
    },
    REPORT{


        @Override
        public EntityManager getEntityManager() {
            return SpringContextUtil.getBean("monitor",EntityManager.class);
        }
    };


    public abstract EntityManager  getEntityManager();

    public  static EntityManagerEnum getEnum(String name){
        for (EntityManagerEnum managerEnum:EntityManagerEnum.values()){
            if (managerEnum.name().equalsIgnoreCase(name)){
                 return managerEnum;
            }
        }
        return REPORT;
    }


}
