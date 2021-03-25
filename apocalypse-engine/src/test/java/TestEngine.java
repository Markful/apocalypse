import com.vzoom.apocalypse.engine.FeedbackEngineImpl;
import org.junit.Test;

/**
 * @Description:
 * @Author: wangyh
 * @Date: 2021/3/12
 */
public class TestEngine {

    @Test
    public void test() throws Exception {
        FeedbackEngineImpl engine = new FeedbackEngineImpl();

        /*
        choose(defaultValue,exp1,return1,exp2,return2,.....)：比较参数后返回指定数据：字段值orgValue和exp1,exp2,exp3..比较，
                         找到相同的，则返回对应的value值，都没有比对上则返回defaultValue
        sql() ：执行SQL
        没有任何函数 ：常量
        trim_s0() : 去掉首尾空格
        trim_s1() : 去掉所有空格
        trim_0() : 去掉前面的0，如果有符号，保留符号
        trim_1() : 去掉末尾小数点
        trim_2(n) : 保留n位小数点，默认2位
        trim_3(n) : 元转换成万元，保留n位小数，默认2位，eg:trim_3(3),54321 ->5.432
        trim_4(n) : 万元转换成元，保留n位小数,默认2位
        replace(org,target) : 将org转换成target，并且支持正则。
        日期转换
        date_0(yyyy-mm-dd) : 将当前系统时间转换为指定格式
        date_1(yyyy-MM-dd) : 转换时间为指定格式。
        date_2(格式A,格式B) : 格式转换，支持互转(yyyymmdd,yyyy-mm-dd)(yyyy-mm-dd,yyyy-mm-dd hh:mm:ss)(yyyymmdd,yyyy-mm-dd hh:mm:ss)
         */

//        System.out.println(engine.invokeEngine("12","choose(1,1,2,2,3,3)"));
        System.out.println(engine.invokeEngine("12个月","replace_0(个月,)"));
//        System.out.println(engine.invokeEngine("12.000个月","trim_1()"));


    }




}
