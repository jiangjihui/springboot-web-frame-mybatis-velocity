package com.jjh.business.common.gen.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.jjh.business.common.gen.controller.form.GenEntityForm;
import com.jjh.business.common.gen.controller.form.GenEntitySqlForm;
import com.jjh.business.common.gen.controller.form.GenFileForm;
import com.jjh.business.common.gen.controller.form.GenTargetPathForm;
import com.jjh.business.common.gen.service.GenService;
import com.jjh.common.exception.BusinessException;
import com.jjh.framework.plugin.velocity.VelocityInitializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 代码生成 服务层处理
 *
 */
@Service
public class GenServiceImpl implements GenService {

    private static final Logger logger = LoggerFactory.getLogger(GenServiceImpl.class);

    /**
     * 生成实体相关代码
     *
     * @param dto 实体类信息
     * @return 数据
     */
    @Override
    public void generatorCodeForEntity(GenEntityForm dto) {
        String moduleName = dto.getModuleName();
        String ClassName = dto.getClassName();
        String comment = dto.getComment();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        VelocityInitializer.initVelocity();

        // java对象数据传递到模板文件vm
        VelocityContext velocityContext = new VelocityContext();
        String packageName = dto.getPackageName();
        String className = StringUtils.uncapitalize(ClassName);
        String classname = StrUtil.toUnderlineCase(ClassName);
        velocityContext.put("tableComment", comment);
        velocityContext.put("ClassName", ClassName);
        velocityContext.put("classEntity", ClassName);
        velocityContext.put("className", className);
        velocityContext.put("classname", classname);
        velocityContext.put("moduleName", moduleName);
        velocityContext.put("package", packageName);
        velocityContext.put("author", dto.getAuthor());
        velocityContext.put("datetime", DateUtil.formatDate(new Date()).replace("-","/"));
        velocityContext.put("importAndExport", dto.getImportAndExport());

        // 生成压缩文件
        velocityApply(velocityContext, dto);

    }

    /**
     * 生成指定目录下的实体相关代码
     * @param form
     */
    @Override
    public void genCodeFromTargetPath(GenTargetPathForm form) {
        String packagePath = form.getPackagePath();
        String author = form.getAuthor();
        Boolean importAndExport = form.getImportAndExport() == null ? false : form.getImportAndExport();
        if (StrUtil.isBlank(packagePath)) {
            throw new BusinessException("目录不能为空");
        }
        // 获取各个类
        File[] modelFileArray = new File(packagePath).listFiles();
        String prefix = "src\\main\\java\\";
        String packageName = packagePath.substring(packagePath.lastIndexOf(prefix) + prefix.length(), packagePath.length()).replace("\\",".");
        // 包含项集合
        Vector<String> includeEntityCollection = new Vector<>();
        if (StrUtil.isNotBlank(form.getIncludeEntity())) {
            includeEntityCollection.addAll(Arrays.asList(form.getIncludeEntity().split(",")));
        }
        // 排除项集合
        Vector<String> excludeEntityCollection = new Vector<>();
        if (StrUtil.isNotBlank(form.getExcludeEntity())) {
            excludeEntityCollection.addAll(Arrays.asList(form.getExcludeEntity().split(",")));
        }
        try {
            for (File file : modelFileArray) {
                String className = FileUtil.mainName(file.getName());
                // 检查包含项
                if (CollectionUtil.isNotEmpty(includeEntityCollection) && !includeEntityCollection.contains(className)) {
                    continue;
                }
                // 忽略排除项
                if (excludeEntityCollection.contains(className)) {
                    continue;
                }
                String classPackageName = packageName + "." + className;
                Class<?> clazz = Class.forName(classPackageName);
                // 获取对应的注解（方便获取注解内的值）
                ApiModel apiModel = clazz.getAnnotation(ApiModel.class);

                GenEntityForm dto = new GenEntityForm();
                String packageParentName = packageName.substring(0, packageName.lastIndexOf("."));
                String moduleName = StrUtil.subAfter(packageName, "business.", false).replace(".", "/");
                moduleName = moduleName.substring(0, moduleName.lastIndexOf("/"));
                dto.setAuthor(author);
                dto.setModuleName(moduleName);
                dto.setClassName(className);
                dto.setPackageName(packageParentName);
                dto.setComment(apiModel.value());
                dto.setTargetPath(packagePath);
                dto.setImportAndExport(importAndExport);
                dto.setTargetFile(form.getTargetFile());

                logger.info(dto.toString());

                this.generatorCodeForEntity(dto);
            }
        } catch (Exception e) {
            throw new BusinessException("反射获取类信息异常", e);
        }
    }

