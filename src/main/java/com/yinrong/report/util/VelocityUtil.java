package com.yinrong.report.util;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.ToolManager;

import java.io.StringWriter;
import java.util.Map;

/**
 * @Classname VelocityUtil
 * @Description
 * @Date 2020-04-14 14:16
 * @Created by yinrong
 */
public class VelocityUtil {
    VelocityEngine velocityEngine;

    ToolManager toolManager;
    /**
     * 测试用
     */
    public static String exportFixedVelocityWithToolbox(String content) {
        // 创建引擎
        VelocityEngine ve = new VelocityEngine();
        // 设置模板加载路径，这里设置的是class下
        ve.setProperty(Velocity.RESOURCE_LOADER, "class");
        ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        // 进行初始化操作
        ve.init();
        // 加载模板，设定模板编码

        // 设置初始化数据

        // 加载toolbox
        ToolManager manager = new ToolManager();
        manager.setVelocityEngine(ve);
        manager.configure("/velocity/tools.xml");

        ToolContext context = manager.createContext();
        context.put("name", "张三");

        String[] hobbyArray={"吃饭","喝水","洗澡"};
        context.put("hobby", "爱好");
        context.put("hobbyArray", hobbyArray);

        // 设置输出
        StringWriter writer = new StringWriter();

        // 将环境数据转化输出
        //String content ="$data.get('yyyyMMdd:HHmmss')";
        // String content ="$ramdomUtil.nonRepetNumber()";

        ve.evaluate(context, writer, "", content); // 关键方法

        return writer.toString();

    }
    public String render(String content, Map<String,Object> data){
        ToolContext context = toolManager.createContext();
        context.putAll(data);
        // 设置输出
        StringWriter writer = new StringWriter();
        // 将环境数据转化输出
        velocityEngine.evaluate(context, writer, "", content); // 关键方法
        return writer.toString();
    }


    public VelocityUtil(VelocityEngine velocityEngine, ToolManager toolManager) {
        this.velocityEngine = velocityEngine;
        this.toolManager = toolManager;
    }

    public static void main(String[] args) {
        /*String content ="$name,$esc.java($name)";
        System.out.println(exportFixedVelocityWithToolbox(content));
        System.out.println(exportFixedVelocityWithToolbox(content));
        content ="$name,$esc.javascript($name)";
        System.out.println(exportFixedVelocityWithToolbox(content));
        content ="$name,$esc.javascript('$name')";
        System.out.println(exportFixedVelocityWithToolbox(content));
        content ="$name,'$name'";
        System.out.println(exportFixedVelocityWithToolbox(content));
        content ="$name,$esc.html('$name')";
        System.out.println(exportFixedVelocityWithToolbox(content));
        content ="$name,$esc.html('$name')";
        System.out.println(exportFixedVelocityWithToolbox(content));*/
        String conent = "$Value >0.01";
    }
}
