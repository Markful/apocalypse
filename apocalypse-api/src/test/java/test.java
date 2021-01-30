
import com.vzoom.apocalypse.ApocalypseApplication;
import com.vzoom.apocalypse.api.config.FeedbackProperties;
import com.vzoom.apocalypse.api.entity.ApocalypseAreaRules;
import com.vzoom.apocalypse.common.spel.SpelExcetor;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest(classes = ApocalypseApplication.class)
@RunWith(SpringRunner.class)
public class test {

    @Autowired
    private SpelExcetor spelExcetor;

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
    public void test2(){

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
    public void test3(){

        System.out.println(environment.getProperty("feedback.param.jiangsu") == null);
        System.out.println(environment.getActiveProfiles()[0]);

//        System.out.println(feedbackProperties.getHubei());
        List<ApocalypseAreaRules> apocalypseAreaRules = new ArrayList<>();
        apocalypseAreaRules.add(new ApocalypseAreaRules("","","123",",",",","","123","123412"));
        apocalypseAreaRules.add(new ApocalypseAreaRules("","","34634",",",",","","123","123412"));
        apocalypseAreaRules.add(new ApocalypseAreaRules("","","345345346",",",",","","123","123412"));
        apocalypseAreaRules.add(new ApocalypseAreaRules("","","2132`13",",",",","","123","123412"));
        apocalypseAreaRules.add(new ApocalypseAreaRules("","","`43`1`234`",",",",","","123","123412"));
        apocalypseAreaRules.add(new ApocalypseAreaRules("","","`43`1`234`",",",",","","123","123412"));
        apocalypseAreaRules.add(new ApocalypseAreaRules("","","`43`1`234`",",",",","","123","123412"));
        apocalypseAreaRules.add(new ApocalypseAreaRules("","","123",",",",","","123","123412"));
        apocalypseAreaRules.add(new ApocalypseAreaRules("","","123",",",",","","123","123412"));


        //去重得到所有配置的地区
        List<ApocalypseAreaRules> collect = apocalypseAreaRules.stream().filter(x -> StringUtils.isNotEmpty(x.getArea())).collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ApocalypseAreaRules::getArea))), ArrayList::new)
        );
        System.out.println("=========="+collect.size());
        for (ApocalypseAreaRules areaRules : collect) {
            System.out.println(areaRules);
        }



    }

}
