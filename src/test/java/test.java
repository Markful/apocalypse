import com.vzoom.zxxt.apocalypse.spel.SpelExcetor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Object result = spelExcetor.doneInSpringContext(dataMap, spel);

        System.out.println(result);
    }

}
