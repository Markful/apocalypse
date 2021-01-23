import com.vzoom.zxxt.apocalypse.ApocalypseApplication;
import com.vzoom.zxxt.apocalypse.dto.FeedbackProperties;
import com.vzoom.zxxt.apocalypse.spel.SpelExcetor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void test3(){

        System.out.println(feedbackProperties.getHubei());

    }

}