    /**
     * 生成指定文件
     * @param form
     */
    @Override
    public void genFile(GenFileForm form) {

        String templateFilePath = form.getTemplateFilePath();
        String targetFilePath = form.getTargetFilePath();

        VelocityInitializer.initVelocity();
        VelocityContext velocityContext = new VelocityContext();
        // 将参数传入模板
        for (Map.Entry<String, Object> entry : form.getParams().entrySet()) {
            velocityContext.put(entry.getKey(), entry.getValue());
        }

        // 渲染模板
        StringWriter sw = new StringWriter();

        Template tpl = Velocity.getTemplate(templateFilePath, "UTF-8");
        tpl.merge(velocityContext, sw);

        // 输出文件
        if (new File(targetFilePath).exists()) {
            throw new BusinessException("目标文件已存在");
        }
        logger.info("gen file: {}", targetFilePath);

        FileWriter fileWriter = null;
        try {
            FileUtil.mkParentDirs(targetFilePath);
            fileWriter = new FileWriter(targetFilePath);
            fileWriter.append(sw.toString());
            fileWriter.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("生成文件失败：" + e.getMessage());
        }
        finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    /**
     * 生成实体SQL
     * @param form 模型类包名+类名
     * @return
     */
    @Override
    public String createModelSql(GenEntitySqlForm form) {
        String result = "";
        try {
            Class<?> clazz = Class.forName(form.getClassPackageName());
            // 获取对应的注解（方便获取注解内的值）
            ApiModel apiModel = clazz.getAnnotation(ApiModel.class);
            String tableComment = apiModel.value();
            Field[] fields = clazz.getDeclaredFields();
            StringBuilder tableStrBuilder = new StringBuilder();
            StringBuilder columnStrBuilder = new StringBuilder();
            // 初始化为类名，后续被TABLE_NAME替换
            String tableName = clazz.getName();

            if (form.getBaseColumnFlag() != null && form.getBaseColumnFlag().equals(Boolean.TRUE)) {
                // 初始化基础字段
                columnStrBuilder.append(
                        "  `id` varchar(40) NOT NULL, " +
                                "  `create_time` datetime(3) DEFAULT NULL, " +
                                "  `update_time` datetime(3) DEFAULT NULL, " +
                                "  `create_by` varchar(40) DEFAULT NULL, " +
                                "  `update_by` varchar(40) DEFAULT NULL, ");
            }

            String firstFieldName = fields[0].getName();
            if ("TABLE_NAME".equals(firstFieldName)) {
                firstFieldName = fields[1].getName();
            }

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String fieldName = field.getName();
                if ("TABLE_NAME".equals(fieldName)){
                    tableName = (String) field.get(clazz);
                }
                else {
                    // 过滤掉不保存字段（回显字段）
                    TableField fieldAnnotation = field.getAnnotation(TableField.class);
                    if (fieldAnnotation != null && Boolean.FALSE.equals(fieldAnnotation.exist())) {
                        continue;
                    }

                    columnStrBuilder.append("`").append(StrUtil.toUnderlineCase(fieldName)).append("` ");
                    Class<?> fieldType = field.getType();
                    String columnType = "";
                    String columnComment = null;
                    if (String.class == fieldType) {
                        columnType = "varchar(255)";
                    }
                    else if(Integer.class == fieldType){
                        columnType = "int(11)";
                    }
                    else if(Long.class == fieldType){
                        columnType = "bigint";
                    }
                    else if(Double.class == fieldType){
                        columnType = "double";
                    }
                    else if(Boolean.class == fieldType){
                        columnType = "bit(1)";
                    }
                    else if(Date.class == fieldType){
                        columnType = "datetime(3)";
                    }
                    ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                    if (apiModelProperty != null){
                        columnComment = apiModelProperty.value();
                    }

                    columnStrBuilder.append(columnType);
                    if (!firstFieldName.equals(fieldName)) {
                        columnStrBuilder.append(" DEFAULT");
                    }
                    else {
                        columnStrBuilder.append(" NOT");
                    }
                    columnStrBuilder.append(" NULL");
                    if (columnComment != null) {
                        columnStrBuilder.append(" COMMENT '").append(columnComment).append("'");
                    }
                    columnStrBuilder.append(",");
                }
            }

            tableStrBuilder.append("DROP TABLE IF EXISTS `").append(tableName).append("`;");
            tableStrBuilder.append("CREATE TABLE `").append(tableName).append("` ( ");
            tableStrBuilder.append(columnStrBuilder);

            if (form.getBaseColumnFlag() != null && form.getBaseColumnFlag().equals(Boolean.TRUE)) {
                tableStrBuilder.append("PRIMARY KEY (`id`) ");
            }
            else {
                tableStrBuilder.append("PRIMARY KEY (`" + StrUtil.toUnderlineCase(firstFieldName) + "`) ");
            }
            tableStrBuilder.append(" ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='").append(tableComment).append("'");

            result = tableStrBuilder.toString();
        }
        catch (Exception e) {
            throw new BusinessException("生成实体SQL异常：" + e.getMessage());
        }
        return result;
    }

