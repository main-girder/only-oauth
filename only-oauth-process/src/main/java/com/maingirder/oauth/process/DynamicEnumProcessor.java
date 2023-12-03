package com.maingirder.oauth.process;

import com.alibaba.fastjson.JSONObject;
import com.maingirder.oauth.process.annotation.DynamicEnum;
import com.maingirder.oauth.process.config.DynamicEnumConfig;
import com.maingirder.oauth.process.utils.FileUtils;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 动态枚举processor
 *
 * @author jason-guo-cr
 * @version 1.0.0
 * @since 2023/12/2 19:41
 */
@SupportedAnnotationTypes("com.maingirder.oauth.process.annotation.DynamicEnum")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DynamicEnumProcessor extends AbstractProcessor {


    private Filer filer;

    /**
     * Java语法树的工具类
     */
    private JavacTrees trees;

    /**
     * 创建语法树节点的工厂类
     */
    private TreeMaker treeMaker;

    /**
     * 编译器名称表的访问，提供了一些标准的名称和创建新名称的方法
     */
    private Names names;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.filer = processingEnv.getFiler();

        // 创建TreeMaker和Names所需的上下文
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.trees = JavacTrees.instance(processingEnv);
        this.treeMaker = TreeMaker.instance(context);
        this.names = new Names(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            // 通过lambda表达式，处理被注解标记的每个元素
            roundEnv.getElementsAnnotatedWith(annotation).forEach(element -> {
                DynamicEnum dynamicEnum = element.getAnnotation(DynamicEnum.class);
                String configPath = dynamicEnum.config();
                DynamicEnumConfig dynamicEnumConfig = getConfig(filer, configPath);

                if (dynamicEnumConfig == null || dynamicEnumConfig.isEnumsBlank()) {
                    return;
                }

                // 获取配置的字段列表
                java.util.List<String> fields = dynamicEnumConfig.getFields() == null ? new ArrayList<>() : dynamicEnumConfig.getFields();

                // 获取配置的枚举列表
                java.util.List<Map<String, Map<String, String>>> enums = dynamicEnumConfig.getEnums();

                // jcClassDecl.defs 类定义信息参见下方注释
                // 0 = {JCTree$JCVariableDecl@5766} "/*public static final*/ DEFAULT /* = new XXXEnum("default", "default") */"
                // 1 = {JCTree$JCVariableDecl@5767} "private final String code"
                // 2 = {JCTree$JCVariableDecl@5768} "private final String desc"
                // 3 = {JCTree$JCMethodDecl@5769} "\r\n<init>(String code, String desc) {\r\n    this.code = code;\r\n    this.desc = desc;\r\n}"
                // 4 = {JCTree$JCMethodDecl@5770} "\r\npublic String getCode() {\r\n    return code;\r\n}"
                // 5 = {JCTree$JCMethodDecl@5771} "\r\npublic String getDesc() {\r\n    return desc;\r\n}"
                JCTree.JCClassDecl jcClassDecl = (JCTree.JCClassDecl) trees.getTree(element);
                List<JCTree> sourceClassDefs = jcClassDecl.defs;

                // 获取构造方法索引
                int constructorIndex = sourceClassDefs.size() >> 1;

                // 获取原枚举中的字段
                java.util.List<String> defaultFieldList = new ArrayList<>(constructorIndex - 1);
                for (int i = 1; i < constructorIndex; i++) {
                    defaultFieldList.add(((JCTree.JCVariableDecl) sourceClassDefs.get(i)).getName().toString());
                }

                // 计算配置中新增的字段
                java.util.List<String> newFields = fields.stream().filter(field -> !defaultFieldList.contains(field)).collect(Collectors.toList());

                // 补充全部字段
                defaultFieldList.stream().filter(defaultField -> !fields.contains(defaultField)).forEach(fields::add);

                // 构造新的类定义
                java.util.List<JCTree> tmpClassDefs = new ArrayList<>();
                for (int i = 1; i < sourceClassDefs.size(); i++) {

                    // 如果有新的字段，那么不需要添加原有的构造方法
                    if (constructorIndex == i && newFields.size() > 0) {
                        continue;
                    }
                    tmpClassDefs.add(sourceClassDefs.get(i));
                }
                List<JCTree> newClassDefs = List.from(tmpClassDefs);

                // 动态扩展
                if (newFields.size() > 0) {
                    // 修改类信息之前需要修改当前pos
                    JCTree tree = trees.getTree(element);
                    treeMaker.pos = tree.pos;

                    // 获取字段类型（这里都是string）
                    JCTree.JCVariableDecl fieldVarDecl = (JCTree.JCVariableDecl) sourceClassDefs.get(1);
                    JCTree.JCExpression stringType = fieldVarDecl.vartype;

                    // 创建字段和getter方法
                    for (String field : newFields) {
                        newClassDefs = newClassDefs.prepend(buildField(field, stringType));
                        newClassDefs = newClassDefs.prepend(buildGetterMethod(field, stringType));
                    }

                    // 修改构造方法
                    JCTree.JCMethodDecl constructor = (JCTree.JCMethodDecl) sourceClassDefs.get(constructorIndex);
                    JCTree.JCMethodDecl jcMethodDecl = modifyConstructor(constructor, newFields, stringType);
                    newClassDefs = newClassDefs.prepend(jcMethodDecl);
                }

                // 构建枚举
                JCTree.JCVariableDecl enumVarDecl = (JCTree.JCVariableDecl) sourceClassDefs.get(0);
                List<JCTree> enumList = createEnum(enumVarDecl.vartype, enums, fields);
                newClassDefs = newClassDefs.prependList(enumList);

                // 修改类定义
                jcClassDecl.defs = newClassDefs;
            });
        }
        return true;

    }

    /**
     * 创建字段
     *
     * @param field   字段名
     * @param varType 类型
     * @return 字段定义
     */
    private JCTree.JCVariableDecl buildField(String field, JCTree.JCExpression varType) {
        return treeMaker.VarDef(treeMaker.Modifiers(Flags.PRIVATE + Flags.FINAL), names.fromString(field), varType, null);
    }


    /**
     * 创建getter方法
     *
     * @param field   字段名
     * @param varType 类型
     * @return getter方法定义
     */
    public JCTree.JCMethodDecl buildGetterMethod(String field, JCTree.JCExpression varType) {
        // 方法名
        String methodName = "get" + Character.toUpperCase(field.charAt(0)) + field.substring(1);

        // 方法体
        JCTree.JCBlock methodBody = treeMaker.Block(0, List.of(treeMaker.Return(treeMaker.Ident(names.fromString(field)))));

        // Create method
        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC), names.fromString(methodName), varType, List.nil(), List.nil(), List.nil(), methodBody, null);
    }


    /**
     * 修改构造方法
     *
     * @param constructor  原构造方法
     * @param fields       字段列表
     * @param fieldVarType 字段类型
     * @return 新的构造方法定义
     */
    public JCTree.JCMethodDecl modifyConstructor(JCTree.JCMethodDecl constructor, java.util.List<String> fields, JCTree.JCExpression fieldVarType) {
        java.util.List<JCTree.JCVariableDecl> params = new ArrayList<>(fields.size());
        java.util.List<JCTree.JCStatement> statements = new ArrayList<>(fields.size());

        for (String field : fields) {
            params.add(createParameter(fieldVarType, field));
            statements.add(createAssignmentStatement(field, treeMaker.Ident(names.fromString(field))));
        }

        // 构造方法参数
        List<JCTree.JCVariableDecl> appendParamList = List.from(params);
        List<JCTree.JCVariableDecl> newParams = constructor.params.appendList(appendParamList);

        // 方法体
        List<JCTree.JCStatement> appendStatementList = List.from(statements);
        List<JCTree.JCStatement> newStatements = constructor.body.stats.appendList(appendStatementList);

        // 修改构造方法
        return treeMaker.MethodDef(constructor.mods, constructor.name, constructor.restype, constructor.typarams, newParams, constructor.thrown, treeMaker.Block(0, newStatements), constructor.defaultValue);
    }


    /**
     * 创建构造方法参数
     *
     * @param varType   参数类型
     * @param paramName 参数名
     * @return 参数定义
     */
    private JCTree.JCVariableDecl createParameter(JCTree.JCExpression varType, String paramName) {
        return treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), names.fromString(paramName), varType, null);
    }

    /**
     * 创建方法体定义
     *
     * @param variable 参数
     * @param value    参数值
     * @return 方法体定义
     */
    private JCTree.JCStatement createAssignmentStatement(String variable, JCTree.JCExpression value) {
        JCTree.JCFieldAccess fieldAccess = treeMaker.Select(treeMaker.This(Type.noType), names.fromString(variable));
        JCTree.JCAssign assign = treeMaker.Assign(fieldAccess, value);
        return treeMaker.Exec(assign);
    }


    /**
     * 创建枚举
     *
     * @param varType 类型
     * @param enums   配置的枚举列表
     * @param fields  字段列表
     * @return 枚举定义列表
     */
    private List<JCTree> createEnum(JCTree.JCExpression varType, java.util.List<Map<String, Map<String, String>>> enums, java.util.List<String> fields) {
        java.util.List<JCTree.JCVariableDecl> enumList = new ArrayList<>(fields.size());

        for (Map<String, Map<String, String>> enumMap : enums) {
            for (Map.Entry<String, Map<String, String>> enumEntry : enumMap.entrySet()) {
                Name enumName = names.fromString(enumEntry.getKey());

                java.util.List<JCTree.JCLiteral> valueList = new ArrayList<>();

                for (String field : fields) {
                    String value = enumEntry.getValue().get(field) == null ? "" : enumEntry.getValue().get(field);
                    valueList.add(treeMaker.Literal(value));
                }

                // 初始化表达式
                JCTree.JCExpression initializer = treeMaker.NewClass(null, null, varType, List.from(valueList), null);

                // 创建枚举
                JCTree.JCVariableDecl enumDecl = treeMaker.VarDef(treeMaker.Modifiers(Flags.PUBLIC + Flags.STATIC + Flags.FINAL + Flags.ENUM), enumName, varType, initializer);
                enumList.add(enumDecl);
            }
        }
        return List.from(enumList);
    }


    /**
     * 读取配置
     *
     * @param filer    {@link Filer}
     * @param filePath 文件路径
     * @return {@link DynamicEnumConfig}
     */
    public DynamicEnumConfig getConfig(Filer filer, String filePath) {
        DynamicEnumConfig dynamicEnumConfig = null;
        try {
            //target/classes/
            FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "");
            File classPathFile = new File(resource.toUri()).getParentFile();

            String projectRootPath = FileUtils.findProjectRoot(classPathFile);

            java.util.List<File> aptConfigFiles = new ArrayList<>();

            while (projectRootPath != null && classPathFile != null && projectRootPath.length() <= classPathFile.getAbsolutePath().length()) {
                File aptConfig = new File(classPathFile, filePath);
                if (aptConfig.exists()) {
                    aptConfigFiles.add(aptConfig);
                }
                classPathFile = classPathFile.getParentFile();
            }


            for (File aptConfigFile : aptConfigFiles) {
                try (InputStream stream = Files.newInputStream(aptConfigFile.toPath()); BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                    StringBuilder jsonData = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonData.append(line);
                    }
                    reader.close();

                    // TODO 替换json解析
                    dynamicEnumConfig = JSONObject.parseObject(jsonData.toString(), DynamicEnumConfig.class);

                }
            }
        } catch (Exception e) {
            return dynamicEnumConfig;
        }
        return dynamicEnumConfig;
    }


}
