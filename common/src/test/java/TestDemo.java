import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        String arg = "000001000.00";
        String rule = "trim_0:${dkje}";
        String columnName = "dkje";

        if(rule.contains("trim:")){
            arg = arg.trim();

        }else if(rule.contains("trim_0:")){
            //去掉所有正负号和最前面的0，不会改动小数位
            arg = arg.trim();
            arg = arg.replaceAll("\\+","").replaceAll("-","").replaceAll("^(0+)","");

        }else if(rule.contains("trim_1:")){



        }else if(rule.contains("expand")){



        }



        System.out.println(arg);
    }

}