    /**
     *
     * @param context
     * @param dto
     */
    public static void velocityApply(VelocityContext context, GenEntityForm dto) {
        // 获取模板列表
        List<String> templates = new ArrayList<String>();
        HashMap<String, String> templateMap = new HashMap<>();
        templateMap.put("Service", "vm/java/Service.java.vm");
        templateMap.put("ServiceImpl", "vm/java/ServiceImpl.java.vm");
        templateMap.put("Controller", "vm/java/Controller.java.vm");
//        templateMap.put("Repository", "vm/java/Repository.java.vm");
        templateMap.put("Mapper", "vm/java/Mapper.java.vm");
        // 限制导出的文件
        String targetFiles = dto.getTargetFile();
        if (StrUtil.isBlank(targetFiles)) {
            templates.addAll(templateMap.values());
        }
        else {
            String[] targetFileList = targetFiles.split(",");
            for (String targetFile : targetFileList) {
                for (Map.Entry<String, String> entry : templateMap.entrySet()) {
                    if (StrUtil.equalsIgnoreCase(targetFile, entry.getKey())) {
                        templates.add(entry.getValue());
                    }
                }
            }
        }

        // 渲染模板
        for (String template : templates) {
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8" );
            tpl.merge(context, sw);

            FileWriter fileWriter = null;
            try {
                String fileName = getFileName(template, dto);
                if (new File(fileName).exists()) {
                    throw new BusinessException("文件已存在");
                }
                logger.info("gen file: {}", fileName);
                FileUtil.mkParentDirs(fileName);
                fileWriter = new FileWriter(fileName);
                fileWriter.append(sw.toString());
                fileWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException("渲染模板失败，类名：" + dto.getClassName(), e);
            }
            finally {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, GenEntityForm dto)
    {
        String packageName = dto.getPackageName();
        String className = dto.getClassName();
        // 小写类名
        String classname = StringUtils.uncapitalize(className);
        String javaPath = "D:\\";
        if (StrUtil.isNotBlank(dto.getTargetPath())) {
            javaPath = dto.getTargetPath().substring(0, dto.getTargetPath().lastIndexOf("\\") + 1);
        }

//        if (StringUtils.isNotEmpty(classname))
//        {
//            javaPath += classname.replace(".", "/") + "/";
//        }

        if (template.contains("domain.java.vm"))
        {
            return javaPath + "domain" + "/" + className + ".java";
        }

        if (template.contains("Service.java.vm"))
        {
            return javaPath + "service" + "/" + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm"))
        {
            return javaPath + "service" + "/" + "impl" + "/" + className + "ServiceImpl.java";
        }

        if (template.contains("Controller.java.vm"))
        {
            return javaPath + "controller" + "/" + className + "Controller.java";
        }

//        if (template.contains("Repository.java.vm"))
//        {
//            return javaPath + "repository" + "/" + className + "Repository.java";
//        }
        if (template.contains("Mapper.java.vm"))
        {
            return javaPath + "mapper" + "/" + className + "Mapper.java";
        }
        return null;
    }

}
