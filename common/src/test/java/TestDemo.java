import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vzoom.apocalypse.common.constants.Constants.SPEL_METHOD_TRIM_2;
import static java.util.regex.Pattern.compile;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/1/26
 */
public class TestDemo {

    @Test
    public void test123(){

        String response = "|abc|asdasd|1231casd|fsaasd|";
        String response2 = ",abc,asdasd,1231casd,fsaasd,";
        String encryptXml = "";
        String regex = "^[A-Za-z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(response.replaceAll("\n","").replaceAll("\r",""));
        if(m.find()){
            encryptXml = m.group(1);
        }
        System.out.println(encryptXml);

        String[] split = response.split("[A-Za-z0-9]+");
        for (String s : split) {
            System.out.println(s);
        }

        String[] split2 = response2.split("[A-Za-z0-9]+");
        for (String s : split2) {
            System.out.println(s);
        }

    }

    @Test
    public void test1234(){

        String arg = "+01000.00";
        String rule = "trim_1:${dkje}";
        String columnName = "dkje";

        if(rule.contains("trim:")){
            arg = arg.trim();

        }else if(rule.contains("trim_0:")){
            //去掉所有正负号和最前面的0，不会改动小数位
            arg = arg.trim();
//            arg = arg.replaceAll("^(\\-|\\+|)(0+)","");
            arg = arg.replaceAll("^(\\-)(0+)","-").replaceAll("^(\\+)(0+)","+").replaceAll("^(0+)","");
//.replaceAll("\\+(0+)","+").replaceAll("(0+)","")
//.replaceAll("\\+","").replaceAll("-","")
        }else if(rule.contains("trim_1:")){
            arg = arg.replaceAll("\\.[0-9]+","");


        }else if(rule.contains("expand")){


        }

        System.out.println(arg);

        System.out.println("  123  ".trim());
        System.out.println("  12  3  ".trim());
        System.out.println("  12  3".replaceAll("\\s",""));


        Pattern pattern = compile("\\((.*?)\\)");
        Matcher m = pattern.matcher("date_0(yyyy-mm-dd,yyyymmdd)");
        if(m.find()){
            System.out.println(m.group(1));
        }


    }


    @Test
    public void test33(){
        String originalField = "34.34345567";
        String encryptXml = "2222";
        Pattern pattern = Pattern.compile("\\((\\d+)\\)");
        Matcher m = pattern.matcher("trim_2(3)");
        if(m.find()){
            encryptXml = m.group(1);
        }
        System.out.println(encryptXml);
        int count = Integer.parseInt(encryptXml);
        if (count < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }else if(count == 0){
            originalField = new DecimalFormat("0").format(Double.valueOf(originalField)*10000);
        }else {
            StringBuilder formatStr = new StringBuilder("0.");
            for (int k = 0; k < count; k++) {
                formatStr.append("0");
            }
            originalField = new DecimalFormat(formatStr.toString()).format(Double.valueOf(originalField)*10000);
        }
        System.out.println(originalField);
/*        String[] split = "trim_2(3)".split("^"+SPEL_METHOD_TRIM_2+"([0-9]+)");
        System.out.println(split[0]);*/
    }

    @Test
    public void test13(){
        // 创建一个ExpressionParser对象，用于解析表达式
        ExpressionParser parser = new SpelExpressionParser();
        // 最简单的字符串表达式
        Expression exp = parser.parseExpression("'HelloWorld'");
        System.out.println("'HelloWorld'的结果： " + exp.getValue());
        // 调用方法的表达式
        exp = parser.parseExpression("'HelloWorld'.concat('!')");
        System.out.println("'HelloWorld'.concat('!')的结果： " + exp.getValue());
        // 调用对象的getter方法
        exp = parser.parseExpression("'HelloWorld'.bytes");
        System.out.println("'HelloWorld'.bytes的结果： " + exp.getValue());
        // 访问对象的属性(d相当于HelloWorld.getBytes().length)
        exp = parser.parseExpression("'HelloWorld'.bytes.length");
        System.out.println("'HelloWorld'.bytes.length的结果：" + exp.getValue());
        // 使用构造器来创建对象
        exp = parser.parseExpression("new String('helloworld')" + ".toUpperCase()");
        System.out.println("new String('helloworld')" + ".toUpperCase()的结果是： " + exp.getValue(String.class));

        Person person = new Person(1, "孙悟空", new Date());
        exp = parser.parseExpression("name");
        // 以指定对象作为root来计算表达式的值
        // 相当于调用person.name表达式的值
        System.out.println("以persn为root，name表达式的值是： " + exp.getValue(person, String.class));
        exp = parser.parseExpression("name=='孙悟空'");
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        // 将person设为Context的root对象
        ctx.setRootObject(person);

        // 以指定Context来计算表达式的值
        System.out.println(exp.getValue(ctx, Boolean.class));
        List<Boolean> list = new ArrayList<Boolean>();
        list.add(true);
        EvaluationContext ctx2 = new StandardEvaluationContext();
        // 将list设置成EvaluationContext的一个变量
        ctx2.setVariable("list", list);
        // 修改list变量的第一个元素的值
        parser.parseExpression("#list[0]").setValue(ctx2, "false");
        // list集合的第一个元素被改变
        System.out.println("list集合的第一个元素为：" + parser.parseExpression("#list[0]").getValue(ctx2));

    }

}
class Person{
    Integer id;
    String name;
    Date date;

    public Person(Integer id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}