import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vzoom.apocalypse.common.utils.ReadJarConfig;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/3/2
 */
public class TestJiangsu {

    @Test
    public void test2(){

        String response = "{\n" +
                "\"responseCode\": \"000000\"," +
                "\"responseMsg\": \"成功\"," +
                "\"data\": [\"NSRXXKZ_91320505723546779A.xml\",\"XGMNSRSBXX_91320505723546779A.xml\",\"ZZJGXX_91320505723546779A.xml\"]" +
                "}";
        JSONObject responseJson = JSONObject.parseObject(response);
        JSONArray jsonArray = JSON.parseArray(responseJson.get("data").toString());
        List list = JSONObject.parseArray(jsonArray.toJSONString());
        System.out.println(list);
    }

    @Test
    public void test123(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append("123").append(",");
        }

        String substring = sb.toString().substring(0, sb.length() - 1);
        System.out.println(substring);
    }

    @Test
    public void test1(){
        try {
            Properties properties = new Properties();
            // 读取SRC下配置文件 --- 属于读取内部文件
            properties.load(ReadJarConfig.class.getResourceAsStream("/application.properties"));

            properties.getProperty("username");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
