
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vzoom.apocalypse.ApocalypseApplication;
import com.vzoom.apocalypse.api.config.FeedbackProperties;
import com.vzoom.apocalypse.common.entity.ApocalypseAreaRules;
import com.vzoom.apocalypse.api.repository.AreaRulesMapper;
import com.vzoom.apocalypse.common.spel.SpelExcetor;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest(classes = ApocalypseApplication.class)
@RunWith(SpringRunner.class)
public class test {

    @Autowired
    private SpelExcetor spelExcetor;

    @Autowired
    AreaRulesMapper areaRulesMapper;

    @Test
    public void test111() {
        QueryWrapper<ApocalypseAreaRules> wrapper = new QueryWrapper<>();

        wrapper.eq("area", "jiangsu");
        wrapper.orderByAsc("SERIAL_NUM");
        List<ApocalypseAreaRules> areaRulesList = areaRulesMapper.selectList(wrapper);

    }

    @Test
    public void test1() {

        List<Integer> numList = new ArrayList<>();
        numList.add(1);
        numList.add(2);
        numList.add(3);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("testList", numList);
        String spel = "6+2";
        Object result = spelExcetor.doneInSpringContext(dataMap, spel);

        System.out.println(result);
    }

    @Test
    public void test2() {

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("6+2");
        Integer result = (Integer) expression.getValue();
        System.out.println("result:" + result);

    }

    @Autowired
    FeedbackProperties feedbackProperties;

    @Autowired
    private Environment environment;

/*    @Value("#{${feedback.param}}")
    public List<String> areaCodeList;*/

    @Test
    public void test3() {

        System.out.println(environment.getProperty("feedback.param.jiangsu") == null);
        System.out.println(environment.getActiveProfiles()[0]);

//        System.out.println(feedbackProperties.getHubei());
        List<ApocalypseAreaRules> apocalypseAreaRules = new ArrayList<>();


        //去重得到所有配置的地区
        List<ApocalypseAreaRules> collect = apocalypseAreaRules.stream().filter(x -> StringUtils.isNotEmpty(x.getArea())).collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ApocalypseAreaRules::getArea))), ArrayList::new)
        );
        System.out.println("==========" + collect.size());
        for (ApocalypseAreaRules areaRules : collect) {
            System.out.println(areaRules);
        }


    }


    @Test
    public void test12() {
        String orgField = "004";

        EvaluationContext context = new StandardEvaluationContext();
//        context.setVariable("orgField","004");
        ExpressionParser parser = new SpelExpressionParser();
//        Expression exp = parser.parseExpression("#orgField.equals('004') ? '004' : '005' ");
//        System.out.println(exp.getValue(context));

//        System.out.println(parser.parseExpression("choose('1','2','5','3','4','4','1','5')").getValue(this, String.class));
        String expression = "choose('2','5','3','4','4','1','5')";
        String[] split = expression.split("choose\\(");

        expression = "choose('" + "1" + "'," + split[1];
        System.out.println(parser.parseExpression(expression).getValue(this, String.class));
    }

    @Test
    public void tets() {

        System.out.println(choose("1", "2"));//2
        System.out.println(choose("1", "2", "5", "3", "4", "4"));//2
        System.out.println(choose("1", "2", "5", "3", "4", "4", "1", "5")); //5
        System.out.println(choose("1", "2", "5", "3", "4", "4", "1", "5", "1", "10")); //5

    }

    public String choose(String target, String defaultValue, String... arg) {
        //0,1,2,3
        //1,3,5,7
        int length = arg.length;
        if (length == 0) {
            return defaultValue;
        } else {
            int n = 0;
            while (2 * n + 1 <= length) {
                if (target.equals(arg[2 * n])) {
                    return arg[2 * n + 1];
                }
                n++;
            }
            return defaultValue;
        }

    }


}
